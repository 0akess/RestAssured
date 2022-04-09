package ru.yandex.scooter.requests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseSpecForRequest {
    private final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    protected RequestSpecification baseSpec(){
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}
