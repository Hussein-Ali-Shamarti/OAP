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

/**
 * EmployeeView class extends MainView to provide a user interface for managing employees.
 * It includes functionality to display, add, edit, delete, and search employees using a graphical user interface.
 * The class uses an EmployeeDAO instance for database operations and interacts with the user through various UI components.
 *
 * @author 7080
 * @version 2.12.2023
 */

public class EmployeeView extends MainView {

	
    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private EmployeeHandler employeeHandler;
    private EmployeeDAO employeeDAO;
  
    /**
     * Constructor to initialize the EmployeeView. Sets up the UI and fetches initial data to display.
     */
    
    public EmployeeView() {
    	
        super();
        this.employeeDAO = new EmployeeDAO();
        this.employeeHandler = new EmployeeHandler(this, this.employeeDAO);
        initializeUI();
        fetchAndDisplayEmployees();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        pack(); 
        setVisible(true); 
        
    }

    /**
     * Initializes the UI components of the EmployeeView.
     */
    
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

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); 
    }

    /**
     * Sets up the table model and structure for displaying employee data.
     */
    
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

    /**
     * Sets up the control panel with buttons for various actions like search, add, edit, delete, and save.
     */
    
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
        buttonPanelHolder.add(Box.createVerticalStrut(10), BorderLayout.CENTER); 
        this.add(buttonPanelHolder, BorderLayout.SOUTH);
    }

    /**
     * Creates and returns a JButton with specified text and action listener.
     *
     * @param text The text to display on the button.
     * @param listener The ActionListener to attach to the button.
     * @return A JButton instance.
     */

	private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }
	
    /**
     * Fetches and displays employee data from the database in the table.
     *
     * @return A list of employee data as String arrays.
     */
	
	public List<String[]> fetchAndDisplayEmployees() {
	    List<String[]> employees = employeeDAO.fetchEmployees(); 
	    tableModel.setRowCount(0); 

	    for (String[] employee : employees) {
	        tableModel.addRow(employee); 
	    }
		return employees;
	}
	
    /**
     * Gathers user input for adding a new employee. Presents a form to the user to enter new employee details.
     *
     * @return An Employee object with the details entered by the user, or null if the operation is canceled.
     */

	
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
	                int reportsTo = reportsToField.getText().isEmpty() ? 0 : Integer.parseInt(reportsToField.getText()); 
	                String jobTitle = jobTitleField.getText();

	                
	                return new Employee(employeeNumber, firstName, lastName, extension, email, officeCode, reportsTo, jobTitle);
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(null, "Invalid number format.");
	                return null;
	            }
	        }
	        return null; 
	    }
	 
	    /**
	     * Gathers user input for updating an existing employee. Presents a pre-filled form with the employee's current details.
	     *
	     * @param employee The Employee object to be updated.
	     * @return The updated Employee object, or null if the operation is canceled.
	     */
	 
	    public Employee gatherUserInputForUpdateEmployee(Employee employee) {
	       
	        JTextField firstNameField = new JTextField(employee.getFirstName(), 10);
	        JTextField lastNameField = new JTextField(employee.getLastName(), 10);
	        JTextField extensionField = new JTextField(employee.getExtension(), 10);
	        JTextField emailField = new JTextField(employee.getEmail(), 10);
	        JTextField officeCodeField = new JTextField(employee.getOfficeCode(), 5);
	        JTextField reportsToField = new JTextField(String.valueOf(employee.getReportsTo()), 10);
	        JTextField jobTitleField = new JTextField(employee.getJobTitle(), 10);

	        
	        JPanel panel = new JPanel(new GridLayout(0, 2));
	        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
	        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
	        panel.add(new JLabel("Extension:")); panel.add(extensionField);
	        panel.add(new JLabel("Email:")); panel.add(emailField);
	        panel.add(new JLabel("Office Code:")); panel.add(officeCodeField);
	        panel.add(new JLabel("Reports to:")); panel.add(reportsToField);
	        panel.add(new JLabel("Job Title:")); panel.add(jobTitleField);

	       
	        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Employee Details", JOptionPane.OK_CANCEL_OPTION);
	        if (result == JOptionPane.OK_OPTION) {
	           
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

	            return employee; 
	        }
	        return null; 
	    }
	
	 
	    /**
	     * Gathers user input for the employee number to be deleted.
	     *
	     * @return The employee number to delete, or null if the operation is canceled.
	     */
	    
	 public Integer gatherUserInputForDeleteEmployee() {
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
	 
	    /**
	     * Gathers the user input for searching employees. Prompts the user to enter a search string.
	     *
	     * @return The search string entered by the user, or null if the operation is canceled.
	     */
	 
	 public String gatherInputForSearchEmployee() {
	        JTextField searchField = new JTextField(20);
	        JPanel panel = new JPanel();
	        panel.add(new JLabel("Search Employees:"));
	        panel.add(searchField);

	        int result = JOptionPane.showConfirmDialog(this, panel, "Search Employees", JOptionPane.OK_CANCEL_OPTION);
	        if (result == JOptionPane.OK_OPTION) {
	            return searchField.getText().trim();
	        } else {
	            return null; 
	        }
	    }
	 
	    /**
	     * Updates the table with the provided list of search results.
	     *
	     * @param searchResults A list of Employee objects representing the search results.
	     */

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

