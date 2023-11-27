package Listners;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import view.EmployeeView;

class EmployeesListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open EmployeeView when "Employees" button is pressed
    	 SwingUtilities.invokeLater(() -> {
             EmployeeView employeeView = new EmployeeView();
             employeeView.setVisible(true);
    	 });
    	 
    }
    
}