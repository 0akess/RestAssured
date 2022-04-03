package assistSteps;

import dataForTests.DataForCreateOrder;
import model.Orders;
import requests.orders.GetOrderTrack;
import requests.orders.PostOrders;

public class StepGetIdOrder {

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