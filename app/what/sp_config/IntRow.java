package what.sp_config;

import what.sp_parser.DataEntry;

public class IntRow extends RowEntry {
	
	private static final String TABLE_TYPE = "INT(3)";
	
	protected IntRow(String name, String logId, int lvl, String categorie, String scale) {
		super(name, logId, lvl, categorie, scale, RowId.INT);	
	}
	
	public boolean split(DataEntry de, String string, int location) {
		try {
			de.setInfo(Integer.parseInt(string), location);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public String getTableType() {
		return TABLE_TYPE;
	}
	
}
