package what.sp_config;

// intern imports
import what.sp_parser.DataEntry;

/**
 * This class represents a int row as a RowEntry.<br>
 * 
 * This means the type of this row is a int.
 * 
 * @author Jonathan, Alex, PSE Gruppe 14
 * @see RowEntry
 */
public class IntRow extends RowEntry {
	
	/** The table type of a IntRow in the warehouse. */
	private static final String TABLE_TYPE = "INT(3)";
	
	/**
	 * Protected constructor for the class IntRow.
	 * 
	 * @param name the name
	 * @param logId the logId
	 * @param lvl the level
	 * @param category the category
	 * @param scale the scale String
	 */
	protected IntRow(String name, String logId, int lvl, String category, String scale) {
		super(name, logId, lvl, category, scale, RowId.INT);	
	}
	
	@Override
	public boolean split(DataEntry de, String string, int location) {
		try {
			if (string.length() == 0) {
				return false;
			}
			int i = Integer.parseInt(string);
			if (i >= 0) {
				de.setInfo(i, location);
			} else if (i == -1) {
				de.setInfo(0, location);
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public String getTableType() {
		return TABLE_TYPE;
	}
	
}
