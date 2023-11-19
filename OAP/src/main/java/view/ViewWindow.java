package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewWindow extends JPanel {
    private static final long serialVersionUID = 1L; // forstå hvorfor denne må legges inn
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

