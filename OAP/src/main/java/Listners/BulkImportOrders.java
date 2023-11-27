package Listners;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import database.DataBaseConnection;
import model.Order;

public class BulkImportOrders {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 

    public boolean importOrders(File csvFile) {
        try {
            List<Order> orders = parseCsvFile(csvFile);
            return insertOrdersIntoDatabase(orders);
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

    private List<Order> parseCsvFile(File csvFile) throws IOException {
        List<Order> orders = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust date format as necessary
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line = br.readLine(); // Read the header to skip it

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assuming CSV values are comma-separated
                // Make sure you have the correct number of columns in each row
                if (values.length != 6) {
                    throw new IOException("Invalid number of columns in CSV file");
                }
                
                // Parse CSV values into Order fields
                Date orderDate = new Date(dateFormat.parse(values[0]).getTime());
                Date requiredDate = new Date(dateFormat.parse(values[1]).getTime());
                Date shippedDate = new Date(dateFormat.parse(values[2]).getTime());
                String status = values[3];
                String comments = values[4];
                int customerNumber = Integer.parseInt(values[5]);
                
                // Ensure proper values are being parsed and are not the header
                if (!status.equals("status")) { // Assuming 'status' is not a valid order status
                    Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
                    orders.add(order);
                }
            }
        } catch (ParseException e) {
            throw new IOException("Error parsing the date in the CSV file: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing the customer number in the CSV file: " + e.getMessage(), e);
        }
        return orders;
    }


    private boolean insertOrdersIntoDatabase(List<Order> orders) throws Exception {
        String sql = "INSERT INTO orders (orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DataBaseConnection.prepareStatement(sql)) {
            for (Order order : orders) {
                // Convert java.util.Date to java.sql.Date
                java.sql.Date sqlOrderDate = new java.sql.Date(order.getOrderDate().getTime());
                java.sql.Date sqlRequiredDate = new java.sql.Date(order.getRequiredDate().getTime());
                java.sql.Date sqlShippedDate = new java.sql.Date(order.getShippedDate().getTime());

                statement.setDate(1, sqlOrderDate);
                statement.setDate(2, sqlRequiredDate);
                statement.setDate(3, sqlShippedDate);
                statement.setString(4, order.getStatus());
                statement.setString(5, order.getComments());
                statement.setInt(6, order.getCustomerNumber());
                statement.addBatch();
            }
            int[] counts = statement.executeBatch();
            return counts.length == orders.size();
        } 
    }

}
