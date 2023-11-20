package view;

import javax.swing.*;
import controller.DeliveryHandler;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ViewWindow extends JPanel {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DeliveryHandler deliveryHandler; // Add a DeliveryHandler member

    public ViewWindow() {
        this.deliveryHandler = new DeliveryHandler(); // Initialize the DeliveryHandler
        initializeUI();
    }

    private void initializeUI() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JTable productsTable = new JTable();
        JTable ordersTable = new JTable();
        JTable customersTable = new JTable();
        JTable employeesTable = new JTable();

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

        // Create a label to display the delivery status message
        JLabel deliveryStatusLabel = new JLabel(" ");
        deliveryStatusLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton checkDeliveryButton = new JButton("Check Delivery Status");
        checkDeliveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderNrStr = JOptionPane.showInputDialog(ViewWindow.this, "Enter Order Number:", "Check Delivery Status", JOptionPane.QUESTION_MESSAGE);
                if (orderNrStr != null && !orderNrStr.trim().isEmpty()) {
                    try {
                        int orderNr = Integer.parseInt(orderNrStr.trim());
                        String status = deliveryHandler.checkShipmentStatus(orderNr);
                        deliveryStatusLabel.setText("Order " + orderNr + " Status: " + status);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(ViewWindow.this, "Invalid order number format");
                    }
                }
            }
        });

        JButton checkPaymentButton = new JButton("Check Payment Status");
        // ... add action listener for checkPaymentButton

        // Panel to hold buttons and status label
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkDeliveryButton);
        buttonPanel.add(checkPaymentButton);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(deliveryStatusLabel, BorderLayout.CENTER);

        orderPanel.add(bottomPanel, BorderLayout.SOUTH);

        return orderPanel;
    }

    

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }
}

