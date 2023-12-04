package model;

/**
 * Represents address information for an entity. This abstract class encapsulates basic information of an address,
 * including first and second address lines, city, state, postal code, country, and phone.
 * It serves as a base for more specific address implementations.
 * 
 * @author Henrik
 * @version: 07.11.2023
 */

public abstract class Address {
    protected String addressLine2;
    protected String addressLine1;
    protected String city;
    protected String state;
    protected String postalCode;
    private String country;
    private String phone;

    /**
     * Constructs an Address with specified details.
     *
     * @param addressLine1 First address line.
     * @param addressLine2 Second address line.
     * @param city         City of the address.
     * @param state        State of the address.
     * @param postalCode   Postal code of the address.
     * @param country      Country of the address.
     * @param phone        Phone number associated with the address.
     */
    
    public Address(String addressLine1, String addressLine2, String city, String state, String postalCode, String country, String phone) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
    } 

    /**
     * Gets the first address line.
     * 
     * @return The first address line.
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Gets the second address line. Returns a default message if the second address is not available.
     * 
     * @return The second address line, or a default message if it is not available.
     */
    public String getAddressLine2() {
        if (addressLine2 == null || addressLine2.isEmpty()) {
            return "No second address registered";
        }
        return addressLine2;
    }

    /**
     * Gets the city of the address.
     * 
     * @return The city of the address.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the state of the address.
     * 
     * @return The state of the address.
     */
    public String getState() {
        return state;
    }

    /**
     * Gets the postal code of the address.
     * 
     * @return The postal code of the address.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gets the country of the address.
     * 
     * @return The country of the address.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the phone number associated with the address.
     * 
     * @return The phone number.
     */
    public String getPhone() {
        return phone;
    }

   /**
    * Sets the first address line.
    * 
    * @param addressLine1 First address line.
    */
   public void setAddressLine1(String addressLine1) {
       this.addressLine1 = addressLine1;
   }

   /**
    * Sets the second address line.
    * 
    * @param addressLine2 Second address line.
    */
   public void setAddressLine2(String addressLine2) {
       this.addressLine2 = addressLine2;
   }

   /**
    * Sets the city of the address.
    * 
    * @param city City of the address.
    */
   public void setCity(String city) {
       this.city = city;
   }

   /**
    * Sets the state of the address.
    * 
    * @param state State of the address.
    */
   public void setState(String state) {
       this.state = state;
   }

   /**
    * Sets the postal code of the address.
    * 
    * @param postalCode Postal code of the address.
    */
   public void setPostalCode(String postalCode) {
       this.postalCode = postalCode;
   }

   /**
    * Sets the country of the address.
    * 
    * @param country Country of the address.
    */
   public void setCountry(String country) {
       this.country = country;
   }

   /**
    * Sets the phone number associated with the address.
    * 
    * @param phone Phone number.
    */
   public void setPhone(String phone) {
       this.phone = phone;
   }

   /**
    * Abstract method to get the full address as a string.
    * This method should be implemented by subclasses to define the format of the full address.
    * 
    * @return The full address as a string.
    */
   public abstract String getAddress();
}
