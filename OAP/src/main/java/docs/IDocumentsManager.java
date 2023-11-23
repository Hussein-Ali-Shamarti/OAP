package docs;

import java.io.File;
import java.io.IOException;

/**
 * Abstraction for file handling functionalities.
 */
public interface IDocumentsManager {
    
	/**
     *  Writes text to specific file.
     */
    void writeToFile(String text, File file) throws IOException;
}
