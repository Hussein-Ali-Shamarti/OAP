/**
 * Represents an order entity with information such as order number, dates, status, comments,

 * customer number, and associated order date.
 * 
 * <p>Orders may also contain order details, which are not implemented in this version.</p>
 * 
 * @author 7094
 * @version 07.11.2023
 */
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