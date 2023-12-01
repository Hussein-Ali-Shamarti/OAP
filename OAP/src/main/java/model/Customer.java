package model;

import java.math.BigDecimal;

/**
 * Represents information about a customer, including customer number, company name,
 * contact name, sales representative, credit limit, and address information.
 * 
 * <p>This class serves as an entity class for the "customer" entity.</p>
 * 
 * @author Henrik
 * @version 07.11.23
 */
public class Customer extends Address {

    private int customerNumber;
    private String customerName;
    private String contactLastName;
    private String contactFirstName;
   

    private int salesRepEmployeeNumber;
    private BigDecimal creditLimit;

    /**
     * Constructor to create a Customer object with the specified attributes.
     *
     * @param customerNumber          The customer number.
     * @param customerName            The customer name.
     * @param contactLastName        The contact's last name.
     * @param contactFirstName       The contact's first name.
     * @param salesRepEmployeeNumber  The sales representative's employee number.
     * @param creditLimit             The credit limit.
     */
    public Customer(int customerNumber, String customerName, String contactLastName, String contactFirstName,
                    String phone, String addressLine1, String addressLine2, String city,
                    String state, String postalCode, String country,
                    int salesRepEmployeeNumber, BigDecimal creditLimit) {
        super(addressLine1, addressLine2, city, state, postalCode, country, phone);
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.contactLastName = contactLastName;
        this.contactFirstName = contactFirstName;
        this.salesRepEmployeeNumber = salesRepEmployeeNumber;
        this.creditLimit = creditLimit;
    }

    /**
     * Gets the customer number.
     * 
     * @return The customer number.
     */
    public int getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the customer number.
     * 
     * @param customerNumber The customer number to set.
     */
    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**
     * Gets the customer name.
     * 
     * @return The customer name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     * 
     * @param customerName The customer name to set.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the contact's last name.
     * 
     * @return The contact's last name.
     */
    public String getContactLastName() {
        return contactLastName;
    }

    /**
     * Sets the contact's last name.
     * 
     * @param contactLastName The contact's last name to set.
     */
    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    /**
     * Gets the contact's first name.
     * 
     * @return The contact's first name.
     */
    public String getContactFirstName() {
        return contactFirstName;
    }

    /**
     * Sets the contact's first name.
     * 
     * @param contactFirstName The contact's first name to set.
     */
    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }


    /**
     * Gets the sales representative's employee number.
     * 
     * @return The sales representative's employee number.
     */
    public int getSalesRepEmployeeNumber() {
        return salesRepEmployeeNumber;
    }

    /**
     * Sets the sales representative's employee number.
     * 
     * @param salesRepEmployeeNumber The sales representative's employee number to set.
     */
    public void setSalesRepEmployeeNumber(int salesRepEmployeeNumber) {
        this.salesRepEmployeeNumber = salesRepEmployeeNumber;
    }

    /**
     * Gets the credit limit.
     * 
     * @return The credit limit.
     */
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    /**
     * Sets the credit limit.
     * 
     * @param creditLimit The credit limit to set.
     */
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    /**
     * Gets the complete address as a formatted string.
     * 
     * @return The complete address.
     */
    @Override
    public String getAddress() {
        // You may customize the format of the address based on your requirements.
        return getAddressLine1() + ", " + getAddressLine2() + ", " + getCity() + ", " +
               getState() + ", " + getPostalCode() + ", " + getCountry();
    }
}