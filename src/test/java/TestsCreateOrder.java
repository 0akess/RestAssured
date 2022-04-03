import assistSteps.StepCancelOrder;
import dataForTests.DataForCreateOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Orders;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import requests.orders.PostOrders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(Parameterized.class)
@DisplayName("Набор тестов на метод 'Создание заказа'")
public class TestsCreateOrder {
    private final int color;

    public TestsCreateOrder(int color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getTextData() {
        return new Object[][]{
                {0},
                {1},
                {2},
                {3},
        };
    }

    @Test()
    @DisplayName("Корректное создание заказа")
    public void orders_NormalCreate_200AndGetTrack() {

        DataForCreateOrder order = new DataForCreateOrder();
        Orders orders = new Orders(order.getJsonOrder(color));
        PostOrders postOrders = new PostOrders();

        ValidatableResponse response = postOrders.createOrder(orders);
        int statusCode = response.extract().statusCode();
        assertThat("Не удалось создать заказ",
                statusCode, equalTo(HttpStatus.SC_CREATED));
        int trackNumber = response.extract().path("track");
        assertThat("Не удалось создать заказ", trackNumber, notNullValue());

        StepCancelOrder step = new StepCancelOrder();
        step.cancelOrder(trackNumber);
    }
}