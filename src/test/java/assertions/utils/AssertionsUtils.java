package assertions.utils;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AssertionsUtils {
    private AssertionsUtils(){}

    public static void assertStatusCode(Response response, int expected ){
        Assertions.assertEquals(response.statusCode(), expected,
                String.format("Was expected a status code equal to %s, but received %s", expected, response.statusCode()));
    }

    public static void assertJsonSchema(Response response, String path){
        response.then().assertThat().body(matchesJsonSchemaInClasspath(path));
    }

    public static void assertIfFieldsExistsInResponse(Response response, String jsonPath, boolean expected){
        if(expected){
            Assertions.assertNotNull(response.path(jsonPath),
                    String.format("Not Expected %s, but it was received", jsonPath));
        }else {
            Assertions.assertNull(response.path(jsonPath),
                    String.format("Expected %s, but it not received", jsonPath));
        }
    }

    public static void assertIfFieldsExistsAndItHasValuesInResponse(Response response, String jsonPath){
        String contentLength = response.path(jsonPath).toString();
        Assertions.assertNotNull(contentLength, String.format("Parameter %s not found or null", jsonPath));
    }

    public static void assertResponse(Response response, Map<Object, Object> objectMap){
        objectMap.keySet().forEach(key -> {
                String valueOfKey = response.path("" + key).toString();
                Assertions.assertEquals(
                        objectMap.get(key).toString(),
                        valueOfKey,
                        String.format(
                                "Expected %s in key %s, but was received %s",
                                objectMap.get(key).toString(),
                                key.toString(),
                                valueOfKey
                        )
                );
            }
        );
    }
}
