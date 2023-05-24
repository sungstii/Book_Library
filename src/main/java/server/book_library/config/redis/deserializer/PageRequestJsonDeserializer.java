package server.book_library.config.redis.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;

public class PageRequestJsonDeserializer extends JsonDeserializer<PageRequest> {
    @Override
    public PageRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode rootNode = mapper.readTree(jsonParser);

        JsonNode pageNode = rootNode.get("pageNumber");
        JsonNode sizeNode = rootNode.get("pageSize");
        JsonNode sortNode = rootNode.get("sort");

        int page = pageNode.asInt();
        int size = sizeNode.asInt();
        Sort sort = mapper.treeToValue(sortNode, Sort.class);

        return PageRequest.of(page, size, sort);

    }
}