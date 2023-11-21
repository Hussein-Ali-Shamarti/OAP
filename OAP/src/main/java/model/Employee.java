/**
 * File: Employee.java
 * Description: This is an entity class for "Employee" and encapsulates the basic information of an employee.
 * This class represents information about an employee's attributes, such as employee number, firstname, lastname,
 * job title, email, role, and more.
 * @author Marziyeh
 * @version 09.11.2023
 */ 
package model;

public class Employee {

    // Private data fields
    private int employeeNumber;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String email;
    private int reportsTo;
    private String extension;
    private String officeCode;

    // Constructor for employee class with parameters
    public Employee(int employeeNumber, String firstName, String lastName, String extension, String email, String officeCode, int reportsTo, String jobTitle) {
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.email = email;
        this.reportsTo = reportsTo;
        this.extension = extension;
        this.officeCode = officeCode;
    }

    // Getter and Setter methods
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(int reportsTo) {
        this.reportsTo = reportsTo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }
}
