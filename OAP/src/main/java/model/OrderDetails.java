/**
 * 
 * File: OrderDetails.java
 * Description: This is a class for order details and encapsulates the details of an order, including order quantities and the unit price. 
 * This class contains a cunstructor, and getter & setter methods.
 * This class contains a calculateSubTotal() method that returns sub total price of an order.
 * @author Kim
 * @version 07.11.2023
*/ 

package model;

public class OrderDetails {
   
   //Declaration of private data fields 
   private int quantityOrdered;
   private double priceEach;
   private int orderLineNr;

   /**
   * Constructor for OrderDetails class.
   * @param quantityOrdered    The quantity of the product ordered.
   * @param priceEach          The unit price of the product.
   * @param orderLineNr        The order line number.
   */

   public OrderDetails(int quantityOrdered, double priceEach, int orderLineNr) {
      this.quantityOrdered = quantityOrdered;
      this.priceEach = priceEach;
      this.orderLineNr = orderLineNr;
   }

   /**
   * Getter methods for product quantity, unit price, and the order line number.
   * @return q'ty, unit price, order line number.
   */

   public int getQuantityOrdered() {
      return this.quantityOrdered;
   }

   public double getPriceEach() {
      return this.priceEach;
   }

   public int getorderLineNr() {
      return this.orderLineNr;
   }

   /**
   * Setter methods for product quantity, unit price, and the order line number.
   * setting @param quantityOrdered, @param priceEach, @param orderLineNr   
   */

   public void setQuantityOrdered(int quantityOrdered){
        this.quantityOrdered = quantityOrdered;
    }
   public void setPriceEach(double priceEach){
        this.priceEach = priceEach;
    }
   public void setOrderLineNr(int orderLineNr){
        this.orderLineNr = orderLineNr;
    }

   /**
   * Calculation methods for a sub total amount
   * @return subtotal     
   */

   public double calculateSubTotal() {
      return quantityOrdered * priceEach;
   }
}
