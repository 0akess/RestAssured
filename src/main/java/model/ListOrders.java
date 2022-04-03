package model;

import java.util.List;

public class ListOrders {
    private List<Orders> orders;


    public ListOrders(){
    }

    public ListOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
}
