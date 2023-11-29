package controller;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


import model.EmployeeDAO;

import view.EmployeeView;


public class AddEmployeeButtonListener implements ActionListener {
	private final EmployeeView employeeView;

    public AddEmployeeButtonListener(EmployeeView employeeView) {
        this.employeeView = employeeView;
        
    }
    
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField employeeNumberField = new JTextField(5);
            JTextField firstNameField = new JTextField(10);
            JTextField lastNameField = new JTextField(10);
            JTextField extensionField = new JTextField(10);
            JTextField emailField = new JTextField(10);
            JTextField officeCodeField = new JTextField(5);
            JTextField reportsToField = new JTextField(10);
            JTextField jobTitleField = new JTextField(10);
           

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Employee Number:"));
            panel.add(employeeNumberField);
            panel.add(new JLabel("First Name:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Last Name:"));
            panel.add(lastNameField);
            panel.add(new JLabel("Extension:"));
            panel.add(extensionField);
            panel.add(new Label("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Office Code:"));
            panel.add(officeCodeField);
            panel.add(new JLabel("Reports to:"));
            panel.add(reportsToField);
            panel.add(new JLabel("jobTitle:"));
            panel.add(jobTitleField);
           

            int result = JOptionPane.showConfirmDialog(null, panel, 
                   "Enter Employee Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int employeeNumber = Integer.parseInt(employeeNumberField.getText());
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String extension = extensionField.getText();
                    String email = emailField.getText();
                    String officeCode = officeCodeField.getText();
                    int reportsTo = Integer.parseInt(reportsToField.getText());
                    String jobTitle = jobTitleField.getText();
                    
                    
               
                   

                    EmployeeDAO handler = new EmployeeDAO();
                    boolean success = handler.addEmployee("employees", employeeNumber, firstName, lastName, extension, email, officeCode, reportsTo, jobTitle);

                    if (success) {
                        JOptionPane.showMessageDialog(employeeView, "Employee added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(employeeView, "Failed to add employee.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(employeeView, "Invalid number format in Employee Number field.");
                }
            }
        }
    }

