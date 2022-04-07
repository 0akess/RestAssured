package ru.yandex.scooter.requests.orders;

import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class GetOrders extends BaseSpecForRequest {

    public ValidatableResponse getOrder(Integer courierId, String nearestStation, Integer limit, Integer page){

        return given().spec(baseSpec())
                .queryParam("courierId", courierId)
                .queryParam("nearestStation", nearestStation)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .get("/api/v1/orders").then();
    }

}