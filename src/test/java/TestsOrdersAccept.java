import assistSteps.StepDeleteCourier;
import assistSteps.StepGetIdCourier;
import assistSteps.StepGetIdOrder;
import dataForTests.DataForCreateCourier;
import dataForTests.DataForCreateOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Orders;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import requests.courier.PostCreateCourier;
import requests.courier.PostLoginCourier;
import requests.orders.GetOrderTrack;
import requests.orders.PostOrders;
import requests.orders.PutOrderAccept;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@DisplayName("Набор тестов на метод 'Принять заказ'")
public class TestsOrdersAccept {

    static DataForCreateCourier scooterCourier = new DataForCreateCourier();
    private static final ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
    private static final String courierLogin = dataForCreate.get(0);
    private static final String courierPassword = dataForCreate.get(1);
    private static final String courierFirstName = dataForCreate.get(2);

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

        DataForCreateOrder data = new DataForCreateOrder();
        Orders order = new Orders(data.getJsonOrder(1));
        PostOrders postOrders = new PostOrders();
        int track = postOrders.createOrder(order).extract().path("track");
        GetOrderTrack getOrderId = new GetOrderTrack();
        int idOrder = getOrderId.getIdOrder(track);

        PutOrderAccept accept = new PutOrderAccept();
        ValidatableResponse response = accept.acceptOrder(idOrder, idCourier);
        boolean actual = response.extract().path("ok");

        response.statusCode(HttpStatus.SC_OK);
        assertEquals("Не удалось принять заказ", true, actual);
    }

    @Test
    @DisplayName("Запрос на принятие заказа без 'IdOrder'")
    public void acceptOrder_WithoutIdOrder_400AndErrorMassage() {

        PostLoginCourier post = new PostLoginCourier();
        PutOrderAccept put = new PutOrderAccept();

        int idCourier = post.returnIdCourier(courierLogin, courierPassword);

        ValidatableResponse response = put.acceptOrderWithoutIdOrder(idCourier);
        String actual = response.extract().path("message");
        response.statusCode(HttpStatus.SC_BAD_REQUEST);
        assertEquals("Недостаточно данных для поиска", actual);
    }

    @Test
    @DisplayName("Запрос на принятие заказа с невалидным 'IdOrder'")
    public void acceptOrder_WithFakeIdOrder_400AndErrorMassage() {

        DataForCreateOrder data = new DataForCreateOrder();
        PostLoginCourier post = new PostLoginCourier();
        PutOrderAccept put = new PutOrderAccept();

        int idCourier = post.returnIdCourier(courierLogin, courierPassword);
        int trackOrder = data.getFakeTrack();

        ValidatableResponse response = put.acceptOrder(trackOrder, idCourier);
        String actual = response.extract().path("message");
        response.statusCode(HttpStatus.SC_NOT_FOUND);
        assertEquals("Заказа с таким id не существует", actual);
    }

    @Test
    @DisplayName("Запрос на принятие заказа без 'IdCourier'")
    public void acceptOrder_WithoutIdCourier_400AndErrorMassage() {

        StepGetIdOrder idOrder = new StepGetIdOrder();
        PutOrderAccept put = new PutOrderAccept();

        int trackOrder = idOrder.getIdOrder();

        ValidatableResponse response = put.acceptOrder(trackOrder, null);
        String actual = response.extract().path("message");
        response.statusCode(HttpStatus.SC_BAD_REQUEST);
        assertEquals("Недостаточно данных для поиска", actual);
    }

    @Test
    @DisplayName("Запрос на принятие заказа с невалидным 'IdCourier'")
    public void acceptOrder_WithFakeIdCourier_404AndErrorMassage() {

        StepGetIdOrder idOrder = new StepGetIdOrder();
        PutOrderAccept put = new PutOrderAccept();
        DataForCreateOrder dataOrder = new DataForCreateOrder();

        int trackOrder = idOrder.getIdOrder();
        int idCourierFake = dataOrder.getFakeTrack();

        ValidatableResponse response = put.acceptOrder(trackOrder, idCourierFake);
        String actual = response.extract().path("message");
        response.statusCode(HttpStatus.SC_NOT_FOUND);
        assertEquals("Курьера с таким id не существует", actual);
    }
}