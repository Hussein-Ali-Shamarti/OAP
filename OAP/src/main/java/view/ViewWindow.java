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

