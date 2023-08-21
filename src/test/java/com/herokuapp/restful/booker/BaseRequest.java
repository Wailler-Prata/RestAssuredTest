package com.herokuapp.restful.booker;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import request.utils.Requests;

//https://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-GetBooking
public abstract class BaseRequest {

    protected final Requests REQUEST;

    {
        this.REQUEST = new Requests("https://restful-booker.herokuapp.com/");

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.requestSpecification = new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                build();
    }
}
