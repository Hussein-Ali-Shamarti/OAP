package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidePanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private ViewWindow viewWindow;

    public SidePanel(ViewWindow viewWindow) {
        this.viewWindow = viewWindow;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridLayout(4, 1));

        JButton productsButton = new JButton("Products");
        JButton ordersButton = new JButton("Orders");
        JButton customersButton = new JButton("Customers");
        JButton employeesButton = new JButton("Employees");

        // Add action listeners to category buttons
        productsButton.addActionListener(createActionListener("Products"));
        ordersButton.addActionListener(createActionListener("Orders"));
        customersButton.addActionListener(createActionListener("Customers"));
        employeesButton.addActionListener(createActionListener("Employees"));

        add(productsButton);
        add(ordersButton);
        add(customersButton);
        add(employeesButton);
    }

    private ActionListener createActionListener(final String category) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewWindow.getCardLayout().show(viewWindow.getCardPanel(), category);
            }
        };
    }
}