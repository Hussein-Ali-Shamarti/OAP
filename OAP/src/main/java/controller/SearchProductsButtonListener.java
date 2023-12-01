package controller;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Products;
import view.ProductView;

//Action listener for "Search" button
public class SearchProductsButtonListener implements ActionListener {
	
	 private ProductView productView;
	   

	    public SearchProductsButtonListener(ProductView productView) {
	        this.productView = productView;
	       
	    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // Create a dialog to input search criteria
        JTextField searchField = new JTextField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Search Products:"));
        panel.add(searchField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Search Products", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String searchCriteria = searchField.getText().trim();

            // Perform the search based on the user's input
            List<Products> searchResults = productView.performSearch(searchCriteria);

            // Update the table with the search results
            productView.updateTableWithSearchResults(searchResults);
        }
    }
}