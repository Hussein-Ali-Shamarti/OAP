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

//import com.OBJ2100.ExamApp.gui.listeners.AboutAppListener;

import model.Employee;
import model.EmployeeDAO;
import controller.AddEmployeeButtonListener;
import controller.UpdateEmployeeButtonListener;



public class EmployeeView extends MainView {

	private EmployeeDAO employeeDAO;
	
    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField textField;
    
    public EmployeeView() {
    	
        super();
        this.employeeDAO = new EmployeeDAO();
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
        JButton addButton = createButton("Add", new AddEmployeeButtonListener(null));
        JButton editButton = createButton("Edit", new UpdateEmployeeButtonListener(this, this.employeeDAO)); 
        JButton deleteButton = createButton("Delete",new DeleteButtonListener());
        JButton saveEmployeeButton = createButton("Save to File", new SaveEmployeeButtonListener());

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
	
	private void fetchAndDisplayEmployees() {
	    List<String[]> employees = employeeDAO.fetchEmployees(); // Fetch data using DAO
	    tableModel.setRowCount(0); // Clear existing rows

	    for (String[] employee : employees) {
	        tableModel.addRow(employee); // Add rows to the table model
	    }
	}

    	// update button
  
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

        int userSelection = fileChooser.showSaveDialog(this); // 'this' refers to the current JFrame

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> employees = employeeDAO.fetchEmployees(); // Fetch employee data using DAO

                // Write header row (optional)
                writer.write("Employee Number, First Name, Last Name, Extension, Email, Office Code, Reports To, Job Title");
                writer.newLine();

                // Write data rows
                for (String[] employee : employees) {
                    String line = String.join(",", employee); // Comma-separated values
                    writer.write(line);
                    writer.newLine();
                }

                JOptionPane.showMessageDialog(null, "CSV file saved successfully at " + fileToSave.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
   
 }
