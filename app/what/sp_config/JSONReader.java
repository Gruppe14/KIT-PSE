package what.sp_config;

// java imports
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

// JSON imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// intern imports
import what.Printer;

/**
 * The ConfigHelper class provides methods helping to
 * read from JSONObjects and building RowEntries.<br>
 * It also provides the static Strings for the JSON configuration file. 
 * 
 * @author Jonathan, PSE Gruppe 14
 * @version 1.0
 *
 * @see ConfigWrap
 * @see RowEntries
 */
public class JSONReader {
	
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
	 * Static String for the parent/topic of a string array set in the .json configuration file.
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

	// specific strings for the database 
	public static final String DIM_TABLE = "dim_";
	public static final String ROW_TABLE = "row_";
	public static final String KEY_TABLE = "_key";
	public static final String FACT_TABLE = "fact_";
	
	// -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES --
	/** JSONObject on which this JSONReader will work. */
	private JSONObject json;
	
	// -- CONSTRUCTOR --CONSTRUCTOR --CONSTRUCTOR --CONSTRUCTOR --CONSTRUCTOR --
	/**
	 * Private constructor for a JSONReader.
	 * 
	 * @param json JSONObject on which this JSONReader will work
	 */
	protected JSONReader(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException();
		}
		
		this.json = json;
	}

	// -- SETTER -- SETTER -- SETTER -- SETTER -- SETTER -- 
	/**
	 * Changes the JSONObject on which this JSONReader will work.
	 * 
	 * @param json JSONObject on which this JSONReader will work
	 */
	protected void setJson(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException();
		}
		
		this.json = json;
	}

	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns a String belonging to a given key (String) 
	 * from the JSONObject of this JSONReader.
	 * 
	 * @param key field in the JSONObject
	 * @return a String belonging to a given key
	 */
	protected String getString(String key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		
		String requ = null;
		try {
			if (json.has(key)) {
				requ = json.getString(key);
			}
		} catch (JSONException e) {
			Printer.perror("Extracting String from JSONObject for key: " + key);
			return null;
		}
		
		return requ;
	}
	
	/**
	 * Returns a integer belonging to a given key (String) 
	 * from the JSONObject of this JSONReader.
	 * 
	 * @param key field in the JSONObject
	 * @return a integer belonging to a given key
	 */
	protected int getInt(String key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		
		int requ = -1;
		try {
			if (json.has(key)) {
				requ = json.getInt(key);
			}
		} catch (JSONException e) {
			Printer.perror("Extracting int from JSONObject for key: " + key);
			return -2;
		}
		
		return requ;
	}

	/**
	 * Returns a JSONArray belonging to a given key (String) 
	 * from the JSONObject of this JSONReader.
	 * 
	 * @param key field in the JSONObject
	 * @return a JSONArray belonging to a given key
	 */
	protected JSONArray getJSONArray(String key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		
		JSONArray ary = null;
		
		try {
			if (json.has(key)) {
				ary = json.getJSONArray(key);
			}
		} catch (JSONException e) {
			Printer.perror("Extracting JSONArray from JSONObject for key: " + key);
			return null;
		}
		
		return ary;
	}
	
	/**
	 * Returns a RowEntry from the JSONObject of this reader.
	 * It also extracts all information for it.
	 * 
	 * @return a RowEntry from a given JSONObject
	 */
	protected RowEntry getRowEntry()  {
		// extract type
		String type = getString(ATRI_TYPE);
		if (type == null) {
			Printer.pfail("Getting RowEntry from JSONObject. Type from JSONObject.");
			return null;
		}
		
		// determine type
		RowId id = RowId.getRowIdByString(type);
		if (id == null) {
			Printer.perror("Getting RowEntry. RowId unknown: " + type);
			return null;
		}
				
		// get RowEntry
		RowEntry re = getEntryById(id);
		if (re == null) {
			Printer.pfail("Getting RowEntry.");
			return null;
		}
		
		return re;		
	}
	
	// -- PRIVATE GETTER -- PRIVATE GETTER -- PRIVATE GETTER --
	/**
	 * Helper class for getEntry(...).
	 * Returns a RowEntry in dependency to a JSONObject and a RowId.
	 * 
	 * @param id RowId of the RowEntry required
	 * @return a RowEntry in dependency to a JSONObject and a RowId
	 */
	private RowEntry getEntryById(RowId id) {
		assert (id != null);
		
		String name = null;
		String logId = null;
		String cat = null;
		String scale = null;
		int lvl = 0;
		
		// extract information for any type
		name = getString(ATRI_NAME);
		logId = getString(ATRI_LOG);
		cat = getString(ATRI_CAT);
		lvl = getInt(ATRI_LVL);
		if ((name == null) || (logId == null) || (cat == null) || (lvl < 0)) {
			Printer.pfail("Getting a field for the RowEntry.");
		}
		
		// special treatment for scale
		scale = getString(ATRI_SCL);
		if ((scale == null)) {
				scale = ConfigWrap.NOT_AVAILABLE;
			}
		
		// determine the case and create RowEntry
		switch (id) {
		case INT : 
			return new IntRow(name, logId, lvl, cat, scale);
		case DOUBLE :
			return new DoubleRow(name, logId, lvl, cat, scale);
		case STRING : 
			Set<String> strings = getStrings();
			if (strings == null) {
				Printer.perror("Getting Strings of String Row.");
				return null;
			}
			return new StringRow(name, logId, lvl, cat, scale, strings);
		case LOCATION :
			return new IpLocationRow(name, logId, lvl, cat, scale);
		case STRINGMAP :
			HashMap<String, TreeSet<String>> maps = getMap();
			if (maps == null) {
				Printer.perror("Getting Map of StringMapRow.");
				return null;
			}
			return new StringMapRow(name, logId, lvl, cat, scale, maps);
		case DUMMY :
			return DummyRow.getInstance();
		default :
			Printer.perror("No case for given RowId: " + id.toString());
			return null;
		}
	}
	
	/**
	 * Helper class for getEntryById(..).
	 * Returns a TreeSet extracted from the JSONObject of this reader.
	 * 
	 * @return a TreeSet of Strings extracted from the JSONObject of this reader
	 */
	private TreeSet<String> getStrings() {
		JSONArray contents = getJSONArray(ATRI_STRINGS);
		if (contents == null) {
			Printer.pfail("Getting JSONArray for TreeSet");
			return null;
		}
		
		TreeSet<String> strings = new TreeSet<String>();
		for (int i = 0, j = contents.length(); i < j; i++) {
			String s = JSONReader.getStringFromArray(contents, i);
			if (s == null) {
				Printer.perror("Not a String in JSONArray at " + i);
				return null;
			}
			strings.add(s);
		}
		
		return strings;
	}
	
	/**
	 * Helper class for getEntryById(..).
	 * Returns a HashMap extracted from the JSONObject of this reader.
	 * 
	 * @return a HashMap extracted from the JSONObject of this reader
	 */
	private HashMap<String, TreeSet<String>> getMap() {
		HashMap<String, TreeSet<String>> maps = new HashMap<String, TreeSet<String>>();
		
		// get the array of 
		JSONArray contents = getJSONArray(ATRI_STRINGS);
		if (contents == null) {
			Printer.pfail("Getting JSONArray for HashMap.");
			return null;
		}
		
		// get a reader
		JSONReader curReader = new JSONReader(this.json);
		for (int i = 0, l = contents.length(); i < l; i++) {
			// get the JSONobject wrapping the key and values
			JSONObject obj = getJSONObjectFromArray(contents, i);
			if (obj == null) {
				Printer.perror("Not a JSONObject in JSONArray at " + i);
				return null;
			}
			
			// configure a reader for the next tasks
			curReader.setJson(obj);
			
			// get the key
			String key = curReader.getString(STRING_NAME);
			if (key == null) {
				Printer.pfail("Getting key for HashMap.");
				return null;
			}
			
			// get the values
			TreeSet<String> values = getStrings();
			if (values == null) {
				Printer.pfail("Getting values for HashMap.");
				return null;
			}
			
			maps.put(key, values);
		}
		
		return maps;
	}
	
	// -- STATIC GETTER -- STATIC GETTER -- STATIC GETTER --
	/**
	 * Returns a JSONObject from a given JSONArray at the
	 * given position  belonging to a given key (String) 
	 * from the JSONObject of this JSONReader.<br>
	 * If it not exists null is returned.
	 * 
	 * @param ary JSONArray from which a object shall be returned
	 * @return a JSONObject from position i from the array ary
	 */
	protected static JSONObject getJSONObjectFromArray(JSONArray ary, int i) {
		if (ary == null) {
			throw new IllegalArgumentException();
		} else if ((i < 0) || (i > ary.length())) {
			throw new IllegalArgumentException();
		}
		
		JSONObject obj = null;
		try {
			obj = ary.getJSONObject(i);
		} catch (JSONException e) {
			return null;
		}
		
		return obj;
	}

	/**
	 * Returns a String from a given JSONArray at the
	 * given position  belonging to a given key (String) 
	 * from the JSONObject of this JSONReader.<br>
	 * If it not exists null is returned.
	 * 
	 * @param ary JSONArray from which a object shall be returned
	 * @return a String from position i from the array ary
	 */
	protected static String getStringFromArray(JSONArray ary, int i) {
		if (ary == null) {
			throw new IllegalArgumentException();
		} else if ((i < 0) || (i > ary.length())) {
			throw new IllegalArgumentException();
		}
		
		String s = null;
		try {
			s = ary.getString(i);
		} catch (JSONException e) {
			return null;
		}
		
		return s;
	}

}
