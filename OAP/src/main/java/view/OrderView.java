package view;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import controller.OrderHandler;
import model.Order; 
import database.DataBaseConnection;

public class OrderView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField textField;
    private OrderHandler oh = new OrderHandler();

    public OrderView() {
        super("Order Management");
        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayOrders();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        pack(); // Adjusts the frame to fit the components
        setVisible(true); // Make sure the frame is visible
    }
    


    private void initializeUI() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131));
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        setupControlPanel();
        setupTable();
      

        add(titlePanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }


    private void setupTable() {
        String[] columnNames = {"Order Number", "Order Date", "Required Date", "Shipped Date", "Status", "Comments", "Customer Number"};
        tableModel = new DefaultTableModel(null, columnNames) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        customizeTableAppearance();
    }

     private void customizeTableAppearance() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) component).setBorder(new EmptyBorder(5, 10, 5, 10));
                if (isSelected) {
                    component.setBackground(new Color(0x5f0c8e));
                } else {
                    component.setBackground(row % 2 == 0 ? new Color(0xFFFFFF) : new Color(0xF0F0F0));
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
     
  
     
     private void setupControlPanel() {
    	    JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10)); // Adjust as needed
    	    controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
    	    controlPanel.setBackground(new Color(90, 23, 139));

    	    JButton searchButton = createButton("Search", e -> searchOrders());
    	    JButton addButton = createButton("Add", new AddButtonListener());
    	    JButton editButton = createButton("Edit", new AddButtonListener()); // Should be UpdateButtonListener
    	    JButton deleteButton = createButton("Delete", e -> deleteOrder());

    	    controlPanel.add(searchButton);
    	    controlPanel.add(addButton);
    	    controlPanel.add(editButton);
    	    controlPanel.add(deleteButton);

    	    JPanel buttonPanelHolder = new JPanel(new BorderLayout());
    	    buttonPanelHolder.add(controlPanel, BorderLayout.NORTH); // Adjust as per your layout
    	    this.add(buttonPanelHolder, BorderLayout.SOUTH);
    	}


     private void searchOrders() {
    	    String searchText = textField.getText(); 
    	    List<Order> orders = oh.searchOrders(searchText); 
    	    updateTableData(orders); 
    	}

    private void updateTableData(List<Order> orders) {
        tableModel.setRowCount(0);
        for (Order order : orders) {
            tableModel.addRow(new Object[]{
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getRequiredDate(),
                order.getShippedDate(),
                order.getStatus(),
                order.getComments(),
                order.getCustomerNumber()
            });
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
                oh.deleteOrder(valueNew);
                fetchAndDisplayOrders();
                
            }
        } else {
           // JOptionPane.showMessageDialog(frame, "No order selected.");
        	 JOptionPane.showMessageDialog(this, "No order selected.");
        }
    }


    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(84, 11, 131));
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }


 // Method to fetch and display orders from the database
    void fetchAndDisplayOrders() {
       tableModel.setRowCount(0);
       try (Connection conn = database.DataBaseConnection.getConnection();
            Statement statement = conn.createStatement()) {
           String sql = "SELECT OrderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM orders";
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
           JOptionPane.showMessageDialog(this, "Error fetching order data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
       }
   }
 
    
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a dialog to enter the order data
            JPanel panel = new JPanel(new GridLayout(0, 2)); // 2 columns layout

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
            int result = JOptionPane.showConfirmDialog(OrderView.this, panel, "Add New Order", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Parse inputs and validate
                    int orderNumber = Integer.parseInt(fieldOrderNumber.getText());
                    Date orderDate = new SimpleDateFormat("yyyy-MM-dd").parse(fieldOrderDate.getText());
                    Date requiredDate = new SimpleDateFormat("yyyy-MM-dd").parse(fieldRequiredDate.getText());
                    Date shippedDate = fieldShippedDate.getText().isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd").parse(fieldShippedDate.getText());
                    String status = fieldStatus.getText();
                    String comments = fieldComments.getText();
                    int customerNumber = Integer.parseInt(fieldCustomerNumber.getText());

                    // Create a new order object
                    Order newOrder = new Order(orderNumber, new java.sql.Date(requiredDate.getTime()), shippedDate != null ? new java.sql.Date(shippedDate.getTime()) : null, status, comments, customerNumber, new java.sql.Date(orderDate.getTime()));

                    // Add the order to the database
                    boolean success = oh.addOrder(newOrder);
                    if (success) {
                    	JOptionPane.showMessageDialog(OrderView.this, "Order added successfully.");
                        fetchAndDisplayOrders();
                    } else {
                    	JOptionPane.showMessageDialog(OrderView.this, "Failed to add order.");
                    }
                } catch (NumberFormatException | ParseException ex) {
                	JOptionPane.showMessageDialog(OrderView.this, "Invalid input: " + ex.getMessage());
                }
            }
        }


        private class UpdateButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(OrderView.this, "No order selected for update.");
                    return;
                }

                // Extract existing order details from the selected row
                int orderNumber = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                String currentOrderDate = tableModel.getValueAt(selectedRow, 1).toString();
                String currentRequiredDate = tableModel.getValueAt(selectedRow, 2).toString();
                String currentShippedDate = tableModel.getValueAt(selectedRow, 3).toString();
                String currentStatus = tableModel.getValueAt(selectedRow, 4).toString();
                String currentComments = tableModel.getValueAt(selectedRow, 5).toString();
                String currentCustomerNumber = tableModel.getValueAt(selectedRow, 6).toString();

                // Create a dialog to enter updated order data
                JPanel panel = new JPanel(new GridLayout(0, 2)); // 2 columns layout

                // Define labels and fields for each order attribute
                JLabel labelOrderDate = new JLabel("Order Date (yyyy-mm-dd):");
                JTextField fieldOrderDate = new JTextField(currentOrderDate);
                panel.add(labelOrderDate);
                panel.add(fieldOrderDate);

                JLabel labelRequiredDate = new JLabel("Required Date (yyyy-mm-dd):");
                JTextField fieldRequiredDate = new JTextField(currentRequiredDate);
                panel.add(labelRequiredDate);
                panel.add(fieldRequiredDate);

                JLabel labelShippedDate = new JLabel("Shipped Date (yyyy-mm-dd):");
                JTextField fieldShippedDate = new JTextField(currentShippedDate);
                panel.add(labelShippedDate);
                panel.add(fieldShippedDate);

                JLabel labelStatus = new JLabel("Status:");
                JTextField fieldStatus = new JTextField(currentStatus);
                panel.add(labelStatus);
                panel.add(fieldStatus);

                JLabel labelComments = new JLabel("Comments:");
                JTextField fieldComments = new JTextField(currentComments);
                panel.add(labelComments);
                panel.add(fieldComments);

                JLabel labelCustomerNumber = new JLabel("Customer Number:");
                JTextField fieldCustomerNumber = new JTextField(currentCustomerNumber);
                panel.add(labelCustomerNumber);
                panel.add(fieldCustomerNumber);

                // Display the dialog
                int result = JOptionPane.showConfirmDialog(OrderView.this, panel, "Update Order", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        // Parse and validate inputs
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date orderDate = dateFormat.parse(fieldOrderDate.getText());
                        Date requiredDate = dateFormat.parse(fieldRequiredDate.getText());
                        Date shippedDate = fieldShippedDate.getText().isEmpty() ? null : dateFormat.parse(fieldShippedDate.getText());
                        String status = fieldStatus.getText();
                        String comments = fieldComments.getText();
                        int customerNumber = Integer.parseInt(fieldCustomerNumber.getText());

                        // Create an updated order object
                        Order updatedOrder = new Order(orderNumber, requiredDate, shippedDate, status, comments, customerNumber, orderDate);

                        // Update the order in the database using OrderHandler
                        boolean success = oh.editOrder(updatedOrder, orderNumber);

                        if (success) {
                            JOptionPane.showMessageDialog(OrderView.this, "Order updated successfully.");
                            // Optionally, refresh the table to show updated data
                        } else {
                            JOptionPane.showMessageDialog(OrderView.this, "Failed to update order.");
                        }
                    } catch (NumberFormatException | ParseException ex) {
                        JOptionPane.showMessageDialog(OrderView.this, "Invalid input: " + ex.getMessage());
                    }
                }
            }
        


            private class DeleteButtonListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(OrderView.this, "No order selected for deletion.");
                        return;
                    }

                    // Extract the order number from the selected row
                    int orderNumber = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

                    // Confirm deletion
                    int confirm = JOptionPane.showConfirmDialog(OrderView.this, "Are you sure you want to delete this order?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = oh.deleteOrder(orderNumber);

                        if (success) {
                            JOptionPane.showMessageDialog(OrderView.this, "Order deleted successfully.");
                            // Optionally, refresh the table to remove the deleted row
                        } else {
                            JOptionPane.showMessageDialog(OrderView.this, "Failed to delete order.");
                        }
                    }
                }
            }


      // Action listener for "Search" button
         private class SearchButtonListener implements ActionListener {
          @Override
            public void actionPerformed(ActionEvent e) {
            searchOrders();
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
    }
}
