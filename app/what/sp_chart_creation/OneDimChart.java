package what.sp_chart_creation;

import java.io.File;
import java.util.TreeSet;

abstract class OneDimChart {
	
	private File json;

	private TreeSet<String> filters;
	
	
	public File getJSON() {
		return json;
	}
	
	abstract public boolean accept(ChartVisitor v);
	
}
