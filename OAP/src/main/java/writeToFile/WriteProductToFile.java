package writeToFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import view.ProductView;

public class WriteProductToFile {
	
	 private ProductView productView;
	   

	    public WriteProductToFile(ProductView productView) {
	        this.productView = productView;
	       
	    }



public void saveProductsToFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Specify a CSV file to save");
    fileChooser.setSelectedFile(new File("Products.csv")); // Set default file name

    int userSelection = fileChooser.showSaveDialog(null);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            List<String[]> products = productView.fetchAndDisplayProducts(); // Fetch product data

            // Write header row (optional)
            writer.write("Product Code, Product Name, Product Line, Product Scale, Product Vendor, " +
                         "Product Description, Quantity In Stock, Buy Price, MSRP");
            writer.newLine();

            // Write data rows
            for (String[] product : products) {
                String line = String.join(",", product); // Comma as delimiter
                writer.write(line);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "CSV file saved successfully at " + fileToSave.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

}