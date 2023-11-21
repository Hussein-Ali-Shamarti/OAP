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

public class ProductView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    public ProductView() {
        super("Product Management");

        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayProducts();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
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


        // Create buttons with listeners
        JButton addButton = createButton("Add New", new AddButtonListener());
//        JButton updateButton = createButton("Update", new UpdateButtonListener());
//        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
//        JButton searchButton = createButton("Search", new SearchButtonListener());

        // Create JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(addButton);
//        buttonPanel.add(updateButton);
//        buttonPanel.add(deleteButton);
//        buttonPanel.add(searchButton);

        // Create JTable and JScrollPane
        String[] columnNames = {"Column 1", "Column 2", "Column 3"}; // Replace with actual column names
        Object[][] data = {{"Data 1", "Data 2", "Data 3"}, {"Data 4", "Data 5", "Data 6"}}; // Replace with actual data
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the frame

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

        JButton searchButton = createButton("Search", e -> searchProducts());
        JButton addButton = createButton("Add", new AddButtonListener());
        JButton editButton = createButton("Edit", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", e -> deleteProduct());

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
        button.setBackground(new Color(84, 11, 131));
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

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ProductView.this, "Add button pressed");
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ProductView.this, "Update button pressed");
        }
    }

    private void searchProducts() {
        // Implement search functionality
    }

    private void deleteProduct() {
        // Implement delete functionality
    }
}