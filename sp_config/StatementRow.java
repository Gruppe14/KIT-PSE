package what.sp_config;

import java.util.HashSet;
import java.util.Set;

public class StatementRow extends MeasureRow {
	
	private String name;

	public StatementRow(String name, String scaleName) {
		super(name, scaleName);
		// TODO Auto-generated constructor stub
	}


	private HashSet<String> compareSet = new HashSet<String>(100);
	
	// string tree or something...
	
	
	public Set<String> getCompareTo() {
		if (compareSet != null) {
			return compareSet;
		}
		
		compareSet = createCompareSet();
		
		assert (compareSet != null);
		
		return compareSet;		
	}

	
	private HashSet<String> createCompareSet() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
