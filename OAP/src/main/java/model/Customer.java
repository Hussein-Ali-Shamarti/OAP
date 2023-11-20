/**
 * Description: This is an entity class for "customer" and encapsulates the basic information of a customer.
 * This class represents information about a customers information, such as customer number, company name, contact name, sales represenatitve and credit limit.
 * @author Henrik
 * @version: 07.11.23
 */
package model;

import java.math.BigDecimal;
import java.util.*;

import database.DataBaseConnection;

public class Customer {
    private int customerNr;
    private String companyName;
    private String contactLastName;
    private String contactFirstName;
    private int salesRepEmployeeNr;
    private BigDecimal creditLimit;

    /**
     * Constructor for Customer class.
     * @param customerNr            The customer number.
     * @param companyName           The company name.
     * @param contactLastName       The last name of a customer
     * @param contactFirstName      The first name of a customer
     * @param salesRepEmployeeNr    The sales rep for this customer
     * @param creditLimit           The credit limit for the customer   
     */

    public Customer(int customerNr, String companyName, String contactLastName, String contactFirstName, int salesRepEmployeeNr, BigDecimal creditLimit) {
        this.customerNr = customerNr;
        this.companyName = companyName;
        this.contactLastName = contactLastName;
        this.contactFirstName = contactFirstName;
        this.salesRepEmployeeNr = salesRepEmployeeNr;
        this.creditLimit = creditLimit;
    }
    /**
     * Get the customer number.
     * @return The customer number.
     */
    public int getcustomerNr() {
        return customerNr;
    }

    public String getcompanyName() {
        return companyName;
    }

    public String getcontactLastName() {
        return contactLastName;
    }

    public String getcontactFirstName() {
        return contactFirstName;
    }

    public int getsalesRepEmployeeNr() {
        return salesRepEmployeeNr;
    }

    /**
     * Get the credit limit of the customer.
     * @return The credit limit for the customer
     */
    public BigDecimal getcreditLimit() {
        return creditLimit;
    }

    /**
     * Setter methods for the Customer class
     */
    public void setCustomerNr(int customerNr) {
        this.customerNr = customerNr;
    }      

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public void setSalesRepEmployeeNr(int salesRepEmployeeNr) {
        this.salesRepEmployeeNr = salesRepEmployeeNr;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    /**
     * Method to get the address information from the Address class and combine it with companyName to provide useful information. Overrides the method in the abstract class.
     */
    public class CustomerAddress extends Address {
        public CustomerAddress(String addressLine1, String addressLine2, String city, String state, String postalCode, String country, int phone) {
            super(addressLine1, addressLine2, city, state, postalCode, country, phone);
        }

        @Override
        public String getAddress() {
            String fullAddress = getaddressLine1() + " " + getaddressLine2() + ", " + getcity() + ", " + getstate() + " " + getPostalCode() + ", " + getcountry() + " " + getphone();
            return fullAddress + " - " + Customer.this.companyName;
        }
    }
}