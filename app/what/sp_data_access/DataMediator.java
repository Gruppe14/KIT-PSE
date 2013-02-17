package what.sp_data_access;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import org.json.JSONObject;

import what.sp_chart_creation.DimChart;
import what.sp_chart_creation.TwoDimChart;
import what.sp_config.ConfigWrap;
import what.sp_config.DimRow;
import what.sp_config.RowId;
import what.sp_config.StringMapRow;
import what.sp_parser.DataEntry;

/**
 * All things concerning the warehouse go over this class.<br>
 * 
 * It directs all requests to the right class.
 * 
 * @author Jonathan, PSE
 */
public class DataMediator {
	
	/** Configuration on which this ChartMediator works on. */
	private ConfigWrap config;
	
	/** MySQL adapter with which this DataMediator will work. */
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
	
	// -- ORGANIZE DATA -- ORGANIZE DATA -- ORGANIZE DATA -- ORGANIZE DATA --
	/**
	 * Organizes things after data got uploaded.<br>
	 * Sets the String sets in the DimRows in the config and
	 * tells the OLAP Cubes to prae-compute it's data new.
	 * @return
	 */
	public boolean organizeData() {
		for (int i = 0, l = config.getNumberOfDims(); i < l; i++) {
			
			DimRow d = config.getDimRowAt(i);
			if (d.isStringDim()) {
				int posi = 0;
				int size = d.getSize();

				TreeSet<String> strings = null;
				if (d.getRowIdAt(posi).equals(RowId.STRINGMAP)) {
					StringMapRow r = (StringMapRow) d.getRowAt(posi);
					strings = r.getKeyStrings();
				} else if (d.getRowIdAt(posi).equals(RowId.STRING))  {
					strings = requestStringsOf(d.getRowNameOfLevel(posi), d.getDimTableName());	
				} else {
					System.out.println("Unkown type at position " + posi + " found while computing Strings for web page.");
				}
				
				if (size == 1) {
					d.setStrings(strings);
					continue;
				} 
				
				HashMap<String, Object> object = getHashMap(strings, d, posi + 1);	
				
				d.setStrings(object);
		
			} else {
				continue;
			}
		}
				
		return true;
	}
	
	/**
	 * Helper class returning a recursive HashMap extracted from a DimRow.
	 * 
	 * @param strings TreeSet as keys
	 * @param d DimRow from which to extract
	 * @param posi position in DimRow
	 * @return a recursive HashMap extracted from a DimRow
	 */
	private HashMap<String, Object> getHashMap(TreeSet<String> strings,	DimRow d, int posi) {
		assert (strings != null);
		assert (d != null);
		assert ((posi >= 0) && (posi < d.getSize()));
		
		int size = d.getSize();
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		for (String s : strings) {
			TreeSet<String> childs = requestStringsWithParent(d.getRowNameOfLevel(posi), d.getRowNameOfLevel(posi - 1), s, d.getDimTableName());
			if (childs == null) {
				System.out.println("ERROR: Requesting chils for " + s + "failed.");
				return null;
			}
			
			if (posi < (size - 1)) {
				result.put(s, (getHashMap(childs, d, posi + 1)));
			} else {
				result.put(s, childs);
			}
			
		}

		return result;
	}

	
	/**
	 * Organizes things after data got uploaded.<br>
	 * Sets the String sets in the DimRows in the configuration and
	 * tells the OLAP Cubes to prae-compute it's data new.
	 * @return
	 */
	public boolean organizeData2() {
		for (int i = 0, l = config.getNumberOfDims(); i < l; i++) {
			getDimKnot(null, null, 0);
		}
				
		return true;
	}
	
	/**
	 * Helper class returning a recursive HashMap extracted from a DimRow.
	 * 
	 * @param strings TreeSet as keys
	 * @param d DimRow from which to extract
	 * @param posi position in DimRow
	 * @return a recursive HashMap extracted from a DimRow
	 */
	private HashMap<String, Object> getDimKnot(TreeSet<String> strings,	DimRow d, int posi) {
		// TODO

		return null;
	}

	
	// -- LOADING REQUEST -- LOADING REQUEST -- LOADING REQUEST - LOADING REQUEST --
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
	
	// -- EXTRACTING REQUEST -- EXTRACTING REQUEST -- EXTRACTING REQUEST - EXTRACTING REQUEST --
	/**
	 * Returns whether the request for the given chart was successful.
	 * 
	 * @param chart DimChart on which the request is based and the result stored
	 * @return whether the request for the given chart was successful
	 */
	public boolean requestDimJSON(DimChart chart) {
		if (chart == null) {
			throw new IllegalArgumentException();
		}
		
		return adapter.requestChartJSON(chart);
	}
	
	/**
	 * Returns a TreeSet of strings of type child with parent as filter from the given table.
	 * 
	 * @param child type of child (like city) for that strings are requested
	 * @param parentType the type of the parent (like country) for which gets filtered 
	 * @param parentFilter parent for which gets filtered (like Germany)
	 * @param table table in which child and parent are stored
	 * @return a TreeSet of strings of type child with parent as filter
	 */
	public TreeSet<String> requestStringsWithParent(String child, String parentType, String parentFilter, String table) {
		return adapter.requestStringsWithParent(child, parentType, parentFilter, table);
	}
	
	/**
	 * Requests strings for a given string from a given table.
	 * 
	 * @param rowName name of row
	 * @param tableName name of table
	 * @return strings for a given string from a given table
	 */
	private TreeSet<String> requestStringsOf(String rowName, String tableName) {
		assert (rowName != null);
		assert (tableName != null);
		
		return adapter.requestStringsOf(rowName, tableName);
	}

}
