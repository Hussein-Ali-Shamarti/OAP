package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Employee;
import model.EmployeeDAO;
import view.EmployeeView;

/**
 * The EmployeeHandler class is responsible for handling the business logic 
 * associated with employee management in the application. It coordinates the 
 * interactions between the EmployeeView and EmployeeDAO, facilitating operations 
 * such as adding, updating, deleting, searching, and saving employee data.
 * 
 * @author 7080
 * @version 2.12.2023
 */

public class EmployeeHandler {
	
	 /**
     * The view component for employee management operations.
     */
	
	private final EmployeeView employeeView;
    
    /**
     * The DAO component for performing CRUD operations on employee data.
     */
	
    private final EmployeeDAO employeeDAO;
    
    /**
     * Constructs an EmployeeHandler with the specified view and DAO.
     * 
     * @param employeeView The EmployeeView instance used for UI interactions.
     * @param employeeDAO The EmployeeDAO instance used for data access operations.
     */
    
    public EmployeeHandler(EmployeeView employeeView, EmployeeDAO employeeDAO) {
        this.employeeView = employeeView;
        this.employeeDAO = employeeDAO;
    }
    
    /**
     * Returns an ActionListener for adding a new employee.
     * 
     * @return ActionListener for the add employee action.
     */
    public ActionListener getAddEmployeeButtonListener() {
        return this::addEmployee;
    }
    
    /**
     * Returns an ActionListener for updating an employee.
     * 
     * @return ActionListener for the update employee action.
     */
    public ActionListener getUpdateEmployeeButtonListener() {
        return this::updateEmployee;
    }
    
    /**
     * Returns an ActionListener for deleting an employee.
     * 
     * @return ActionListener for the delete employee action.
     */
    public ActionListener getDeleteEmployeeButtonListener() {
        return this::deleteEmployee;
    }
    
    /**
     * Returns an ActionListener for searching employees.
     * 
     * @return ActionListener for the search employee action.
     */
    public ActionListener getSearchEmployeeButtonListener() {
        return this::searchEmployee;
    }
    
    /**
     * Returns an ActionListener for saving employee data to a file.
     * 
     * @return ActionListener for the save employee data action.
     */
    public ActionListener getSaveEmployeeButtonListener() {
        return this::saveEmployeesToFile;
    }
    
    /**
     * Handles the addition of a new employee.
     * 
     * @param e The action event that triggers the add operation.
     */
    private void addEmployee(ActionEvent e) {
        Employee employee = employeeView.gatherUserInputForAddEmployee();
        if (employee != null) {
            boolean success = employeeDAO.addEmployee(employee);
            if (success) {
                JOptionPane.showMessageDialog(employeeView, "Employee added successfully!");
                employeeView.fetchAndDisplayEmployees();
            } else {
                JOptionPane.showMessageDialog(employeeView, "Failed to add employee.");
            }
        }
    }
    
    /**
     * Handles updating an existing employee.
     * 
     * @param e The action event that triggers the update operation.
     */
    private void updateEmployee(ActionEvent e) {
        String employeeNumberStr = JOptionPane.showInputDialog(employeeView, "Enter Employee Number to edit:");
        if (employeeNumberStr != null && !employeeNumberStr.isEmpty()) {
            try {
                int employeeNumber = Integer.parseInt(employeeNumberStr);
                Employee employee = employeeDAO.fetchEmployeeData(employeeNumber);
                if (employee != null) {
                    Employee updatedEmployee = employeeView.gatherUserInputForUpdateEmployee(employee);
                    if (updatedEmployee != null) {
                        boolean success = employeeDAO.editEmployeeInDatabase(
                                "employees", // Assuming the table name is provided here
                                updatedEmployee.getEmployeeNumber(),
                                updatedEmployee.getFirstName(),
                                updatedEmployee.getLastName(),
                                updatedEmployee.getExtension(),
                                updatedEmployee.getEmail(),
                                updatedEmployee.getOfficeCode(),
                                updatedEmployee.getReportsTo(),
                                updatedEmployee.getJobTitle()
                        );
                        if (success) {
                            JOptionPane.showMessageDialog(employeeView, "Employee updated successfully!");
                            employeeView.fetchAndDisplayEmployees();
                        } else {
                            JOptionPane.showMessageDialog(employeeView, "Failed to update employee.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(employeeView, "Employee not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(employeeView, "Invalid employee number format.");
            }
        }
    }
    
    /**
     * Handles the deletion of an employee.
     * 
     * @param e The action event that triggers the delete operation.
     */
    private void deleteEmployee(ActionEvent e) {
        Integer employeeNumberToDelete = employeeView.gatherUserInputForDelete();
        if (employeeNumberToDelete != null) {
            boolean success = employeeDAO.removeEmployeeFromDatabase("employees", employeeNumberToDelete);
            if (success) {
                JOptionPane.showMessageDialog(employeeView, "Employee deleted successfully.");
                employeeView.fetchAndDisplayEmployees();
            } else {
                JOptionPane.showMessageDialog(employeeView, "Failed to delete employee.");
            }
        }
    }
    
    /**
     * Handles searching for employees based on a specified criteria.
     * 
     * @param e The action event that triggers the search operation.
     */
    private void searchEmployee(ActionEvent e) {
        String searchCriteria = employeeView.gatherInputForSearch();
        if (searchCriteria != null) {
            List<Employee> searchResults = employeeDAO.searchEmployees(searchCriteria);
            employeeView.updateTableWithSearchResults(searchResults);
        }
    }
    
    /**
     * Handles saving the current employee data to a file.
     * 
     * @param e The action event that triggers the save operation.
     */
    private void saveEmployeesToFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Employees.csv"));
        int userSelection = fileChooser.showSaveDialog(employeeView);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            writeEmployeeDataToFile(fileToSave);
        }
    }
    /**
     * Writes the employee data to the specified file.
     * 
     * @param fileToSave The file to which the employee data will be written.
     */
    
    private void writeEmployeeDataToFile(File fileToSave) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            List<String[]> employees = employeeView.fetchAndDisplayEmployees();
            writer.write("Employee Number, First Name, Last Name, Extension, Email, Office Code, Reports To, Job Title");
            writer.newLine();
            for (String[] employee : employees) {
                String line = String.join(",", employee);
                writer.write(line);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(employeeView, "CSV file saved successfully at " + fileToSave.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(employeeView, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
  
}