package assistSteps;

import org.apache.http.HttpStatus;
import requests.courier.DeleteCourier;
import requests.courier.PostLoginCourier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StepDeleteCourier {

    public void deleteCourier(String courierLogin, String courierPassword) {

        PostLoginCourier loginCourier = new PostLoginCourier();
        int id = loginCourier.returnIdCourier(courierLogin, courierPassword);

        DeleteCourier deleteCourier = new DeleteCourier();
        int statusCodeDelete = deleteCourier.deleteCourier(id).extract().statusCode();
        assertThat("Не удалось удалить пользователя",
                statusCodeDelete, equalTo(HttpStatus.SC_OK));
        System.out.println("Курьер удален, база чистая");
    }
}