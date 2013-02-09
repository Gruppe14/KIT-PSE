package what.sp_config;

// java imports
import java.io.File;
import java.util.ArrayList;

// org.json imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// intern imports
import what.FileHelper;
import what.Printer;
 
/**
 * A ConfigWrap stores all needed information about a database
 * extracted from a .json file given to the static factory.<br>
 * 
 *  It consists of general information and all rows (RowEntry) in
 *  a parse log file and the warehouse tables.
 * 
 * @author Jonathan, PSE Gruppe 14
 * @version 1.0
 * 
 * @see RowEntry
 * @see DimRow
 */
public class ConfigWrap {
	
	/** This constant Strings stand for a attribute which is not available. */
	protected static String NOT_AVAILABLE = "N/A";
	
	/** Name of the data base for which this ConfigWrap is */
	private String dbName;
	
	/** Version of this ConfigWrap for a database. */
	private String version; // not needed yet 
	
	/** The entries in order of the appearance in the log file. */
	private RowEntry[] entries; 
	
	/** The dimensions or rows in order of appearance. */
	private ArrayList<DimRow> dimRows;
		
	// -- CONSTRUCTION -- CONSTRUCTION -- CONSTRUCTION -- CONSTRUCTION --
	/**
	 * Private constructor as the building is solved with a static factory.
	 */
	private ConfigWrap() {
		// private constructor; you only get a ConfigWrap via static factory
	}
	
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
		File configFile = FileHelper.getFileForExtension(path, FileHelper.JSON);
		if (configFile == null) {
			Printer.pfail("Get a .json from the given path: " + path);
			return null;
		}
		
		// gets the content of the file
		String jsonContent = FileHelper.getStringContent(configFile);
		if (jsonContent == null) {
			Printer.pfail("Get the content from the given file of the path: " + path);
			return null;
		}
		
		// gets the json object (all content)
		JSONObject json = null;
		try {
			json = new JSONObject(jsonContent);
		} catch (JSONException e) {
			Printer.perror("File content not a JSON Object!");
			return null;
		}
		
		// build the ConfigWrap
		ConfigWrap confi = buildConfig(json);
		if (confi == null) {
			Printer.pfail("Building ConfigWrap failed.");
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
		
		// ConfigWrap which will be filled continuously
		ConfigWrap confi = new ConfigWrap();
		
		// get a ConfigReader to do all the work
		JSONReader reader = new JSONReader(json);
		
		// get general information
		if (!buildGeneralAttributes(confi, reader)) {
			Printer.pfail("Getting genral information for config.");
			return null;
		}
				
		
		// get the rows
		if (!buildRows(confi, reader)) {
			Printer.pfail("Building RowEntry for config.");
			return null;
		} 
		/* WARNING: JSONReader >> reader << changed JSONObject on which it works!!
		 * If you need something more from him, use him before!
		 */
		
		// compute the dimensions
		confi.dimRows = new ArrayList<DimRow>();
		if (!confi.buildConfigDimRows()) {
			Printer.pfail("Building DimRow for config.");
			return null;
		}
		
		return confi;
	}

	/**
	 * Private helper method which extracts general information
	 * for the given ConfigWrap from the given JSONReader.
	 * 
	 * @param confi for which the information shall be extracted
	 * @param reader from which the information get extracted
	 * @return whether it was successful
	 */
	private static boolean buildGeneralAttributes(ConfigWrap confi, JSONReader reader) {
		assert (confi != null);
		assert (reader != null);

		// get the name
		String name = reader.getString(JSONReader.DB_NAME);
		if (name == null) {
			Printer.pproblem("No DB name found in config file.");
			confi.dbName = NOT_AVAILABLE;
		} else {
			confi.dbName = name;
		}
		// get the version
		String vers = reader.getString(JSONReader.VERSION);
		confi.version = (vers != null) ? vers : NOT_AVAILABLE; 
		
		return true;
	}
	
	/**
	 * Private helper method which extracts the RowEntries
	 * for the given ConfigWrap from the given JSONReader.
	 * 
	 * @param confi for which the RowEntries shall be extracted
	 * @param reader from which the c get extracted
	 * @return whether it was successful
	 */
	private static boolean buildRows(ConfigWrap confi, JSONReader reader) {
		assert (confi != null);
		assert (reader != null);
		
		// get the array of JSONObjects (containing rows) from the reader
		JSONArray jsRows = reader.getJSONArray(JSONReader.FIELDS);
		if (jsRows == null) {
			Printer.pfail("Getting array of rows from the JSONObject.");
			return false;
		}
		
		// get the RowEntries
		int length = jsRows.length();
		confi.entries = new RowEntry[length];
		for (int i = 0; i < length; i++) {
			// get JSONObject for row i
			JSONObject jso = JSONReader.getJSONObjectFromArray(jsRows, i);
			if (jso == null) {
				Printer.perror("No JSONObject found for RowEntry at position " + i);
				return false;			
			}
			
			// get RowEntry
			reader.setJson(jso); // WARNING: changes the reader!
			RowEntry re = reader.getRowEntry();
			if (re == null) {
				Printer.pfail("Constructing RowEntry " + i);
				return false;
			}
			
			// store row
			confi.entries[i] = re;
		}		
		
		
		return true;
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
	
	/**
	 * Return the table key (in the warehouse) of a dimension
	 * for a given String, which refers to a dimension name.
	 * If non is found or it refers to a non-dimension DimRow
	 * null is returned.
	 * 
	 * @param s String referring to a DimRow
	 * @return table
	 */
	public String getTableKeyFor(String s) {
		for (DimRow d : dimRows) {
			if (d.getName().equalsIgnoreCase(s)) {
				if (d.isDimension()) {
					return d.getTableKey();
				} else {
					return null;
				}
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
		return JSONReader.FACT_TABLE + dbName;
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
	
	// -- OVERRIDES -- OVERRIDES -- OVERRIDES -- OVERRIDES --	
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


	// -- CHECKS -- CHECKS -- CHECKS -- CHECKS -- CHECKS -- CHECKS --
	/**
	 * Returns whether the given String equals a category of any dimension.
	 * 
	 * @param x String to compare with category names
	 * @return whether the given String equals a category of any dimension
	 */
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
