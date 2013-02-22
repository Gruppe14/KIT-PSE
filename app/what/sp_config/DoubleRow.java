package what.sp_config;

//intern imports
import what.sp_parser.DataEntry;

/**
 * This class represents a double row as a RowEntry.<br>
 * 
 * This means the type of this row is a double.
 * 
 * @author Jonathan, Alex, PSE Gruppe 14
 * @see RowEntry
 */
public class DoubleRow extends RowEntry {
	
	/** The table type of a DoubleRow in the warehouse. */
	private static final String TABLE_TYPE = "FLOAT";
	
	/**
	 * Protected constructor for the class DoubleRow.
	 * 
	 * @param name the name
	 * @param logId the logId
	 * @param lvl the level
	 * @param category the category
	 * @param scale the scale String
	 */
	protected DoubleRow(String name, String logId, int lvl, String categorie, String scale) {
		super(name, logId, lvl, categorie, scale, RowId.DOUBLE);
	}	
	
	@Override
	public boolean split(DataEntry de, String string, int location) {
		try {
			if (string.length() == 0) {
				return false;
			}
			de.setInfo(Double.parseDouble(string), location);
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
