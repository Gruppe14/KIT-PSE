package what.sp_chart_creation;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

public class DimChart {
	
	private File json;


	private String x;
	
	private String measure;
	
	private HashMap<String, TreeSet<String>> filters;
	
	
	public File getJSON() {
		return json;
	}
	
	public boolean accept(ChartVisitor v) {
		
		return true;
	}
	
}
