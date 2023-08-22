package com.herokuapp.restful.booker.booking;

import assertions.utils.AssertionsUtils;
import com.herokuapp.restful.booker.BaseRequest;
import com.herokuapp.restful.booker.authentication.TestAuthentication;
import io.restassured.response.Response;
import json.utils.JsonUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

public class TestBooking extends BaseRequest {
    private static final String END_POINT = "/booking";
    private static final String GET_ALL_BOOKINGS_JSON_SCHEMA_PATH = "com/herokuapp/restful.booker/booking/jsonSchema/getAllBookingsJsonSchema.json";
    private static final String GET_BOOKING_BY_ID_JSON_SCHEMA_PATH = "com/herokuapp/restful.booker/booking/jsonSchema/getBookingByIdJsonSchema.json";
    private static final String POST_BOOKING_JSON_SCHEMA_PATH = "com/herokuapp/restful.booker/booking/jsonSchema/postBookingJsonSchema.json";
    private static final String PUT_BOOKING_JSON_SCHEMA_PATH = GET_BOOKING_BY_ID_JSON_SCHEMA_PATH;
    private static final String PATCH_BOOKING_JSON_SCHEMA_PATH = GET_BOOKING_BY_ID_JSON_SCHEMA_PATH;
    private static final String PATH_SAMPLE_JSON_FOR_CREATE_NEW_BOOKING = "src/test/resources/com/herokuapp/restful.booker/booking/dataForTest/exampleForCreateNewBooking.json";
    private final Map<String, String> COOKIE = new HashMap<>();
    private final HashMap<String, String> INVALID_COOKIE;


    {
        TestAuthentication authentication = new TestAuthentication();
        this.COOKIE.put("Cookie", String.format("token=%s", authentication.validToken));

        INVALID_COOKIE = new HashMap<>();
        INVALID_COOKIE.put("Cookie", "token=126365515487kbhhvbjkhjfd");
    }

