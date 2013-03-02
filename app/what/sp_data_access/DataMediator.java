package what.sp_data_access;

// java imports
import java.util.Collection;
import java.util.TreeSet;

// intern imports
import what.Printer;
import what.sp_chart_creation.DimChart;
import what.sp_config.ConfigWrap;
import what.sp_config.DimKnot;
import what.sp_config.DimRow;
import what.sp_config.RowId;
import what.sp_config.StringDim;
import what.sp_config.StringMapRow;
import what.sp_config.TimeDimension;
import what.sp_parser.DataEntry;

/**
 * All things concerning the warehouse go over this class.<br>
 * 
 * It directs all requests to the right class.
 * 
 * @author Jonathan, PSE Gruppe 14
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
	 * Organizes things after data got uploaded or at the start of the program.<br>
	 * This means it computes the tree of DimKnots containing the String
	 * for the selection boxes.
	 * 
	 * @return whether it was successful
	 */
	public boolean organizeData() {
		for (int i = 0, l = config.getNumberOfDims(); i < l; i++) {
			DimRow d = config.getDimRowAt(i);
			
			// if is no String dimension no computing is necessary 
			if (d.isStringDim()) {
				StringDim sd = (StringDim) d;
				int posi = 0;
				int size = sd.getSize();
				TreeSet<String> strings = null;
				
				
				if (sd.getRowIdAt(posi).equals(RowId.STRINGMAP)) {
					// case 1: it's a StringMap and the strings are known
					StringMapRow r = (StringMapRow) sd.getRowAt(posi);
					strings = r.getKeyStrings();
				
				
				} else if (sd.getRowIdAt(posi).equals(RowId.STRING))  {
					// it's a StringRow, request all string of the highest level from the warehouse
					strings = requestStringsOf(d.getColumnNameOfLevel(posi), sd.getDimTableName());	
					
				} else {
					// error
					Printer.perror("Unkown row type at pos. " + posi + " for dim: " + sd.getName());
					return false;
				}
				
				// for every String add a DimKnot
				for (String s : strings) {			
					DimKnot dk =  new DimKnot(s, sd.getRowAt(posi));
					
					// if it has deeper levels, add children
					if (size > 1) {
						if (!addChildrenTo(dk, sd, posi + 1)) {
							Printer.pproblem("Adding children to DimKnot: " + dk);
						}
					}
					
					//Printer.ptest(dk.toString());
					
					sd.addTree(dk);
				}

			} else if (d.isTimeDim()) {
				TimeDimension td = (TimeDimension) d;
				
				int[][] time = adapter.getTime(td);
				if (time != null) {
					td.setMinMaxTime(time);
				} else {
					return false;
				}
				
				
			} else {
				// nothing to do here, cause only String dimensions give selection boxes
				continue;
			}
		}
				
		return true;
	}
	
	/**
	 * Adds to the given DimKnot  all it's children knots. 
	 * The given position describes the
	 * position/level of the children (!) in the given DimRow.
	 * 
	 * @param dk DimKnot for which the children are requested
	 * @param d DimRow dimension of the DimKnots
	 * @param posi position of the children(!), position of dk + 1
	 * @return whether it was successful
	 */
	private boolean addChildrenTo(DimKnot dk, DimRow d, int posi) {
		assert (dk != null);
		assert (d != null);
		assert ((posi >= 0) && (posi < d.getSize()));
		
		int size = d.getSize();
		
		// request children
		TreeSet<String> children = requestStringsWithParent(d.getColumnNameOfLevel(posi), d.getColumnNameOfLevel(posi - 1), 
															dk.getValue(), d.getDimTableName());
		// request failed
		if (children == null) {
			Printer.pfail("Requesting children for: " + dk.getValue());
			return false;
		}
		
		// for every String add a DimKnot
		for (String s : children) {			
			DimKnot child =  new DimKnot(s, d.getRowAt(posi));
			
			// if it has deeper levels, add children
			if (posi < (size - 1)) {
				if (!addChildrenTo(child, d, posi + 1)) {
					Printer.pfail("Adding children to DimKnot: " + dk);
					return false;
				}
			}
			
			dk.addChild(child);
		}

		return true;
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
	 * @param tbl DataEntry to be uploaded
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
	public TreeSet<String> requestStringsWithParent(String child, String parentType, 
													String parentFilter, String table) {
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

	// -- TABLE CREATION -- TABLE CREATION -- TABLE CREATION -- TABLE CREATION --
	/**
	 * Creates the warehouse tables for the configuration.
	 * 
	 * @return whether it was successful
	 */
	public boolean createDBTables() {
		if (adapter.createDBTables()) {
			Printer.psuccess("Creating tables.");
			setTablesCreated();
			return true;
		} else {
			Printer.pfail("Creating tables.");
			return false;
		}
	}

	/**
	 * Sets the boolean for the table is created to the given boolean.
	 */
	private void setTablesCreated() {
		 adapter.storeDataBase(config.getNameOfDB());
	}
	
	/**
	 * Returns whether the tables in the warehouse are created yet.
	 * 
	 * @return whether the tables in the warehouse are created yet
	 */
	public boolean areTablesCreated() {
		return adapter.tablesAreSet(config.getNameOfDB());
	}

}
