package docs;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DocumentsManager implements IDocumentsManager {
    
    public static final DateTimeFormatter DEFAULT_TIMESTAMP_FORMATTER = 
            DateTimeFormatter.ofPattern("d_MMM_uuuu-HH_mm_ss");
    
    @Override
    public void writeToFile(String text, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        }
        // No need for a finally block; try-with-resources automatically closes the writer
    }
}
