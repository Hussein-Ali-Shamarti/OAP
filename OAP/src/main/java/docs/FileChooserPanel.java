package docs;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileChooserPanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private JTextField filePathField;
    private JButton browseButton;
    private JFileChooser fileChooser;

    public FileChooserPanel() {
        setLayout(new BorderLayout());

        filePathField = new JTextField();
        add(filePathField, BorderLayout.CENTER);

        browseButton = new JButton("Browse");
        add(browseButton, BorderLayout.EAST);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(FileChooserPanel.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    filePathField.setText(file.getAbsolutePath());
                }
            }
        });
    }

    public String getSelectedFilePath() {
        return filePathField.getText();
    }
}
