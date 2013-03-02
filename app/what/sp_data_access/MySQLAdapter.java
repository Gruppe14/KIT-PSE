package what.sp_data_access;

// java imports
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	/** Constant to modify a dimension with to get a table name. */
	public static final String DIM_TABLE = "dim_";
	/** Constant to modify a row with to get a column name. */
	public static final String ROW_TABLE = "row_";
	/** Constant to modify a dimension with to get a key name. */
	public static final String KEY_TABLE = "_key";
	/** Constant to modify a configuration name with to get a fact table name. */
	public static final String FACT_TABLE = "fact_";
	
	/** MySQL short constant. */
	public static final String SELECT = "SELECT ";
	/** MySQL short constant. */
	public static final String FROM = " FROM ";
	/** MySQL short constant. */
	public static final String JOIN = " JOIN ";
	/** MySQL short constant. */
	public static final String ON = " ON ";
	/** MySQL short constant. */
	public static final String WHERE = " WHERE ";	
	/** MySQL short constant. */
	protected static final String GROUPBY = " GROUP BY ";
	
	/** MySQL short constant. */
	public static final String AND = " AND ";
	/** MySQL short constant. */
	public static final String OR = " OR ";
	/** MySQL short constant. */
	public static final String EQL = " = ";
	/** MySQL short constant. */
	public static final String MORE = " > ";
	/** MySQL short constant. */
	public static final String LESS = " < ";
	
	
	/** MySQL short constant. */
	public static final String AS = " AS ";
	/** MySQL short constant. */
	private static final String AS_FACTTABLE = " FT "; 
	/** MySQL short constant. */
	public static final String ALL = " * ";
	
	/** MySQL short constant. */
	public static final String LBR = " ( ";
	/** MySQL short constant. */
	public static final String KOMMA = ",  ";
	/** MySQL short constant. */
	public static final String RBR = " ) ";
	
	/** MySQL short constant. */
	private static final String INSERT = "INSERT INTO ";
	/** MySQL short constant. */
	private static final String IIFNOTEX = "INSERT IGNORE INTO ";
	/** MySQL short constant. */
	private static final String VALUES = "VALUES ";
	
	/** MySQL short constant. */
	public static final String APOS = "'";
	/** MySQL short constant. */
	public static final String DOT = ".";
	
	/** Constant number for HashCodeBuilder. */
	public static final int HASHONE = 7;
	/** Constant number for HashCodeBuilder. */
	public static final int HASHTWO = 17;
	/** Constant number to wait till request new Connection. */
	public static final int CON = 30;
		
	/**
	 * The trunks of the uploads.<br>
	 * [0] is for the fact table,
	 * [>0] is for the dimensions in ordering of the appearance
	 * when getting the DimRows from a configuration
	 * via getDims(). 
	 * Trunks contain: INSERT ... VALUE
	 */
	private String[] uploadTrunks;
		
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
		if (!(computeUploadTrunk())) {
			Printer.pfail("Computing upload trunk.");
		}

	}

	// -- CREATING -- CREATING -- CREATING -- CREATING -- CREATING --
	/**
	 * Creates the tables for the warehouse 
	 * for the configuration of this MySQLAdapter.
	 * 
	 * @return whether it was successful
	 */
	protected boolean createDBTables() {
		Printer.print("Trying to create new tables in warehouse.");
		
		String[] queries = DBTableBuilder.getDataBaseQuery(config);
		
		Connection c = getNoAutoCommitConnection();
		Statement s = getStatement(c);
		if (s == null) {
			Printer.pfail("Getting Statment.");
			close(c);
		}
		
		// add query to statement
		for (String str : queries) {
			try {
				s.addBatch(str);
			} catch (SQLException e) {
				Printer.perror("Adding String to batch: " + str);
				close(s, c);
				return false;
			}
		}
		
		// execute
		if (!(executeStatementBatch(s))) {
			Printer.pfail("Executing table creating query.");
			close(s, c);
			return false;
		} else {
			Printer.psuccess("Creating tables.");
			close(s, c);
			return true;
		}
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
		
		String[] queries = getUploadTrunk();
		if (queries == null) {
			Printer.perror("No upload trunk accessable, we die here...");
			return false;
		}
				
		// counts the position in the info array of the data entry
		int rowCounter = 0;
		int dimCounter = 0;
		
		// fact query (
		queries[0] += LBR;
		
		ArrayList<DimRow> dims = config.getDims();
		// get last to set "," at right positions
		DimRow lastDim = dims.get(dims.size() - 1); 
		for (DimRow d : dims) {
			if (d.isDimension()) { // case it's a dimension
				dimCounter++;
				
				// get a HashCode Builder which will produce the key
				HashCodeBuilder hashi = new HashCodeBuilder(HASHTWO, HASHONE);
				
				// dim query (
				queries[dimCounter] += LBR;
				
				
				// add all values to the query
				for (int i = 0, l = d.getSize(); i < l; i++) {			
					// if it is a string we have to add '
					RowId id = d.getRowIdAt(i);
					if ((id.equals(RowId.STRING)) || id.equals(RowId.STRINGMAP)) {
						queries[dimCounter] += APOS + de.getInfo(rowCounter) + APOS;
						hashi.append(de.getInfo(rowCounter));
						rowCounter++;
					} else {
						queries[dimCounter] += "" + de.getInfo(rowCounter);
						hashi.append(de.getInfo(rowCounter));
						rowCounter++;
					}
					queries[dimCounter] += KOMMA;
				}
				
				// get the key 
				int key = hashi.toHashCode();
				
				// add key to the fact and dimension query..
				queries[dimCounter] += key + RBR;
				queries[0] += "" + key;
				
			} else { // case it is fact
				queries[0] += de.getInfo(rowCounter);
				rowCounter++;		
			}
			
			// not last one -> ,
			if (!d.equals(lastDim)) {
				queries[0] += KOMMA;
			}
		
		}
		
		// fact query )
		queries[0] += RBR;
	
		return executeCompleteUpload(queries);
	}

	// -- EXTRACTING -- EXTRACTING -- EXTRACTING -- EXTRACTING -- EXTRACTING --
	// EXTRACTNING >> 1 dim Table (x + measure)
	/**
	 * Returns a JSONObject with two rows containing for a given x from table xTable with key xKey
	 * the measure (including aggregation) with given filters.
	 * 
	 * @param chart DimChart for which a JSON will be produced
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
	protected TreeSet<String> requestStringsWithParent(String child, String parentType, 
														String parentFilter, String table) {
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
	 * Executes the given array of Strings.
	 * 
	 * @param queries Strings to be executed
	 * @return whether it was successful
	 */
	private boolean executeCompleteUpload(String[] queries) {
		Connection c = getNoAutoCommitConnection();
		Statement s = getStatement(c);
		if (s == null) {
			Printer.pfail("Getting Statment.");
			close(c);
		}
		
		// add query to statement
		for (String str : queries) {
			try {
				//Printer.ptest(str);
				s.addBatch(str);
			} catch (SQLException e) {
				Printer.perror("Adding String to batch: " + str);
				close(s, c);
				return false;
			}
		}
		
		
		if (!(executeStatementBatch(s))) {
			Printer.pfail("Executing queries.");
			close(s, c);
			return false;
		} else {
			//Printer.psuccess("Upload.");
			close(s, c);
			return true;
		}
	}
	
	/**
	 * Executes the batch of the given statement.
	 * 
	 * @param s Statement which batch will be executed
	 * @return whether all query in the batch were successful
	 */
	private boolean executeStatementBatch(Statement s) {
		assert (s != null);

		int[] result;
		try {
			result = s.executeBatch();
		} catch (SQLException e) {
			Printer.perror("Clearing batch of statement.");
			return false;
		}
		
		if (result != null) {
			for (int i = 0, l = result.length; i < l; i++) {
				if (result[i] == Statement.EXECUTE_FAILED) {
					Printer.pfail("Query number " + i + "failed.");
					return false;
				}
			}
		}
		
		return true;
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
		int a = 0;
		do {
			c = whConnections.getConnectionFromPool();
			
			if (c == null) {
				a++;
				try {
					wait(CON);
				} catch (InterruptedException e) {
					// nothing to do here... it's okay if we get woken up
					Printer.pproblem("No connection available.");
				}
			}
		} while ((c == null) && (a < (CON * CON)));
		
		return c;
	}
	
 	/**
	 * Returns a Connection with auto committing disabled.
	 * 
	 * @return a Connection with auto committing disabled
	 */
 	private Connection getNoAutoCommitConnection() {
		Connection c = getConnection();

		try {
			c.setAutoCommit(false);
		} catch (SQLException e) {
			Printer.perror("Changing auto commit to false.");
			
			// yeah f**k it... will work anyway :D
			return c;
		}
		
		return c;
	}

	/**
	 * Returns the upload trunks.
	 * 
	 * @return the upload trunks
	 */
	protected String[] getUploadTrunk() {
		if (uploadTrunks == null) {
			Printer.perror("UploadTrunk not computed.");
			if (!(computeUploadTrunk())) {
				return null;
			}
		}
		
		// make defensive copy
		int l = uploadTrunks.length;
		String[] copy = new String[l];
		for (int i = 0; i < l; i++) {
			copy[i] = "" + uploadTrunks[i];
		}
		
		return copy;
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
 	
	/**
	 * Closes given Statement and returns given Connection to pool.
	 * @param s Statement to be closed
	 * @param c Connection to be returned to pool
	 */
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
	
	/**
	 * Returns given Connection to pool.
	 * 
	 * @param c Connection to be returned to pool
	 */
	private void close(Connection c) {
		try {
			c.setAutoCommit(true);
		} catch (SQLException e) {
			Printer.pfail("Changing back to auto commit.");
		}
		
		whConnections.returnConnectionToPool(c);		
	}
	
	/**
	 * Computes the upload trunks for the the fact table and the dimension.
	 * 
	 * @return whether it was successful
	 */
	private boolean computeUploadTrunk() {
		int dimCounter = 0;
		
		String[] trunks = new String[config.getNumberOfDimsWitoutRows() + 1];
		// query for the fact table
		String factTrunk = INSERT + config.getFactTableName() + LBR;
		
		
		ArrayList<DimRow> dims = config.getDims();
		// get last to set "," at right positions
		DimRow lastDim = dims.get(dims.size() - 1); 
		for (DimRow d : dims) {
			if (d.isDimension()) { // case it's a dimension
				dimCounter++;
				String curDimTrunk = IIFNOTEX + d.getDimTableName() + LBR;
				
				// add all rows to the trunk
				for (int i = 0, l = d.getSize(); i < l; i++) {
					curDimTrunk += d.getRowNameOfLevel(i) + KOMMA;
				}
				
				
				// end this trunk: key + ) + VALUES
				curDimTrunk += d.getTableKey() + RBR + VALUES;
				// add to trunks
				trunks[dimCounter] = curDimTrunk;
	
				// store key row of this dimension to the fact trunk
				factTrunk += d.getTableKey();				
			} else { // case it is fact
				factTrunk += d.getRowNameOfLevel(0);	
			}
			
			// not last one -> ,
			if (!d.equals(lastDim)) {
				factTrunk += KOMMA;
			}
		
		}
		
		// end fact trunk
		factTrunk += RBR + VALUES;
		
		// add the fact trunk
		trunks[0] = factTrunk;

		this.uploadTrunks = trunks;
		
		// TEST
		//for (int i = 0, j = trunks.length; i < j; i++) {
		//	Printer.ptest("Trunk " + i + ": " + trunks[i]);
		//}
		
		return true;
	}

	// -- WHAT CONFIG --  WHAT CONFIG  --  WHAT CONFIG  --  WHAT CONFIG  --
	/** MySQL constant name. */
	private static final String SOURCE_DB = "sourceDB";	
	/** MySQL constant name. */
	private static final String CONFIG_NAME = "WHAT_CONFIG";
	/** MySQL query constant. */
	private static final String CONFIG_TABLE = "CREATE TABLE " + CONFIG_NAME + LBR 
										+ SOURCE_DB + " CHAR(40)" + RBR;
	/** MySQL query constant. */
	private static final String TABLE_EXIST = "SHOW TABLES LIKE ";
	
	/**
	 * Returns whether the tables are set for the given name of a database
	 * which is operated on (like SkyServer).
	 * 
	 * @param name name of the database which is checked (like SkyServer)
	 * @return whether the tables are set for the given name of a database
	 */
	protected boolean tablesAreSet(String name) {
		assert (name != null);
		
		// getting Connection and statement
		Connection c = getConnection();
		Statement s = getStatement(c);
		if (s == null) {
			Printer.pfail("Getting statement.");
			close(c);
			return false;
		}
		
		
		
		// if the table doesn't exist we create it and store the value for this configuration and set it false
		if (!(tableExists(s, CONFIG_NAME))) {
			try {
				s.executeUpdate(CONFIG_TABLE);
				Printer.psuccess("New configuration table created.");
				s.execute(INSERT + CONFIG_NAME + LBR + SOURCE_DB + RBR
						+ VALUES + LBR + APOS + config.getNameOfDB() + APOS + RBR);
			} catch (SQLException e1) {
				Printer.perror("Creating config table not possible.");
			}
			close(s, c);
			return false;
		} 
		
		// if it exists we try to get an answer
		ResultSet re = null;
		try {
			s.execute(SELECT + SOURCE_DB + FROM + CONFIG_NAME + WHERE + SOURCE_DB + EQL + APOS + name + APOS);
			re = s.getResultSet();
			if (re.next()) {
				close(s, c);
				return true;
			} else {
				close(s, c);
				return false;
			}
			
		} catch (SQLException e) {
			Printer.pfail("Check whether it is set.");
		}
		
		close(s, c);
		return false; 
	}
	
	/**
	 * Returns whether the table with the given name exists.
	 * 
	 * @param s Statement to operate with
	 * @param name of the table which is checked
	 * @return whether the table with the given name exists
	 */
	private boolean tableExists(Statement s, String name) {
		assert (s != null);
		assert (name != null);
		
		ResultSet res = null;
		try {
			res = s.executeQuery(TABLE_EXIST + APOS + name + APOS);
			if (res.next()) {
				return true;
			}
		} catch (SQLException e) {
			Printer.perror("Checking whether table exists.");
		}
		
		return false;
	}

	/**
	 * Stores a database (table on which is operated, like SkyServer) 
	 * as initialized by storing it in a table.
	 * 
	 * @param name Name of the database which shall be stored
	 */
	protected void storeDataBase(String name) {
		assert (name != null);
		
		// getting Connection and statement
		Connection c = getConnection();
		Statement s = getStatement(c);
		if (s == null) {
			Printer.pfail("Getting statement.");
			close(c);
		}
		
		
		// try to update
		
		try {
			String str = INSERT + CONFIG_TABLE + LBR + SOURCE_DB + RBR + VALUES + LBR + APOS + name + APOS + RBR;
			Printer.ptest("Table storing query:" + str);
			s.executeUpdate(str);
		} catch (SQLException e) {
			Printer.pfail("Storing table: " + name);
		}
		
	}
	
}
