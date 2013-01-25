package what.sp_config;

// java imports
import java.io.File;
import java.util.ArrayList;

// org.json imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import what.JSON_Helper;
 
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
	private ArrayList<DimRow> dimRows;
	
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
	 */
	public static ConfigWrap buildConfig(String path) {
		if (path == null) {
			throw new IllegalArgumentException();
		}
		
		// gets the file
		File configFile = JSON_Helper.getJSONFile(path);
		
		// gets the content of the file
		String jsonContent = JSON_Helper.getJSONContent(configFile);
		
		// gets the json object (all content)
		JSONObject json = null;
		try {
			json = new JSONObject(jsonContent);
		} catch (JSONException e) {
			System.out.println("ERROR: File content not a JSON Object!");
			e.printStackTrace();
			return null;
		}
		
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
	 */
	private static ConfigWrap buildConfig(JSONObject json) {
		assert (json != null);
		
		ConfigWrap confi = new ConfigWrap();
		
		// get general information
		try {
			confi.dbName = json.getString(ConfigHelper.DB_NAME);
		} catch (JSONException e) {
			System.out.println("ERROR: Getting " + ConfigHelper.DB_NAME + " failed!");
			e.printStackTrace();
			return null;
		}
		try {
			confi.version = json.getString(ConfigHelper.VERSION);
		} catch (JSONException e) {
			System.out.println("ERROR: Getting " + ConfigHelper.VERSION + " failed!");
			e.printStackTrace();
			return null;
		}
		
		// get the rows
		JSONArray rows;
		try {
			rows = json.getJSONArray(ConfigHelper.FIELDS);
		} catch (JSONException e) {
			System.out.println("ERROR: Getting " + ConfigHelper.FIELDS + " failed!");
			e.printStackTrace();
			return null;
		}
		
		confi.entries = new RowEntry[rows.length()];
		if (!confi.buildConfigEntries(rows)) {
			System.out.println("Building RowEntries failed.");
			return null;
		}
		
		// compute the dimensions
		confi.dimRows = new ArrayList<DimRow>();
		if (!confi.buildConfigDimRows()) {
			System.out.println("Building DimRow's failed.");
			return null;
		}
		
		return confi;
	}

	/**
	 * Private helper method for buildConfig(json).<br>
	 * Sets up the DimRows.
	 * 
	 * @return whether setting up the DimRows was successful
	 */
	private boolean buildConfigDimRows() {
		for (int i = 0, l = getNumberOfRows(); i < l; i++) {
			DimRow cur = new DimRow();
			RowEntry re = entries[i];
			
			if (re.getId().equals(RowId.LOCATION)) {
				cur = DimRow.getLocationDim();
			} else if (re.getId().equals(RowId.DUMMY)) {
				continue;
			} else {
				cur.add(re); 
				
				if (re.getLevel() > 0) {
					String cat = re.getCategory();
					i++;
					
					while ((i < l) && (entries[i].getCategory().equalsIgnoreCase(cat))) {
						cur.add(entries[i]);
						i++;
					}
					
					i--;
				}
				
			}
			
			
			dimRows.add(cur);		
		}
		
		return true;
	}
	

	/**
	 * Private helper method for buildConfig(json).<br>
	 * Builds and stores the RowEntrys from a given JSONArray.  
	 * 
	 * @param jsRows JSONArray from which the entries will be extracted
	 */
	private boolean buildConfigEntries(JSONArray jsRows) {
		assert (jsRows != null);
	
		for (int i = 0, l = jsRows.length(); i < l; i++) {
			// get RowEntry
			JSONObject jso;
			try {
				jso = (JSONObject) jsRows.get(i);
			} catch (JSONException e) {
				System.out.println("ERROR: Getting entry at " + i + " failed!");
				e.printStackTrace();
				return false;
			} 
			
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

	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the number of rows.
	 * 
	 * @return the number of rows
	 */
	public int getNumberOfRows() {
		return entries.length;
	}
	
	/**
	 * Returns the number of dimensions and rows.
	 * 
	 * @return the number of dimensions and rows
	 */
	public int getNumberOfDims() {
		return dimRows.size();
	}
	
	/**
	 * Return the # of dimensions, and also dimension tables, of this config
	 * without counting the columns of the fact table.
	 * @return # of dimensions
	 */
	public int getNumberOfDimsWitoutRows() {
		int sum = 0;
		for (DimRow d : dimRows) {
			if (d.isDimension()) {
				sum++;
			}
		}
		
		return sum;
	}
	
	/**
	 * Returns the RowEntry at the given position.
	 * 
	 * @param i position from which the RowEntry is required
	 * @return the RowEntry at the given position
	 */
	public RowEntry getEntryAt(int i) {
		if ((i < 0) || (i >= getNumberOfRows())) {
			throw new IllegalArgumentException();
		}
		
		return entries[i];
	}
	
	/**
	 * Returns the RowEntry at the given position.
	 * 
	 * @param i position from which the RowEntry is required
	 * @return the RowEntry at the given position
	 */
	public DimRow getDimRowAt(int i) {
		if ((i < 0) || (i >= dimRows.size())) {
			throw new IllegalArgumentException();
		}
		
		return dimRows.get(i);
	}

	/**
	 * Returns the dimensions and rows.
	 * 
	 * @return the dimensions and rows
	 */
	public ArrayList<DimRow> getDims() {
		return dimRows;
	}
	
	public String getTableKeyFor(String s) {
		for (DimRow d : dimRows) {
			if (d.getName().equalsIgnoreCase(s)) {
				return d.getTableKey();
			}
		}
		
		return null;
	}
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

	/**
	 * Return the name of the fact table in the warehouse of this config.
	 * @return
	 */
	public String getFactTableName() {
		return ConfigHelper.FACT_TABLE + dbName;
	}
	
	
	/**
	 * Returns the row name for the highest level for a dimension
	 * containing x as category or row
	 * @param x String for which the dimension is searched
	 * @return row name for the highest level for a dimension
	 */
	public String getHighestRowFor(String x) {
		if (x == null) {
			throw new IllegalArgumentException();
		}
		
		for (DimRow d : dimRows) {
			if (d.getName().equalsIgnoreCase(x)) {
				return d.getRowNameOfLevel(0);
			}
		}
		return null;
	}
	
	/**
	 * Returns the size of the history.
	 * 
	 * @return the size of the history
	 */
	public int getSizeOfHistory() {
		return sizeOfHistory;
	}
	
	@Override
	public String toString() {
		String s = "ConfigWrap [dbName= " + dbName + ", version= " + version
				+ ", entries= \n[";
		for (int i = 0, j = getNumberOfRows(); i < j; i++) {
			s += getEntryAt(i).toString(); 
		}
		
		s += "]\n dimRows = [\n"; 
		
		for (int i = 0, k = dimRows.size(); i < k; i++) {
			s += getDimRowAt(i).toString() + "\n";
		}
		
		s +=  "] ]";
		
		return s;
	}


	// -- CHECKER -- CHECKER -- CHECKER -- CHECKER -- CHECKER --
	public boolean isCategorie(String x) {
		if (x == null) {
			throw new IllegalArgumentException();
		}
		for (DimRow d : dimRows) {
			if (d.getName().equalsIgnoreCase(x)) {
				return true;
			}
		}
		return false;
	}

	
	
	
	
}
