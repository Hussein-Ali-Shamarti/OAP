package view;

import javax.swing.*;

import controller.EmployeeHandler;
import model.Employee;

import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L; // why is this added
	private ViewWindow viewWindow;
    private SidePanel sidePanel;
    private ProductView productView;
    private EmployeeView employeeView;
    private OrderView orderView;
    private CustomerView customerView;

    public MainView() {
        initializeUI();
    }
    
    private void displayAllEmployees() {
        EmployeeHandler employeeHandler = new EmployeeHandler();
        List<Employee> employees = employeeHandler.displayAll();

        // Building a simple string from the employee list
        StringBuilder employeeListBuilder = new StringBuilder();
        for (Employee emp : employees) {
            employeeListBuilder.append(emp.getEmployeeNr())
                               .append(" - ")
                               .append(emp.getFirstName())
                               .append(" ")
                               .append(emp.getLastName())
                               .append("\n");
        }

        // Display the employees in a simple dialog
        JOptionPane.showMessageDialog(this, employeeListBuilder.toString(), "All Employees", JOptionPane.INFORMATION_MESSAGE);
    }

    private ActionListener createActionListener1(final String action) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Display All".equals(action)) {
                    displayAllEmployees();
                }
                // ... other cases ...
            }
        };
    }
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setTitle("Main View");

        // Create components
        viewWindow = new ViewWindow();
        sidePanel = new SidePanel(viewWindow);

        // Set layout manager
        setLayout(new BorderLayout());

        // Add top empty panel for company name and logo
        add(createTopPanel(), BorderLayout.NORTH);

        // Add side panel and view window
        add(sidePanel, BorderLayout.WEST);
        add(viewWindow, BorderLayout.CENTER);

        // Add bottom button panel
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Add right panel with buttons
        add(createRightPanel(), BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        JLabel companyNameField = new JLabel("Model Perfect");

        topPanel.add(companyNameField);

    
    
        return topPanel;
    }

    

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        // Add components for category actions
        // You can customize this based on your requirements
        JButton displayAllButton = new JButton("Display All");
        JButton addButton = new JButton("Add New");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton searchButton = new JButton("Search");

        // Add action listeners to category buttons
        displayAllButton.addActionListener(createActionListener1("Display All"));
        addButton.addActionListener(createActionListener1("Add New"));
        updateButton.addActionListener(createActionListener1("Update"));
        deleteButton.addActionListener(createActionListener1("Delete"));
        searchButton.addActionListener(createActionListener1("Search"));

        // Add buttons to the bottom panel
        bottomPanel.add(displayAllButton);
        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(searchButton);

        return bottomPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JButton storeResultButton = new JButton("Store Result");
        JButton sqlQueriesButton = new JButton("SQL Queries");
        JButton enterTitleButton = new JButton("Enter Title");

        // Add action listeners to right panel buttons
        storeResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement store result functionality
                JOptionPane.showMessageDialog(MainView.this, "Store Result Button Clicked");
            }
        });

        sqlQueriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement SQL queries functionality
                JOptionPane.showMessageDialog(MainView.this, "SQL Queries Button Clicked");
            }
        });

        enterTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement enter title functionality
                String title = JOptionPane.showInputDialog(MainView.this, "Enter Title:");
                if (title != null) {
                    // Use the entered title as needed
                    JOptionPane.showMessageDialog(MainView.this, "Entered Title: " + title);
                }
            }
        });
        
    
        

        rightPanel.add(storeResultButton);
        rightPanel.add(sqlQueriesButton);
        rightPanel.add(enterTitleButton);

        return rightPanel;
    }

    private ActionListener createActionListener(final String action) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement category actions based on the button clicked
                JOptionPane.showMessageDialog(MainView.this, action + " Button Clicked");
            }
        };
    }

    
}