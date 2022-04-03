import dataForTests.DataForCreateCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;
import requests.courier.PostCreateCourier;
import assistSteps.StepDeleteCourier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Набор тестов на метод 'Создание курьера'")
public class TestsCreateCourier {

    DataForCreateCourier scooterCourier = new DataForCreateCourier();
    private final ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
    private final String courierLogin = dataForCreate.get(0);
    private final String courierPassword = dataForCreate.get(1);
    private final String courierFirstName = dataForCreate.get(2);

    @Test
    @DisplayName("Базовое и корректное создание курьера")
    public void courier_CreateNormal_201AndTrue() {

        ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
        String courierLogin = dataForCreate.get(0);
        String courierPassword = dataForCreate.get(1);
        String courierFirstName = dataForCreate.get(2);

        PostCreateCourier post = new PostCreateCourier();
        ValidatableResponse response = post.createCourier
                (courierLogin, courierPassword, courierFirstName);
        int statusCode = response.extract().statusCode();
        boolean messageStatus = response.extract().path("ok");

        try {
            assertThat("Не удалось создать пользователя",
                    statusCode, equalTo(HttpStatus.SC_CREATED));
            assertThat("Не удалось создать пользователя",
                    messageStatus, equalTo(true));
        } finally {
            StepDeleteCourier stepDeleteCourier = new StepDeleteCourier();
            stepDeleteCourier.deleteCourier(courierLogin, courierPassword);
        }
    }


    @Test
    @DisplayName("Создание курьера без 'FirstName'")
    public void courier_CreateWithoutFirstName_201AndTrue() {

        ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
        String courierLogin = dataForCreate.get(0);
        String courierPassword = dataForCreate.get(1);

        PostCreateCourier post = new PostCreateCourier();
        ValidatableResponse response = post.createCourier
                (courierLogin, courierPassword, "");
        int statusCode = response.extract().statusCode();
        boolean messageStatus = response.extract().path("ok");

        try {
            assertThat("Не удалось создать пользователя",
                    statusCode, equalTo(HttpStatus.SC_CREATED));
            assertThat("Не удалось создать пользователя",
                    messageStatus, equalTo(true));
        } finally {
            StepDeleteCourier stepDeleteCourier = new StepDeleteCourier();
            stepDeleteCourier.deleteCourier(courierLogin, courierPassword);
        }
    }

    @Test
    @DisplayName("Создание курьера без 'Login'")
    public void courier_CreateWithoutLogin_400AndErrorMessage() {

        PostCreateCourier post = new PostCreateCourier();
        ValidatableResponse response = post.createCourier
                (null, courierPassword, courierFirstName);
        int statusCode = response.extract().statusCode();
        String messageStatus = response.extract().path("message");
        assertThat("Не удалось создать пользователя",
                statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Не удалось создать пользователя",
                messageStatus, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без 'Password'")
    public void courier_CreateWithoutPassword_400AndErrorMessage() {

        PostCreateCourier post = new PostCreateCourier();
        ValidatableResponse response = post.createCourier
                (courierLogin, "", courierFirstName);
        int statusCode = response.extract().statusCode();
        String messageStatus = response.extract().path("message");
        assertThat("Не удалось создать пользователя",
                statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Не удалось создать пользователя",
                messageStatus, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без 'Password & FirstName'")
    public void courier_CreateWithoutPasswordAndFirstName_400AndErrorMessage() {

        PostCreateCourier post = new PostCreateCourier();
        ValidatableResponse response = post.createCourier
                (courierLogin, "", "");
        int statusCode = response.extract().statusCode();
        String messageStatus = response.extract().path("message");
        assertThat("Не удалось создать пользователя",
                statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Не удалось создать пользователя",
                messageStatus, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без 'Login & Password'")
    public void courier_CreateWithoutLoginAndPassword_400AndErrorMessage() {

        PostCreateCourier post = new PostCreateCourier();
        ValidatableResponse response = post.createCourier
                ("", "", courierFirstName);
        int statusCode = response.extract().statusCode();
        String messageStatus = response.extract().path("message");
        assertThat("Не удалось создать пользователя",
                statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("Не удалось создать пользователя",
                messageStatus, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с ранее использованными данными")
    public void courier_CreateWithSameData_409AndMessageError() {

        PostCreateCourier post = new PostCreateCourier();
        post.createCourier(courierLogin, courierPassword, courierFirstName);

        ValidatableResponse response = post.createCourier
                (courierLogin, courierPassword, courierFirstName);
        int statusCode = response.extract().statusCode();
        String messageStatus = response.extract().path("message");

        try {
            assertThat("Не удалось создать пользователя",
                    statusCode, equalTo(409));
            assertThat("Не удалось создать пользователя",
                    messageStatus, equalTo("Этот логин уже используется"));
        } finally {
            StepDeleteCourier stepDeleteCourier = new StepDeleteCourier();
            stepDeleteCourier.deleteCourier(courierLogin, courierPassword);
        }
    }
}