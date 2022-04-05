package ru.yandex.Scooter.requests.assistSteps;


import ru.yandex.Scooter.requests.dataForTests.DataForCreateOrder;
import io.qameta.allure.Step;
import ru.yandex.Scooter.requests.model.Orders;
import ru.yandex.Scooter.requests.orders.GetOrderTrack;
import ru.yandex.Scooter.requests.orders.PostOrders;

public class StepGetIdOrder {

    @Step("Получение ID заказа")
    public int getIdOrder() {

        DataForCreateOrder data = new DataForCreateOrder();
        Orders dataForOrder = data.getJsonOrder(1);
        Orders order = new Orders(dataForOrder);

        PostOrders postOrders = new PostOrders();
        int track = postOrders.createOrder(order).extract().path("track");

        GetOrderTrack getOrderId = new GetOrderTrack();
        int idOrder = getOrderId.getIdOrder(track);

        return idOrder;
    }
}