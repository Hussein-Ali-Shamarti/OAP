package docs;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileAccessSettingsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
	private JButton changeFolderBtn; // Button to change the folder
    private JLabel folderPathLabel; // Label to display the selected folder path
    private FolderManager folderManager;

    /**
     * Constructs a FileAccessSettingsPanel.
     */
    public FileAccessSettingsPanel(FolderManager folderManager) {
        this.folderManager = folderManager;

        setPreferredSize(new Dimension(600, 60));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "File Access Settings"));

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        changeFolderBtn = new JButton("Change Folder");
        changeFolderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChangeFolder();
            }
        });
        add(changeFolderBtn);

        folderPathLabel = new JLabel("Current folder: " + folderManager.getFolderPath());
        add(folderPathLabel);
    }

    private void handleChangeFolder() {
        String newPath = folderManager.chooseFolder();
        if (newPath != null) {
            folderPathLabel.setText("Current folder: " + newPath);
            checkTextLength();
        }
    }

    public void checkTextLength() {
        String text = folderPathLabel.getText();
        int maxLength = 50; // Maximum number of characters allowed

        if (text.length() > maxLength) {
            folderPathLabel.setToolTipText(text); // Set full path as tooltip
            String truncatedText = text.substring(0, maxLength - 3) + "...";
            folderPathLabel.setText(truncatedText);
        }
    }
}
