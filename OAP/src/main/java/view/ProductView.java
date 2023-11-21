package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import database.DataBaseConnection;
import controller.ProductHandler;

public class ProductView extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductView() {
        // Set title
        super("Product Management");

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create title panel with purple background and white font
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131)); // Purple background color
        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE); // White font color
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel);

        // Create buttons with listeners
        JButton addButton = createButton("Add New", new AddButtonListener());
        JButton updateButton = createButton("Update", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
        JButton searchButton = createButton("Search", new SearchButtonListener());

        // Create JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        // Create JTable and JScrollPane
        String[] columnNames = {"Column 1", "Column 2", "Column 3"}; // Replace with actual column names
        Object[][] data = {{"Data 1", "Data 2", "Data 3"}, {"Data 4", "Data 5", "Data 6"}}; // Replace with actual data
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the frame
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK); // White text color
        button.setBackground(new Color(84, 11, 131)); // Purple background color
        button.setFocusPainted(false); // Remove focus highlighting for better appearance
        button.addActionListener(listener); // Add the listener
        return button;
    }

 // Action listener for "Add New" button
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField productNameField = new JTextField(10);
            JTextField productDescriptionField = new JTextField(10);
            JTextField productPriceField = new JTextField(10);
            JTextField productStockField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Product Name:"));
            panel.add(productNameField);
            panel.add(new JLabel("Product Description:"));
            panel.add(productDescriptionField);
            panel.add(new JLabel("Product Price:"));
            panel.add(productPriceField);
            panel.add(new JLabel("Product Stock:"));
            panel.add(productStockField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Enter Product Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String productName = productNameField.getText();
                    String productDescription = productDescriptionField.getText();
                    double productPrice = Double.parseDouble(productPriceField.getText());
                    int productStock = Integer.parseInt(productStockField.getText());

                    ProductHandler productHandler = new ProductHandler();
                    boolean success = productHandler.addProduct(null);

                    if (success) {
                        JOptionPane.showMessageDialog(ProductView.this, "Product added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(ProductView.this, "Failed to add product.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductView.this, "Invalid number format in Price or Stock fields.");
                }
            }
        }
    }

    // Action listener for "Update" button
    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ProductView.this, "Update button pressed");
        }
    }

    // Action listener for "Delete" button
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ProductView.this, "Delete button pressed");
        }
    }

    // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ProductView.this, "Search button pressed");
        }
    }

    }
