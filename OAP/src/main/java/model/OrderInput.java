package model;

import java.util.List;

public class OrderInput {
    private Order order;
    private List<OrderDetails> orderDetailsList;

    public OrderInput(Order order, List<OrderDetails> orderDetailsList) {
        this.order = order;
        this.orderDetailsList = orderDetailsList;
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }
}