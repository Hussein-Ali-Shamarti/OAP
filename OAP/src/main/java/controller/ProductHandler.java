 package controller;

    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import javax.swing.JOptionPane;

    import model.ProductDAO;
    import model.Products;
    import view.ProductView;

public class ProductHandler {
	
	
	
	private final ProductView productView;
    private final ProductDAO productDAO;

    public ProductHandler(ProductView productView, ProductDAO productDAO) {
        this.productView = productView;
        this.productDAO = productDAO;
    }

    
   

    public class AddProductButtonListener implements ActionListener {
      
        

        @Override
        public void actionPerformed(ActionEvent e) {
            Products product = productView.gatherUserInputForAddProduct();

            if (product != null) {
                boolean success = productDAO.addProduct(product);

                if (success) {
                    JOptionPane.showMessageDialog(productView, "Product added successfully!");
                    // Refresh the product list or take any other necessary action
                } else {
                    JOptionPane.showMessageDialog(productView, "Failed to add product.");
                }
            }
        }
    }
    
    public class UpdateProductButtonListener implements ActionListener {
  

        @Override
        public void actionPerformed(ActionEvent e) {
            String productCodeToUpdate = JOptionPane.showInputDialog(productView, "Enter Product Code to update:");

            if (productCodeToUpdate != null && !productCodeToUpdate.isEmpty()) {
                Products existingProduct = productDAO.fetchProductFromDatabase(productCodeToUpdate);

                if (existingProduct != null) {
                    Products updatedProduct = productView.gatherUserInputForUpdate(existingProduct);

                    if (updatedProduct != null) {
                        boolean success = productDAO.updateProduct(updatedProduct);

                        if (success) {
                            JOptionPane.showMessageDialog(productView, "Product updated successfully!");
                            productView.fetchAndDisplayProducts(); // Update the table with the new data
                        } else {
                            JOptionPane.showMessageDialog(productView, "Failed to update product.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(productView, "Product not found.");
                }
            }
        }
        
    }
    
    
    
    
    
}
