package ru.yandex.scooter.requests.courier;

import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.model.CourierData;
import ru.yandex.scooter.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PostCreateCourier extends BaseSpecForRequest {

    public ValidatableResponse createCourier(String loginCourier, String passwordCourier, String firstNameCourier) {

        CourierData registerRequestBody = new CourierData
                (loginCourier, passwordCourier, firstNameCourier);

        return given().spec(baseSpec())
                .and().body(registerRequestBody)
                .when().post("/api/v1/courier").then();
    }
}
