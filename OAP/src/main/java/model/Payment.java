package model;

import java.util.Date;

/**
 * Represents a payment, including check number, payment date, and the paid amount.
 * 
 * This class contains a constructor and getter methods for retrieving check number,
 * payment date, and the total paid amount.
 * 
 * @author Kim
 * @version 07.11.2023
 */
public class Payment {
    
  
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