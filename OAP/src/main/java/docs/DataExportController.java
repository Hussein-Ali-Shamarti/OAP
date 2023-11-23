
package docs;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DataBaseConnection;

public class DataExportController {

    private DataBaseConnection dbConnection;

    public DataExportController(DataBaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void exportCustomersByCity(String city, String filePath) throws SQLException, IOException {
        String sql = "SELECT * FROM customers WHERE city = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, city);
            ResultSet resultSet = pstmt.executeQuery();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Writing the header
                writer.write("CustomerNumber,CompanyName,ContactLastName,ContactFirstName,Phone,AddressLine1,AddressLine2,City,State,PostalCode,Country,SalesRepEmployeeNumber,CreditLimit");
                writer.newLine();
                
                // Writing the data
                while (resultSet.next()) {
                    writer.write(resultSet.getInt("customerNumber") + ",");
                    writer.write(resultSet.getString("customerName") + ",");
                    writer.write(resultSet.getString("contactLastName") + ",");
                    writer.write(resultSet.getString("contactFirstName") + ",");
                    writer.write(resultSet.getString("phone") + ",");
                    writer.write(resultSet.getString("addressLine1") + ",");
                    writer.write(resultSet.getString("addressLine2") + ",");
                    writer.write(resultSet.getString("city") + ",");
                    writer.write(resultSet.getString("state") + ",");
                    writer.write(resultSet.getString("postalCode") + ",");
                    writer.write(resultSet.getString("country") + ",");
                    writer.write(resultSet.getInt("salesRepEmployeeNumber") + ",");
                    writer.write(resultSet.getString("creditLimit"));
                    writer.newLine();
                }
            }
        }
    }
}
