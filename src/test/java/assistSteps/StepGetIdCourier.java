package assistSteps;

import requests.courier.PostCreateCourier;
import requests.courier.PostLoginCourier;

public class StepGetIdCourier {

    public int getIdCourier(String courierLogin, String courierPassword, String courierFirstName) {

        PostCreateCourier postCreateCourier = new PostCreateCourier();
        postCreateCourier.createCourier(courierLogin, courierPassword, courierFirstName);

        PostLoginCourier postLoginCourier = new PostLoginCourier();
        return postLoginCourier.returnIdCourier(courierLogin, courierPassword);
    }
}