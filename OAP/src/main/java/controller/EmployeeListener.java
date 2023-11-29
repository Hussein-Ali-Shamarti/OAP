package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import view.EmployeeView;

/**
 * ActionListener for handling events related to employees.
 * Opens the {@link EmployeeView} when the "Employees" button is pressed.
 * Uses {@link SwingUtilities#invokeLater(Runnable)} to ensure the GUI is updated on the Event Dispatch Thread.
 */
public class EmployeeListener implements ActionListener {

    /**
     * Invoked when an action occurs, in this case, opens the {@link EmployeeView}.
     *
     * @param e The event representing the user's action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open EmployeeView when "Employees" button is pressed
        SwingUtilities.invokeLater(() -> {
            EmployeeView employeeView = new EmployeeView();
            employeeView.setVisible(true);
        });
    }
}