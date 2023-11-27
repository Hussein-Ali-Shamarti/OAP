package Listners;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import database.DataBaseConnection;
import model.Order;

public class BulkImportOrders {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust date format as necessary

    public boolean importOrders(File csvFile) {
        try {
            List<Order> orders = parseCsvFile(csvFile);
            return insertOrdersIntoDatabase(orders);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
            return false;
        }
    }

    private List<Order> parseCsvFile(File csvFile) throws IOException {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assuming CSV values are comma-separated
                // Parse CSV values into Order fields
                Date orderDate = new Date(dateFormat.parse(values[1]).getTime());
                Date requiredDate = new Date(dateFormat.parse(values[2]).getTime());
                Date shippedDate = new Date(dateFormat.parse(values[3]).getTime());
                String status = values[4];
                String comments = values[5];
                int customerNumber = Integer.parseInt(values[6]);
                Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
                orders.add(order);
            }
        } catch (Exception e) {
            throw new IOException("Error parsing CSV file: " + e.getMessage(), e);
        }
        return orders;
    }

    private boolean insertOrdersIntoDatabase(List<Order> orders) throws Exception {
        String sql = "INSERT INTO orders (orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DataBaseConnection.prepareStatement(sql)) {
            for (Order order : orders) {
                statement.setDate(1, (Date) order.getOrderDate());
                statement.setDate(2, (Date) order.getRequiredDate());
                statement.setDate(3, (Date) order.getShippedDate());
                statement.setString(4, order.getStatus());
                statement.setString(5, order.getComments());
                statement.setInt(6, order.getCustomerNumber());
                statement.addBatch();
            }
            int[] counts = statement.executeBatch();
            return counts.length == orders.size();
        } // try-with-resources will auto-close the statement and connection
    }
}
