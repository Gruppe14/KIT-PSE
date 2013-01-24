package what.sp_dataMediation;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import what.sp_config.ConfigWrap;
import what.sp_parser.DataEntry;

/**
 * All things concerning the warehouse go over this class.<br>
 * 
 * It directs all requests to the right class.
 * 
 * @author Jonathan, PSE
 */
public class DataMediator {
	
	/** Configuration on which this ChartMediator works on */
	private ConfigWrap config;
		
	private MySQLAdapter adapter;
	
	/**
	 * Public constructor for a new DataMediator which
	 * work bases on a ConfigWrap.
	 * @param config ConfigWrap on which the work is based.
	 */
	public DataMediator(ConfigWrap config) {
		this.config = config;
		this.adapter = new MySQLAdapter(config);
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
	
	/**
	 * Loads a collection of DataEntry into the warehouse.
	 * 
	 * @param tbl collection of DataEntry to be loaded
	 * @return whether it was successful
	 */
	public boolean loadEntries(Collection<DataEntry> tbl) {
		if (tbl == null) {
			throw new IllegalArgumentException();
		}
		
		return adapter.loadEntries(tbl);
	}
	
	/**
	 * Loads a DataEntry into the warehouse. 
	 * 
	 * @param de DataEntry to be uploaded
	 * @return whether it was successful
	 */
	public boolean loadEntry(DataEntry tbl) {
		if (tbl == null) {
			throw new IllegalArgumentException();
		}
		
		return adapter.loadEntry(tbl);
	}
	
	/**
	 * Returns a resultset for a chart for given parameters
	 * 
	 * @param x x-axis 
	 * @param xTable table where x-axis is stored
	 * @param measure the measure requested
	 * @param filters a map of filters, consisting of key1 = value1 or key1 = value2 and key2 = value3
	 * @return
	 */
	public ResultSet requestTable(String x, String xTable, String measure, HashMap<String, TreeSet<String>> filters) {
		return adapter.requestTable(x, xTable, measure, filters);
	}

	// testing:
	

}
