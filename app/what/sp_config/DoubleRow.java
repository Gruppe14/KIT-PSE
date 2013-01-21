package what.sp_config;

import what.sp_parser.DataEntry;

public class DoubleRow extends RowEntry {

	protected DoubleRow(String name, String logId, int lvl, String categorie, String scale) {
		super(name, logId, lvl, categorie, scale, RowId.DOUBLE);
	}	
	
	public boolean split(DataEntry de, String string, int location) {
		try {
			de.setInfo(Double.parseDouble(string), location);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
