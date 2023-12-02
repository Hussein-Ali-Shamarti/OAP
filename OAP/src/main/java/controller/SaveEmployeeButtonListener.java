package controller;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.EmployeeDAO;
import view.EmployeeView;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * ActionListener for saving employee data to a file.
 */
public class SaveEmployeeButtonListener implements ActionListener {
    private EmployeeView employeeView;
    private EmployeeDAO employeeDAO;

    /**
     * Constructs a SaveEmployeeButtonListener with the specified view and data access object.
     *
     * @param employeeView The view component associated with this listener.
     * @param employeeDAO The data access object for employee data.
     */
    public SaveEmployeeButtonListener(EmployeeView employeeView, EmployeeDAO employeeDAO) {
        this.employeeView = employeeView;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        saveEmployeesToFile();
    }

    /**
     * Saves the employees' data to a CSV file.
     */
    private void saveEmployeesToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Employees.csv"));

        int userSelection = fileChooser.showSaveDialog(employeeView);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            writeEmployeeDataToFile(fileToSave);
        }
    }

    /**
     * Writes employee data to the specified file.
     *
     * @param fileToSave The file where data will be written.
     */
    private void writeEmployeeDataToFile(File fileToSave) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            List<String[]> employees = employeeDAO.fetchEmployees();
            writer.write("Employee Number, First Name, Last Name, Extension, Email, Office Code, Reports To, Job Title");
            writer.newLine();

            for (String[] employee : employees) {
                String line = String.join(",", employee);
                writer.write(line);
                writer.newLine();
            }

            JOptionPane.showMessageDialog(employeeView, "CSV file saved successfully at " + fileToSave.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(employeeView, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
