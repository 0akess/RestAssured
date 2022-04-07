package ru.yandex.scooter.data;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;

public class DataForCreateCourier {

    public ArrayList<String> registerCourierData() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);

        ArrayList<String> dataForCourier = new ArrayList<>();
        dataForCourier.add(courierLogin);
        dataForCourier.add(courierPassword);
        dataForCourier.add(courierFirstName);

        return dataForCourier;
    }

    public int getFakeId() {
        int metroStation = RandomUtils.nextInt(1, 12);
        return metroStation;
    }
}