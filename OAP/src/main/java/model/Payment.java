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

public class Payment {
    
    //Declaration of private data fields 
    
    private final String checkNr;
    private final Date paymentDate;
    private final double amount;

   /**
   * Constructor for payment class to create objects
   * @param checkNr        The unique serial number consists of two letters followed by 6 digits.
   */

    public Payment(String checkNr, Date paymentDate, double amount){
        this.checkNr = checkNr;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

   /**
   * Getter methods for following variables.
   * @return check number, payment date, and the total paid amount. 
   */

    public String getCheckNr(){
        return checkNr;
    }
    public Date getPaymentDate(){
        return paymentDate;
    }
    public double getAmount(){
        return amount;
    }
}