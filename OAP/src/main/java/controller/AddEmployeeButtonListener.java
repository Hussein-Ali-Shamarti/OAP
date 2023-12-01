package controller;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Employee;
import model.EmployeeDAO;

import view.EmployeeView;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class AddEmployeeButtonListener implements ActionListener {
    private final EmployeeView employeeView;
    private final EmployeeDAO employeeDAO;

    public AddEmployeeButtonListener(EmployeeView employeeView, EmployeeDAO employeeDAO) {
        this.employeeView = employeeView;
        this.employeeDAO = employeeDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Employee employee = employeeView.gatherUserInputForAddEmployee();

        if (employee != null) {
            boolean success = employeeDAO.addEmployee(employee);

            if (success) {
                JOptionPane.showMessageDialog(employeeView, "Employee added successfully!");
            } else {
                JOptionPane.showMessageDialog(employeeView, "Failed to add employee.");
            }
        }
    }
}

