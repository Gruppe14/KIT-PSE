package what.sp_data_access;

// java imports
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

// intern imports
import what.Printer;
import what.sp_chart_creation.DimChart;
import what.sp_config.ConfigWrap;
import what.sp_config.DimRow;
import what.sp_config.RowId;
import what.sp_parser.DataEntry;

/**
 * MySQLAdapter provides all needed methods to load or receive data
 * into or from the warehouse.
 * 
 * @author Jonathan, PSE Group 14
 *
 */
public class MySQLAdapter {
	
	// specific strings for the database 
	public static final String DIM_TABLE = "dim_";
	public static final String ROW_TABLE = "row_";
	public static final String KEY_TABLE = "_key";
	public static final String FACT_TABLE = "fact_";
	
	public static final String SELECT = "SELECT ";
	public static final String FROM = " FROM ";
	public static final String JOIN = " JOIN ";
	public static final String ON = " ON ";
	public static final String WHERE = " WHERE ";	
	protected static final String GROUPBY = " GROUP BY ";
	
	public static final String AND = " AND ";
	public static final String OR = " OR ";
	public static final String EQL = " = ";
	
	public static final String AS = " AS ";
	private static final String AS_FACTTABLE = " FT "; 
	public static final String ALL = " * ";
	
	public static final String LBR = " ( ";
	public static final String KOMMA = ",  ";
	public static final String RBR = " ) ";
	
	private static final String INSERT = "INSERT INTO ";
	private static final String VALUES = "VALUES ";
	
	public static final String APOS = "'";
	public static final String DOT = ".";
	private static final String SPACE = " ";
	
	/** Cached string for the fact query */
	//private String factQueryStart;
	
	/** The WHConnectionMangager for this MySQLAdapter. */
	private WHConnectionManager whConnections = new WHConnectionManager();

	/** The configuration of this MySQLAdapter. */
	private ConfigWrap config;
	
	/**
	 * Public constructor for a new MySQLAdapter
	 * for a given ConfigWrap.
	 * 
	 * @param config given configuration on which the work is based
	 */
	protected MySQLAdapter(ConfigWrap config) {
		assert (config != null);
		
		this.config = config;

	}

	// -- LOADING -- LOADING -- LOADING -- LOADING -- LOADING --
	/**
	 * Loads a collection of DataEntry into the warehouse.
	 * 
	 * @param tbl collection of DataEntry to be loaded
	 * @return whether it was successful
	 */
	protected boolean loadEntries(Collection<DataEntry> tbl) {
		assert (tbl != null);
		
		
		for (DataEntry d : tbl) {
			loadEntry(d);
		}
		
		return false;
	}
	
	// super complex computing of queries...
	/**
	 * Loads a DataEntry into the warehouse. 
	 * 
	 * @param de DataEntry to be uploaded
	 * @return whether it was successful
	 */
	protected boolean loadEntry(DataEntry de) {
		assert (de != null);
		
		// counts the position in the info array of the data entry
		int counter = 0;
		
		// query for the fact table
		String factQuery = INSERT + config.getFactTableName() + LBR;
		String factValues = VALUES + LBR;
		
		ArrayList<DimRow> dims = config.getDims();
		// get last to set "," at right positions
		DimRow lastDim = dims.get(dims.size() - 1); 
		for (DimRow d : dims) {
			if (d.isDimension()) { // case it's a dimension
				String dimQuery = INSERT + d.getDimTableName() + LBR ;
				String dimValue = VALUES + LBR;
				
				// add all values to the query
				for (int i = 0, l = d.getSize(); i < l; i++) {
					// add level i for table and value
					dimQuery += d.getRowNameOfLevel(i);
					
					// if it is a string we have to add '
					RowId id = d.getRowIdAt(i);
					if ((id.equals(RowId.STRING)) || id.equals(RowId.STRINGMAP)) {
						dimValue += APOS + de.getInfo(counter) + APOS;
						counter++;
					} else {
						dimValue += "" + de.getInfo(counter);
						counter++;
					}
					
					// not last entry -> , 
					if (i != l -1) {
						dimQuery += KOMMA;
						dimValue += KOMMA;
					}
				}
				
				dimQuery += RBR;
				dimValue += RBR;
				
				// execute dimension query  
				String curQuery = dimQuery + dimValue;
				int key = getKey(curQuery, true); 
				
				if (key <= 0) { // it seems that it already existed... we have to ask for the key
					String keyQuery = SELECT + d.getTableKey() + FROM + d.getDimTableName() + WHERE;
					
					for (int i = 0, l = d.getSize(); i < l; i++) {
						
						// if it is a string we have to add '
						RowId id = d.getRowIdAt(i);
						if ((id.equals(RowId.STRING)) || id.equals(RowId.STRINGMAP)) {
							keyQuery += d.getRowNameOfLevel(i) +  EQL + APOS + de.getInfo(counter - l + i) + APOS + SPACE;
						} else {
							keyQuery += d.getRowNameOfLevel(i) + EQL + de.getInfo(counter - l + i) + SPACE;
						}
						
						if ( i != (l - 1)) {
							keyQuery += AND;
						}
					}
					
					key = getKey(keyQuery, false);
	
					if (key <= 0) {
						Printer.pfail("Receiving a key.");
						return false;
					}	
				}
				
				factQuery += d.getTableKey();
				factValues += "" + key;
				
			} else { // case it is fact
				factQuery += d.getRowNameOfLevel(0);
				factValues += de.getInfo(counter);
				counter++;		
			}
			
			// not last one -> ,
			if (!d.equals(lastDim)) {
				factQuery += KOMMA;
				factValues += KOMMA;
			}
		
		}

		// run fact query
		String curQuery = factQuery + RBR + factValues + RBR;	
		return executeUpload(curQuery);
	}

