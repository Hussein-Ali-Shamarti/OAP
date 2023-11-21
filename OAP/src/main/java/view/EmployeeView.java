package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.EmployeeHandler;
import database.DataBaseConnection;

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
            JTextField employeeNrField = new JTextField(5);
            JTextField firstNameField = new JTextField(10);
            JTextField lastNameField = new JTextField(10);
            JTextField roleField = new JTextField(10);
            JTextField jobTitleField = new JTextField(10);
            JPasswordField passwordField = new JPasswordField(10);
            JTextField emailField = new JTextField(10);
            JCheckBox canCheckDeliveryStatusBox = new JCheckBox();
            JTextField postalCodeField = new JTextField(5);
            JTextField rolesField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Employee Number:"));
            panel.add(employeeNrField);
            panel.add(new JLabel("First Name:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Last Name:"));
            panel.add(lastNameField);
            panel.add(new JLabel("Role:"));
            panel.add(roleField);
            panel.add(new JLabel("Job Title:"));
            panel.add(jobTitleField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Can Check Delivery Status:"));
            panel.add(canCheckDeliveryStatusBox);
            panel.add(new JLabel("Postal Code:"));
            panel.add(postalCodeField);
            panel.add(new JLabel("Roles:"));
            panel.add(rolesField);

            int result = JOptionPane.showConfirmDialog(null, panel, 
                   "Enter Employee Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int employeeNr = Integer.parseInt(employeeNrField.getText());
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String role = roleField.getText();
                    String jobTitle = jobTitleField.getText();
                    String password = new String(passwordField.getPassword());
                    String email = emailField.getText();
                    boolean canCheckDeliveryStatus = canCheckDeliveryStatusBox.isSelected();
                    String postalCode = postalCodeField.getText();
                    String roles = rolesField.getText();

                    EmployeeHandler handler = new EmployeeHandler();
                    boolean success = handler.addEmployee("employees", employeeNr, firstName, lastName, role, jobTitle, password, email, canCheckDeliveryStatus, postalCode, roles);

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


    // Action listener for "Update" button
    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(EmployeeView.this, "Update button pressed");
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