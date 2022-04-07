package ru.yandex.scooter.steps;


import ru.yandex.scooter.data.DataForCreateOrder;
import ru.yandex.scooter.model.Orders;
import ru.yandex.scooter.requests.orders.GetOrderTrack;
import ru.yandex.scooter.requests.orders.PostOrders;
import io.qameta.allure.Step;

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