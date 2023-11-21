package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

import database.DataBaseConnection;

public class ProductHandler {
    private DefaultTableModel tableModel;
    private DataBaseConnection DataBaseConnection;

    // Constructor to initialize the tableModel and database connection
    public ProductHandler(DefaultTableModel tableModel, DataBaseConnection dbConnection) {
        this.tableModel = tableModel;
        this.setDataBaseConnection(dbConnection);
    }

    // Method to fetch and display products from the database
    public void fetchAndDisplayProducts() {
        tableModel.setRowCount(0);
        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {

            String sql = "SELECT productCode, productName, productScale, productVendor, productDescription, quantityInStock, buyPrice, msrp FROM products";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getString("productCode"),
                    resultSet.getString("productName"),
                    resultSet.getString("productScale"),
                    resultSet.getString("productVendor"),
                    resultSet.getString("productDescription"),
                    resultSet.getString("quantityInStock"),
                    resultSet.getString("buyPrice"),
                    resultSet.getString("msrp")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public DataBaseConnection getDataBaseConnection() {
		return DataBaseConnection;
	}

	public void setDataBaseConnection(DataBaseConnection dataBaseConnection) {
		DataBaseConnection = dataBaseConnection;
	}
}
