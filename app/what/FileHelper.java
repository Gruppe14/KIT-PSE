package what;

// java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Helper class concerning files.<br>
 * 
 * Provides methods for getting a file from a given path,
 * also with a given file extension, and to read a file.
 * 
 * @author Jonathan, PSE Gruppe 14
 *
 */
public class FileHelper {

	/** Static String to identify a JSON file. */
	public static String JSON = "json";
	
	/** Static String to identify a CSV file. */
	public static String CSV = "csv";
	
	/**
	 * Private constructor, which should no be used, 
	 * because this is a static helper class.
	 */
	private FileHelper() {
		// Method class, not to be instanced
	}
	
	/**
	 * Returns a file from a given path. Also checks if it exists and is readable.
	 * 
	 * @param path String path where a file should be
	 * @return a file from a given path
	 */
	public static File getFile(String path) {
		if (path == null) {
			throw new IllegalArgumentException();
		}
		
		// get file
		File file = new File(path);
		
		// check file
		if (!file.exists() || !file.isFile()) {
			Printer.perror("Not a file!");
			return null;
		} else if (!file.canRead()) {
			Printer.perror("Not a readable file!");
			return null;
		}
		
		return file;
	}
	
	/**
	 * Returns a file from a given path. Also checks if it exists, is readable
	 * and is of the given type (extension).<br>
	 * You might use the constants of this class to determine the type.
	 * 
	 * @param path String path where a file should be
	 * @param extension String extension the file should have
	 * @return a file from a given path, if it is of the requested type
	 */
	public static File getFileForExtension(String path, String extension) {
		if (path == null) {
			throw new IllegalArgumentException();
		}
		
		// get file
		File configFile = getFile(path);
		if (configFile == null) {
			return null; // error output made in the getFile method 
		}
		
		// get file extension
		String fileExtension = getFileExtension(configFile);
		if (fileExtension == null) {
			Printer.pfail("Determine file type.");
			return null;
		}
		
		// check file extension
		if (fileExtension.equalsIgnoreCase(extension)) {
			return configFile;
		} else {
			Printer.perror("Wrong file extension");
			return null;
		}
	}
	
	/**
	 * Returns the file extension for a given file (without the dot).
	 * 
	 * @param file File from which the extension is requested
	 * @return the file extension for a given file
	 */
	private static String getFileExtension(File file) {
		assert (file != null);
		
		// get the file name
		String fileName = file.getName();
		if ((fileName == null) || (fileName.equalsIgnoreCase(""))) {
			Printer.perror("Reading file name");
			return null;
		}
		
		String extension = null;
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		} else {
			Printer.pfail("No extension found for: " + fileName);
		}
		
		return extension;
	}

	/**
	 * Extracts the String from the file.
	 * 
	 * @param configFile file from which is to be read
	 * @return the string from the file
	 * @throws IOException if reading doesn't work
	 * @throws FileNotFoundException if file did not exist
	 */
	public static String getStringContent(File configFile) {
		assert (configFile != null);
		
		// get a buffered reader
		Reader in;
		try {
			in = new FileReader(configFile);
		} catch (FileNotFoundException e) {
			Printer.perror("File not found for given path!");
			e.printStackTrace();
			return null;
		}
		BufferedReader bIn = new BufferedReader(in);
		
		// read
		String content;
		try {
			content = readFile(bIn);
		} catch (IOException e) {
			Printer.perror("Reading file failed!");
			e.printStackTrace();
			return null;
		}
		
		// failed?
		if (content == null) {
			 Printer.pfail("No content found in file!");
		}
		
		return content;
	}

	/**
	 * Little helper method for getJSONContent(..).
	 * Reads the string from the file
	 * 
	 * @param bIn buffered reader from which shall be read
	 * @return string what was to be read from reader
	 * @throws IOException if reading failed
	 */
	private static String readFile(BufferedReader bIn) throws IOException {
		String cur;
		String content = "";
		while ((cur = bIn.readLine()) != null) {
			content = content + cur;
		}
		
		return content;
	}
	
}
