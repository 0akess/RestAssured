package ru.yandex.scooter.requests.orders;

import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PutOrderCancel extends BaseSpecForRequest {

    public ValidatableResponse cancelOrder(Integer track){

        return given().spec(baseSpec())
                .queryParam("track", track)
                .put("/api/v1/orders/cancel").then();
    }
}
