package com.herokuapp.restful.booker.authentication;

import assertions.utils.AssertionsUtils;
import com.herokuapp.restful.booker.BaseRequest;
import io.restassured.response.Response;
import json.utils.JsonUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Map;
public class TestAuthentication extends BaseRequest {
    private static final String END_POINT = "/auth";
    private static final String TOKEN_JSON_PATH = "token";
    private static final String JSON_SCHEMA_PATH = "com/herokuapp/restful.booker/authentication/authenticationJsonSchema.json";
    private static final String JSON_CREDENTIALS_PATH = "src/test/java/com/herokuapp/restful/booker/credentials.json";
    private final Map<Object,Object> VALID_CREDENTIALS;
    public String validToken;

    {
        this.VALID_CREDENTIALS = JsonUtils.ImportJsonFileAndReturnMapOfThen(JSON_CREDENTIALS_PATH);
        this.validToken = generateValidAccessToken();
    }

    private Response postAuthentication(Map<Object, Object> credentials){
        return REQUEST.post(END_POINT, credentials);
    }

    private String getTokenInAuthenticateResponse(Response response){
        return response.path(TOKEN_JSON_PATH);
    }

    private String generateValidAccessToken(){
        Response response = postAuthentication(this.VALID_CREDENTIALS);
        return getTokenInAuthenticateResponse(response);
    }

    @Test
    @DisplayName("'" + END_POINT + "' Authenticate with valid credentials")
    public void testAuthenticationWithValidCredentialsExpectedStatusCode200(){
        Response response = postAuthentication(this.VALID_CREDENTIALS);
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_OK);
        AssertionsUtils.assertJsonSchema(response, JSON_SCHEMA_PATH);
        AssertionsUtils.assertIfFieldsExistsAndItHasValuesInResponse(response, TOKEN_JSON_PATH);
    }

    @Test
    @DisplayName("'" + END_POINT + "' Authenticate with invalid credentials")
    public void testAuthenticationWithInvalidCredentialsExpectedStatusCode401(){
        Map<Object, Object> invalidCredentials = this.VALID_CREDENTIALS;
        invalidCredentials.put("password", "123");

        Response response = postAuthentication(invalidCredentials);
        AssertionsUtils.assertIfFieldsExistsInResponse(response, TOKEN_JSON_PATH, false);
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_OK);
    }
}
