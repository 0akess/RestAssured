import assistSteps.StepGetIdCourier;
import dataForTests.DataForCreateCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import requests.courier.DeleteCourier;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@DisplayName("Набор тестов на метод 'Удаление курьера'")
public class TestsDeleteCourier {

    @Test
    @DisplayName("Стандартное удаление курьера")
    public void deleteCourier_NormalDelete_200AndTrue() {

        DataForCreateCourier scooterCourier = new DataForCreateCourier();
        ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
        String courierLogin = dataForCreate.get(0);
        String courierPassword = dataForCreate.get(1);
        String courierFirstName = dataForCreate.get(2);

        StepGetIdCourier stepGetIdCourier = new StepGetIdCourier();
        int idCourier = stepGetIdCourier.getIdCourier(courierLogin, courierPassword, courierFirstName);
        DeleteCourier deleteCourier = new DeleteCourier();

        ValidatableResponse response = deleteCourier.deleteCourier(idCourier);
        boolean actual = response.extract().path("ok");
        response.statusCode(HttpStatus.SC_OK);
        assertEquals("Не удалось удалить курьера", true, actual);
    }

    @Test
    @DisplayName("Удаление курьера без 'IdCourier'")
    public void deleteCourier_WithoutIdCourier_400AndMessageError() {

        DeleteCourier deleteCourier = new DeleteCourier();
        ValidatableResponse response = deleteCourier.deleteCourierWithoutID();
        String actual = response.extract().path("message");
        response.statusCode(HttpStatus.SC_BAD_REQUEST);
        assertEquals("Что-то не так", "Недостаточно данных для удаления курьера", actual);
    }

    @Test
    @DisplayName("Удаление курьера с некорректным 'IdCourier'")
    public void deleteCourier_InvalidIdCourier_404AndMessageError() {

        DataForCreateCourier data = new DataForCreateCourier();
        DeleteCourier deleteCourier = new DeleteCourier();
        int invalidId = data.getFakeId();
        ValidatableResponse response = deleteCourier.deleteCourier(invalidId);
        String actual = response.extract().path("message");
        response.statusCode(HttpStatus.SC_NOT_FOUND);
        assertEquals("Что-то не так", "Курьера с таким id нет", actual);
    }
}