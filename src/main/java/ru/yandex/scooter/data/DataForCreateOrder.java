package ru.yandex.scooter.data;

import ru.yandex.scooter.model.Orders;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;

public class DataForCreateOrder {

    public Orders getJsonOrder(String whichColor) {

        String firstName = RandomStringUtils.randomAlphabetic(6);
        String lastName = RandomStringUtils.randomAlphabetic(6);
        String address = RandomStringUtils.randomAlphabetic(10);
        int metroStation = RandomUtils.nextInt(1, 15);
        String phone = RandomStringUtils.randomNumeric(11);
        int rentTime = RandomUtils.nextInt(1, 7);
        String deliveryDate = String.valueOf(LocalDateTime.now().toLocalDate());
        String comment = RandomStringUtils.randomAlphabetic(10);
        String[] color = {whichColor};

        Orders order = new Orders(firstName, lastName, address,
                metroStation, phone, rentTime, deliveryDate, comment, color);

        return order;
    }

    public int getFakeTrack() {
        int track = RandomUtils.nextInt(10000000, 99999999);
        return track;
    }
}