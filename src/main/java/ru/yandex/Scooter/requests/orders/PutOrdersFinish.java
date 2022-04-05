package ru.yandex.Scooter.requests.orders;

import io.restassured.response.ValidatableResponse;
import ru.yandex.Scooter.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PutOrdersFinish extends BaseSpecForRequest {

    public ValidatableResponse ordersFinish(int idOrder) {

        return given().spec(baseSpec())
                .and()
                .pathParam("id", idOrder)
                .when()
                .put("/api/v1/orders/finish/{id}").then();
    }
}
