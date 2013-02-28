package what.sp_config;

// intern imports
import what.sp_parser.DataEntry;

/**
 * This class represents a ip and location row as a RowEntry.<br>
 * 
 * This means a Row of this type  represents the row storing the IP,
 * from which the location like city and country is extracted.
 * 
 * @author Jonathan, Alex, PSE Gruppe 14
 * @see RowEntry
 */
public class IpLocationRow extends RowEntry {
	
	/**
	 * Protected constructor for the class IpLocationRow.
	 * 
	 * @param name the name
	 * @param logId the logId
	 * @param lvl the level
	 * @param category the category
	 * @param scale the scale String
	 */
	protected IpLocationRow(String name, String logId, int lvl,	String category, String scale) {
		super(name, logId, lvl, category, scale, RowId.LOCATION);
		
	}

	@Override
	public boolean split(DataEntry de, String string, int location) {
		// Returns false, shouldn't be used.
		return false;
	}

	@Override
	public String getTableType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
