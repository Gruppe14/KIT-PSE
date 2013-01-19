package what.sp_config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ConfigHelper {
	
	protected static final String DB_NAME = "Name";
	
	protected static final String VERSION = "Version";
	
	protected static final String FIELDS = "Fields";

	private ConfigHelper() {
		// Method class, not there to be instanced
	}
	
	protected static File getConfigFile(String path) throws IOException {
		assert (path != null);
		
		File configFile = new File(path);
		
		if (!configFile.exists() || !configFile.isFile()) {
			throw new IOException();
		} else if (!configFile.exists() || !configFile.canRead()) {
			throw new IOException();
		}
		
		return configFile;
	}

	protected static String getJSONContent(File configFile) throws IOException, FileNotFoundException {
		assert (configFile != null);
		
		// I hope standard readers will do, otherwise we have to specify some for ourselves		
		Reader in = new FileReader(configFile);
		BufferedReader bIn = new BufferedReader(in);
		
		String content = readFile(bIn);
		
		if (content == null) {
			throw new IllegalArgumentException();
		}
		
		return content;
	}

	private static String readFile(BufferedReader bIn) throws IOException {
		String cur;
		String content = "";
		while ((cur = bIn.readLine()) != null) {
			content = content + cur;
		}
		
		return content;
	}
}
