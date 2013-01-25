package what.sp_config;

import java.util.TreeSet;
import java.util.HashMap;

import what.sp_parser.DataEntry;

public class StringMapRow extends RowEntry {
	
	private static final String TABLE_TYPE = "VARCHAR(40)";
	
	private final HashMap<String, TreeSet<String>> comparer;
	
	private TreeSet<String> compareSet;
	
	protected StringMapRow(String name, String logId, int lvl, String categorie, String scale,
							HashMap<String, TreeSet<String>> comparer) {
		super(name, logId, lvl, categorie, scale, RowId.STRINGMAP);
	
		this.comparer = comparer;
	}

	public String isTopicTo(String value) {
		for (String s :comparer.keySet()) {
			if (comparer.get(s).contains(value)) {
				return s;
			}
		}
		
		return null;
	}
	
	public TreeSet<String> getTopicStrings() {
		TreeSet<String> a = new TreeSet<String>();
		a.addAll(comparer.keySet());
		return a;	
	}
	
	public TreeSet<String> getCompareTo() {
		if (compareSet != null) {
			return compareSet;
		}
		
		
		compareSet = createCompareSet();
		assert (compareSet != null);
		
		return compareSet;	
	}

	private TreeSet<String> createCompareSet() {
		TreeSet<String> comp = new TreeSet<String>();
		for (String s :comparer.keySet()) {
			comp.addAll(comparer.get(s));
		}
		
		return comp;
	}
	
	public boolean split(DataEntry de, String string, int location) {

		
		for (String str : this.getCompareTo()) {
			if (string.toLowerCase().contains(str.toLowerCase())) {
				de.setInfo(isTopicTo(str).toUpperCase(), location);						
				return true;
			} 
		}				
	

		de.setInfo("other", location);
		return true;
		
	}
	
	@Override
	public String getTableType() {
		return TABLE_TYPE;
	}
	
}
