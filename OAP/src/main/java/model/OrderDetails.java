package model;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents order details, including quantity, unit price, product code, and order line number.
 * 
 * This class contains a constructor, getter and setter methods, and a method to calculate
 * the subtotal price of an order.
 * 
 * @Author 7094
 * @CoAuthor Kim
 * @version 07.11.2023
 */

public class OrderDetails {
	   
	   private int quantityOrdered;
	   private double priceEach; 
	   private String productCode;
	   private int orderNumber;
	   private int orderLineNumber;

   /**
    * Constructor for OrderDetails class.
    * 
    * @param quantityOrdered The quantity of the product ordered.
    * @param priceEach       The unit price of the product.
    * @param productCode     The product code.
    * @param orderNumber     The order number.
    * @param orderLineNr     The order line number.
    */

	   public OrderDetails(int quantityOrdered, double priceEach, String productCode, int orderNumber, int orderLineNumber) {
	      this.quantityOrdered = quantityOrdered;
	      this.priceEach = priceEach;
	      this.productCode = productCode;
	      this.orderNumber = orderNumber;
	      this.orderLineNumber = orderLineNumber;
	   }

	   /**
	    * Constructs a new OrderDetails object with the specified details.
	    *
	    * @param quantityOrdered The number of units ordered.
	    * @param priceEach The price per unit of the product.
	    * @param productCode The code identifying the product.
	    * @param orderLineNumber The line number of this order detail in the order.
	    */
	   public OrderDetails(int quantityOrdered, double priceEach, String productCode, int orderLineNumber) {
	       this.quantityOrdered = quantityOrdered;
	       this.priceEach = priceEach;
	       this.productCode = productCode;
	       this.orderLineNumber = orderLineNumber;
	   }

	   /**
	    * Gets the quantity of products ordered.
	    *
	    * @return The number of units ordered.
	    */
	   public int getQuantityOrdered() {
	       return this.quantityOrdered;
	   }

	   /**
	    * Gets the price per unit of the product.
	    *
	    * @return The price per unit.
	    */
	   public double getPriceEach() {
	       return this.priceEach;
	   }

	   /**
	    * Gets the order line number.
	    *
	    * @return The line number of this detail in the order.
	    */
	   public int getOrderLineNr() {
	       return this.orderLineNumber;
	   }

	   /**
	    * Gets the product code.
	    *
	    * @return The code identifying the product.
	    */
	   public String getProductCode() {
	       return this.productCode;
	   }

	   /**
	    * Gets the order number this detail is associated with.
	    *
	    * @return The order number.
	    */
	   public int getOrderNumber() {
	       return this.orderNumber;
	   }
	
   /**
    * Sets the quantity of the product ordered.
    * 
    * @param quantityOrdered The quantity to set.
    */
	
   public void setQuantityOrdered(int quantityOrdered) {
      this.quantityOrdered = quantityOrdered;
   }

   /**
    * Sets the unit price of the product.
    * 
    * @param priceEach The unit price to set.
    */
   
   public void setPriceEach(double priceEach) {
      this.priceEach = priceEach;
   }

   /**
    * Sets the order line number.
    * 
    * @param orderLineNr The order line number to set.
    */
   
   public void setOrderLineNumber(int orderLineNumber) {
      this.orderLineNumber = orderLineNumber;
   }
   
   /**
    * Sets the product code.
    * 
    * @param productCode The product code to set.
    */
   
   public void setProductCode(String productCode) {
      this.productCode = productCode;
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
    * Calculates the subtotal amount for the order detail.
    * This converts priceEach to BigDecimal for precise calculations.
    * 
    * @return The subtotal amount as BigDecimal.
    */
   
   public BigDecimal calculateSubTotal() {
       BigDecimal price = BigDecimal.valueOf(priceEach);
       return price.multiply(BigDecimal.valueOf(quantityOrdered)).setScale(2, RoundingMode.HALF_UP);
   }
   
   /**
    * Gets the quantity of products ordered.
    * 
    * Note: This method currently returns null and needs to be implemented.
    *
    * @return The number of units ordered, or null if not implemented.
    */
   public Object getquantityOrdered() {
       return null;
   }

   /**
    * Gets the order line number.
    * 
    * Note: This method currently returns null and needs to be implemented.
    *
    * @return The line number of this detail in the order, or null if not implemented.
    */
   public Object getOrderLineNumber() {
       return null;
   }

}
