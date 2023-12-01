package view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
import controller.DeleteEmployeeButtonListener;
import controller.SaveEmployeeButtonListener;
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
        JButton deleteButton = createButton("Delete",new DeleteEmployeeButtonListener(null, employeeDAO));
        JButton saveEmployeeButton = createButton("Save to File", new SaveEmployeeButtonListener(null, employeeDAO));

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
}
    

