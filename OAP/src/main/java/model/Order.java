package model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {

    private int orderNumber;
    private Date orderDate;
    private Date requiredDate;
    private Date shippedDate;
    private String status;
    private String comments;
    private int customerNumber;
    private List<OrderDetails> orderDetailsList;

    /**
     * Constructor for the Order class that sets all fields.
     * @param OrderNumber The unique serial number consisting of 5 digits.
     * @param requiredDate The date when the order is required to be delivered.
     * @param shippedDate The date when the order was actually shipped.
     * @param status The status of the order.
     * @param comments Any comments related to the order.
     * @param customerNumber The customer number associated with the order.
     * @param orderDate 
     */
    
    public Order(Date requiredDate, Date shippedDate, String status, String comments, int customerNumber, Date orderDate) {
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.status = status;
        this.comments = comments;
        this.customerNumber = customerNumber;
        this.orderDate = orderDate;
        this.orderDetailsList = new ArrayList<>();
    }

    public Order(ResultSet rs) {
		// TODO Auto-generated constructor stub
	}

	public int getOrderNumber() {
        return this.orderNumber;
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public Date getRequiredDate() {
        return this.requiredDate;
    }

    public Date getShippedDate() {
        return this.shippedDate;
    }

    public String getStatus() {
        return this.status;
    }

    public String getComments() {
        return this.comments;
    }

    public int getCustomerNumber() {
        return this.customerNumber;
    }
    
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public void addOrderDetail(OrderDetails orderDetail) {
        orderDetailsList.add(orderDetail);
    }

    public double calculateOrderTotal() {
        double total = 0.0;
        for (OrderDetails orderDetail : orderDetailsList) {
            total += orderDetail.calculateSubTotal();
        }
        return total;
    }
}

