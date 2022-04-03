import assistSteps.StepDeleteCourier;
import assistSteps.StepGetIdCourier;
import dataForTests.DataForCreateCourier;
import dataForTests.DataForCreateOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.ListOrders;
import model.Orders;
import org.apache.http.HttpStatus;
import org.junit.Test;
import requests.orders.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@DisplayName("Набор тестов на метод 'Получение списка заказов'")
public class TestsGetOrders {

    @Test
    @DisplayName("Получение списка заказов с корректным Body")
    public void getOrders_WithCourierId_200AndNormalResponse() {

        DataForCreateCourier scooterCourier = new DataForCreateCourier();
        ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
        String courierLogin = dataForCreate.get(0);
        String courierPassword = dataForCreate.get(1);
        String courierFirstName = dataForCreate.get(2);

        StepGetIdCourier stepGetIdCourier = new StepGetIdCourier();
        int idCourier = stepGetIdCourier.getIdCourier(courierLogin, courierPassword, courierFirstName);

        DataForCreateOrder data = new DataForCreateOrder();
        Orders order = new Orders(data.getJsonOrder(1));

        PostOrders postOrders = new PostOrders();
        int track = postOrders.createOrder(order).extract().path("track");

        GetOrderTrack getOrderId = new GetOrderTrack();
        int idOrder = getOrderId.getIdOrder(track);

        PutOrderAccept putOrderAccept = new PutOrderAccept();
        putOrderAccept.acceptOrder(idOrder, idCourier);

        GetOrders getOrders = new GetOrders();
        ValidatableResponse response = getOrders.getOrder
                (idCourier, null, 1, null);

        ListOrders orders = response.extract().as(ListOrders.class);
        Orders actualOrder = orders.getOrders().get(0);

        try {
            response.statusCode(HttpStatus.SC_OK);
            assertEquals("Id курьера отличается",
                    idCourier, actualOrder.getCourierId());
            assertEquals("Трек заказа отличается",
                    track, actualOrder.getTrack());
            assertEquals("Метро отличается",
                    order.getMetroStation(), actualOrder.getMetroStation());
            assertEquals("Телефон отличается",
                    order.getPhone(), actualOrder.getPhone());
            assertEquals("Имя отличается",
                    order.getFirstName(), actualOrder.getFirstName());
        } finally {
            StepDeleteCourier stepDeleteCourier = new StepDeleteCourier();
            stepDeleteCourier.deleteCourier(courierLogin, courierPassword);
            PutOrdersFinish putOrdersFinish = new PutOrdersFinish();
            putOrdersFinish.ordersFinish(actualOrder.getId());
        }
    }

    @Test
    @DisplayName("Получение количества заказов в соответствии с лимитом")
    public void getOrders_WithUseParamLimit_200AndGetTenOrder() {

        int amountOrders = 10;
        GetOrders getOrders = new GetOrders();
        ValidatableResponse response = getOrders.getOrder
                (null, null, amountOrders, null);
        ListOrders orders = response.extract().as(ListOrders.class);

        assertEquals("Полученное количество заказов отличается", amountOrders, orders.getOrders().size());
    }

    @Test
    @DisplayName("Получение заказов только с выбранной станцией")
    public void getOrders_WithUseParamStation_200AndCheckStation() {

        int numberStation = 3;
        GetOrders getOrders = new GetOrders();
        ValidatableResponse response = getOrders.getOrder
                (null, "[\""+ numberStation +"\"]", null, null);
        ListOrders orders = response.extract().as(ListOrders.class);
        Orders order = orders.getOrders().get(0);

        assertEquals("Станция отличается, метод возвращает другую станцию",
                numberStation, order.getMetroStation());
    }

    @Test
    @DisplayName("Получение страниц Списка заказа в соответствии с лимитом страниц")
    public void getOrders_WithUseParamPage_200AndNumberPage() {

        int amountPage = 3;
        GetOrders getOrders = new GetOrders();
        ValidatableResponse response = getOrders.getOrder
                (null, null, null, amountPage);
        int page = response.extract().path("pageInfo.page");

        assertEquals("Станция отличается, метод возвращает другую станцию",
                amountPage, page);
    }
}