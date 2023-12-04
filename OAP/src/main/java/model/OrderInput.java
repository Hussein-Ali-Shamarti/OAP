package model;

import java.util.List;

/**
 * Represents an order entity with information such as order number, dates, status, comments,

 * customer number, and associated order date.
 * 
 * Orders may also contain order details, which are not implemented in this version.
 * 
 * @author 7094
 * @version 07.11.2023
 */

public class OrderInput {
    private Order order;
    private List<OrderDetails> orderDetailsList;

    /**
     * Constructs an OrderInput instance with a specified order and its associated order details.
     *
     * @param order The order object.
     * @param orderDetailsList The list of OrderDetails associated with the order.
     */
   
    public OrderInput(Order order, List<OrderDetails> orderDetailsList) {
        this.order = order;
        this.orderDetailsList = orderDetailsList;
    }

    /**
     * Retrieves the Order object contained within this OrderInput.
     *
     * @return The Order object.
     */
    
    public Order getOrder() {
        return order;
    }

    /**
     * Retrieves the list of OrderDetails associated with the Order object in this OrderInput.
     *
     * @return A list of OrderDetails objects.
     */
    
    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }
}