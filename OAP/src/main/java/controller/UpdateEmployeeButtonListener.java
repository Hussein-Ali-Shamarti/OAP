package controller;
import javax.swing.*;

import model.Employee;
import model.EmployeeDAO;
import view.EmployeeView;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateEmployeeButtonListener implements ActionListener {
	private EmployeeView employeeView;
	private EmployeeDAO employeeDAO;
	
	
	public UpdateEmployeeButtonListener(EmployeeView employeeView, EmployeeDAO employeeDAO) {
        this.employeeView = employeeView;
        this.employeeDAO = employeeDAO;
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String employeeNumberStr = JOptionPane.showInputDialog(employeeView, "Enter Employee Number to edit:");
        if (employeeNumberStr != null && !employeeNumberStr.isEmpty()) {
            try {
                int employeeNumber = Integer.parseInt(employeeNumberStr);
                Employee employee = employeeDAO.fetchEmployeeData(employeeNumber);
                if (employee != null) {
                    displayEditForm(employee);
                } else {
                    JOptionPane.showMessageDialog(employeeView, "Employee not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(employeeView, "Invalid employee number format.");
            }
        }
    }

    private void displayEditForm(Employee employee) {
        JTextField firstNameField = new JTextField(employee.getFirstName(), 10);
        JTextField lastNameField = new JTextField(employee.getLastName(), 10);
        JTextField extensionField = new JTextField(employee.getExtension(), 10);
        JTextField emailField = new JTextField(employee.getEmail(), 10);
        JTextField officeCodeField = new JTextField(employee.getOfficeCode(), 5);
        JTextField reportsToField = new JTextField(String.valueOf(employee.getReportsTo()), 10);
        JTextField jobTitleField = new JTextField(employee.getJobTitle(), 10);
        // Assuming there are no other fields as per your Employee class
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Extension:"));
        panel.add(extensionField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Office Code:"));
        panel.add(officeCodeField);
        panel.add(new JLabel("Reports to:"));
        panel.add(reportsToField);
        panel.add(new JLabel("Job Title:"));
        panel.add(jobTitleField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Employee Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String extension = extensionField.getText();
            String email = emailField.getText();
            String officeCode = officeCodeField.getText();
            int reportsTo = Integer.parseInt(reportsToField.getText());
            String jobTitle = jobTitleField.getText();
            EmployeeDAO handler = new EmployeeDAO();
            boolean success = handler.editEmployeeInDatabase("employees", employee.getEmployeeNumber(), firstName, lastName, extension, email, officeCode, reportsTo, jobTitle);
            if (success) {
                JOptionPane.showMessageDialog(employeeView, "Employee updated successfully!");
            } else {
                JOptionPane.showMessageDialog(employeeView, "Failed to update employee.");
            }
        }
    }
}
