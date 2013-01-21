package what.sp_config;

// java imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// org.json imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
/**
 * A ConfigWrap stores all needed information about a database
 * extracted from a .json file given to the static factory.<br>
 * 
 *  It consists of general information and all rows (RowEntry) in
 *  a a parse log file and the warehouse tables.
 * 
 * @author Jonathan, PSE Gruppe 14
 * @version 1.0
 * 
 * @see RowEntry
 * 
 */
public class ConfigWrap {
	
	/** Name of the data base for which this ConfigWrap is */
	private String dbName;
	
	/** Version of this ConfigWrap for a database. */
	private String version; // not necessary yet, 
						   // but i see potential and so it may be better to be in from the beginning
	
	/** The entries in order of the appearance in the log file. */
	private RowEntry[] entries; 
	
	/** The dimensions or rows in order of appearance. */
	//private ArrayList<DimRow> dimRows;
	
	private int sizeOfHistory = 10;

	/**
	 * Private constructor as the building is solved with a static factory.
	 */
	private ConfigWrap() {
		
	}
	
	// -- CONSTRUCTION -- CONSTRUCTION -- CONSTRUCTION -- CONSTRUCTION -- 
	/**
	 * Static factory for ConfiWraps.<br>
	 * Builds up ConfigWraps from a given configuration file. 
	 * 
	 * @param path the path of the configuration file 
	 * 		  from witch this ConfigWrap will be build up
	 * @return a ConfigWrap build up from the given file
	 * @throws IOException path wasn't correct
	 * @throws JSONException something went wrong while working with the JSON lib
	 */
	public static ConfigWrap buildConfig(String path) throws IOException, JSONException {
		if (path == null) {
			throw new IllegalArgumentException();
		}
		
		// gets the file
		File configFile = ConfigHelper.getConfigFile(path);
		
		// gets the content of the file
		String jsonContent = ConfigHelper.getJSONContent(configFile);
		
		// gets the json object (all content)
		JSONObject json = new JSONObject(jsonContent);
		
		// build the ConfigWrap
		ConfigWrap confi = buildConfig(json);
		if (confi == null) {
			System.out.println("Building ConfigWrap failed.");
			return null;
		}
		
		return confi;
	}
		
	/**
	 * Private helper method for buildConfig(path).<br>
	 * Builds the ConfigWrap from the given JSONOBject.
	 * 
	 * @param json JSONObject from which the information will be extracted
	 * @return the constructed ConfigWrap
	 * @throws JSONException something went wrong while working with the JSON lib
	 */
	private static ConfigWrap buildConfig(JSONObject json) throws JSONException {
		assert (json != null);
		
		ConfigWrap confi = new ConfigWrap();
		
		// get general information
		confi.dbName = json.getString(ConfigHelper.DB_NAME);
		confi.version = json.getString(ConfigHelper.VERSION);
		
		// get the rows
		JSONArray rows = json.getJSONArray(ConfigHelper.FIELDS);
		confi.entries = new RowEntry[rows.length()];
		if (!confi.buildConfigEntries(rows)) {
			System.out.println("Building RowEntries failed.");
			return null;
		}
		
		// compute the dimensions
		//confi.dimRows = new ArrayList<DimRow>();
		/*if (!confi.buildConfigDimRows()) {
			System.out.println("Building DimRow's failed.");
			return null;
		}*/
		
		return confi;
	}

	/**
	 * Private helper method for buildConfig(json).<br>
	 * Sets up the DimRows.
	 * 
	 * @return whether setting up the DimRows was successful
	 */
	/*private boolean buildConfigDimRows() {
		for (int i = 0, l = getSize(); i < l; i++) {
			DimRow cur = new DimRow();
			RowEntry re = entries[i];
			cur.add(re); 
			
			if (re.getLevel() > 0) {
				String cat = re.getCategory();
				i++;
				while (entries[i].getCategory().equals(cat)) {
					cur.add(entries[i]);
					i++;
				}
			}
			
			dimRows.add(cur);		
		}
		
		return true;
	}*/
	

	/**
	 * Private helper method for buildConfig(json).<br>
	 * Builds and stores the RowEntrys from a given JSONArray.  
	 * 
	 * @param jsRows JSONArray from which the entries will be extracted
	 * @throws JSONException something went wrong while working with the JSON lib
	 */
	private boolean buildConfigEntries(JSONArray jsRows) throws JSONException {
		assert (jsRows != null);
	
		for (int i = 0, l = jsRows.length(); i < l; i++) {
			// get RowEntry
			JSONObject jso = (JSONObject) jsRows.get(i); 
			RowEntry re = ConfigHelper.getEntry(jso);
			
			// failed?
			if (re == null) {
				System.out.println("Constructing RowEntry " + i + " failed.");
				return false;
			}
			
			// stores row
			this.entries[i] = re;
		}
		
		return true;
	}

	// -- SETTER -- SETTER -- SETTER -- SETTER -- SETTER --
	/*public boolean computeDimRows() {
		for (int i = 0, l = getSize(); i < l; i++) {
			if (!dimRows.get(i).setStrings()) {
				System.out.println("Computing strings for DimRow #" + i + " failed!");
				return false;
			}
		}
				
		return true;
	}
	*/
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the number of rows.
	 * 
	 * @return the number of rows
	 */
	public int getSize() {
		return entries.length;
	}
	
	/**
	 * Returns the RowEntry at the given position.
	 * 
	 * @param i position from which the RowEntry is required
	 * @return the RowEntry at the given position
	 */
	public RowEntry getEntryAt(int i) {
		if ((i < 0) || (i >= entries.length)) {
			throw new IllegalArgumentException();
		}
		
		return entries[i];
	}

	/**
	 * Returns the dimensions and rows.
	 * 
	 * @return the dimensions and rows
	 */
	/*
	public ArrayList<DimRow> getDims() {
		return dimRows;
	}
	*/
	/**
	 * Returns the name of the database for which this configuration is.
	 * 
	 * @return the name of the database for which this configuration is
	 */
	public String getNameOfDB() {
		return dbName;
	}

	/**
	 * Returns the version of the configuration.
	 * 
	 * @return the version of the configuration
	 */
	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		String s = "ConfigWrap [dbName= " + dbName + ", version= " + version
				+ ", entries= [";
		for (int i = 0, j = getSize(); i < j; i++) {
			s += getEntryAt(i).toString(); 
		}
		
		s += "] ]";
		return s;
	}
	
	

	
}
