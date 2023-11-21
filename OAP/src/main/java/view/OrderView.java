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
import javax.swing.table.DefaultTableModel;

public class OrderView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;

    public OrderView() {
<<<<<<< HEAD
        // Set title
=======
>>>>>>> 47e97275ed0112486fba52e8bb5651afe5c8206f
        super("Order Management");

        // Set layout for the frame
        setLayout(new BorderLayout());
<<<<<<< HEAD

        // Set background color
        getContentPane().setBackground(Color.WHITE);

        // Create JLabel for the title
=======
        initializeUI();
        fetchAndDisplayOrders();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        pack(); // Adjusts the frame to fit the components
        setVisible(true); // Make sure the frame is visible
    }
    


    private void initializeUI() {
>>>>>>> 47e97275ed0112486fba52e8bb5651afe5c8206f
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131));
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
<<<<<<< HEAD

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
        String[] columnNames = {"OrderNumber", "orderDate", "requiredDate", "shippedDate", "status", "comments", "customerNumber"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
=======
        setupControlPanel();
        setupTable();
      
>>>>>>> 47e97275ed0112486fba52e8bb5651afe5c8206f

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

<<<<<<< HEAD
=======

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


>>>>>>> 47e97275ed0112486fba52e8bb5651afe5c8206f
    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(84, 11, 131));
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

<<<<<<< HEAD
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
     
  // Action listener for "Update" button
     private class AddButtonListener implements ActionListener {
         @Override
         public void actionPerformed(ActionEvent e) {
             JOptionPane.showMessageDialog(OrderView.this, "Add button pressed");
         }
     }

    // Action listener for "Update" button
    private class UpdateButtonListener implements ActionListener {
=======

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
>>>>>>> 47e97275ed0112486fba52e8bb5651afe5c8206f
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