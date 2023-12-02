package view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;


import java.awt.event.ActionListener;


import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
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
import controller.EmployeeHandler;





public class EmployeeView extends MainView {

	private EmployeeDAO employeeDAO;
	
    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private EmployeeHandler employeeHandler;
    
    public EmployeeView() {
    	
        super();
        this.employeeDAO = new EmployeeDAO();
        this.employeeHandler = new EmployeeHandler(this, this.employeeDAO);
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
        
        
        
        JButton searchButton = createButton("Search", employeeHandler.getSearchEmployeeButtonListener());
        JButton addButton = createButton("Add", employeeHandler.getAddEmployeeButtonListener());
        JButton editButton = createButton("Edit", employeeHandler.getUpdateEmployeeButtonListener());
        JButton deleteButton = createButton("Delete", employeeHandler.getDeleteEmployeeButtonListener());
        JButton saveEmployeeButton = createButton("Save to File", employeeHandler.getSaveEmployeeButtonListener());

        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(saveEmployeeButton);

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
	
	public List<String[]> fetchAndDisplayEmployees() {
	    List<String[]> employees = employeeDAO.fetchEmployees(); // Fetch data using DAO
	    tableModel.setRowCount(0); // Clear existing rows

	    for (String[] employee : employees) {
	        tableModel.addRow(employee); // Add rows to the table model
	    }
		return employees;
	}
	
	 public Employee gatherUserInputForAddEmployee() {
	        
	        JTextField employeeNumberField = new JTextField(5);
	        JTextField firstNameField = new JTextField(10);
	        JTextField lastNameField = new JTextField(10);
	        JTextField extensionField = new JTextField(10);
	        JTextField emailField = new JTextField(10);
	        JTextField officeCodeField = new JTextField(5);
	        JTextField reportsToField = new JTextField(10);
	        JTextField jobTitleField = new JTextField(10);

	        
	        JPanel panel = new JPanel(new GridLayout(0, 1));
	        panel.add(new JLabel("Employee Number:")); panel.add(employeeNumberField);
	        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
	        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
	        panel.add(new JLabel("Extension:")); panel.add(extensionField);
	        panel.add(new JLabel("Email:")); panel.add(emailField);
	        panel.add(new JLabel("Office Code:")); panel.add(officeCodeField);
	        panel.add(new JLabel("Reports To (Employee Number):")); panel.add(reportsToField);
	        panel.add(new JLabel("Job Title:")); panel.add(jobTitleField);

	     
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
	                int reportsTo = reportsToField.getText().isEmpty() ? 0 : Integer.parseInt(reportsToField.getText()); // Assuming this can be empty
	                String jobTitle = jobTitleField.getText();

	                // Assuming the existence of a constructor in Employee class that takes all these fields
	                return new Employee(employeeNumber, firstName, lastName, extension, email, officeCode, reportsTo, jobTitle);
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(null, "Invalid number format.");
	                return null;
	            }
	        }
	        return null; // Return null if the user cancels or an error occurs
	    }
	 
	    public Employee gatherUserInputForUpdateEmployee(Employee employee) {
	        // Define fields for editing employee details
	        JTextField firstNameField = new JTextField(employee.getFirstName(), 10);
	        JTextField lastNameField = new JTextField(employee.getLastName(), 10);
	        JTextField extensionField = new JTextField(employee.getExtension(), 10);
	        JTextField emailField = new JTextField(employee.getEmail(), 10);
	        JTextField officeCodeField = new JTextField(employee.getOfficeCode(), 5);
	        JTextField reportsToField = new JTextField(String.valueOf(employee.getReportsTo()), 10);
	        JTextField jobTitleField = new JTextField(employee.getJobTitle(), 10);

	        // Panel to hold the form
	        JPanel panel = new JPanel(new GridLayout(0, 2));
	        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
	        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
	        panel.add(new JLabel("Extension:")); panel.add(extensionField);
	        panel.add(new JLabel("Email:")); panel.add(emailField);
	        panel.add(new JLabel("Office Code:")); panel.add(officeCodeField);
	        panel.add(new JLabel("Reports to:")); panel.add(reportsToField);
	        panel.add(new JLabel("Job Title:")); panel.add(jobTitleField);

	        // Show confirm dialog with the form
	        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Employee Details", JOptionPane.OK_CANCEL_OPTION);
	        if (result == JOptionPane.OK_OPTION) {
	            // Update the employee object with new values from the form
	            employee.setFirstName(firstNameField.getText());
	            employee.setLastName(lastNameField.getText());
	            employee.setExtension(extensionField.getText());
	            employee.setEmail(emailField.getText());
	            employee.setOfficeCode(officeCodeField.getText());
	            try {
	                employee.setReportsTo(Integer.parseInt(reportsToField.getText()));
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(null, "Invalid format for 'Reports To'.");
	                return null;
	            }
	            employee.setJobTitle(jobTitleField.getText());

	            return employee; // Return updated employee
	        }
	        return null; // Return null if the user cancels the operation
	    }
	
	 
	 public Integer gatherUserInputForDelete() {
	        String employeeNumberStr = JOptionPane.showInputDialog(this, "Enter Employee Number to delete:");
	        if (employeeNumberStr != null && !employeeNumberStr.isEmpty()) {
	            try {
	                int employeeNumber = Integer.parseInt(employeeNumberStr);

	                EmployeeDAO handler = new EmployeeDAO();
	                Employee employee = handler.fetchEmployeeData(employeeNumber);

	                if (employee != null) {
	                    int confirm = JOptionPane.showConfirmDialog(this, 
	                        "Are you sure you want to delete this employee?\n" +
	                        "Employee Nr: " + employee.getEmployeeNumber() + "\n" +
	                        "Name: " + employee.getFirstName() + " " + employee.getLastName(), 
	                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

	                    if (confirm == JOptionPane.YES_OPTION) {
	                        return employeeNumber;
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(this, "Employee not found.");
	                }
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(this, "Invalid employee number format.");
	            }
	        }
	        return null;
	    }
	 
	 public String gatherInputForSearch() {
	        JTextField searchField = new JTextField(20);
	        JPanel panel = new JPanel();
	        panel.add(new JLabel("Search Employees:"));
	        panel.add(searchField);

	        int result = JOptionPane.showConfirmDialog(this, panel, "Search Employees", JOptionPane.OK_CANCEL_OPTION);
	        if (result == JOptionPane.OK_OPTION) {
	            return searchField.getText().trim();
	        } else {
	            return null; // User canceled the operation
	        }
	    }


	public void updateTableWithSearchResults(List<Employee> searchResults) {
	    tableModel.setRowCount(0); 

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
}