	// -- EXTRACTING -- EXTRACTING -- EXTRACTING -- EXTRACTING -- EXTRACTING --
	// EXTRACTNING >> 1 dim Table (x + measure)
	/**
	 * Returns a JSONObject with two rows containing for a given x from table xTable with key xKey
	 * the measure (including aggregation) with given filters.
	 * 
	 * @param xFilter for the x Filter
	 * @param xTable from the table
	 * @param xKey with the key
	 * @param measure for the measure
	 * @param filters with the filters
	 * @return a JSONObject as a result of the given parameters
	 */
	protected boolean requestChartJSON(DimChart chart) {
		assert (chart != null);
		Printer.ptest("Start chart request.");
		
		String factTable = config.getFactTableName();
		
		// -- SELECT part -- SELECT part -- SELECT part --
		String select = SELECT + chart.getSelect();
		
		// -- FROM part -- FROM part -- FROM part --
		//  FROM >> fact Table;	
		String from = FROM + factTable + AS + AS_FACTTABLE;
		
		// -- JOIN part -- JOIN part -- JOIN part --
		String join = JOIN + LBR + chart.getTableQuery() + RBR; 		
		
		// -- WHERE/ON part -- WHERE/ON part -- WHERE/ON part --	
		String on = ON;
		
		// WHERE >> x.key = fact.key
		on += chart.getKeyRestrictions(AS_FACTTABLE); 
		
		// WHERE >> filter restrictions;
		on += chart.getRestrictions();
				
		// -- GROUPBY PART -- GROUPBY PART -- GROUPBY PART -- 
		String groupBy = GROUPBY + chart.getGroupBy();
		
		// -- EXECUTE -- EXECUTE -- EXECUTE -- EXECUTE --
		return executeChartRequest(select + from + join + on + groupBy, chart);
	} 
	
	// EXTRACTNING >> all values of a Row
	/**
	 * Returns a TreeSet of strings of type child with parent as filter from the given table.
	 * 
	 * @param child type of child (like city) for that strings are requested
	 * @param parentType the type of the parent (like country) for which gets filtered 
	 * @param parentFilter parent for which gets filtered (like Germany)
	 * @param table table in which child and parent are stored
	 * @return a TreeSet of strings of type child with parent as filter
	 */
	protected TreeSet<String> requestStringsWithParent(String child, String parentType, String parentFilter, String table) {
		assert (parentFilter != null);
		assert (table != null);
		assert (child != null);
		
		String query = "" + SELECT + child + FROM + table
						+ WHERE + parentType + EQL + APOS + parentFilter + APOS;
		
		return requestStringSet(query);
	}

	/**
	 * Requests strings for a given string from a given table.
	 * 
	 * @param rowName name of row
	 * @param tableName name of table
	 * @return strings for a given string from a given table
	 */
	protected TreeSet<String> requestStringsOf(String rowName, String tableName) {
		assert (rowName != null);
		assert (tableName != null);
	
		String query = SELECT + rowName + FROM + tableName + GROUPBY + rowName;
				
		return requestStringSet(query);
	}
	
	// -- EXECUTING -- EXECUTING -- EXECUTING -- EXECUTING -- EXECUTING --
	// EXECUTING >> UPLOAD concerning
	/**
	 * Executes a upload request from a given query (String).
	 * 
	 * @param query query which will be excuted as upload
	 * @return whether uploading was successful
	 */
	private boolean executeUpload(String query) {
		assert (query != null);
		
		// getting Connection and statement
		Connection c = getConnection();
		Statement s = getStatement(c);		
		if (s == null) {
			Printer.pfail("Getting statement.");
			close(c);
			return false;
		}
		
		// Printer.print("Try to execute: " + query);
		
		// execute upload
		try {
			s.executeUpdate(query);
		} catch (SQLException e) {
			Printer.pfail("Executing update for query: \n >> " + query + " <<");
			close(s, c);
			return false;
		}
		
		close(s, c);
		return true;
	}
	
