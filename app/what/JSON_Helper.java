package what;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JSON_Helper {

	private JSON_Helper() {
		// Method class, not there to be instanced
	}
	
	/**
	 * Returns a file from a given path. Also checks if it exists, is readable
	 * and a .json file. 
	 * @param path path where a file should be
	 * @return a file from a given path
	 * @throws IOException if the file was not found, illegal or of wrong format
	 */
	public static File getJSONFile(String path) {
		assert (path != null);
		
		// get file
		File configFile = new File(path);
		
		// checks file
		if (!configFile.exists() || !configFile.isFile()) {
			printError("Not a file!");
		} else if (!configFile.canRead()) {
			printError("Not a readable file!");
		}
		
		// checks if it is a .json file
		String fileName = null;
		
		try {
			fileName = configFile.getCanonicalPath();
		} catch (IOException e) {
			printError("Reading the path failed!");
			e.printStackTrace();
		}  
		
		if(!fileName.endsWith(".json"))  
		{ 
		  printError("Incorrect file format!");
		  return null;
		} 
		
		return configFile;
	}
	

	

	/**
	 * Extracts the String from the file.
	 * 
	 * @param configFile file from which is to be read
	 * @return the string from the file
	 * @throws IOException if reading doesn't work
	 * @throws FileNotFoundException if file did not exist
	 */
	public static String getJSONContent(File configFile) {
		assert (configFile != null);
		
		// get a buffered reader
		Reader in;
		try {
			in = new FileReader(configFile);
		} catch (FileNotFoundException e) {
			printError("File not found for given path!");
			e.printStackTrace();
			return null;
		}
		
		BufferedReader bIn = new BufferedReader(in);
		
		// read
		String content;
		try {
			content = readFile(bIn);
		} catch (IOException e) {
			printError("Reading file failed!");
			e.printStackTrace();
			return null;
		}
		
		// failed?
		if (content == null) {
			 printError("No content found in file!");
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
	
	private static void printError(String string) {
		System.out.print("ERROR: " + string);		
	}
	
}
