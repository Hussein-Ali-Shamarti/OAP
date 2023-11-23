package docs;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFileManager {

    public void writeToCSV(File file, List<String[]> data) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String[] line : data) {
                writer.println(String.join(",", line));
            }
        }
    }

    public List<String[]> readFromCSV(File file) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parsedLine = line.split(",");
                data.add(parsedLine);
            }
        }
        return data;
    }
}
