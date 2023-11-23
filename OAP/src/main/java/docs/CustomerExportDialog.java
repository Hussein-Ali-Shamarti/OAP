package docs;
import javax.swing.*;

import database.DataBaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class CustomerExportDialog extends JDialog {

    private static final long serialVersionUID = 1L;
	private JComboBox<String> citySelector;
    private JButton exportButton;
    private FileChooserPanel fileChooserPanel;
    private DataBaseConnection dbConnection;

    public CustomerExportDialog(Frame owner, DataBaseConnection dbConnection) {
        super(owner, "Export Customers", true);
        this.dbConnection = dbConnection;

        citySelector = new JComboBox<>();
        exportButton = new JButton("Export");
        fileChooserPanel = new FileChooserPanel();

        populateCitySelector();

        setLayout(new BorderLayout());
        add(citySelector, BorderLayout.NORTH);
        add(fileChooserPanel, BorderLayout.CENTER);
        add(exportButton, BorderLayout.SOUTH);

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCity = (String) citySelector.getSelectedItem();
                String filePath = fileChooserPanel.getSelectedFilePath();
                if (filePath.isEmpty()) {
                    JOptionPane.showMessageDialog(CustomerExportDialog.this, "Please select a file path for export.");
                    return;
                }
                // Implement the export logic here using DataExportImportController
            }
        });

        pack();
    }

    private void populateCitySelector() {
        try (Connection conn = dbConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String sql = "SELECT DISTINCT city FROM customers ORDER BY city";
            ResultSet resultSet = statement.executeQuery(sql);
            Vector<String> cities = new Vector<>();
            while (resultSet.next()) {
                cities.add(resultSet.getString("city"));
            }
            citySelector.setModel(new DefaultComboBoxModel<>(cities));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching city data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
