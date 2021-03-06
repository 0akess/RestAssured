package courier;

import ru.yandex.scooter.steps.StepGetIdCourier;
import ru.yandex.scooter.data.DataForCreateCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.scooter.requests.courier.DeleteCourier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Набор тестов на метод 'Удаление курьера'")
public class TestsDeleteCourier {

    private final DeleteCourier deleteCourier = new DeleteCourier();

    @Test
    @DisplayName("Стандартное удаление курьера")
    public void deleteCourier_NormalDelete_200AndTrue() {

        StepGetIdCourier stepGetIdCourier = new StepGetIdCourier();
        int idCourier = stepGetIdCourier.getIdCourierWithoutData();

        ValidatableResponse response = deleteCourier.deleteCourier(idCourier);
        boolean actual = response.extract().path("ok");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_OK));

        assertThat("Не удалось удалить курьера",
                actual, equalTo(true));
    }

    @Test
    @DisplayName("Удаление курьера без 'IdCourier'")
    public void deleteCourier_WithoutIdCourier_400AndMessageError() {

        ValidatableResponse response = deleteCourier.deleteCourierWithoutID();
        String actual = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(actual, equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Удаление курьера с некорректным 'IdCourier'")
    public void deleteCourier_InvalidIdCourier_404AndMessageError() {

        DataForCreateCourier data = new DataForCreateCourier();
        int invalidId = data.getFakeId();

        ValidatableResponse response = deleteCourier.deleteCourier(invalidId);
        String actual = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        assertThat(actualStatusCode, equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat(actual, equalTo("Курьера с таким id нет"));
    }
}