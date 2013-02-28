package what.sp_config;

//java imports
import java.util.ArrayList;
import java.util.TreeSet;

// intern imports
import what.sp_data_access.MySQLAdapter;

/**
 * This class represents a dimension in the warehouse, 
 * wrapping all the rows (levels of the dimension), or 
 * just a row for itself, standing for a measure in the warehouse.<br>
 * 
 * Provides methods to check, whether it is a dimension, a dimension
 * containing strings, methods to get the size, name, names for SQL
 * specific things, ...
 * 
 * @author Jonathan, PSE
 * @see ConfigWrap
 */
public class DimRow {

	/** This are the actual content of this DimRow, the rows. */
	private ArrayList<RowEntry> rows = new ArrayList<RowEntry>();
		
	/** The trees with the content in the warehouse for this dimension. */
 	private TreeSet<DimKnot> trees = new TreeSet<DimKnot>(); 
	
	// -- LOCATION DIM -- LOCATION DIM -- LOCATION DIM -- 
	// static Strings for the standard names
	//private static final String CONTINENT = "continent";
	/** Constant for the location singleton. */
 	private static final String CITY = "city";
 	/** Constant for the location singleton. */
 	private static final String COUNTRY = "country";
 	/** Constant for the location singleton. */
 	private static final String LOCATION = "location";
	
	// static initialization
 	/** Singleton constant location dimension. */
	private static final DimRow LOCATION_DIM_INSTANCE; 
	static {
		LOCATION_DIM_INSTANCE = new DimRow();
		//LOCATION_DIM_INSTANCE.add(new StringRow(CONTINENT, "", 1, LOCATION, CONTINENT, null));
		LOCATION_DIM_INSTANCE.add(new StringRow(COUNTRY, "", 1, LOCATION, COUNTRY));
		LOCATION_DIM_INSTANCE.add(new StringRow(CITY, "", 2, LOCATION, CITY));
	}
		
	// -- SETTER -- SETTER -- SETTER -- SETTER -- SETTER --
	/**
	 * Adds a RowEntry to this DimRow.
	 * 
	 * @param rowEntry RowEntry to be added
	 * @return whether adding was successful
	 */
 	protected boolean add(RowEntry rowEntry) {
		if (rowEntry == null) {
			throw new IllegalArgumentException();
		}
		
		return rows.add(rowEntry);		
	}
	
	/**
	 * Adds a DimKnot to the collection of trees.
	 * 
	 * @param dk DimKnot to be added
	 * @return whether adding was successful
	 */
	public boolean addTree(DimKnot dk) {
		if (dk == null) {
			throw new IllegalArgumentException();
		}
		
		return trees.add(dk);		
	}
	
	// -- CHECKER -- CHECKER -- CHECKER -- CHECKER -- CHECKER --
	/**
	 * Returns whether this is a dimension (and not just a row (measure)).
	 * 
	 * @return whether this is a dimension
	 */
	public boolean isDimension() {
		if (!isNotEmpty()) {
			return false;
		}
		
		RowEntry re = rows.get(0);
		
		return ((re.getLevel() > 0) || (re.getId().equals(RowId.STRING)) || (re.getId().equals(RowId.STRINGMAP)));
	}
	
	/**
	 * Returns whether this is a String dimension.<br>
	 * This means, the dimension contains Strings.
	 *
	 * @return  whether this is a String dimension
	 * @see StringRow
	 * @see StringMapRow
	 */
	public boolean isStringDim() {
		if (!isNotEmpty()) {
			return false;
		}
		
		// we assume it's just strings in a dimension or all not strings
		RowId cur = rows.get(0).getId();
		return ((cur == RowId.STRING) || (cur == RowId.STRINGMAP));
	}
	
	/**
	 * Private helper class to check whether this DimRow is empty
	 * to avoid null pointer exceptions.
	 * 
	 * @return whether this DimRow is empty
	 */
	private boolean isNotEmpty() {
		return (getSize() > 0);
	}
		
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the size of this DimRow, which is the number of 
	 * wrapped rows.
	 * 
	 * @return  the size of this DimRow
	 */
	public int getSize() {
		return rows.size();
	}
	
	/**
	 * Returns the name of this DimRow.
	 * 
	 * @return the name of this DimRow
	 */
	public String getName() {
		if (!isNotEmpty()) {
			return null;
		}
		
		return rows.get(0).getCategory();
	}
	
