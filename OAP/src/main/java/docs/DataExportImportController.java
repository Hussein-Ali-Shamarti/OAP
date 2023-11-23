package docs;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataExportImportController {

    private CSVFileManager csvFileManager;

    public DataExportImportController() {
        this.csvFileManager = new CSVFileManager();
    }

    public void exportDataToCSV(File file, List<String[]> data) {
        try {
            csvFileManager.writeToCSV(file, data);
            // Notify success
        } catch (IOException e) {
            // Handle exception
        }
    }

    public void importDataFromCSV(File file) {
        try {
            List<String[]> data = csvFileManager.readFromCSV(file);
            // Process and display data
        } catch (IOException e) {
            // Handle exception
        }
    }
}
