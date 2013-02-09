package what.sp_dataMediation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import org.json.JSONObject;

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
	
	private static final String SELECT = "SELECT ";
	private static final String FROM = " FROM ";
	private static final String WHERE = " WHERE ";	
	private static final String GROUPBY = " GROUP BY ";
	
	private static final String AND = " AND ";
	private static final String OR = " OR ";
	private static final String EQL = " = ";
	
	private static final String LBR = " ( ";
	private static final String KOMMA = ",  ";
	private static final String RBR = " ) ";
	
	private static final String INSERT = "INSERT INTO ";
	private static final String VALUES = "VALUES ";
	
	private static final String APOS = "'";
	private static final String DOT = ".";
	private static final String SPACE = " ";
	
	/** Cached string for the fact query */
	//private String factQueryStart;
	
	private WHConnectionManager whConnections = new WHConnectionManager();

	private ConfigWrap config;
	
	/**
	 * Public constructor for a new MySQLAdapter
	 * for a given ConfigWrap.
	 * 
	 * @param config given configuration on which the work bases
	 */
	protected MySQLAdapter(ConfigWrap config) {
		assert (config != null);
		
		this.config = config;

	}

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
		// get last to set , at right positions
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
				System.out.println("Try to execute: " + curQuery);
				
				Connection c = whConnections.getConnectionFromPool(); 
				Statement s = getStatement(c);
				
				int key = getKeyOfUpload(curQuery, s); 
				whConnections.returnConnectionToPool(c);
				
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
					
					key = executeKeyRequest(keyQuery);
	
					if (key <= 0) {
						System.out.println("ERROR: Receiving a key failed for " + keyQuery);
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

	
	// super complex computing of queries...
	/**
	 * Loads a DataEntry into the warehouse. 
	 * 
	 * @param de DataEntry to be uploaded
	 * @return whether it was successful
	 *
	protected boolean loadEntry(DataEntry de) {
		assert (de != null);
		
		// counts the position in the info array of the data entry
		int counter = 0;
		
		
		// query for the fact table
		String factQuery = getQueryStartFactTable();
		String factValues = "";
		
		ArrayList<DimRow> dims = config.getDims();
		// get last to set , at right positions
		DimRow lastDim = dims.get(dims.size() - 1); 
		for (DimRow d : dims) {
			if (d.isDimension()) { // case it's a dimension
				
				// test if key exists
				String keyQuery = SELECT + d.getTableKey() + FROM + d.getTableName() + WHERE;
					
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
					
				int key = executeKeyRequest(keyQuery);
				
				
				if (key <= 0) {
					
					String dimQuery = INSERT + d.getTableName() + LBR ;
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
					System.out.println("Try to execute: " + curQuery);
				
					Connection c = whConnections.getConnectionFromPool(); 
					Statement s = getStatement(c);
				
					key = getKeyOfUpload(curQuery, s); 
					whConnections.returnConnectionToPool(c);
				
					if (key <= 0) {
						System.out.println("ERROR: Receiving a key failed for " + keyQuery);
						return false;
					}					
				}
				
				
				factValues += "" + key;
				
				
				
			} else { // case it is fact
				factValues += de.getInfo(counter);
				counter++;		
			}
			
			// not last one -> ,
			if (!d.equals(lastDim)) {
				factValues += KOMMA;
			}
		
		}
		
		
		// run fact query
		String curQuery = factQuery + factValues + RBR;	
		return executeUpload(curQuery);
	}
	
	
	private String getQueryStartFactTable() {
		if (factQueryStart == null) {
			factQueryStart = computeQueryFactTable();
		}
		return factQueryStart;
	}

	private String computeQueryFactTable() {
		// query for the fact table
		String factQuery = INSERT + config.getFactTableName() + LBR;
			
		ArrayList<DimRow> dims = config.getDims();
		// get last to set , at right positions
		DimRow lastDim = dims.get(dims.size() - 1); 
		for (DimRow d : dims) {
			if (d.isDimension()) { // case it's a dimension					
				factQuery += d.getTableKey();					
			} else { // case it is fact
				factQuery += d.getRowNameOfLevel(0);				
			}
					
			// not last one -> ,
			if (!d.equals(lastDim)) {
				factQuery += KOMMA;
			}
				
		}
				
		return factQuery + RBR + VALUES + LBR;
	}

	*/


	/**
	 * NOT FINISHED
	 * Returns a JSONObject with two rows containing for a given x from table xTable with key xKey
	 * the measure (including aggregation) with given filters.
	 * 
	 * @param x for x
	 * @param xTable from the table
	 * @param xKey with the key
	 * @param measure for the measure
	 * @param filters with the filters
	 * @return a JSONObject as a result of the given parameters
	 */
	protected JSONObject requestTable(String x, String xTable, String xKey, String measure,  HashMap<String, TreeSet<String>> filters) {
		assert (x != null);
		assert (xTable != null);
		assert (filters != null);
		assert (measure != null);
		
		String query = "" + SELECT + x + KOMMA + measure
						  + FROM + xTable + KOMMA + config.getFactTableName()
						  + WHERE + xTable + DOT + xKey + EQL + config.getFactTableName() + DOT + xKey; 
		
		/* TODO check out how queries have to be...
		TreeSet<String> keys = (TreeSet<String>) filters.keySet();		
		String last = keys.last();
		
		for (String s : keys) {
			query += LBR;
			
			TreeSet<String> values = filters.get(s);
			String lastVal = values.last();
			
			for (String t : values) {
				query += LBR + s + EQL + t;								
				
				if (lastVal.equals(t)) {
					query += RBR;
				} else {
					query += RBR + OR;
				}
			}
			
			if (last.equals(s)) {
				query += RBR;
			} else {
				query += RBR + AND;
			}			
		}
		*/
		query += GROUPBY + x;
		
		
		JSONObject result = executeRequest(query, x, measure);
		if (result == null) {
			System.out.println("ERROR: No File returned...");
		}
		
		return result;
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
	
	private TreeSet<String> requestStringSet(String query) {
		assert (query != null);
		
		Connection c = getConnection();
		Statement s = getStatement(c);
		if (s == null) {
			System.out.println("ERROR: Opening statement failed...");
			return null;
		}
		
		ResultSet re = null;
		try {
			re = s.executeQuery(query);
			
		} catch (SQLException e) {
			System.out.println("ERROR: Executing query not possible:\n >> " + query + "<<");
			e.printStackTrace();
			return null;
		}
		
		TreeSet<String> requested = DataChanger.getTreeSetFromRS(re);
		
		if (requested == null) {
			System.out.println("ERROR: Reading from result set failed.");
		}
		
		try {
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERROR: Closing statement failed...");
		}
		
		whConnections.returnConnectionToPool(c);
		
		return requested;		
	}
	
		
	private boolean executeUpload(String query) {
		assert (query != null);
		
		Connection c = getConnection();

		Statement s = getStatement(c);
		
		if (s == null) {
			System.out.println("ERROR: Did not get a Statement");
			return false;
		}
		
		System.out.println("Try to execute: " + query);
		
		try {
			s.executeUpdate(query);
			s.close();
		} catch (SQLException e) {
			System.out.println("ERROR: Executing query not possible:\n >> " + query + "<<");
			e.printStackTrace();
			return false;
		}
		
		whConnections.returnConnectionToPool(c);
		
		return true;
	}
	
	private JSONObject executeRequest(String query, String x, String measure) {
		assert (query != null);
		
		Connection c = getConnection();
		Statement s = getStatement(c);
		if (s == null) {
			return null;
		}
		
		System.out.println("Try to execute: " + query);
		
		ResultSet re = null;
		JSONObject json;
		try {
			re = s.executeQuery(query);
			json = DataChanger.getFileFromResultSet(re, x, measure);
			s.close();
		} catch (SQLException e) {
			System.out.println("ERROR: Executing query not possible:\n >> " + query + "<<");
			e.printStackTrace();
			return null;
		}
		
		whConnections.returnConnectionToPool(c);
		
		return json;
	}
	
	private Statement getStatement(Connection c) {
		Statement s;
		try {
			s = c.createStatement();
			return s;
		} catch (SQLException e) {
			System.out.println("ERROR: Creating statement not possible!");
			e.printStackTrace();
			return null;
		}
	}

	private int executeKeyRequest(String query) {
	assert (query != null);
		
		Connection c = getConnection();
		Statement s = getStatement(c);
		int key = -1;
		
		if (s == null) {
			return 0;
		}
		
		System.out.println("Try to execute: " + query);
		
		ResultSet res = null;
		try {
			res = s.executeQuery(query);
			if (res.next()){
	            key = res.getInt(1);
	        }
			res.close();
		} catch (Exception e) {
			System.out.println("ERROR: Something wrong with the result set.");
			//e.printStackTrace();
		}
		
		// close statement and put connection back	
		try {	
			s.close();
		} catch (SQLException e) {
			System.out.println("ERROR: Executing query not possible:\n >> " + query + "<<");
			e.printStackTrace();
			return 0;
		}
		whConnections.returnConnectionToPool(c);
		
		return key;
	}

 	private Connection getConnection() {
		Connection c = null;
		do {
			c = whConnections.getConnectionFromPool();
				
			if (c == null) {
				try {
					wait(30);
				} catch (InterruptedException e) {
					// nothing to do here... it's ok if we get woken up
				}
			}
		} while (c == null) ;
		
		return c;
	}

	private int getKeyOfUpload(String query, Statement s) {
		assert (query != null);
		assert (s != null);
		
		int key = -1;
		
		try {
			s.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = s.getGeneratedKeys();
			if (rs.next()){
		            key = rs.getInt(1);
		        }
		    rs.close();

		} catch (SQLException e) {
			System.out.println("ERROR: Failed executing query and get key.");
		}

		      
        try {
			s.close();
		} catch (SQLException e) {
			System.out.println("ERROR: Failed closing statement.");
		}
        
		return key;
	}
	

	
}
















