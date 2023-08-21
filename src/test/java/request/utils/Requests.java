package request.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;
import static io.restassured.RestAssured.baseURI;

public final class Requests {

    public Requests(String uri){
        baseURI = uri;
    }
    public Response get(String endpoint){
        return RestAssured.given().get(endpoint);
    }

    public Response post(String url, Map<?,?> parameters){
        return RestAssured.given().body(parameters).post(url);
    }

    public Response post(String url, Map<String,String> header, Map<?,?> parameters){
        return RestAssured.given().headers(header).body(parameters).post(url);
    }

    public Response put(String url, Map<String,String> header, Map<?,?> parameters){
        return RestAssured.given().headers(header).body(parameters).put(url);
    }

    public Response patch(String url, Map<String,String> header, Map<?,?> parameters){
        return RestAssured.given().headers(header).body(parameters).patch(url);
    }

    public Response delete(String url, Map<String,String> header){
        return RestAssured.given().headers(header).delete(url);
    }
}
