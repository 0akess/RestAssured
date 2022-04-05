import ru.yandex.Scooter.requests.assistSteps.StepDeleteCourier;
import ru.yandex.Scooter.requests.assistSteps.StepGetIdCourier;
import ru.yandex.Scooter.requests.dataForTests.DataForCreateCourier;
import ru.yandex.Scooter.requests.dataForTests.DataForCreateOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import ru.yandex.Scooter.requests.model.ListOrders;
import ru.yandex.Scooter.requests.model.Orders;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Test;
import ru.yandex.Scooter.requests.orders.GetOrderTrack;
import ru.yandex.Scooter.requests.orders.GetOrders;
import ru.yandex.Scooter.requests.orders.PostOrders;
import ru.yandex.Scooter.requests.orders.PutOrderAccept;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@DisplayName("Набор тестов на метод 'Получение списка заказов'")
public class TestsGetOrders {

    private static final DataForCreateCourier scooterCourier = new DataForCreateCourier();
    private static final ArrayList<String> dataForCreate = scooterCourier.registerCourierData();
    private static final String courierLogin = dataForCreate.get(0);
    private static final String courierPassword = dataForCreate.get(1);
    private final String courierFirstName = dataForCreate.get(2);

    private final GetOrders getOrders = new GetOrders();


    @AfterClass
    @DisplayName("Удаление курьера")
    public static void after() {

        StepDeleteCourier stepDeleteCourier = new StepDeleteCourier();
        stepDeleteCourier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    @DisplayName("Получение списка заказов с корректным Body")
    public void getOrders_WithCourierId_200AndNormalResponse() {

        StepGetIdCourier stepGetIdCourier = new StepGetIdCourier();
        int idCourier = stepGetIdCourier.getIdCourier(courierLogin, courierPassword, courierFirstName);

        DataForCreateOrder data = new DataForCreateOrder();
        Orders order = new Orders(data.getJsonOrder(1));

        PostOrders postOrders = new PostOrders();
        int track = postOrders.createOrder(order).extract().path("track");

        GetOrderTrack getOrderId = new GetOrderTrack();
        int idOrder = getOrderId.getIdOrder(track);

        PutOrderAccept putOrderAccept = new PutOrderAccept();
        putOrderAccept.acceptOrder(idOrder, idCourier);

        GetOrders getOrders = new GetOrders();
        ValidatableResponse response = getOrders.getOrder
                (idCourier, null, 1, null);

        ListOrders orders = response.extract().as(ListOrders.class);
        Orders actualOrder = orders.getOrders().get(0);
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_OK));

        assertThat("Id курьера отличается",
                actualOrder.getCourierId(), equalTo(idCourier));

        assertThat("Трек заказа отличается",
                actualOrder.getTrack(), equalTo(track));

        assertThat("Метро отличается",
                actualOrder.getMetroStation(), equalTo(order.getMetroStation()));

        assertThat("Телефон отличается",
                actualOrder.getPhone(), equalTo(order.getPhone()));

        assertThat("Имя отличается",
                actualOrder.getFirstName(), equalTo(order.getFirstName()));
    }

    @Test
    @DisplayName("Получение количества заказов в соответствии с лимитом")
    public void getOrders_WithUseParamLimit_200AndGetTenOrder() {

        int amountOrders = 10;
        ValidatableResponse response = getOrders.getOrder
                (null, null, amountOrders, null);
        ListOrders orders = response.extract().as(ListOrders.class);

        assertThat("Полученное количество заказов отличается",
                orders.getOrders().size(), equalTo(amountOrders));
    }

    @Test
    @DisplayName("Получение заказов только с выбранной станцией")
    public void getOrders_WithUseParamStation_200AndCheckStation() {

        int numberStation = 3;
        ValidatableResponse response = getOrders.getOrder
                (null, "[\"" + numberStation + "\"]", null, null);
        ListOrders orders = response.extract().as(ListOrders.class);
        Orders order = orders.getOrders().get(0);

        assertThat("Станция отличается, метод возвращает другую станцию",
                order.getMetroStation(), equalTo(numberStation));
    }

    @Test
    @DisplayName("Получение страниц Списка заказа в соответствии с лимитом страниц")
    public void getOrders_WithUseParamPage_200AndNumberPage() {

        int amountPage = 3;
        ValidatableResponse response = getOrders.getOrder
                (null, null, null, amountPage);
        int page = response.extract().path("pageInfo.page");

        assertThat("Станция отличается, метод возвращает другую станцию",
                page, equalTo(amountPage));
    }
}