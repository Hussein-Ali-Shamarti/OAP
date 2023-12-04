package model;

/**
* This is an entity class for "Employee" and encapsulates the basic information of an employee.
* This class represents information about an employee's attributes, such as employee number, firstname, lastname,
* job title, email, role, and more.
* 
* The class inherits from the Address class, incorporating address-related information.
* 
* @author Marziyeh
* @version 11.11.2023
*/ 

public class Employee extends Address {

    private int employeeNumber;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String email;
    private int reportsTo;
    private String extension;
    private String officeCode;

    /**
     * Constructor for the Employee class with parameters.
     *
     * @param employeeNumber          The employee number.
     * @param firstName               The first name of the employee.
     * @param lastName                The last name of the employee.
     * @param extension               The extension number.
     * @param email                   The email address of the employee.
     * @param officeCode              The office code.
     * @param reportsTo               The employee to whom this employee reports.
     * @param jobTitle                The job title of the employee.
     */
    public Employee(int employeeNumber, String firstName, String lastName, String extension, String email, String officeCode, int reportsTo, String jobTitle) {
        super(null, null, null, null, null, null, null); 
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.email = email;
        this.reportsTo = reportsTo;
        this.extension = extension;
        this.officeCode = officeCode;
    }

    /**
     * Gets the employee number.
     * 
     * @return The employee number.
     */
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Gets the first name of the employee.
     * 
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the employee.
     * 
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the job title of the employee.
     * 
     * @return The job title.
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the job title of the employee.
     * 
     * @param jobTitle The job title to set.
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Gets the email address of the employee.
     * 
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the employee.
     * 
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the employee to whom this employee reports.
     * 
     * @return The employee to whom reports are made.
     */
    public int getReportsTo() {
        return reportsTo;
    }

    /**
     * Sets the employee to whom this employee reports.
     * 
     * @param reportsTo The employee number to set.
     */
    public void setReportsTo(int reportsTo) {
        this.reportsTo = reportsTo;
    }

    /**
     * Gets the extension number.
     * 
     * @return The extension number.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Sets the extension number.
     * 
     * @param extension The extension number to set.
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Gets the office code.
     * 
     * @return The office code.
     */
    public String getOfficeCode() {
        return officeCode;
    }

    /**
     * Sets the office code.
     * 
     * @param officeCode The office code to set.
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }
    
    /**
     * Sets the employeeNumber.
     * 
     * @param employeenumber the employeenumber to set.
     */
    
	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	
    /**
     * Sets the firstname.
     * 
     * @param firstname the firstname to set.
     */

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
     * Sets the lastname.
     * 
     * @param lastname the lastname to set.
     */

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	  @Override
	    public String getAddress() {
	        return getAddressLine1() + ", " + getAddressLine2() + ", " + getCity() + ", " +
	               getState() + ", " + getPostalCode() + ", " + getCountry();
	    }
}
