package requests.orders;

import io.restassured.response.ValidatableResponse;
import model.Orders;
import requests.BaseSpecForRequest;

import static io.restassured.RestAssured.given;

public class PostOrders extends BaseSpecForRequest {

    public ValidatableResponse createOrder (Orders orders){

        return given().spec(baseSpec())
                .and().body(orders)
                .when().post("/api/v1/orders").then();
    }
}


