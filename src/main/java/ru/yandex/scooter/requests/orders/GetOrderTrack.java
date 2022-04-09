package ru.yandex.scooter.requests.orders;

import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class GetOrderTrack extends BaseSpecForRequest {

    public int getIdOrder(int track){

        return given().spec(baseSpec())
                .queryParam("t", track)
                .get( "/api/v1/orders/track").then().extract().path("order.id");
    }

    public ValidatableResponse getOrderTrackResponse(Integer track){

        return given().spec(baseSpec())
                .queryParam("t", track)
                .get( "/api/v1/orders/track").then();
    }
}
