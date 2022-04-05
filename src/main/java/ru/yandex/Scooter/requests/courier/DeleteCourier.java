package ru.yandex.Scooter.requests.courier;

import io.restassured.response.ValidatableResponse;
import ru.yandex.Scooter.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class DeleteCourier extends BaseSpecForRequest {

    public ValidatableResponse deleteCourier(Integer id) {

        return given().spec(baseSpec())
                .and().pathParam("id", id)
                .when().delete("/api/v1/courier/{id}").then();
    }

    public ValidatableResponse deleteCourierWithoutID() {

        return given().spec(baseSpec())
                .and().pathParam("id", "")
                .when().delete("/api/v1/courier/{id}").then();
    }
}
