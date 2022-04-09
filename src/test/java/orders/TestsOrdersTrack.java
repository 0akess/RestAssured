package orders;

import ru.yandex.scooter.data.DataForCreateOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.model.Orders;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.scooter.requests.orders.GetOrderTrack;
import ru.yandex.scooter.requests.orders.PostOrders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Набор тестов на метод 'Получить заказ по его номеру'")
public class TestsOrdersTrack {

    private final GetOrderTrack getOrder = new GetOrderTrack();
    private final DataForCreateOrder data = new DataForCreateOrder();

    @Test
    @DisplayName("Корректное получение заказа по трек-номеру заказа")
    public void ordersTrack_WithCorrectTrackOrder_200AndBody() {

        PostOrders postOrders = new PostOrders();
        Orders orders = new Orders(data.getJsonOrder("GREY"));
        int track = postOrders.createOrder(orders).extract().path("track");

        ValidatableResponse response = getOrder.getOrderTrackResponse(track);
        Orders orderActual = response.extract().as(Orders.class);
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_OK));

        assertThat("Метро отличается",
                orderActual.getMetroStation(), equalTo(orders.getMetroStation()));

        assertThat("Телефон отличается",
                orderActual.getPhone(), equalTo(orders.getPhone()));

        assertThat("Имя отличается",
                orderActual.getFirstName(), equalTo(orders.getFirstName()));
    }

    @Test
    @DisplayName("Запрос без трек-номера заказа")
    public void ordersTrack_WithoutTrackOrder_400AndErrorMessage() {

        ValidatableResponse response = getOrder.getOrderTrackResponse(null);
        String actualMessage = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(actualMessage, equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Запрос с несуществующем трек-номером заказа")
    public void ordersTrack_WithFakeTrack_404AndErrorMessage() {

        ValidatableResponse response = getOrder.getOrderTrackResponse(data.getFakeTrack());
        String actualMessage = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat(actualMessage, equalTo("Заказ не найден"));
    }
}