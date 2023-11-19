package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L; // why is this added
	private ViewWindow viewWindow;
    private SidePanel sidePanel;

    public MainView() {
        initializeUI();
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
        displayAllButton.addActionListener(createActionListener("Display All"));
        addButton.addActionListener(createActionListener("Add New"));
        updateButton.addActionListener(createActionListener("Update"));
        deleteButton.addActionListener(createActionListener("Delete"));
        searchButton.addActionListener(createActionListener("Search"));

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

class SidePanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private ViewWindow viewWindow;

    public SidePanel(ViewWindow viewWindow) {
        this.viewWindow = viewWindow;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridLayout(4, 1));

        JButton productsButton = new JButton("Products");
        JButton ordersButton = new JButton("Orders");
        JButton customersButton = new JButton("Customers");
        JButton employeesButton = new JButton("Employees");

        // Add action listeners to category buttons
        productsButton.addActionListener(createActionListener("Products"));
        ordersButton.addActionListener(createActionListener("Orders"));
        customersButton.addActionListener(createActionListener("Customers"));
        employeesButton.addActionListener(createActionListener("Employees"));

        add(productsButton);
        add(ordersButton);
        add(customersButton);
        add(employeesButton);
    }

    private ActionListener createActionListener(final String category) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewWindow.getCardLayout().show(viewWindow.getCardPanel(), category);
            }
        };
    }
}

class ViewWindow extends JPanel {
    private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
    private JPanel cardPanel;

    public ViewWindow() {
        initializeUI();
    }

    private void initializeUI() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Example tables with sample data
        JTable productsTable = new JTable(
                new Object[][]{{1, "Product 1", 10.0}, {2, "Product 2", 20.0}},
                new Object[]{"ProductNr", "ProductName", "Price"}
        );
        JTable ordersTable = new JTable(
                new Object[][]{{1, "Order 1", "In Progress"}, {2, "Order 2", "Delivered"}},
                new Object[]{"OrderNr", "OrderName", "Status"}
        );
        JTable customersTable = new JTable(
                new Object[][]{{1, "Customer 1", "Address 1"}, {2, "Customer 2", "Address 2"}},
                new Object[]{"CustomerNr", "CustomerName", "Address"}
        );
        JTable employeesTable = new JTable(
                new Object[][]{{1, "Employee 1", "Role 1"}, {2, "Employee 2", "Role 2"}},
                new Object[]{"EmployeeNr", "EmployeeName", "Role"}
        );

        cardPanel.add(new JScrollPane(productsTable), "Products");
        cardPanel.add(createOrderPanel(ordersTable), "Orders");
        cardPanel.add(new JScrollPane(customersTable), "Customers");
        cardPanel.add(new JScrollPane(employeesTable), "Employees");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createOrderPanel(JTable ordersTable) {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        // Add "Check Delivery Status" and "Check Payment Status" buttons
        JButton checkDeliveryButton = new JButton("Check Delivery Status");
        JButton checkPaymentButton = new JButton("Check Payment Status");

        checkDeliveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement check delivery status functionality
                JOptionPane.showMessageDialog(ViewWindow.this, "Check Delivery Status Button Clicked");
            }
        });

        checkPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement check payment status functionality
                JOptionPane.showMessageDialog(ViewWindow.this, "Check Payment Status Button Clicked");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkDeliveryButton);
        buttonPanel.add(checkPaymentButton);

        orderPanel.add(buttonPanel, BorderLayout.SOUTH);

        return orderPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }
}