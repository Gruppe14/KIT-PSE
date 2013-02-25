package what;

// JSON imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// intern imports
import what.sp_config.ConfigWrap;

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
	
	// -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES --
	/** JSONObject on which this JSONReader will work. */
	private JSONObject json;

	// -- CONSTRUCTOR --CONSTRUCTOR --CONSTRUCTOR --CONSTRUCTOR --CONSTRUCTOR --
	/**
	 * Private constructor for a JSONReader.
	 * 
	 * @param json JSONObject on which this JSONReader will work
	 */
	public JSONReader(JSONObject json) {
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
	public void setJson(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException();
		}
		
		this.json = json;
	}

	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the JSONObject of this reader.
	 * 
	 * @return the JSONObject of this reader
	 */
	public JSONObject getJson() {
		return json;
	}
	
	/**
	 * Returns a String belonging to a given key (String) 
	 * from the JSONObject of this JSONReader.
	 * 
	 * @param key field in the JSONObject
	 * @return a String belonging to a given key
	 */
	public String getString(String key) {
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
	public int getInt(String key) {
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
	 * Returns a JSONObject belonging to a given key (String) 
	 * from the JSONObject of this JSONReader.
	 * 
	 * @param key field in the JSONObject
	 * @return a JSONObject belonging to a given key
	 */
	public JSONObject getJSONObject(String key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		
		JSONObject requ = null;
		try {
			if (json.has(key)) {
				requ = json.getJSONObject(key);
			}
		} catch (JSONException e) {
			Printer.perror("Extracting JSONObject from JSONObject for key: " + key);
			return null;
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
	public JSONArray getJSONArray(String key) {
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
	 * Returns a Object belonging to a given key (String) 
	 * from the JSONObject of this JSONReader.
	 * 
	 * @param key field in the JSONObject
	 * @return a Object belonging to a given key
	 */
	public Object getObject(String key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		
		Object requ = null;
		try {
			if (json.has(key)) {
				requ = json.get(key);
			}
		} catch (JSONException e) {
			Printer.perror("Extracting String from JSONObject for key: " + key);
			return null;
		}
		
		return requ;
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
	public static JSONObject getJSONObjectFromArray(JSONArray ary, int i) {
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
	public static String getStringFromArray(JSONArray ary, int i) {
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

	/**
	 * Returns a JSONObject for the given String.
	 * 
	 * @param s String which should describe a JSONObject
	 * @return a JSONObject for the given String
	 */
	public static JSONObject getJSONObjectForString(String s) {
		// gets the json object (all content)
		JSONObject json = null;
		try {
			json = new JSONObject(s);
		} catch (JSONException e) {
			Printer.perror("File content not a JSON Object!");
			return null;
		}
		
		return json;
	}
}
