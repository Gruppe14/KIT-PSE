package what.sp_config;

// java imports
import java.util.Set;

// intern imports
import what.sp_parser.DataEntry;

// TODO decide whether to split in two subclasses or kill contents!
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
	
	/** TODO when decided what happens with this class */
	private final Set<String> contents;
		
	/** TODO when decided what happens with this class */
	protected StringRow(String name, String logId, int lvl,
						String categorie, String scale, Set<String> strings) {
		
		super(name, logId, lvl, categorie, scale, RowId.STRING);
		this.contents = strings;
	}

	/** TODO when decided what happens with this class */
	public Set<String> getCompareTo() {
		return contents;
	}
	
	@Override
	public boolean split(DataEntry de, String string, int location) {
		de.setInfo(string.toUpperCase(), location);
		return true;
	}

	@Override
	public String getTableType() {
		return TABLE_TYPE;
	}
}