	/**
	 * Returns the name of the row at position i.
	 * 
	 * @param i the position of the row for which the name is requested
	 * @return the name of the row at position i
	 */	
	public String getNameOfLevel(int i) {
		if ((i < 0) || (i >= getSize())) {
			throw new IllegalArgumentException();
		}
		
		return rows.get(i).getName();
	}
	
	/**
	 * Returns the RowEntry at the position i.
	 * 
	 * @param i the position of the requested row
	 * @return the RowEntry at the position i
	 */
	public RowEntry getRowAt(int i) {
		if ((i < 0) || (i >= getSize())) {
			throw new IllegalArgumentException();
		}
		
		return rows.get(i);
	}
	
	/**
	 * Returns the RowId of the row at position i.
	 * 
	 * @param i the position of the row for which RowId is requested
	 * @return the RowId of the row at position i
	 */
	public RowId getRowIdAt(int i) {
		if ((i < 0) || (i >= getSize())) {
			throw new IllegalArgumentException();
		}
		
		return rows.get(i).getId();
	}
	
	/**
	 * Returns the row of this dimension with the given name.
	 * 
	 * @param name String as the name for the row which is requested
	 * @return the row of this dimension with the given name
	 */
	public RowEntry getRowEntryFor(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		
		for (RowEntry r : rows) {
			if (r.getName().equalsIgnoreCase(name)) {
				return r;
			}
		}
		return null;
	}
	
	// >> GETTER for the static DimRow location
	/**
	 * Returns a DimRow for location.
	 * 
	 * @return a DimRow for location
	 */
	public static DimRow getLocationDim() {
		return LOCATION_DIM_INSTANCE;
	}
	
	// >> GETTER for the WEB PAGE
	/**
	 * Returns the scale of this DimRow.
	 * 
	 * @return the scale of this DimRow
	 */
	public String getScale() {
		if (isDimension()) {
			return getName();
		}
		if (isNotEmpty()) {
			rows.get(0).getScale();
		}
		
		return ConfigWrap.NOT_AVAILABLE;
	}

	/**
	 * Returns the tree of Strings of this DimRow if it exists.
	 * 
	 * @return the tree of Strings of this DimRow
	 */
	public TreeSet<DimKnot> getStrings() {
		if (!isStringDim()) {
			return null;
		}
		
		return trees;
	}
		
	// >> GETTER for the SQL + WAREHOUSE
	/**
	 * Returns the table name in the warehouse of this dimension,
	 * or null if it is a just a measure.
	 * 
	 * @return the table name in the warehouse of this dimension
	 */
	public String getDimTableName() {
		if (isDimension()) {
			return MySQLAdapter.DIM_TABLE + ConfigWrap.getStringWithoutSpace(getName());
		}
		return null;
	}
	
	/**
	 * Returns the table key in the warehouse of this dimension,
	 * or null if it is a just a measure.
	 * 
	 * @return the table key in the warehouse of this dimension
	 */
	public String getTableKey() {
		if (isDimension()) {
			return ConfigWrap.getStringWithoutSpace(getName()) + MySQLAdapter.KEY_TABLE;
		}
		return null;
	}
	
	/**
	 * Returns the type of the the row at the given position i in the warehouse.
	 * E.g. "INT(3)", "VARCHAR(40)", ... 
	 * 
	 * @param i position of the Row from which the table type is requested
	 * @return the table type of the row at position i
	 */
	public String getTableTypeAt(int i) {
		if ((i < 0) || (i >= getSize())) {
			throw new IllegalArgumentException();
		}
			
		return rows.get(i).getTableType();
	}
	
	/**
	 * Returns the row name of the row at position i.<br>
	 * This is the name of the row in the warehouse.
	 * 
	 * @param i the position of the row for which the row name is requested
	 * @return the row name in the warehouse of the row at position i
	 */
	public String getRowNameOfLevel(int i) {
		if ((i < 0) || (i >= getSize())) {
			throw new IllegalArgumentException();
		}
		
		return rows.get(i).getColumnName();
	}
	
	/**
	 * Returns the level of the row with the given name.<br>
	 * 
	 * @param s name of the searched row
	 * @return the level of the row with the given name
	 */
	public int getLevelOfRow(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		
		int level = -1;
		for (int i = 0, l = getSize(); i < l; i++) {
			if (rows.get(i).getName().equalsIgnoreCase(s)) {
				return i;
			}
		}
		
		return level;
	}
	
	// -- OVERRIDES -- OVERRIDES -- OVERRIDES -- OVERRIDES --	
	@Override
	public String toString() {
		return "DimRow [name= " + getName() + ", size= " + getSize() + "]";
	}
	
}
