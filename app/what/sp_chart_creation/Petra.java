package what.sp_chart_creation;

import java.io.File;

abstract class Petra {
	
	private File json;

	
	
	public File getJSON() {
		return json;
	}
	
	abstract public boolean accept(ChartVisitor v);
	
}
