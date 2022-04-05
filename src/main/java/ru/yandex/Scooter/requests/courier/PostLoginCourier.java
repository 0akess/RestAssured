package ru.yandex.Scooter.requests.courier;

import io.restassured.response.ValidatableResponse;
import ru.yandex.Scooter.requests.model.CourierData;
import ru.yandex.Scooter.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PostLoginCourier extends BaseSpecForRequest {

    public ValidatableResponse loginCourier(String loginCourier, String passwordCourier) {

        CourierData registerRequestBody = new CourierData
                (loginCourier, passwordCourier);

        return given().spec(baseSpec())
                .and().body(registerRequestBody)
                .when().post("/api/v1/courier/login").then();
    }

    public int returnIdCourier(String loginCourier, String passwordCourier) {

        CourierData registerRequestBody = new CourierData
                (loginCourier, passwordCourier);

        return given().spec(baseSpec())
                .and().body(registerRequestBody)
                .when().post("/api/v1/courier/login").then().extract().path("id");
    }
}
