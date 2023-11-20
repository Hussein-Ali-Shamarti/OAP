package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import com.mysql.cj.xdevapi.Statement;

import database.DataBaseConnection;

public class ProductHandler {
	
	

	// Method to fetch and display orders from the database
    private void fetchAndDisplayOrders() {
    	
    
		tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(dbURL, user, password);
             Statement statement = conn.createStatement()) {

            String sql = "SELECT orderNumber, orderDate, requiredDate, shippedDate ,status, comments, customerNumber FROM orders";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getString("productCode"),
                    resultSet.getString("customerName"),
                    resultSet.getString("productScale"),
                    resultSet.getString("productVendor"),
                    resultSet.getString("productDescription"),
                    resultSet.getString("quantityInStock"),
                    resultSet.getString("buyPrice")
                    
                    this.productCode = productCode;
                    this.productName = productName;
                    this.productScale = productScale;
                    this.productVendor = productVendor;
                    this.productDescription = productDescription;
                    this.quantityInStock = quantityInStock;
                    this.buyPrice = buyPrice;
                    this.msrp = msrp;
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   

    
}


