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

public class EmployeeHandler {

    private final EmployeeView employeeView;
    private final EmployeeDAO employeeDAO;

    public EmployeeHandler(EmployeeView employeeView, EmployeeDAO employeeDAO) {
        this.employeeView = employeeView;
        this.employeeDAO = employeeDAO;
    }

    public ActionListener getAddEmployeeButtonListener() {
        return this::addEmployee;
    }

    public ActionListener getUpdateEmployeeButtonListener() {
        return this::updateEmployee;
    }

    public ActionListener getDeleteEmployeeButtonListener() {
        return this::deleteEmployee;
    }

    public ActionListener getSearchEmployeeButtonListener() {
        return this::searchEmployee;
    }

    public ActionListener getSaveEmployeeButtonListener() {
        return this::saveEmployeesToFile;
    }

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

    private void searchEmployee(ActionEvent e) {
        String searchCriteria = employeeView.gatherInputForSearch();

        if (searchCriteria != null) {
            List<Employee> searchResults = employeeDAO.searchEmployees(searchCriteria);
            employeeView.updateTableWithSearchResults(searchResults);
        }
    }

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