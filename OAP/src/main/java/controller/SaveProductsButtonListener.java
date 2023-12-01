package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import view.ProductView;

public class SaveProductsButtonListener implements ActionListener {
	
	private final ProductView productView;
    

    public SaveProductsButtonListener(ProductView productView) {
        this.productView = productView;
        
    }
	
        @Override
        public void actionPerformed(ActionEvent e) {
            productView.saveProductsToFile();
        }
    }

