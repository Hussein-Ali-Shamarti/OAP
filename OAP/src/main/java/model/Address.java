/**
 * Description: This is an entity class for "address" and encapsulates the basic information of a address.
 * This class represents information about address information, such as first address, second address, city, state, postal code, country and phone.
 * @author Henrik
 * @version: 07.11.2023
 */
package model;


public abstract class Address {
    protected String addressLine2;
    protected String addressLine1;
    protected String city;
    protected String state;
    protected String postalCode;
    private String country;
    private int phone;

    /**
     * Constructor for Address class.
     *
     * @param addressLine1      The first address of the customer.
     * @param addressLine2      The second address of the customer.
     * @param city              The city of the customer
     * @param state             The state name of a customer
     * @param postalCode        The postal code of the customer
     * @param country           The country of the customer
     * @param phone             The phone number of the customer
     */
    public Address(String addressLine2, String addressLine1, String city, String state, String postalCode, String country, int phone) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
    }

    /**
     * Get the first address of the customer.
     * @return The first address of the customer.
     */
    public String getaddressLine1() {
        return addressLine1;
    }

   /**
   * Get the second address of the customer.
   * @return The second address of the customer, or a message if it is not available.
   */
    public String getaddressLine2() {
    if (addressLine2 == null || addressLine2.isEmpty()) {
        return "No second address registered";
            }
        return addressLine2;
    }

    public String getcity() {
        return city;
    }

    public String getstate() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

   public String getcountry() {
        return country;
    }
    
   public int getphone() {
        return phone;
    }

    /**
    * Setter methods for the Address class
    */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    /**
     * abstract method to get address information
     */
    public abstract String getAddress();
}