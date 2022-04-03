import dataForTests.DataForCreateOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Orders;
import org.apache.http.HttpStatus;
import org.junit.Test;
import requests.orders.GetOrderTrack;
import requests.orders.PostOrders;

import static org.junit.Assert.assertEquals;

@DisplayName("Набор тестов на метод 'Получить заказ по его номеру'")
public class TestsOrdersTrack {

    @Test
    @DisplayName("Корректное получение заказа по трек-номеру заказа")
    public void ordersTrack_WithCorrectTrackOrder_200AndBody() {

        DataForCreateOrder data = new DataForCreateOrder();
        Orders orders = new Orders(data.getJsonOrder(1));
        PostOrders postOrders = new PostOrders();
        int track = postOrders.createOrder(orders).extract().path("track");
        GetOrderTrack getOrder = new GetOrderTrack();

        ValidatableResponse response = getOrder.getOrderTrackResponse(track);
        Orders orderActual = response.extract().as(Orders.class);

        response.statusCode(HttpStatus.SC_OK);
        assertEquals("Метро отличается",
                orders.getMetroStation(), orderActual.getMetroStation());
        assertEquals("Телефон отличается",
                orders.getPhone(), orderActual.getPhone());
        assertEquals("Имя отличается",
                orders.getFirstName(), orderActual.getFirstName());
    }

    @Test
    @DisplayName("Запрос без трек-номера заказа")
    public void ordersTrack_WithoutTrackOrder_400AndErrorMessage() {

        GetOrderTrack getOrder = new GetOrderTrack();
        ValidatableResponse response = getOrder.getOrderTrackResponse(null);
        String actualMessage = response.extract().path("message");

        response.statusCode(HttpStatus.SC_BAD_REQUEST);
        assertEquals("Недостаточно данных для поиска", actualMessage);
    }

    @Test
    @DisplayName("Запрос с несуществующем трек-номером заказа")
    public void ordersTrack_WithFakeTrack_404AndErrorMessage() {

        GetOrderTrack getOrder = new GetOrderTrack();
        DataForCreateOrder data = new DataForCreateOrder();
        ValidatableResponse response = getOrder.getOrderTrackResponse(data.getFakeTrack());
        String actualMessage = response.extract().path("message");

        response.statusCode(HttpStatus.SC_NOT_FOUND);
        assertEquals("Заказ не найден", actualMessage);
    }
}