package ru.yandex.scooter.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.yandex.scooter.requests.courier.DeleteCourier;
import ru.yandex.scooter.requests.courier.PostLoginCourier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StepDeleteCourier {

    @Step("Удаление курьера")
    public void deleteCourier(String courierLogin, String courierPassword) {

        PostLoginCourier loginCourier = new PostLoginCourier();
        DeleteCourier deleteCourier = new DeleteCourier();
        ValidatableResponse response = loginCourier.loginCourier(courierLogin, courierPassword);

        if (response.extract().statusCode() == 200) {
            int id = response.extract().path("id");
            int statusCodeDelete = deleteCourier.deleteCourier(id).extract().statusCode();
            assertThat("Не удалось удалить пользователя",
                    statusCodeDelete, equalTo(HttpStatus.SC_OK));
        }
    }
}