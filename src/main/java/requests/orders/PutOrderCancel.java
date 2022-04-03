package requests.orders;

import io.restassured.response.ValidatableResponse;
import requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PutOrderCancel extends BaseSpecForRequest {

    public ValidatableResponse cancelOrder(Integer track){

        return given().spec(baseSpec())
                .queryParam("track", track)
                .put("/api/v1/orders/cancel").then();
    }
}
