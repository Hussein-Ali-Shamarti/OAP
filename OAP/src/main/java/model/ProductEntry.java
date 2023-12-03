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

import javax.swing.JTextField;

public class ProductEntry {
    private JTextField productCodeField;
    private JTextField quantityOrderedField;
    private JTextField orderLineNumberField; // Added field
    private JTextField buyPriceField; // This could be what you mean by priceEachField
    private JTextField msrpField;

    // Constructor
    public ProductEntry(
            JTextField productCodeField,
            JTextField quantityOrderedField,
            JTextField orderLineNumberField, // Added parameter
            JTextField buyPriceField,
            JTextField msrpField) {
        this.productCodeField = productCodeField;
        this.quantityOrderedField = quantityOrderedField;
        this.orderLineNumberField = orderLineNumberField; // Added assignment
        this.buyPriceField = buyPriceField;
        this.msrpField = msrpField;
    }

    // Getters and setters for all fields

    public JTextField getProductCodeField() {
        return productCodeField;
    }

    public void setProductCodeField(JTextField productCodeField) {
        this.productCodeField = productCodeField;
    }

    public JTextField getQuantityOrderedField() {
        return quantityOrderedField;
    }

    public void setQuantityOrderedField(JTextField quantityOrderedField) {
        this.quantityOrderedField = quantityOrderedField;
    }

    public JTextField getOrderLineNumberField() { // Added getter
        return orderLineNumberField;
    }

    public void setOrderLineNumberField(JTextField orderLineNumberField) { // Added setter
        this.orderLineNumberField = orderLineNumberField;
    }

    public JTextField getBuyPriceField() {
        return buyPriceField;
    }

    public void setBuyPriceField(JTextField buyPriceField) {
        this.buyPriceField = buyPriceField;
    }

    public JTextField getMsrpField() {
        return msrpField;
    }

    public void setMsrpField(JTextField msrpField) {
        this.msrpField = msrpField;
    }
}
