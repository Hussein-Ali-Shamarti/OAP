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

/**
 * Represents a product entry in an order, containing fields for product code, quantity ordered,
 * order line number, buy price, and manufacturer's suggested retail price (MSRP).
 */
public class ProductEntry {
    // Attributes of ProductEntry
    private JTextField productCodeField;
    private JTextField quantityOrderedField;
    private JTextField orderLineNumberField;
    private JTextField buyPriceField;
    private JTextField msrpField;
    /**
     * Constructs a new ProductEntry with the given fields.
     * 
     * @param productCodeField       The text field for product code.
     * @param quantityOrderedField   The text field for quantity ordered.
     * @param orderLineNumberField   The text field for order line number.
     * @param buyPriceField          The text field for buy price.
     * @param msrpField              The text field for MSRP.
     */
    public ProductEntry(JTextField productCode, JTextField quantityOrdered, JTextField orderLineNumber, JTextField buyPrice, JTextField msrp) {
        this.productCodeField = productCode;
        this.quantityOrderedField = quantityOrdered;
        this.orderLineNumberField = orderLineNumber;
        this.buyPriceField = buyPrice;
        this.msrpField = msrp;
    }

    public ProductEntry() {
        this.productCodeField = new JTextField(10);
        this.quantityOrderedField = new JTextField(10);
        this.orderLineNumberField = new JTextField(10);
        this.buyPriceField = new JTextField(10);
        this.msrpField = new JTextField(10);
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

    public JTextField getOrderLineNumberField() {
        return orderLineNumberField;
    }

    public void setOrderLineNumberField(JTextField orderLineNumberField) {
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
