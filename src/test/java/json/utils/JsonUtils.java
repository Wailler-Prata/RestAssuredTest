package json.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    private JsonUtils(){}
    public static Map ImportJsonFileAndReturnMapOfThen(String filePath) {
        try {
            File file = new File(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(file);
            return  objectMapper.convertValue(jsonNode, HashMap.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
