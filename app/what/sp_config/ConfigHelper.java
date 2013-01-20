package what.sp_config;

// java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

// json imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The ConfigHelper class provides static methods helping to
 * build a ConfigWrap and RowEntries.<br>
 * It also provides the static Strings for the .json configuration file. 
 * 
 * @author Jonathan, PSE Gruppe 14
 * @version 1.0
 *
 * @see ConfigWrap
 * @see RowEntries
 */
public class ConfigHelper {
	
	// highest level in configuration file
	/** 
	 * Static String for the name in the .json configuration file. 
	 * @see ConfigWrap
	 */
	protected static final String DB_NAME = "Name";
	
	/** 
	 * Static String for the version in the .json configuration file. 
	 * @see ConfigWrap
	 */
	protected static final String VERSION = "Version";
	
	/** 
	 * Static String for the fields in the .json configuration file.
	 * @see ConfigWrap
	 */
	protected static final String FIELDS = "Fields";

	// second level specifying fields/rows in configuration file
	/** 
	 * Static String for the name of a field/row in the .json configuration file. 
	 * @see RowEntry
	 */
	protected static final String ATRI_NAME = "Name";
	
	/** 
	 * Static String for the category of a field/row in the .json configuration file. 
	 * @see RowEntry
	 */
	protected static final String ATRI_CAT = "Categorie";
	
	/** 
	 * Static String for the level (in dimension) of a field/row in the .json configuration file.  
	 * @see RowEntry
	 */
	protected static final String ATRI_LVL = "Level";
	
	
	/** 
	 * Static String for the logId of a field/row in the .json configuration file.
	 * @see RowEntry
	 */
	protected static final String ATRI_LOG = "LogId";
	
	/** 
	 * Static String for the  of a field/row in the .json configuration file. 
	 * @see RowEntry
	 */
	protected static final String ATRI_SCL = "Scale";
	
	/** 
	 * Static String for the set or map of Strings of a StringRow 
	 * or StrinMapRow in the .json configuration file.
	 * @see RowEntry
	 * @see StringRow
	 * @see StringMapRow
	 */
	protected static final String ATRI_STRINGS = "List";
	
	/** 
	 * Static String for the partent/topic of a string array set in the .json configuration file.
	 * @see StringMapRow
	 */
	protected static final String STRING_NAME = "ParentName";

	
	/** 
	 * Static Strings for the type of a field/row in the .json configuration file 
	 * @see RowEntry
	 * @see RowId
	 */
	protected static final String ATRI_TYPE = "Type";
	
	// specific strings for the types
	/** 
	 * Static Strings for a specific type of a field/row in the .json configuration file 
	 * @see RowEntry
	 * @see RowId
	 * @see IntRow
	 */
	protected static final String TYPE_INT = "INT";
	
	/** 
	 * Static Strings for a specific type of a field/row in the .json configuration file 
	 * @see RowEntry
	 * @see RowId
	 * @see DoubleRow
	 */
	protected static final String TYPE_DOUBLE = "DOUBLE";
	
	/** 
	 * Static Strings for a specific type of a field/row in the .json configuration file 
	 * @see RowEntry
	 * @see RowId
	 * @see StringRow
	 */
	protected static final String TYPE_STRING = "STRING";
	
	/** 
	 * Static Strings for a specific type of a field/row in the .json configuration file 
	 * @see RowEntry
	 * @see RowId
	 * @see IpLocationRow
	 */
	protected static final String TYPE_LOCATION = "IP";
	
	/** Static Strings for a specific type of a field/row in the .json configuration file 
	 * @see RowEntry
	 * @see RowId
	 * @see StringMapRow
	 */
	protected static final String TYPE_STRINGMAP = "STRINGMAP";
	
	/** Static Strings for a specific type of a field/row in the .json configuration file 
	 * @see RowEntry
	 * @see RowId
	 * @see DummyRow
	 */
	protected static final String TYPE_DUMMY = "NULL";

	
	
	/**
	 * Private constructor, should not be used as this is a static tool class.
	 */
	private ConfigHelper() {
		// Method class, not there to be instanced
	}
	
