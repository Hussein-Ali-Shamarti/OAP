package view;

// Essential imports for GUI, events, and database operations
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.OrderHandler;
import model.Order;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;


public class OrderView extends JPanel {
   // private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField textField;

    // Database connection details
    private final String dbURL = "jdbc:mysql://127.0.0.1:3306/classicmodels";
    private final String user = "student";
    private final String password = "student";
	private OrderHandler oh = new OrderHandler();


    public OrderView() {
        initializeUI();
        fetchAndDisplayOrders();
    }
    /**
	 * @wbp.parser.entryPoint
	 */
    // Method to initialize the user interface
    private void initializeUI() {
       /* frame = new JFrame("Order Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setSize(1000, 600);
*/
    	  this.setLayout(new BorderLayout());
          this.setSize(1000, 600); 
        // Create and add the table to the frame
        setupTable();
        // Create and add control panel with buttons
        setupControlPanel();
        // Create and add bottom panel for reports
//        setupBottomPanel();

       // frame.setVisible(true);
    }

    // Method to set up the table
    private void setupTable() { 
        String[] columnNames = {"Order Number ", "Order Date", "requiredDate", "shippedDate", "Status", "comments", "Customer Number"};
        tableModel = new DefaultTableModel(null, columnNames) {
            private static final long serialVersionUID = 1L; // serialVersionUID added here

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        customizeTableAppearance();
        JScrollPane scrollPane = new JScrollPane(table);
        //frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    // Method to customize table appearance
    private void customizeTableAppearance() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L; // serialVersionUID added here

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) component).setBorder(new EmptyBorder(5, 10, 5, 10)); // Adjust padding

                if (isSelected) {
                    component.setBackground(new Color(0x5f0c8e)); // Selected row color
                } else {
                    component.setBackground(row % 2 == 0 ? new Color(0xFFFFFF) : new Color(0xF0F0F0)); // Zebra stripes
                }
                return component;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(true);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // Method to set up control panel with buttons
    private void setupControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        controlPanel.setBackground(new Color(90, 23, 139));

        // Add buttons to control panel
        String[] buttonTitles = {"Search", "Add", "Edit", "Delete", "back"};
        for (String title : buttonTitles) {
            JButton button = new JButton(title);
            button.addActionListener(this::onButtonClick); // Simplified action listener
            controlPanel.add(button);
        }

        textField = new JTextField(10);
        textField.setBackground(new Color(246, 248, 250));
        controlPanel.add(textField);

