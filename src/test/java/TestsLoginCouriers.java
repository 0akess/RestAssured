import assistSteps.StepDeleteCourier;
import dataForTests.DataForCreateCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import requests.courier.PostCreateCourier;
import requests.courier.PostLoginCourier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Набор тестов на метод 'Логин курьера в системе'")
public class TestsLoginCouriers {

    private final PostLoginCourier loginCourier = new PostLoginCourier();

    private static final DataForCreateCourier scooterCourier = new DataForCreateCourier();
    private static final ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
    private static final String courierLogin = dataForCreate.get(0);
    private static final String courierPassword = dataForCreate.get(1);
    private static final String courierFirstName = dataForCreate.get(2);

    private final ArrayList<String> fakeDataForLogin = scooterCourier.registerCourierData();
    // фейковый логин и пароль
    private final String courierLoginFake = fakeDataForLogin.get(0);
    private final String courierPasswordFake = fakeDataForLogin.get(1);

    @BeforeClass
    @DisplayName("Подготовка: курьер создан!")
    public static void setUp() {

        PostCreateCourier newCourier = new PostCreateCourier();
        newCourier.createCourier(courierLogin, courierPassword, courierFirstName);
    }

    @AfterClass
    @DisplayName("Завершение: курьер удален!")
    public static void end() {

        StepDeleteCourier stepDeleteCourier = new StepDeleteCourier();
        stepDeleteCourier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    @DisplayName("Базовая авторизация, с валидным логином и паролем")
    public void courier_AuthorizationNormal_200AndReturnId() {

        ValidatableResponse response = loginCourier.loginCourier(courierLogin, courierPassword);
        int actualStatusCode = response.extract().statusCode();
        int id = response.extract().path("id");

        assertThat("Не удалось создать пользователя",
                actualStatusCode, equalTo(HttpStatus.SC_OK));

        assertThat("Не удалось создать пользователя", id, notNullValue());
    }

    @Test
    @DisplayName("Авторизация без логина")
    public void courier_AuthorizationWithoutLogin_400AndErrorMessage() {

        ValidatableResponse response = loginCourier.loginCourier("", courierPassword);
        int actualStatusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(errorMessage, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void courier_AuthorizationWithoutPassword_400AndErrorMessage() {

        ValidatableResponse response = loginCourier.loginCourier(courierLogin, "");
        int actualStatusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(errorMessage, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неправильным логином")
    public void courier_LoginNotExist_404AndErrorMessage() {

        ValidatableResponse response = loginCourier.loginCourier(courierLoginFake, courierPassword);
        int actualStatusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat(errorMessage, equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void courier_PasswordNotExist_404AndErrorMessage() {

        ValidatableResponse response = loginCourier.loginCourier(courierLogin, courierPasswordFake);
        int actualStatusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat(errorMessage, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином и паролем")
    public void courier_LoginAndPasswordNotExist_404AndErrorMessage() {

        ValidatableResponse response = loginCourier.loginCourier(courierLoginFake, courierPasswordFake);
        int actualStatusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat(errorMessage, equalTo("Учетная запись не найдена"));
    }
}