package what.sp_config;

// intern imports
import what.sp_data_access.MySQLAdapter;
import what.sp_parser.DataEntry;

/**
 * <p>A RowEntry describes a element in the log file and
 * a row in the warehouse.<br>
 * Therefore it stores some information like type, name, logId
 * and whether it is in a dimension or not.</p>
 * 
 * This is the abstract parent class for specific RowEntries.
 * 
 * @author Jonathan, Alex, PSE Gruppe 14
 * @see ConfigWrap
 * @see ParserMediator
 */
public abstract class RowEntry {
	
	// -- STATIC STRINGS -- STATIC STRINGS -- STATIC STRINGS --
	/** 
	 * Static Strings for a specific type of a field/row in the .JSON configuration file. 
	 * @see RowEntry
	 * @see RowId
	 * @see IntRow
	 */
	protected static final String TYPE_INT = "INT";
	
	/** 
	 * Static Strings for a specific type of a field/row in the .JSON configuration file.
	 * @see RowEntry
	 * @see RowId
	 * @see DoubleRow
	 */
	protected static final String TYPE_DOUBLE = "DOUBLE";
	
	/** 
	 * Static Strings for a specific type of a field/row in the .JSON configuration file.
	 * @see RowEntry
	 * @see RowId
	 * @see StringRow
	 */
	protected static final String TYPE_STRING = "STRING";
	
	/** 
	 * Static Strings for a specific type of a field/row in the .JSON configuration file. 
	 * @see RowEntry
	 * @see RowId
	 * @see IpLocationRow
	 */
	protected static final String TYPE_LOCATION = "IP";
	
	/** Static Strings for a specific type of a field/row in the .JSON configuration file. 
	 * @see RowEntry
	 * @see RowId
	 * @see StringMapRow
	 */
	protected static final String TYPE_STRINGMAP = "STRINGMAP";
	
	/** Static Strings for a specific type of a field/row in the .JSON configuration file. 
	 * @see RowEntry
	 * @see RowId
	 * @see DummyRow
	 */
	protected static final String TYPE_DUMMY = "NULL";	
	
	/** Static Strings for time.  */
	protected static final String TIME = "time";	
	
	// -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES --
	/**
	 * The RowId of this RowEntry to determine the type without using instanceOf().
	 * @see RowId
	 */
	private final RowId id;
	
	/**
	 * The name of this RowEntry.<br>
	 * Examples are: "location", "elapsed time", "time", "security level", ...	
	 */
	private final String name;
	
	/**
	 * The logId of this RowEntry, like "yy" or "yyyy" for year.
	 * @see ParserMediator
	 */
	private final String logId;
	
	/**
	 * The level in a dimension for this RowEntry.<br>
	 * Should be zero if not a dimension. 1 stands for top level.
	 */
	private final int level;
	
	/**
	 * The category or dimension name of this RowEntry.<br>
	 *  Should be "none" if it's not part of a dimension.
	 */
	private final String category;
	
	/**
	 * The scale stands for the String describing the scale for this RowEntry.<br>
	 * Examples are: "ms", "rows", ...
	 */
	private final String scale;
	
	/** Column name of this row entry, which is the name in the warehouse. */
	private final String columnName;
	
	// -- CONSTRUCTOR -- CONSTRUCTOR -- CONSTRUCTOR -- CONSTRUCTOR --
	/**
	 * Constructor for a RowEntry.
	 * 
	 * @param name the name
	 * @param logId the logId
	 * @param lvl the level
	 * @param category the category
	 * @param scale the scale String
	 * @param id the RowId
	 */
	protected RowEntry(String name, String logId, int lvl, String category, String scale, RowId id) {
		assert (name != null);
		assert (logId != null);
		assert (category != null);
		assert (scale != null);
		assert (lvl < 0);
		
		this.name = name;
		this.logId = logId;
		this.level = lvl;
		this.category = category;
		this.scale = scale;
		
		this.id = id;
		
		columnName = generateColumnName(name);
	}
	
	/**
	 * Generates the column name of this RowEntry from
	 * a given name.
	 * 
	 * @param name to generate column name from
	 * @return the ColumnName generated from a given name
	 */
	private static String generateColumnName(String name) {
		assert (name != null);
				
		return MySQLAdapter.ROW_TABLE + ConfigWrap.getStringWithoutSpace(name);
	}

	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the logId.
	 * 
	 * @return the logId
	 */
	public String getLogId() {
		return logId;
	}

	/**
	 * Returns the level.
	 * 
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Returns the category.
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the scale String.
	 * 
	 * @return the scale
	 */
	public String getScale() {
		if (this.scale == ConfigWrap.NOT_AVAILABLE) {
			return getName();
		}
		
		return scale;
	}

	/**
	 * Returns the RowId.
	 * 
	 * @return the RowId
	 */
	public RowId getId() {
		return id;
	} 
	
	/**
	 * Returns the table type of this RowEntry.
	 * 
	 * @return  the table type of this RowEntry
	 */
	public abstract String getTableType();
	
	/**
	 * Returns the column name in the warehouse for this row.
	 * 
	 * @return the columnName in the warehouse for this row
	 */
	public String getColumnName() {
		return columnName;
	}
	
	// -- PARSER STRATEGIE -- PARSER STRATEGIE -- PARSER STRATEGIE --
	/**
	 * Splits the given String and stores it at the given location in the given DataEntry.
	 * Returns whether it was successful.<br>
	 * 
	 * This method represents a strategie, because every RowEntry type, parses differently.
	 * 
	 * @param de DataEntry where the result is stored
	 * @param string String from which shall
	 * @param location int location in the dataEntry where the split result will be stored
	 * @return whether it was successful
	 */
	public abstract boolean split(DataEntry de, String string, int location);
	
	// -- OVERRIDES -- OVERRIDES -- OVERRIDES -- OVERRIDES --
	@Override
	public String toString() {
		return "RowEntry [id=" + id + ", name=" + name + ", logId=" + logId
				+ ", level=" + level + ", category=" + category + ", scale="
				+ scale + "]\n";
	}

	

	
	
}
