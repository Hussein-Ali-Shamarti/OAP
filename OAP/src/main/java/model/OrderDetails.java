package model;

/**
 * Represents order details, including quantity, unit price, and order line number.
 * 
 * <p>This class contains a constructor, getter and setter methods, and a method to calculate
 * the subtotal price of an order.</p>
 * 
 * @author Hussein
 * @version 07.11.2023
 */
public class OrderDetails {
   
   // Declaration of private data fields 
   private int quantityOrdered;
   private double priceEach;
   private int orderLineNr;

   /**
    * Constructor for OrderDetails class.
    * 
    * @param quantityOrdered The quantity of the product ordered.
    * @param priceEach       The unit price of the product.
    * @param orderLineNr     The order line number.
    */
   public OrderDetails(int quantityOrdered, double priceEach, int orderLineNr) {
      this.quantityOrdered = quantityOrdered;
      this.priceEach = priceEach;
      this.orderLineNr = orderLineNr;
   }

   /**
    * Gets the quantity of the product ordered.
    * 
    * @return The quantity ordered.
    */
   public int getQuantityOrdered() {
      return this.quantityOrdered;
   }

   /**
    * Gets the unit price of the product.
    * 
    * @return The unit price.
    */
   public double getPriceEach() {
      return this.priceEach;
   }

   /**
    * Gets the order line number.
    * 
    * @return The order line number.
    */
   public int getorderLineNr() {
      return this.orderLineNr;
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
   public void setOrderLineNr(int orderLineNr) {
      this.orderLineNr = orderLineNr;
   }

   /**
    * Calculates the subtotal amount.
    * 
    * @return The subtotal amount.
    * 
   
   /*public double calculateSubTotal() {
      return quantityOrdered * priceEach;
   }*/
}