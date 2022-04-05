import assistSteps.StepDeleteCourier;
import assistSteps.StepGetIdCourier;
import assistSteps.StepGetIdOrder;
import dataForTests.DataForCreateCourier;
import dataForTests.DataForCreateOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import requests.courier.PostCreateCourier;
import requests.courier.PostLoginCourier;
import requests.orders.PutOrderAccept;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Набор тестов на метод 'Принять заказ'")
public class TestsOrdersAccept {

    private static final DataForCreateCourier scooterCourier = new DataForCreateCourier();
    private static final ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
    private static final String courierLogin = dataForCreate.get(0);
    private static final String courierPassword = dataForCreate.get(1);
    private static final String courierFirstName = dataForCreate.get(2);

    private final StepGetIdOrder idOrder = new StepGetIdOrder();
    private final PutOrderAccept putAccept = new PutOrderAccept();
    private final DataForCreateOrder dataOrder = new DataForCreateOrder();
    private final PostLoginCourier postCourier = new PostLoginCourier();


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
    @DisplayName("Успешное принятие заказа курьером")
    public void acceptOrder_NormalAccept_200AndAccept() {

        StepGetIdCourier stepGetIdCourier = new StepGetIdCourier();
        int idCourier = stepGetIdCourier.getIdCourier(courierLogin, courierPassword, courierFirstName);

        StepGetIdOrder getIdOrder = new StepGetIdOrder();
        int idOrder = getIdOrder.getIdOrder();

        ValidatableResponse response = putAccept.acceptOrder(idOrder, idCourier);
        boolean actualResponse = response.extract().path("ok");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_OK));
        assertThat("Не удалось принять заказ", actualResponse, equalTo(true));
    }

    @Test
    @DisplayName("Запрос на принятие заказа без 'IdOrder'")
    public void acceptOrder_WithoutIdOrder_400AndErrorMassage() {

        int idCourier = postCourier.returnIdCourier(courierLogin, courierPassword);

        ValidatableResponse response = putAccept.acceptOrderWithoutIdOrder(idCourier);
        String actualMessage = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(actualMessage, equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Запрос на принятие заказа с невалидным 'IdOrder'")
    public void acceptOrder_WithFakeIdOrder_400AndErrorMassage() {

        int idCourier = postCourier.returnIdCourier(courierLogin, courierPassword);
        int trackOrder = dataOrder.getFakeTrack();

        ValidatableResponse response = putAccept.acceptOrder(trackOrder, idCourier);
        String actualMessage = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat(actualMessage, equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Запрос на принятие заказа без 'IdCourier'")
    public void acceptOrder_WithoutIdCourier_400AndErrorMassage() {

        int trackOrder = idOrder.getIdOrder();
        ValidatableResponse response = putAccept.acceptOrder(trackOrder, null);
        String actualMessage = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(actualMessage, equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Запрос на принятие заказа с невалидным 'IdCourier'")
    public void acceptOrder_WithFakeIdCourier_404AndErrorMassage() {

        int trackOrder = idOrder.getIdOrder();
        int idCourierFake = dataOrder.getFakeTrack();

        ValidatableResponse response = putAccept.acceptOrder(trackOrder, idCourierFake);
        String actualMessage = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat(actualMessage, equalTo("Курьера с таким id не существует"));
    }
}