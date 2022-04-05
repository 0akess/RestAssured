package assistSteps;

import dataForTests.DataForCreateCourier;
import io.qameta.allure.Step;
import requests.courier.PostCreateCourier;
import requests.courier.PostLoginCourier;

import java.util.ArrayList;

public class StepGetIdCourier {

    @Step("Получение ID курьера")
    public int getIdCourier(String courierLogin, String courierPassword, String courierFirstName) {

        PostCreateCourier postCreateCourier = new PostCreateCourier();
        postCreateCourier.createCourier(courierLogin, courierPassword, courierFirstName);

        PostLoginCourier postLoginCourier = new PostLoginCourier();
        return postLoginCourier.returnIdCourier(courierLogin, courierPassword);
    }

    @Step("Получение ID курьера, без ввода доп данных")
    public int getIdCourierWithoutData() {

        DataForCreateCourier scooterCourier = new DataForCreateCourier();
        ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
        String courierLogin = dataForCreate.get(0);
        String courierPassword = dataForCreate.get(1);
        String courierFirstName = dataForCreate.get(2);

        PostCreateCourier postCreateCourier = new PostCreateCourier();
        postCreateCourier.createCourier(courierLogin, courierPassword, courierFirstName);

        PostLoginCourier postLoginCourier = new PostLoginCourier();
        return postLoginCourier.returnIdCourier(courierLogin, courierPassword);
    }
}