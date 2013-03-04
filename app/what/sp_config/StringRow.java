package what.sp_config;

// intern imports
import what.sp_parser.DataEntry;

/**
 * This class represents a String row as a RowEntry.<br>
 * 
 * This means the type of this row is a String.
 * 
 * @author Jonathan, Alex, PSE Gruppe 14
 * @see RowEntry
 */
public class StringRow extends RowEntry {

	/** The table type of a StringRow in the warehouse. */
	private static final String TABLE_TYPE = "VARCHAR(40)";
		
	/**
	 * Protected constructor for the class StringRow.
	 * 
	 * @param name the name
	 * @param logId the logId
	 * @param lvl the level
	 * @param category the category
	 * @param scale the scale
	 */
	protected StringRow(String name, String logId, int lvl,	String category, String scale) {
		
		super(name, logId, lvl, category, scale, RowId.STRING);
	}

	
	@Override
	public boolean split(DataEntry de, String string, int location) {
		if (string.toUpperCase().trim().length() == 0) {
			return false;
		}
		de.setInfo(string.toUpperCase().trim(), location);
		return true;
	}

	@Override
	public String getTableType() {
		return TABLE_TYPE;
	}
}
