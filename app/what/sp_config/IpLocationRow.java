package what.sp_config;

import what.sp_parser.DataEntry;

public class IpLocationRow extends RowEntry {

	protected IpLocationRow(String name, String logId, int lvl,	String categorie, String scale) {
		super(name, logId, lvl, categorie, scale, RowId.LOCATION);
	}

	@Override
	public boolean split(DataEntry de, String string, int location) {
		// Returns false, shouldn't be used.
		return false;
	}

}
