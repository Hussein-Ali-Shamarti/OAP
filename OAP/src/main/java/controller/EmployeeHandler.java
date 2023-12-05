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
 * Handles the business logic associated with employee management in the application.
 * This class coordinates interactions between the EmployeeView for user interface
 * actions and EmployeeDAO for data persistence, facilitating operations like add,
 * update, delete, search, and save of employee data.
 * 
 * @author 7080
 */
public class EmployeeHandler {
	
	
	private final EmployeeView employeeView;
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
     * Provides an ActionListener for adding a new employee.
     * 
     * @return An ActionListener that triggers the addition of a new employee.
     */
    public ActionListener getAddEmployeeButtonListener() {
        return this::addEmployee;
    }
    
    /**
     * Provides an ActionListener for updating an employee.
     * 
     * @return ActionListener for the update employee action.
     */
    public ActionListener getUpdateEmployeeButtonListener() {
        return this::updateEmployee;
    }
    
    /**
     * Provides an ActionListener for deleting an employee.
     * 
     * @return ActionListener for the delete employee action.
     */
    public ActionListener getDeleteEmployeeButtonListener() {
        return this::deleteEmployee;
    }
    
    /**
     * Provides an ActionListener for searching employees.
     * 
     * @return ActionListener for the search employee action.
     */
    public ActionListener getSearchEmployeeButtonListener() {
        return this::searchEmployee;
    }
    
    /**
     * Provides an ActionListener for saving employee data to a file.
     * 
     * @return ActionListener for the save employee data action.
     */
    public ActionListener getSaveEmployeeButtonListener() {
        return this::saveEmployeesToFile;
    }
    
    /**
     * Handles the addition of a new employee. Gathers user input from the EmployeeView
     * and adds the new employee through EmployeeDAO.
     * 
     * @param e The action event that triggers the add operation.
     */
    public void addEmployee(ActionEvent e) {
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
     * Handles the updating of an existing employee. It fetches the employee details
     * based on the provided employee number and updates the employee's data.
     * 
     * @param e The action event that triggers the update operation.
     */

    public void updateEmployee(ActionEvent e) {
        String employeeNumberStr = JOptionPane.showInputDialog(employeeView, "Enter Employee Number to edit:");
        if (employeeNumberStr != null && !employeeNumberStr.isEmpty()) {
            try {
                int employeeNumber = Integer.parseInt(employeeNumberStr);
                Employee employee = employeeDAO.fetchEmployeeData(employeeNumber);
                if (employee != null) {
                    Employee updatedEmployee = employeeView.gatherUserInputForUpdateEmployee(employee);
                    if (updatedEmployee != null) {
                        boolean success = employeeDAO.editEmployeeInDatabase(
                                "employees",
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
     * Handles the deletion of an employee. Deletes the employee specified by the
     * employee number obtained from EmployeeView.
     * 
     * @param e The action event that triggers the delete operation.
     */
    public void deleteEmployee(ActionEvent e) {
        Integer employeeNumberToDelete = employeeView.gatherUserInputForDeleteEmployee();
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
     * Handles the searching of employees based on criteria provided through EmployeeView.
     * 
     * 
     * @param e The action event that triggers the search operation.
     */
   public void searchEmployee(ActionEvent e) {
        String searchCriteria = employeeView.gatherInputForSearchEmployee();
        if (searchCriteria != null) {
            List<Employee> searchResults = employeeDAO.searchEmployees(searchCriteria);
            employeeView.updateTableWithSearchResults(searchResults);
        }
    }
    
    /**
     * Handles saving the current employee data to a file. It allows the user to choose
     * a file destination and saves the employee data in CSV format.
     * 
     * @param e The action event that triggers the save operation.
     */
   public void saveEmployeesToFile(ActionEvent e) {
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
     * Writes employee data to the specified file in CSV format.
     * 
     * @param fileToSave The file where employee data will be written.
     */
    
    public void writeEmployeeDataToFile(File fileToSave) {
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