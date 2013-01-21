package what.sp_config;

import java.util.Set;

import what.sp_parser.DataEntry;


public class StringRow extends RowEntry {

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
		de.setInfo(string, location);
		return true;
	}
}
