package what.sp_config;

// java imports
import java.util.HashSet;
import java.util.TreeSet;
import java.util.HashMap;

// intern imports
import what.sp_parser.DataEntry;

/**
 * This class represents a StringMap row as a RowEntry.<br>
 * 
 * This means the type of this row works as following:
 * You have a set of Strings (keys) for whales a entry in the warehouse can
 * take in this column. All keys have a Set of Strings to which the parsed
 * content gets compared to and if found this key is selected.
 * 
 * @author Jonathan, Alex, PSE Gruppe 14
 * @see RowEntry
 */
public class StringMapRow extends RowEntry {
	
	/** The table type of a StringMapRow in the warehouse. */
	private static final String TABLE_TYPE = "VARCHAR(40)";
	
	/** Map of keys and compare values for them. */
	private final HashMap<String, TreeSet<String>> comparer;
	
	/** 
	 * Cached set of Strings to which will be compared
	 * searching for the right key.
	 */
	private HashSet<String> compareSet;
	
	/**
	 * Protected constructor for the class StringMapRow.
	 * 
	 * @param name the name
	 * @param logId the logId
	 * @param lvl the level
	 * @param category the category
	 * @param scale the scale String
	 * @param comparer the Map of Strings and it's compare-to's
	 */
	protected StringMapRow(String name, String logId, int lvl, String category, String scale,
							HashMap<String, TreeSet<String>> comparer) {
		super(name, logId, lvl, category, scale, RowId.STRINGMAP);
	
		this.comparer = comparer;
	}

	/**
	 * Returns the key to the given String value 
	 * or null if no key is found. 
	 * 
	 * @param value String for which the key is requested
	 * @return the key to the given String value 
	 * 			or null if no key is found
	 */
	private String isTopicTo(String value) {
		for (String s : comparer.keySet()) {
			if (comparer.get(s).contains(value)) {
				return s;
			}
		}
		
		return null;
	}
	
	/**
	 * Return the key Strings of the Map.
	 * 
	 * @return  the key Strings of the Map
	 */
	public TreeSet<String> getKeyStrings() {
		TreeSet<String> a = new TreeSet<String>();
		a.addAll(comparer.keySet());
		return a;	
	}
	
	/**
	 * Returns a set of all compare strings.
	 * 
	 * @return a set of all compare strings
	 */
	public HashSet<String> getCompareTo() {
		// cached?
		if (compareSet != null) {
			return compareSet;
		}
		
		// no - than cache now!
		compareSet = createCompareSet();
		assert (compareSet != null);
		
		return compareSet;	
	}

	/**
	 * Private helper method which returns
	 * a set of all compare strings.
	 * 
	 * @return a set of all compare strings
	 */
	private HashSet<String> createCompareSet() {
		HashSet<String> comp = new HashSet<String>();
		for (String s :comparer.keySet()) {
			comp.addAll(comparer.get(s));
		}
		
		return comp;
	}
	
	@Override
	public boolean split(DataEntry de, String string, int location) {

		
		for (String str : this.getCompareTo()) {
			if (string.toLowerCase().contains(str.toLowerCase())) {
				if (isTopicTo(str).trim().length() == 0) {
					return false;
				}
				de.setInfo(isTopicTo(str).toUpperCase().trim(), location);						
				return true;
			} 
		}				
	

		de.setInfo("other", location);
		return true;
		
	}
	
	@Override
	public String getTableType() {
		return TABLE_TYPE;
	}
	
}
