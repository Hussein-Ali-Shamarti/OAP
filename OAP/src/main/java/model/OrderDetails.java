package model;

/**
 * Represents order details, including quantity, unit price, product code, and order line number.
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
   private String productCode;
   private int orderNumber;

   /**
    * Constructor for OrderDetails class.
    * 
    * @param quantityOrdered The quantity of the product ordered.
    * @param priceEach       The unit price of the product.
    * @param productCode     The product code.
    * @param orderNumber     The order number.
    * @param orderLineNr     The order line number.
    */
   public OrderDetails(int quantityOrdered, double priceEach, String productCode, int orderNumber, int orderLineNr) {
      this.quantityOrdered = quantityOrdered;
      this.priceEach = priceEach;
      this.productCode = productCode;
      this.orderNumber = orderNumber;
      this.orderLineNr = orderLineNr;
   }

   public OrderDetails(int quantityOrdered, double priceEach, String productCode,  int orderLineNr) {
	      this.quantityOrdered = quantityOrdered;
	      this.priceEach = priceEach;
	      this.productCode = productCode;
	      this.orderLineNr = orderLineNr;
	   }
   
   public int getQuantityOrdered() {
	    return this.quantityOrdered;
	}

	public double getPriceEach() {
	    return this.priceEach;
	}

	public int getOrderLineNr() {
	    return this.orderLineNr;
	}

	public String getProductCode() {
	    return this.productCode;
	}

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
   public void setOrderLineNr(int orderLineNr) {
      this.orderLineNr = orderLineNr;
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
    * Calculates the subtotal amount.
    * 
    * @return The subtotal amount.
    */
   public double calculateSubTotal() {
      return quantityOrdered * priceEach;
   }

public Object getquantityOrdered() {
	// TODO Auto-generated method stub
	return null;
}

public Object getOrderLineNumber() {
	// TODO Auto-generated method stub
	return null;
}
}
