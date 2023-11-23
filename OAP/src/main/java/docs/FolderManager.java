package docs;
import javax.swing.JFileChooser;
import java.io.File;

public class FolderManager {
    
    private String folderPath;
    
    public FolderManager() {
        this.folderPath = getDefaultFolderPath();
    }
  
    public String getFolderPath() {
        return folderPath;
    }
    
    public void setFolderPath(String path) {
        folderPath = path;
    }
    
    private String getDefaultFolderPath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator;
    }
    
    public String chooseFolder() {
        JFileChooser fileChooser = new JFileChooser(folderPath); // Start in the current folder path
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = fileChooser.showOpenDialog(null); // parent component is null
        
        if (result == JFileChooser.APPROVE_OPTION) {
            folderPath = fileChooser.getSelectedFile().getPath();
            return folderPath;
        } else {
            return null; // Return null if no folder is chosen
        }
    }
}