    @Test
    @DisplayName("'" + END_POINT + "' . Get All bookings. Expected status code 200")
    public void testGetAllBookingsExpectedStatusCode200(){
        Response response = REQUEST.get(END_POINT);

        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_OK);
        AssertionsUtils.assertJsonSchema(response, GET_ALL_BOOKINGS_JSON_SCHEMA_PATH);
    }
    @Test
    @DisplayName("'" + END_POINT + "/{id}' . Get booking by id. Expected status code 200")
    public void testGetBookingByIdExpectedStatusCode200(){
        Response responseGetAllBooking = REQUEST.get(END_POINT);
        int lastBookingId = responseGetAllBooking.path("collect { it.bookingid }.min()");

        Response response = REQUEST.get(String.format("%s/%d", END_POINT, lastBookingId));

        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_OK);
        AssertionsUtils.assertJsonSchema(response, GET_BOOKING_BY_ID_JSON_SCHEMA_PATH);
    }

    @Test
    @DisplayName("'" + END_POINT + "/{id}' . Get booking by invalid id. Expected status code 401")
    public void testGetBookingByInvalidIdExpectedStatusCode401(){
        Response response = REQUEST.get(String.format("%s/%d", END_POINT, 0));
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("'" + END_POINT + "' . Create booking booking and verify if was created")
    public void testCreateBookingAndValidateIfBookingWasCreated(){
        Map<Object, Object> jsonForRequest = JsonUtils.ImportJsonFileAndReturnMapOfThen(PATH_SAMPLE_JSON_FOR_CREATE_NEW_BOOKING);
        Response response = REQUEST.post(END_POINT, COOKIE, jsonForRequest);

        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_OK);
        AssertionsUtils.assertJsonSchema(response, POST_BOOKING_JSON_SCHEMA_PATH);

        int newBookingId = response.path("bookingid");
        Response responseGetBookingById = REQUEST.get(String.format("%s/%d", END_POINT, newBookingId));
        AssertionsUtils.assertStatusCode(responseGetBookingById, HttpStatus.SC_OK);
        AssertionsUtils.assertResponse(responseGetBookingById, jsonForRequest);
    }

    @Test
    @DisplayName("'" + END_POINT + "' . Update (PUT) booking and verify if was updated")
    public void testUpdateWithAllBookingFieldsAndValidateIfBookingWasUpdated(){
        Map<Object, Object> jsonForRequest = JsonUtils.ImportJsonFileAndReturnMapOfThen(PATH_SAMPLE_JSON_FOR_CREATE_NEW_BOOKING);
        Response responseNewBooking = REQUEST.post(END_POINT, COOKIE, jsonForRequest);
        int newBookingId = responseNewBooking.path("bookingid");

        jsonForRequest.put("firstname", "Smith");
        jsonForRequest.put("lastname", "Krug");
        jsonForRequest.put("totalprice", 250);

        Response response = REQUEST.put(
                String.format("%s/%s", END_POINT, newBookingId),
                COOKIE,
                jsonForRequest
        );
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_OK);
        AssertionsUtils.assertJsonSchema(response, PUT_BOOKING_JSON_SCHEMA_PATH);

        Response responseGetBookingById = REQUEST.get(String.format("%s/%s", END_POINT, newBookingId));
        AssertionsUtils.assertStatusCode(responseGetBookingById, HttpStatus.SC_OK);
        AssertionsUtils.assertResponse(responseGetBookingById, jsonForRequest);
    }

    @Test
    @DisplayName("'" + END_POINT + "' . Update (PUT) booking with a invalid token")
    public void testUpdateWithAllBookingFieldsWithInvalidToken(){
        Map<Object, Object> jsonForRequest = JsonUtils.ImportJsonFileAndReturnMapOfThen(PATH_SAMPLE_JSON_FOR_CREATE_NEW_BOOKING);
        Response responseNewBooking = REQUEST.post(END_POINT, COOKIE, jsonForRequest);
        int lastBookingId = responseNewBooking.path("bookingid");
        Response response = REQUEST.put(
                String.format("%s/%s", END_POINT, lastBookingId),
                INVALID_COOKIE,
                jsonForRequest
        );
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("'" + END_POINT + "' . Update (Patch) booking and verify if was updated ")
    public void testUpdateWithPartOfBookingFieldsAndValidateIfBookingWasUpdated(){
        Map<Object, Object> jsonForRequest = JsonUtils.ImportJsonFileAndReturnMapOfThen(PATH_SAMPLE_JSON_FOR_CREATE_NEW_BOOKING);
        Response responseNewBooking = REQUEST.post(END_POINT, COOKIE, jsonForRequest);
        int newBookingId = responseNewBooking.path("bookingid");

        HashMap<Object, Object> fieldsAndValuesToUpdate = new HashMap<>();
        fieldsAndValuesToUpdate.put("firstname", "Layon");
        jsonForRequest.put("firstname", "Layon");

        fieldsAndValuesToUpdate.put("lastname", "Schrute");
        jsonForRequest.put("lastname", "Schrute");

        Response response = REQUEST.patch(
                String.format("%s/%s", END_POINT, newBookingId),
                COOKIE,
                fieldsAndValuesToUpdate
        );
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_OK);
        AssertionsUtils.assertJsonSchema(response, PATCH_BOOKING_JSON_SCHEMA_PATH);

        Response responseGetBookingById = REQUEST.get(String.format("%s/%s", END_POINT, newBookingId));
        AssertionsUtils.assertStatusCode(responseGetBookingById, HttpStatus.SC_OK);
        AssertionsUtils.assertResponse(responseGetBookingById, jsonForRequest);
    }

    @Test
    @DisplayName("'" + END_POINT + "' . Update (Patch) booking with a invalid token ")
    public void testUpdateWithPartOfBookingFieldsWithInvalidToken(){
        Map<Object, Object> jsonForRequest = JsonUtils.ImportJsonFileAndReturnMapOfThen(PATH_SAMPLE_JSON_FOR_CREATE_NEW_BOOKING);
        Response responseCreateNewBooking = REQUEST.post(END_POINT, COOKIE, jsonForRequest);
        int lastBookingId = responseCreateNewBooking.path("bookingid");

        Response response = REQUEST.patch(
                String.format("%s/%s", END_POINT, lastBookingId),
                INVALID_COOKIE,
                jsonForRequest
        );
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("'" + END_POINT + "' . Delete booking and verify if was deleted")
    public void testDeleteBookingAndValidateIfBookingWasDeleted(){
        Map<Object, Object> jsonForRequest = JsonUtils.ImportJsonFileAndReturnMapOfThen(PATH_SAMPLE_JSON_FOR_CREATE_NEW_BOOKING);
        Response responseCreateNewBooking = REQUEST.post(END_POINT, COOKIE, jsonForRequest);
        int lastBookingId = responseCreateNewBooking.path("bookingid");

        Response response = REQUEST.delete(
                String.format("%s/%s", END_POINT, lastBookingId),
                COOKIE
        );
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_CREATED);

        Response responseGetBookingDeleted = REQUEST.get(String.format("%s/%d", END_POINT, lastBookingId));
        AssertionsUtils.assertStatusCode(responseGetBookingDeleted, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("'" + END_POINT + "' . Delete booking with a invalid token")
    public void testDeleteBookingWithInvalidToken(){
        Map<Object, Object> jsonForRequest = JsonUtils.ImportJsonFileAndReturnMapOfThen(PATH_SAMPLE_JSON_FOR_CREATE_NEW_BOOKING);
        Response responseCreateNewBooking = REQUEST.post(END_POINT, COOKIE, jsonForRequest);
        int lastBookingId = responseCreateNewBooking.path("bookingid");

        Response response = REQUEST.delete(
                String.format("%s/%s", END_POINT, lastBookingId),
                INVALID_COOKIE
        );
        AssertionsUtils.assertStatusCode(response, HttpStatus.SC_FORBIDDEN);
    }
}