	/**
	 * Executes a given query.<br>
	 * This is a upload request if ofUpload is true, otherwise it is a request.
	 * Returns the generated key received for this upload or
	 * -1 if it failed.
	 * 
	 * @param query query for the update
	 * @param ofUpload boolean which tells whether it is a upload or a request
	 * @return the generated key received or -1 if it failed
	 */
	private int getKey(String query, boolean ofUpload) {
		assert (query != null);
		
		int key = -1;
		
		// getting Connection and statement
		Connection c = getConnection();
		Statement s = getStatement(c);		
		if (s == null) {
		//	Printer.pfail("Getting statement.");
			close(c);
			return key;
		}
	
		// Printer.print("Try to execute: " + query);
		
		// execute upload
		ResultSet rs = null;
		try {
			if (ofUpload) {
				s.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				rs = s.getGeneratedKeys();
			} else {
				rs = s.executeQuery(query);
			}
		} catch (SQLException e) {
		//	Printer.pfail("Executing query: \n >> " + query + " <<");
			close(s, c);
			return key;
		}

		// get generated key
		try {
			if (rs.next()){
	            key = rs.getInt(1);
	        }
		} catch (SQLException e1) {
			Printer.pfail("Getting generated key from result set.");
			close(s, c);
			return key;
		}    
		

		close(s,c);
		return key;
	}
	
	// EXECUTING >> REQUEST	
	/**
	 * Executes a given query.
	 * Returns a TreeSet of Strings received.
	 * 
	 * @param query query to be executed
	 * @return a TreeeSet of Strings received
	 */
	private TreeSet<String> requestStringSet(String query) {
		assert (query != null);
		
		// getting Connection and statement
		Connection c = getConnection();
		Statement s = getStatement(c);
		if (s == null) {
			Printer.pfail("Getting statement.");
			close(c);
			return null;
		}
		
		// Printer.print("Try to execute: " + query);
		
		// getting result set
		ResultSet re = null;
		try {
			re = s.executeQuery(query);
		} catch (SQLException e) {
			Printer.pfail("Executing query: \n >> " + query + " <<");
			close(s, c);
			return null;
		}
		
		// changing result to what is requested
		TreeSet<String> requested = getTreeSetFromRS(re);
		if (requested == null) {
			Printer.pfail("Receive changed data.");
		}
		
		close(s, c);
		return requested;		
	}
	
	/**
	 * Executes the given query and stores the result in
	 * the given chart.
	 * 
	 * @param query 
	 * @param chart DimChart in which the result will be changed and stored
	 * @return whether it as successful
	 */
	private boolean executeChartRequest(String query, DimChart chart) {
		assert (query != null);
		assert (chart != null);
		
		// getting Connection and statement
		Connection c = getConnection();
		Statement s = getStatement(c);
		if (s == null) {
			Printer.pfail("Getting statement.");
			close(c);
			return false;
		}
		
		Printer.ptest("Try to execute: " + query);
		
		// execute query
		ResultSet re = null;
		try {
			re = s.executeQuery(query);
		} catch (SQLException e) {
			Printer.pfail("Executing query: \n >> " + query + " <<");
			close(s, c);
			return false;
		}
		
		if (!(chart.createJSONFromResultSet(re))) {
			Printer.pfail("Creating JSON from ResultSet.");
			close(s, c);
			return false;
		}

		close(s, c);
		return true;
	}
	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER --
	/**
	 * Returns a statement created from a given Connection.
	 * 
	 * @param c Connection from which a statement will be created
	 * @return a statement created from a given Connection
	 */
	private Statement getStatement(Connection c) {
		assert (c != null);
		
		Statement s;
		try {
			s = c.createStatement();
			return s;
		} catch (SQLException e) {
			Printer.perror("Creating statement.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns a Connection.
	 * 
	 * @return a Connection
	 */
 	private Connection getConnection() {
		Connection c = null;
		// TODO limit number of requests -> endless loop
		do {
			c = whConnections.getConnectionFromPool();
				
			if (c == null) {
				try {
					wait(30);
				} catch (InterruptedException e) {
					// nothing to do here... it's ok if we get woken up
					Printer.pproblem("No connection available.");
				}
			}
		} while (c == null) ;
		
		return c;
	}
	
 	private void close(Statement s, Connection c) {
		assert (s != null);
		assert (c != null);
		
		close(c);
		
		try {
			s.close();
		} catch (SQLException e) {
			Printer.perror("Closing statement.");
		}
		
	}
	
	private void close(Connection c) {
		whConnections.returnConnectionToPool(c);		
	}
	
	// -- WORKER -- WORKER -- WORKER -- WORKER -- WORKER -- WORKER --
	/**
	 * Transforms a ResultSet into a TreeSet of Strings.
	 *  
	 * @param re ResultSet from which data will be extracted
	 * @return TreeSet of Strings extracted from the ResultSet
	 */
	protected static TreeSet<String> getTreeSetFromRS(ResultSet re) {
		assert (re != null);
		
		TreeSet<String> strings = new TreeSet<String>();
		
		try {
			while (re.next()) {
				strings.add(re.getString(1));
			}
		} catch (SQLException e) {
			Printer.perror("Reading from ResultSet.");
			return null;
		}
		
		return strings;
	}
 	
}
