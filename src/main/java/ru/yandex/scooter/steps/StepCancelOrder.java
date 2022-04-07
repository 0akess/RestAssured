package ru.yandex.scooter.steps;

import io.qameta.allure.Step;
import ru.yandex.scooter.requests.orders.PutOrderCancel;

public class StepCancelOrder {

    @Step("Отмена заказа")
    public void cancelOrder(int track) {
        PutOrderCancel put = new PutOrderCancel();
        int cod = put.cancelOrder(track).extract().statusCode();

        if (cod == 200) {
            System.out.println("Заказ успешно создан!\nПосле чего был отменен");
        } else {
            System.out.println("Не удалось отменить заказ, проверьте метод отмены Заказа");
        }
    }
}