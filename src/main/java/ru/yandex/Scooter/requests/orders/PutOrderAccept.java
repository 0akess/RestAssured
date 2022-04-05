package ru.yandex.Scooter.requests.orders;

import io.restassured.response.ValidatableResponse;
import ru.yandex.Scooter.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PutOrderAccept extends BaseSpecForRequest {

    public ValidatableResponse acceptOrder(Integer idOrder, Integer idCourier){

        return given().spec(baseSpec())
                .and()
                .pathParam("id", idOrder)
                .queryParam("courierId", idCourier)
                .when()
                .put("/api/v1/orders/accept/{id}").then();
    }

    public ValidatableResponse acceptOrderWithoutIdOrder(Integer idCourier){

        return given().spec(baseSpec())
                .and()
                .pathParam("id", "")
                .queryParam("courierId", idCourier)
                .when()
                .put("/api/v1/orders/accept/{id}").then();
    }
}

