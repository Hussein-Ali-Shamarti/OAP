package services;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileService {

    public List<String> readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path);
    }

    public void writeFile(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    // Add more methods if you need to handle specific file operations
}