       // frame.getContentPane().add(controlPanel, BorderLayout.NORTH);
        this.add(controlPanel, BorderLayout.NORTH);
    }

 

    // Method to set up bottom panel for reports
   /* private void setupBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(new Color(90, 23, 139));

        String[] reportTitles = {"Monthly Report", "Yearly Report"};
        for (String title : reportTitles) {
            JButton button = new JButton(title);
            bottomPanel.add(button);
        }

        //frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }*/
    // Method to handle button clicks
    private void onButtonClick(ActionEvent e) {
    
        String command = e.getActionCommand();

        switch (command) {
            case "Search":
                searchOrders();
                break;
            case "Add":            	
                addOrder();
                break;
            case "Edit":
                editOrder();
                break;
            case "Delete":
                deleteOrder();
                break;
       
            default:
//                JOptionPane.showMessageDialog(frame, "Unknown action: " + command);
            	JOptionPane.showMessageDialog(this, "Unknown action: " + command);
                break;
        }
    }

    private void searchOrders() {
        String searchText = textField.getText();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(searchText));
        }
    }

    private void addOrder() {
        // Create a dialog to enter the order data
        JPanel panel = new JPanel(new GridLayout(0, 2)); // 2 columns

     // Define labels and fields for each order attribute
        JLabel labelOrderNumber = new JLabel("Order Number:"); 
        JTextField fieldOrderNumber = new JTextField();        
        panel.add(labelOrderNumber);                           
        panel.add(fieldOrderNumber);                          
        
        JLabel labelOrderDate = new JLabel("Order Date (yyyy-mm-dd):");
        JTextField fieldOrderDate = new JTextField();
        panel.add(labelOrderDate);
        panel.add(fieldOrderDate);

        JLabel labelRequiredDate = new JLabel("Required Date (yyyy-mm-dd):");
        JTextField fieldRequiredDate = new JTextField();
        panel.add(labelRequiredDate);
        panel.add(fieldRequiredDate);

        JLabel labelShippedDate = new JLabel("Shipped Date (yyyy-mm-dd):");
        JTextField fieldShippedDate = new JTextField();
        panel.add(labelShippedDate);
        panel.add(fieldShippedDate);

        JLabel labelStatus = new JLabel("Status:");
        JTextField fieldStatus = new JTextField();
        panel.add(labelStatus);
        panel.add(fieldStatus);

        JLabel labelComments = new JLabel("Comments:");
        JTextField fieldComments = new JTextField();
        panel.add(labelComments);
        panel.add(fieldComments);

        JLabel labelCustomerNumber = new JLabel("Customer Number:");
        JTextField fieldCustomerNumber = new JTextField();
        panel.add(labelCustomerNumber);
        panel.add(fieldCustomerNumber);

        // Display the dialog
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Order", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Parse and validate inputs
                int OrderNumber = Integer.parseInt(fieldOrderNumber.getText());
                Date orderDate = new SimpleDateFormat("yyyy-MM-dd").parse(fieldOrderDate.getText());
                Date requiredDate = new SimpleDateFormat("yyyy-MM-dd").parse(fieldRequiredDate.getText());
                Date shippedDate = new SimpleDateFormat("yyyy-MM-dd").parse(fieldShippedDate.getText());
                String status = fieldStatus.getText();
                String comments = fieldComments.getText();
                int customerNumber = Integer.parseInt(fieldCustomerNumber.getText());

                // Create a new order object
                Order newOrder = new Order(OrderNumber, requiredDate, shippedDate, status, comments, customerNumber, orderDate);
                System.out.println("newOrder " + newOrder);

                // Add the order to the database
                oh.addOrder(newOrder);
                fetchAndDisplayOrders();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
            }
        }
    }



    private void editOrder() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Create a dialog to edit the order data
            JPanel panel = new JPanel(new GridLayout(0, 2)); // 2 columns

            // Define labels and fields for each order attribute
            JLabel labelOrderNumber = new JLabel("Order Number:");
            JTextField fieldOrderNumber = new JTextField(tableModel.getValueAt(selectedRow, 0).toString());
            panel.add(labelOrderNumber);
            panel.add(fieldOrderNumber);

            JLabel labelOrderDate = new JLabel("Order Date (yyyy-mm-dd):");
            JTextField fieldOrderDate = new JTextField(tableModel.getValueAt(selectedRow, 1).toString()); // Use the correct index
            panel.add(labelOrderDate);
            panel.add(fieldOrderDate);

            JLabel labelRequiredDate = new JLabel("Required Date (yyyy-mm-dd):");
            JTextField fieldRequiredDate = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());
            panel.add(labelRequiredDate);
            panel.add(fieldRequiredDate);

            JLabel labelShippedDate = new JLabel("Shipped Date (yyyy-mm-dd):");
            JTextField fieldShippedDate = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
            panel.add(labelShippedDate);
            panel.add(fieldShippedDate);

            JLabel labelStatus = new JLabel("Status:");
            JTextField fieldStatus = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());
            panel.add(labelStatus);
            panel.add(fieldStatus);

            JLabel labelComments = new JLabel("Comments:");
            JTextField fieldComments = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());
            panel.add(labelComments);
            panel.add(fieldComments);

            JLabel labelCustomerNumber = new JLabel("Customer Number:");
            JTextField fieldCustomerNumber = new JTextField(tableModel.getValueAt(selectedRow, 6).toString());
            panel.add(labelCustomerNumber);
            panel.add(fieldCustomerNumber);

            // Display the dialog
            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Order", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Parse and validate inputs
                    int OrderNumber = Integer.parseInt(fieldOrderNumber.getText());
                    Date orderDate = new SimpleDateFormat("yyyy-MM-dd").parse(fieldOrderDate.getText());
                    Date requiredDate = new SimpleDateFormat("yyyy-MM-dd").parse(fieldRequiredDate.getText());
                    Date shippedDate = new SimpleDateFormat("yyyy-MM-dd").parse(fieldShippedDate.getText());
                    String status = fieldStatus.getText();
                    String comments = fieldComments.getText();
                    int customerNumber = Integer.parseInt(fieldCustomerNumber.getText());

                    // Create an updated order object
                    Order updatedOrder = new Order(OrderNumber, requiredDate, shippedDate, status, comments, customerNumber, orderDate);

                    // Update the order in the database
                    oh.editOrder(updatedOrder, OrderNumber);
                    fetchAndDisplayOrders();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No order selected.");
        }
    }

    



    private void deleteOrder() {
        int selectedRow = table.getSelectedRow();
        System.out.print(selectedRow);
      
        if (selectedRow >= 0) {
        	String value = tableModel.getValueAt(selectedRow, 0).toString();
        	int valueNew = Integer.parseInt(value);
        	System.out.println(value);
            // Confirm before deleting
//            int confirm = JOptionPane.showConfirmDialog(frame, "Delete this order?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this order?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
               // tableModel.removeRow(selectedRow);
                // TODO: Delete the order from the database
                oh.deleteOrder(valueNew);
                fetchAndDisplayOrders();
                
            }
        } else {
           // JOptionPane.showMessageDialog(frame, "No order selected.");
        	 JOptionPane.showMessageDialog(this, "No order selected.");
        }
    }




        // Method to fetch and display orders from the database
        private void fetchAndDisplayOrders() {
        	tableModel.setRowCount(0);
            try (Connection conn = DriverManager.getConnection(dbURL, user, password);
                 Statement statement = conn.createStatement()) {

                String sql = "SELECT OrderNumber, orderDate, requiredDate, shippedDate ,status, comments, customerNumber FROM orders";
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    Object[] row = {
                        resultSet.getString("OrderNumber"),
                        resultSet.getString("orderDate"),
                        resultSet.getString("requiredDate"),
                        resultSet.getString("shippedDate"),
                        resultSet.getString("status"),
                        resultSet.getString("comments"),
                        resultSet.getString("customerNumber")
                    };
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                try {
                    new OrderView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            });
            
        }
}
    
    


