package server.book_library.config.redis.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Sort;

import java.io.IOException;

public class SortJsonDeserializer extends JsonDeserializer<Sort> {
    @Override
    public Sort deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode rootNode = mapper.readTree(jsonParser);

        boolean empty = rootNode.get("empty").asBoolean();
        boolean unsorted = rootNode.get("unsorted").asBoolean();
        boolean sorted = rootNode.get("sorted").asBoolean();

        if (empty) {
            return Sort.unsorted();
        } else if (unsorted) {
            return Sort.unsorted();
        } else if (sorted) {
            String property = rootNode.get("property").asText();
            Sort.Direction direction = rootNode.get("direction").asText().equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            return Sort.by(direction, property);
        } else {
            throw new IllegalArgumentException("Invalid Sort JSON representation");
        }
    }
}

