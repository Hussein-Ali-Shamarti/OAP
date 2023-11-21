package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderView extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderView() {
        // Set title
        super("Order Management");

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(Color.WHITE);

        // Create JLabel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131)); // Purple background color
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE); // White text color
        titlePanel.add(titleLabel);

        // Create CRUD buttons with listeners
        JPanel CRUDButtonPanel = new JPanel();
        CRUDButtonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        JButton addButton = createButton("Add New", new AddButtonListener());
        JButton updateButton = createButton("Update", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
        JButton searchButton = createButton("Search", new SearchButtonListener());
        CRUDButtonPanel.add(addButton);
        CRUDButtonPanel.add(updateButton);
        CRUDButtonPanel.add(deleteButton);
        CRUDButtonPanel.add(searchButton);

        // Create Status Check buttons with listeners
        JPanel statusCheckButtonsPanel = new JPanel();
        statusCheckButtonsPanel.setLayout(new GridLayout(1, 2, 10, 10));
        JButton deliveryButton = createButton("Check Delivery Status", new DeliveryButtonListener());
        JButton paymentButton = createButton("Check Payment Status", new PaymentButtonListener1());
        statusCheckButtonsPanel.add(deliveryButton);
        statusCheckButtonsPanel.add(paymentButton);

        // Create JTable and JScrollPane
        String[] columnNames = {"Column 1", "Column 2", "Column 3"}; // Replace with actual column names
        Object[][] data = {{"Data 1", "Data 2", "Data 3"}, {"Data 4", "Data 5", "Data 6"}}; // Replace with actual data
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the frame
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel to hold the two button panels horizontally with some space
        JPanel buttonPanelsHolder = new JPanel();
        buttonPanelsHolder.setLayout(new BorderLayout());
        buttonPanelsHolder.add(CRUDButtonPanel, BorderLayout.SOUTH);
        buttonPanelsHolder.add(Box.createVerticalStrut(10), BorderLayout.CENTER); // Add space
        buttonPanelsHolder.add(statusCheckButtonsPanel, BorderLayout.NORTH);
        add(buttonPanelsHolder, BorderLayout.SOUTH);

        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE); // White text color
        button.setBackground(new Color(84, 11, 131)); // Purple background color
        button.setFocusPainted(false); // Remove focus highlighting for better appearance
        button.addActionListener(listener); // Add the listener
        return button;
    }

    // Action listener for "Add New" button
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(OrderView.this, "Add New button pressed");
        }
    }

    // Action listener for "Update" button
    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(OrderView.this, "Update button pressed");
        }
    }

    // Action listener for "Delete" button
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(OrderView.this, "Delete button pressed");
        }
    }

    // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(OrderView.this, "Search button pressed");
        }
    }

    // Action listener for "Check Delivery Status" button
    private class DeliveryButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(OrderView.this, "Check Delivery Status button pressed");
        }
    }

    // Action listener for "Check Payment Status" button
    private class PaymentButtonListener1 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(OrderView.this, "Check Payment Status button pressed");
        }
    }

}