	/**
	 * Returns a file from a given path. Also checks if it exists, is readable
	 * and a .json file. 
	 * @param path path where a file should be
	 * @return a file from a given path
	 * @throws IOException if the file was not found, illegal or of wrong format
	 */
	protected static File getConfigFile(String path) throws IOException {
		assert (path != null);
		
		// get file
		File configFile = new File(path);
		
		// checks file
		if (!configFile.exists() || !configFile.isFile()) {
			throw new IOException();
		} else if (!configFile.exists() || !configFile.canRead()) {
			throw new IOException();
		}
		
		// checks if it is a .json file
		String fileName = configFile.getCanonicalPath();  
		if(!fileName.endsWith(".json"))  
		{  
		  System.out.print("Incorrect file format!");
		  throw new IOException();
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
	protected static String getJSONContent(File configFile) throws IOException, FileNotFoundException {
		assert (configFile != null);
		
		// get a buffered reader
		Reader in = new FileReader(configFile);
		BufferedReader bIn = new BufferedReader(in);
		
		// read
		String content = readFile(bIn);
		
		// failed?
		if (content == null) {
			System.out.println("No content found in file!");
			throw new IOException();
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
	
	/**
	 * Returns a RowEntry from a given JSONObject.
	 * It also extracts all information for it.
	 * 
	 * @param jso JSONObject from which a RowEntry shall be extracted
	 * @return a RowEntry from a given JSONObject
	 * @throws JSONException something went wrong with the use of JSON lib
	 */
	protected static RowEntry getEntry(JSONObject jso) throws JSONException {
		assert (jso != null);
		
		// determines type
		String type = jso.getString(ATRI_TYPE);
		RowId id = RowId.getRowIdByString(type);
		
		// failed?
		if (id == null) {
			System.out.println("Uncorrect type of row.");
			return null;
		}
		
		// get RowEntry
		RowEntry re = getEntryById(id, jso);
		
		return re;		
	}

	/**
	 * Helper class for getEntry(...).
	 * Returns a RowEntry in dependency to a JSONObject and a RowId.
	 * 
	 * @param id RowId of the RowEntry required
	 * @param jso JSONObject from which the information shall be extracted
	 * @return a RowEntry in dependency to a JSONObject and a RowId
	 * @throws JSONException something went wrong with the use of JSON lib
	 */
	private static RowEntry getEntryById(RowId id, JSONObject jso) throws JSONException {
		assert (id != null);
		assert (jso != null);
		
		// extract information for any type
		String name = jso.getString(ATRI_NAME);
		String logId = jso.getString(ATRI_LOG);
		String cat = jso.getString(ATRI_CAT);
		String scale = jso.getString(ATRI_SCL);
		int lvl = jso.getInt(ATRI_LVL);
		
		// determine the case and creat RowEntry
		switch (id) {
		case INT : 
			return new IntRow(name, logId, lvl, cat, scale);
		case DOUBLE :
			return new DoubleRow(name, logId, lvl, cat, scale);
		case STRING : 
			Set<String> strings = getStrings(jso);
			return new StringRow(name, logId, lvl, cat, scale, strings);
		case LOCATION :
			return new IpLocationRow(name, logId, lvl, cat, scale);
		case STRINGMAP :
			HashMap<String, Set<String>> maps = getMap(jso);
			return new StringMapRow(name, logId, lvl, cat, scale, maps);
		case DUMMY :
			return DummyRow.getInstance();
		default :
			throw new JSONException("No matching row found.");
		}
	}

	/**
	 * Helper class for getEntryById(..).
	 * Returns a HashMap extracted from a json object.
	 * 
	 * @param jso JSONObject from which the information shall be extracted
	 * @return a HashMap extracted from a json object.
	 * @throws JSONException something went wrong with the use of JSON lib
	 */
	private static HashMap<String, Set<String>> getMap(JSONObject jso) throws JSONException {
		assert (jso != null);
		
		HashMap<String, Set<String>> maps = new HashMap<String, Set<String>>();
		
		JSONArray contents = jso.getJSONArray(ATRI_STRINGS);
		
		for (int i = 0, l = contents.length(); i < l; i++) {
			JSONObject obj = (JSONObject) contents.get(i);	
			Set<String> values = getStrings(obj);
			String key = obj.getString(STRING_NAME);
			maps.put(key, values);
		}
		
		return maps;
	}

	/**
	 * Helper class for getEntryById(..).
	 * Returns a TreeSet extracted from a json object.
	 * 
	 * @param jso JSONObject from which the information shall be extracted
	 * @return a TreeSet of Strings extracted from a json object.
	 * @throws JSONException something went wrong with the use of JSON lib
	 */
	private static TreeSet<String> getStrings(JSONObject jso) throws JSONException {
		assert (jso != null);
		
		JSONArray contents = jso.getJSONArray(ATRI_STRINGS);
		TreeSet<String> strings = new TreeSet<String>();
		for (int i = 0, j = contents.length(); i < j; i++) {
			strings.add(contents.getString(i));
		}
		
		return strings;
	}


}
