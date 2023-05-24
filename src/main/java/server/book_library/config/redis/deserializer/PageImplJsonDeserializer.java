package server.book_library.config.redis.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

public class PageImplJsonDeserializer extends JsonDeserializer<PageImpl<?>> {
    @Override
    public PageImpl<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode rootNode = mapper.readTree(jsonParser);

        JsonNode contentNode = rootNode.get("content");
        JsonNode pageableNode = rootNode.get("pageable");
        JsonNode sortNode = rootNode.get("sort");
        JsonNode numberOfElementsNode = rootNode.get("numberOfElements");
        Pageable pageable = mapper.treeToValue(pageableNode, Pageable.class);

        List<?> content = mapper.readValue(contentNode.traverse(mapper), new TypeReference<List<?>>(){});
        Sort sort = mapper.treeToValue(sortNode, Sort.class);

        return new PageImpl<>(content, pageable, numberOfElementsNode.asLong());
    }
}