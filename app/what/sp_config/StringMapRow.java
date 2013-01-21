package what.sp_config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import what.sp_parser.DataEntry;

public class StringMapRow extends RowEntry {
	
	private final Map<String, Set<String>> comparer;
	
	private HashSet<String> compareSet;
	
	protected StringMapRow(String name, String logId, int lvl, String categorie, String scale,
							Map<String, Set<String>> comparer) {
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
	
	public Set<String> getTopicStrings() {
		return comparer.keySet();
	}
	
	public Set<String> getCompareTo() {
		if (compareSet != null) {
			return compareSet;
		}
		
		
		compareSet = createCompareSet();
		assert (compareSet != null);
		
		return compareSet;	
	}

	private HashSet<String> createCompareSet() {
		HashSet<String> comp = new HashSet<String>();
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
	
}
