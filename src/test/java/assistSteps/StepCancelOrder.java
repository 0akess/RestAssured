package assistSteps;

import requests.orders.PutOrderCancel;

public class StepCancelOrder {

    public void cancelOrder(int track) {
        PutOrderCancel put = new PutOrderCancel();
        int cod = put.cancelOrder(track).extract().statusCode();

        if (cod == 200) {
            System.out.println("Заказ успешно создан!\nПосле чего был отменен, база чиста");
        } else {
            System.out.println("Не удалось отменить заказ, проверьте метод отмены Заказа");
        }
    }
}