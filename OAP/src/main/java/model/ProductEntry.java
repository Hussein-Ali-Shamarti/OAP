package model;

import javax.swing.JTextField;
/**
 * Represents an order entity with information such as order number, dates, status, comments,
 * customer number, and associated order date.
 * Orders may also contain order details, which are not implemented in this version.
 * 
 * @author 7094
 */

public class ProductEntry {
    private JTextField productCodeField;
    private JTextField quantityOrderedField;
    private JTextField orderLineNumberField;
    private JTextField buyPriceField;
    private JTextField msrpField;
    
    /**
     * Constructs a new ProductEntry with the given fields.
     * 
     * @param productCode     The text field for product code.
     * @param quantityOrdered  The text field for quantity ordered.
     * @param orderLineNumber   The text field for order line number.
     * @param buyPrice        The text field for buy price.
     * @param msrp             The text field for MSRP.
     */
    
    public ProductEntry(JTextField productCode, JTextField quantityOrdered, JTextField orderLineNumber, JTextField buyPrice, JTextField msrp) {
        this.productCodeField = productCode;
        this.quantityOrderedField = quantityOrdered;
        this.orderLineNumberField = orderLineNumber;
        this.buyPriceField = buyPrice;
        this.msrpField = msrp;
    }
    
    /**
     * Constructs a new ProductEntry instance with initialized text fields.
     */

    public ProductEntry() {
        this.productCodeField = new JTextField(10);
        this.quantityOrderedField = new JTextField(10);
        this.orderLineNumberField = new JTextField(10);
        this.buyPriceField = new JTextField(10);
        this.msrpField = new JTextField(10);
    }
  
    /**
     * Retrieves the text field for the product code.
     *
     * @return The JTextField for the product code.
     */
    
    public JTextField getProductCodeField() {
        return productCodeField;
    }
    
    /**
     * Sets the text field for the product code.
     *
     * @param productCodeField The JTextField to set for the product code.
     */

    public void setProductCodeField(JTextField productCodeField) {
        this.productCodeField = productCodeField;
    }
    
    /**
     * Retrieves the text field for the quantity ordered.
     *
     * @return The JTextField for the quantity ordered.
     */

    public JTextField getQuantityOrderedField() {
        return quantityOrderedField;
    }
    
    /**
     * Sets the text field for the quantity ordered.
     *
     * @param quantityOrderedField The JTextField to set for the quantity ordered.
     */

    public void setQuantityOrderedField(JTextField quantityOrderedField) {
        this.quantityOrderedField = quantityOrderedField;
    }
    
    /**
     * Retrieves the text field for the order line number.
     *
     * @return The JTextField for the order line number.
     */

    public JTextField getOrderLineNumberField() {
        return orderLineNumberField;
    }


    /**
     * Sets the text field for the order line number.
     *
     * @param orderLineNumberField The JTextField to set for the order line number.
     */

    public void setOrderLineNumberField(JTextField orderLineNumberField) {
        this.orderLineNumberField = orderLineNumberField;
    }
    
    /**
     * Retrieves the text field for the buy price.
     *
     * @return The JTextField for the buy price.
     */

    public JTextField getBuyPriceField() {
        return buyPriceField;
    }

    /**
     * Sets the text field for the buy price.
     *
     * @param buyPriceField The JTextField to set for the buy price.
     */
    
    public void setBuyPriceField(JTextField buyPriceField) {
        this.buyPriceField = buyPriceField;
    }
    
    /**
     * Retrieves the text field for the MSRP (Manufacturer's Suggested Retail Price).
     *
     * @return The JTextField for the MSRP.
     */

    public JTextField getMsrpField() {
        return msrpField;
    }
    
    /**
     * Sets the text field for the MSRP (Manufacturer's Suggested Retail Price).
     *
     * @param msrpField The JTextField to set for the MSRP.
     */

    public void setMsrpField(JTextField msrpField) {
        this.msrpField = msrpField;
    }
}
