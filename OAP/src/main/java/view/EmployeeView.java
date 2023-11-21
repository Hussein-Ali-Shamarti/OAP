package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.EmployeeHandler;
import database.DataBaseConnection;
import model.Employee;

public class EmployeeView extends JFrame {

    
	
	private static final long serialVersionUID = 1L;


	public EmployeeView() {
        // Set title
        super("Employee View");
        
        

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(Color.WHITE);

        // Create JLabel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131)); // Purple background color
        JLabel titleLabel = new JLabel("Employee Management");
        titleLabel.setBackground(new Color(84, 11, 131)); // Purple background color
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE); // White text color
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Create buttons with listeners
        JButton addButton = createButton("Add New", new AddButtonListener());
        JButton updateButton = createButton("Update", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
        JButton searchButton = createButton("Search", new SearchButtonListener());
        

        // Create JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        // Create JTable and JScrollPane
        String[] columnNames = {"Column 1", "Column 2", "Column 3"}; // Replace with actual column names
        Object[][] data = {{"Data 1", "Data 2", "Data 3"}, {"Data 4", "Data 5", "Data 6"}}; // Replace with actual data
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the frame
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen

        // Set the EmployeesListener for buttons
        addButton.addActionListener(new EmployeesListener());
        updateButton.addActionListener(new EmployeesListener());
        deleteButton.addActionListener(new EmployeesListener());
        searchButton.addActionListener(new EmployeesListener());
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK); // White text color
        button.setBackground(new Color(84, 11, 131)); // Purple background color
        button.setFocusPainted(false); // Remove focus highlighting for better appearance
        button.addActionListener(listener); // Add the listener
        return button;
    }
    
 
    

    // Action listener for "Add New" button
    private class AddButtonListener implements ActionListener {
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
                    
                    
               
                   

                    EmployeeHandler handler = new EmployeeHandler();
                    boolean success = handler.addEmployee("employees", employeeNumber, firstName, lastName, extension, email, officeCode, reportsTo, jobTitle);

                    if (success) {
                        JOptionPane.showMessageDialog(EmployeeView.this, "Employee added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(EmployeeView.this, "Failed to add employee.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(EmployeeView.this, "Invalid number format in Employee Number field.");
                }
            }
        }
    }

    	// update button

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeNumberStr = JOptionPane.showInputDialog(EmployeeView.this, "Enter Employee Number to edit:");
            if (employeeNumberStr != null && !employeeNumberStr.isEmpty()) {
                try {
                    int employeeNumber = Integer.parseInt(employeeNumberStr);

                    EmployeeHandler handler = new EmployeeHandler();
                    Employee employee = handler.fetchEmployeeData(employeeNumber);

                    if (employee != null) {
                        displayEditForm(employee);
                    } else {
                        JOptionPane.showMessageDialog(EmployeeView.this, "Employee not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(EmployeeView.this, "Invalid employee number format.");
                }
            }
        }
    }

    
    private void displayEditForm(Employee employee) {
    	JTextField firstNameField = new JTextField(10);
        JTextField lastNameField = new JTextField(10);
        JTextField extensionField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JTextField officeCodeField = new JTextField(5);
        JTextField reportsToField = new JTextField(10);
        JTextField jobTitleField = new JTextField(10);
        // Add fields for other attributes like extension, email, etc.

        JPanel panel = new JPanel(new GridLayout(0, 2));
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
       

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Employee Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
        	   String firstName = firstNameField.getText();
               String lastName = lastNameField.getText();
               String extension = extensionField.getText();
               String email = emailField.getText();
               String officeCode = officeCodeField.getText();
               int reportsTo = Integer.parseInt(reportsToField.getText());
               String jobTitle = jobTitleField.getText();

            EmployeeHandler handler = new EmployeeHandler();
            boolean success = handler.editEmployeeInDatabase("employees", employee.getEmployeeNumber(), firstName, lastName, extension, email, officeCode, reportsTo, jobTitle);

            if (success) {
                JOptionPane.showMessageDialog(EmployeeView.this, "Employee updated successfully!");
            } else {
                JOptionPane.showMessageDialog(EmployeeView.this, "Failed to update employee.");
            }
        }
    }

    
    

    

    // Action listener for "Delete" button
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(EmployeeView.this, "Delete button pressed");
        }
    }

    // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(EmployeeView.this, "Search button pressed");
        }
    }

    // Static inner class for "Employees" button in MainMenu
    private static class EmployeesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open EmployeeView when "Employees" button is pressed
            SwingUtilities.invokeLater(() -> {
                new EmployeeView().setVisible(true);
            });
        }
    }
}