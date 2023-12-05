package model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Represents an order entity with information such as order number, dates, status, comments,
 * customer number, and associated order date.
 * 
 * Orders may also contain order details, which are not implemented in this version.
 * 
 * @author 7088
 */

public class Order {

    private int orderNumber;
    private Date orderDate;
    private Date requiredDate;
    private Date shippedDate;
    private String status;
    private String comments;
    private List<OrderDetails> orderDetailsList;

    
    private int customerNumber;

    /**
     * Constructor for the Order class that sets essential fields.
     * 
     * @param requiredDate   The date when the order is required to be delivered.
     * @param shippedDate    The date when the order was actually shipped.
     * @param status         The status of the order.
     * @param comments       Any comments related to the order.
     * @param customerNumber The customer number associated with the order.
     * @param orderDate      The date when the order was placed.
     */
    
    public Order(Date requiredDate, Date shippedDate, String status, String comments, int customerNumber, Date orderDate) {
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;	
        this.status = status;
        this.comments = comments;
        this.customerNumber = customerNumber;
        this.orderDate = orderDate;
      
        

    }
    
    /**
     * Constructs an Order object with the specified details.
     * 
     * @param orderNumber The unique identifier for the order.
     * @param requiredDate The date by which the order is required.
     * @param shippedDate The date on which the order was shipped.
     * @param status The current status of the order (e.g., "Shipped", "Pending").
     * @param comments Additional comments about the order.
     * @param customerNumber The unique identifier of the customer who placed the order.
     * @param orderDate The date on which the order was placed.
     */
    
    public Order(int orderNumber, Date requiredDate, Date shippedDate, String status, String comments, int customerNumber, Date orderDate) {
    	this.orderNumber = orderNumber;
    	this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;	
        this.status = status;
        this.comments = comments;
        this.customerNumber = customerNumber;
        this.orderDate = orderDate;
      
    
    }
    
    
    /**
     * Gets the order number.
     * 
     * @return The order number.
     */
    public int getOrderNumber() {
        return this.orderNumber;
    }

    /**
     * Gets the order date.
     * 
     * @return The order date.
     */
    public Date getOrderDate() {
        return this.orderDate;
    }

    /**
     * Gets the required date for the order.
     * 
     * @return The required date.
     */
    public Date getRequiredDate() {
        return this.requiredDate;
    }

    /**
     * Gets the shipped date of the order.
     * 
     * @return The shipped date.
     */
    public Date getShippedDate() {
        return this.shippedDate;
    }

    /**
     * Gets the status of the order.
     * 
     * @return The status.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Gets any comments related to the order.
     * 
     * @return The comments.
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * Gets the customer number associated with the order.
     * 
     * @return The customer number.
     */
    public int getCustomerNumber() {
        return this.customerNumber;
    }

    /**
     * Sets the order number.
     * 
     * @param orderNumber The order number to set.
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Sets the order date.
     * 
     * @param orderDate The order date to set.
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Sets the required date for the order.
     * 
     * @param requiredDate The required date to set.
     */
    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    /**
     * Sets the shipped date of the order.
     * 
     * @param shippedDate The shipped date to set.
     */
    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    /**
     * Sets the status of the order.
     * 
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets any comments related to the order.
     * 
     * @param comments The comments to set.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Sets the customer number associated with the order.
     * 
     * @param customerNumber The customer number to set.
     */
    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }
    
    @Override
    public String toString() {
        /**
         * Provides a string representation of the Order object, including various order details.
         *
         * @return A string that represents the order, including order number, dates, status, and comments.
         */
        return "orderID: " + orderNumber + ", orderDate: " + orderDate + ", requiredDate: " + requiredDate + 
               ", shippedDate: " + shippedDate + ", status: " + status + ", comments: " + comments;
    }
    
    /**
     * Calculates the total value of the order by summing the subtotal of each order detail.
     *
     * @return The total value of the order as a BigDecimal.
     */

    public BigDecimal calculateOrderTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDetails details : orderDetailsList) {
            total = total.add(details.calculateSubTotal());
        }
        return total;
    }
    
    /**
     * Retrieves the list of OrderDetails associated with this order.
     *
     * @return A list of OrderDetails objects.
     */

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }

    /**
     * Sets the list of OrderDetails for this order.
     *
     * @param orderDetailsList The list of OrderDetails objects to be associated with this order.
     */
    
    public void setOrderDetailsList(List<OrderDetails> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

}