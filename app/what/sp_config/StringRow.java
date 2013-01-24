package what.sp_config;

import java.util.Set;

import what.sp_parser.DataEntry;


public class StringRow extends RowEntry {

	
	private static final String TABLE_TYPE = "VARCHAR(40)";
	
	private final Set<String> contents;
		
	
	protected StringRow(String name, String logId, int lvl,
						String categorie, String scale, Set<String> strings) {
		
		super(name, logId, lvl, categorie, scale, RowId.STRING);
		this.contents = strings;
	}

	public Set<String> getCompareTo() {
		return contents;
	}
	
	public boolean split(DataEntry de, String string, int location) {
		de.setInfo(string.toUpperCase(), location);
		return true;
	}

	@Override
	public String getTableType() {
		return TABLE_TYPE;
	}
}
