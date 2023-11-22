package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

import controller.ProductHandler;
import model.Products;

public class ProductView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField textField; // Assuming you have a JTextField for search

    public ProductView() {
        super("Product Management");

        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayProducts();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        pack(); // Adjusts the frame to fit the components
        setVisible(true); // Make sure the frame is visible
    }

    private void initializeUI() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131));
        JLabel titleLabel = new JLabel("Product Management");
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
        String[] columnNames = {"Product Code", "Product Name", "Product Scale", "Product Vendor",
                "Product Description", "Quantity In Stock", "Buy Price", "MSRP"};
        tableModel = new DefaultTableModel(null, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        //customizeTableAppearance();//
    }

    private void setupControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        controlPanel.setBackground(new Color(90, 23, 139));

        JButton searchButton = createButton("Search",new SearchButtonListener());
        JButton addButton = createButton("Add", new AddButtonListener());
        JButton editButton = createButton("Edit", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());

        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);

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

    void fetchAndDisplayProducts() {
        tableModel.setRowCount(0);
        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String sql = "SELECT productCode, productName, productScale, productVendor, " +
                    "productDescription, quantityInStock, buyPrice, msrp FROM products";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getString("productCode"),
                        resultSet.getString("productName"),
                        resultSet.getString("productScale"),
                        resultSet.getString("productVendor"),
                        resultSet.getString("productDescription"),
                        resultSet.getInt("quantityInStock"),
                        resultSet.getDouble("buyPrice"),
                        resultSet.getDouble("msrp")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching product data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 // Action listener for "Add" button
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create an instance of ProductHandler
            ProductHandler productHandler = new ProductHandler();

            // Implement the logic for the "Add" button here
            // For example, you can open a dialog to input product details
            // and then call the addProduct method on the productHandler instance
            // Here's a simple example using JOptionPane for input:
            JTextField productCodeField = new JTextField(10);
            JTextField productNameField = new JTextField(20);
            JTextField productScaleField = new JTextField(10);
            JTextField productVendorField = new JTextField(20);
            JTextField productDescriptionField = new JTextField(50);
            JTextField quantityInStockField = new JTextField(5);
            JTextField buyPriceField = new JTextField(10);
            JTextField msrpField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Product Code:"));
            panel.add(productCodeField);
            panel.add(new JLabel("Product Name:"));
            panel.add(productNameField);
            panel.add(new JLabel("Product Scale:"));
            panel.add(productScaleField);
            panel.add(new JLabel("Product Vendor:"));
            panel.add(productVendorField);
            panel.add(new JLabel("Product Description:"));
            panel.add(productDescriptionField);
            panel.add(new JLabel("Quantity in Stock:"));
            panel.add(quantityInStockField);
            panel.add(new JLabel("Buy Price:"));
            panel.add(buyPriceField);
            panel.add(new JLabel("MSRP:"));
            panel.add(msrpField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Enter Product Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String productCode = productCodeField.getText();
                    String productName = productNameField.getText();
                    String productScale = productScaleField.getText();
                    String productVendor = productVendorField.getText();
                    String productDescription = productDescriptionField.getText();
                    int quantityInStock = Integer.parseInt(quantityInStockField.getText());
                    double buyPrice = Double.parseDouble(buyPriceField.getText());
                    double msrp = Double.parseDouble(msrpField.getText());

                    // Create a Products object with the entered details
                    Products product = new Products(productCode, productName, productScale, productVendor,
                            productDescription, quantityInStock, buyPrice, msrp);

                    // Call the addProduct method on the productHandler instance
                    boolean success = productHandler.addProduct(product);

                    if (success) {
                        JOptionPane.showMessageDialog(ProductView.this, "Product added successfully!");
                        // Refresh the product list or take any other necessary action
                    } else {
                        JOptionPane.showMessageDialog(ProductView.this, "Failed to add product.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductView.this, "Invalid input format.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProductView.this, "Error: " + ex.getMessage());
                }
            }
        }
    }



    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ProductView.this, "Update button pressed");
        }
    }

 // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ProductView.this, "Search button pressed");
        }
    }

    

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ProductView.this, "Delete button pressed");
        }

}