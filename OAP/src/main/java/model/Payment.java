/**
 * 
 * File: Payment.java
 * Description: This is an entity class for the payments and encapsulates the details of a payment, including the date of payment and the amount.
 * The payment data comes from a 3rd-party and should be final (not editable).
 * @author Kim
 * @version 07.11.2023
*/ 

package model;

import java.util.Date;

/**
 * Represents a payment, including check number, payment date, and the paid amount.
 * 
 * <p>This class contains a constructor and getter methods for retrieving check number,
 * payment date, and the total paid amount.</p>
 * 
 * <p>Note: The check number is a unique serial number consisting of two letters followed by 6 digits.</p>
 * 
 * @author Kim
 * @version [Version Date]
 */
public class Payment {
    
    // Declaration of private data fields 
    private final String checkNr;
    private final Date paymentDate;
    private final double amount;

    /**
     * Constructor for the Payment class to create objects.
     * 
     * @param checkNr      The unique serial number consisting of two letters followed by 6 digits.
     * @param paymentDate  The date when the payment was made.
     * @param amount       The total amount paid.
     */
    public Payment(String checkNr, Date paymentDate, double amount) {
        this.checkNr = checkNr;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    /**
     * Gets the check number.
     * 
     * @return The check number.
     */
    public String getCheckNr() {
        return checkNr;
    }

    /**
     * Gets the payment date.
     * 
     * @return The payment date.
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * Gets the paid amount.
     * 
     * @return The total paid amount.
     */
    public double getAmount() {
        return amount;
    }
}