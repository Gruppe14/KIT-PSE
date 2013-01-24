package what.sp_dataMediation;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import what.sp_config.ConfigWrap;
import what.sp_parser.DataEntry;

public class DataMediator {
	
	/** Configuration on which this ChartMediator works on */
	private ConfigWrap config;
		
	private MySQLAdapter adapter;
	
	public DataMediator(ConfigWrap config) {
		this.config = config;
		this.adapter = new MySQLAdapter();
	}
	
	/**
	 * Organizes things after data got uploaded.<br>
	 * Sets the String sets in the DimRows in the config and
	 * tells the OLAP Cubes to prae-compute it's data new.
	 * @return
	 */
	public boolean organizeData() {
		if (!config.computeDimRows()) {
			System.out.println("Computing a DimRow failed!");
			return false;
		}
		
		return true;
	}
	
	public boolean loadEntries(Collection<DataEntry> tbl) {
		if (tbl == null) {
			throw new IllegalArgumentException();
		}
		
		return adapter.loadEntries(tbl);
	}
	
	public ResultSet requestTable(String x, String xTable, String measure, String factTable, HashMap<String, TreeSet<String>> filters) {
		return adapter.requestTable(x, xTable, measure, factTable, filters);
	}

	
	

}
