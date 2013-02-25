package what.sp_config;

// java imports
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

// org.json imports
import org.json.JSONArray;
import org.json.JSONObject;

// intern imports
import what.FileHelper;
import what.JSONReader;
import what.Printer;
import what.sp_data_access.MySQLAdapter;
 
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

	// -- STATIC STRINGS -- STATIC STRINGS -- STATIC STRINGS --
	/** This constant Strings stand for a attribute which is not available. */
	public static String NOT_AVAILABLE = "N/A";
	
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
	
	// -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES --	
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
		JSONObject json = JSONReader.getJSONObjectForString(jsonContent);
		if (json == null) {
			Printer.pfail("Getting JSONObject from file of path: " + path);
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
		String name = reader.getString(DB_NAME);
		if (name == null) {
			Printer.pproblem("No DB name found in config file.");
			confi.dbName = NOT_AVAILABLE;
		} else {
			confi.dbName = name;
		}
		// get the version
		String vers = reader.getString(VERSION);
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
		JSONArray jsRows = reader.getJSONArray(FIELDS);
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
			RowEntry re = getRowEntry(reader);
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
	 * Returns a RowEntry from the JSONObject of the given reader.
	 * It also extracts all information for it.
	 * 
	 * @param reader JSONReader from which the RowEntry information are extracted
	 * @return a RowEntry from a given JSONObject
	 */
	protected static RowEntry getRowEntry(JSONReader reader)  {
		// extract type
		String type = reader.getString(ATRI_TYPE);
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
		RowEntry re = getEntryById(id, reader);
		if (re == null) {
			Printer.pfail("Getting RowEntry.");
			return null;
		}
		
		return re;		
	}
	
	/**
	 * Helper class for getEntry(...).
	 * Returns a RowEntry in dependency to a JSONObject and a RowId.
	 * 
	 * @param id RowId of the RowEntry required
	 * @return a RowEntry in dependency to a JSONObject and a RowId
	 */
	private static RowEntry getEntryById(RowId id, JSONReader reader) {
		assert (id != null);
		
		String name = null;
		String logId = null;
		String cat = null;
		String scale = null;
		int lvl = 0;
		
		// extract information for any type
		name =  reader.getString(ATRI_NAME);
		logId =  reader.getString(ATRI_LOG);
		cat =  reader.getString(ATRI_CAT);
		lvl =  reader.getInt(ATRI_LVL);
		if ((name == null) || (logId == null) || (cat == null) || (lvl < 0)) {
			Printer.pfail("Getting a field for the RowEntry.");
		}
		
		// special treatment for scale
		scale =  reader.getString(ATRI_SCL);
		if ((scale == null)) {
				scale = NOT_AVAILABLE;
			}
		
		// determine the case and create RowEntry
		switch (id) {
		case INT : 
			return new IntRow(name, logId, lvl, cat, scale);
		case DOUBLE :
			return new DoubleRow(name, logId, lvl, cat, scale);
		case STRING : 
			Set<String> strings = getStrings(reader);
			if (strings == null) {
				Printer.perror("Getting Strings of String Row.");
				return null;
			}
			return new StringRow(name, logId, lvl, cat, scale, strings);
		case LOCATION :
			return new IpLocationRow(name, logId, lvl, cat, scale);
		case STRINGMAP :
			HashMap<String, TreeSet<String>> maps = getMap(reader);
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
	private static TreeSet<String> getStrings(JSONReader reader) {
		JSONArray contents = reader.getJSONArray(ATRI_STRINGS);
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
	private static HashMap<String, TreeSet<String>> getMap(JSONReader reader) {
		HashMap<String, TreeSet<String>> maps = new HashMap<String, TreeSet<String>>();
		
		// get the array of 
		JSONArray contents = reader.getJSONArray(ATRI_STRINGS);
		if (contents == null) {
			Printer.pfail("Getting JSONArray for HashMap.");
			return null;
		}
		
		// get a reader
		JSONReader curReader = new JSONReader(reader.getJson());
		for (int i = 0, l = contents.length(); i < l; i++) {
			// get the JSONobject wrapping the key and values
			JSONObject obj = JSONReader.getJSONObjectFromArray(contents, i);
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
			TreeSet<String> values = getStrings(reader);
			if (values == null) {
				Printer.pfail("Getting values for HashMap.");
				return null;
			}
			
			maps.put(key, values);
		}
		
		return maps;
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
	 * Return the # of dimensions, and also dimension tables, of this configuration
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
	 * Return the name of the fact table in the warehouse of this configuration.
	 * @return
	 */
	public String getFactTableName() {
		return MySQLAdapter.FACT_TABLE + dbName;
	}
	
	// -- SEARCH GETTER -- SEARCH GETTER -- SEARCH GETTER -- SEARCH GETTER -- 
	/**
	 * Returns whether the given String is the name of a dimension.
	 * 
	 * @param name String for which is checked if a dimension exists
	 * @return whether the given String is the name of a dimension
	 */
	public boolean dimensionExistsFor(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		
		return (getDimRowFor(name) != null); 
	}
	
	/**
	 * Returns the DimRow with the name of the given String.
	 * 
	 * @param s name (category) of the DimRow which is searched
	 * @return the DimRow with the name of the given String
	 */
	public DimRow getDimRowFor(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		
		for (DimRow d : dimRows) {
			if (d.getName().equalsIgnoreCase(s)) {
				return d;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the RowEntry with the name of the given String.
	 * 
	 * @param s name of the RowEntry which is searched
	 * @return the RowEntry with the name of the given String
	 */
	public RowEntry getRowEntryFor(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		
		RowEntry row = null;
		for (DimRow d : dimRows) {
			row = d.getRowEntryFor(s);
			if (row != null) {
				return row;
			}
		}
		
		return null;
	}
	
	/**
	 * Return the table key (in the warehouse) of a dimension
	 * for a given String, which refers to a dimension name.
	 * If non is found or it refers to a non-dimension DimRow
	 * null is returned.
	 * 
	 * @param s String referring to a DimRow
	 * @return table key for the given String
	 */
	public String getTableKeyFor(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		
		DimRow d = getDimRowFor(s);
		if (d != null) {
			if (d.isDimension()) {
				return d.getTableKey();
			} else {
				return null;
			}
		}
			
		return null;
	}
	
	/**
	 * Return the table name (in the warehouse) of a dimension
	 * for a given String, which refers to a dimension name.
	 * If non is found or it refers to a non-dimension DimRow
	 * null is returned.
	 * 
	 * @param s String referring to a DimRow
	 * @return table name for the given String
	 */
	public String getTableNameFor(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		
		DimRow d = getDimRowFor(s);
		if (d != null) {
			if (d.isDimension()) {
				return d.getDimTableName();
			} else {
				return null;
			}
		}
			
		return null;
	}
	
	/**
	 * Returns the row name for the highest level for a dimension
	 * containing x as category or row
	 * @param s String for which the dimension is searched
	 * @return row name for the highest level for a dimension
	 */
	public String getHighestRowFor(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		
		DimRow d = getDimRowFor(s);
		if (d != null) {
			if (d.isDimension()) {
				return d.getRowNameOfLevel(0);
			} else {
				return null;
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
