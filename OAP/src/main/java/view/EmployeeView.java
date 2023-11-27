package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Employee;
import model.EmployeeDAO;


public class EmployeeView extends MainView {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField textField;

    public EmployeeView() {
        super();

        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayEmployees();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        pack(); // Adjusts the frame to fit the components
        setVisible(true); // Make sure the frame is visible
    }

    private void initializeUI() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(90, 23, 139));
        JLabel titleLabel = new JLabel("Employee Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        setupTable();
        setupControlPanel();

        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen
    }

    private void setupTable() {
        String[] columnNames = {"Employee Number", "First Name", "Last Name", "Extension", "Email", "Office Code", "Reports To", "Job Title"};
        tableModel = new DefaultTableModel(null, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
    }

    private void setupControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        controlPanel.setBackground(new Color(90, 23, 139));

        JButton searchButton = createButton("Search",new SearchButtonListener());
        JButton addButton = createButton("Add", new AddButtonListener());
        JButton editButton = createButton("Edit", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete",new DeleteButtonListener());
        JButton saveButton = createButton("Save to File", new SaveEmployeeButtonListener());

        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(saveButton);

        JPanel buttonPanelHolder = new JPanel(new BorderLayout());
        buttonPanelHolder.add(controlPanel, BorderLayout.NORTH);
        buttonPanelHolder.add(Box.createVerticalStrut(10), BorderLayout.CENTER); // Add space
        this.add(buttonPanelHolder, BorderLayout.SOUTH);
    }

   

	private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }
	
	List<String[]> fetchAndDisplayEmployees() {
	    List<String[]> employees = new ArrayList<>();
	    tableModel.setRowCount(0); // Clear the existing rows

	    try (Connection conn = database.DataBaseConnection.getConnection();
	         Statement statement = conn.createStatement()) {
	        String sql = "SELECT employeeNumber, firstName, lastName, extension, email, officeCode, reportsTo, jobTitle FROM employees";
	        ResultSet resultSet = statement.executeQuery(sql);
	        while (resultSet.next()) {
	            String[] employee = {
	                resultSet.getString("employeeNumber"),
	                resultSet.getString("firstName"),
	                resultSet.getString("lastName"),
	                resultSet.getString("extension"),
	                resultSet.getString("email"),
	                resultSet.getString("officeCode"),
	                resultSet.getString("reportsTo"),
	                resultSet.getString("jobTitle")
	            };
	            tableModel.addRow(employee); // Add row to the table model
	            employees.add(employee); // Add to the list
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Error fetching employee data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	    }
	    return employees;
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
                    
                    
               
                   

                    EmployeeDAO handler = new EmployeeDAO();
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

                    EmployeeDAO handler = new EmployeeDAO();
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
                JOptionPane.showMessageDialog(EmployeeView.this, "Employee updated successfully!");
            } else {
                JOptionPane.showMessageDialog(EmployeeView.this, "Failed to update employee.");
            }
        }
    }


    
    

    

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeNumberStr = JOptionPane.showInputDialog(EmployeeView.this, "Enter Employee Number to delete:");
            if (employeeNumberStr != null && !employeeNumberStr.isEmpty()) {
                try {
                    int employeeNumber = Integer.parseInt(employeeNumberStr);

                    EmployeeDAO handler = new EmployeeDAO();
                    Employee employee = handler.fetchEmployeeData(employeeNumber);

                    if (employee != null) {
                        int confirm = JOptionPane.showConfirmDialog(EmployeeView.this, 
                            "Are you sure you want to delete this employee?\n" +
                            "Employee Nr: " + employee.getEmployeeNumber() + "\n" +
                            "Name: " + employee.getFirstName() + " " + employee.getLastName(), 
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            boolean success = handler.removeEmployeeFromDatabase("employees", employeeNumber);
                            if (success) {
                                JOptionPane.showMessageDialog(EmployeeView.this, "Employee deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(EmployeeView.this, "Failed to delete employee.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(EmployeeView.this, "Employee not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(EmployeeView.this, "Invalid employee number format.");
                }
            }
        }
    }


    // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField searchField = new JTextField(20);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Search Employees:"));
            panel.add(searchField);

            int result = JOptionPane.showConfirmDialog(EmployeeView.this, panel, 
                                                       "Search Employees", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String searchCriteria = searchField.getText().trim();
                EmployeeDAO handler = new EmployeeDAO();
                List<Employee> searchResults = handler.searchEmployees(searchCriteria);
                updateTableWithSearchResults(searchResults);
            }
        }
    }

    // Method to update the table with search results
    private void updateTableWithSearchResults(List<Employee> searchResults) {
        tableModel.setRowCount(0); // Clear the existing rows

        for (Employee employee : searchResults) {
            Object[] row = new Object[]{
                employee.getEmployeeNumber(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getExtension(),
                employee.getEmail(),
                employee.getOfficeCode(),
                employee.getReportsTo(),
                employee.getJobTitle(),
            };
            tableModel.addRow(row);
        }
    }
    
    private class SaveEmployeeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveEmployeesToFile();
        }
    };
    
    private void saveEmployeesToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Employees.csv")); // Set default file name

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> employees = fetchAndDisplayEmployees(); // Fetch employee data

                // Write header row (optional)
                writer.write("Employee Number, First Name, Last Name, Extension, Email, Office Code, Reports To, Job Title");
                writer.newLine();

                // Write data rows
                for (String[] employee : employees) {
                    String line = String.join(",", employee); // Comma as delimiter
                    writer.write(line);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "CSV file saved successfully at " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    
        
    }
