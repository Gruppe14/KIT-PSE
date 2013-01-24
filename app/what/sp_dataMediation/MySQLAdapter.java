package what.sp_dataMediation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import what.sp_parser.DataEntry;

public class MySQLAdapter {
	
	private static final String SELECT = "SELECT ";
	private static final String FROM = " FROM ";
	private static final String WHERE = " WHERE ";	
	private static final String GROUPBY = " GROUP BY ";
	private static final String AND = " AND ";
	private static final String OR = " OR ";
	private static final String EQL = " = ";
	private static final String KOMMA = ",  ";
	private static final String LBR = " ( ";
	private static final String RBR = " ) ";
	
	private WHConnectionManager whConnections = new WHConnectionManager();

	protected MySQLAdapter() {
	}

	protected boolean loadEntries(Collection<DataEntry> tbl) {
		assert (tbl != null);
		
		//TODO blabla
		return false;
	}

	protected ResultSet requestTable(String x, String xTable, String measure, String factTable, HashMap<String, TreeSet<String>> filters) {
		assert (x != null);
		assert (xTable != null);
		assert (filters != null);
		assert (measure != null);
		assert (factTable != null);
		
		String query = "" + SELECT + x + KOMMA + measure
						  + FROM + xTable + KOMMA + factTable
						  + WHERE; 
		

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
		
		query += GROUPBY + x;
		
		
		ResultSet result = executeRequest(query);
		if (result == null) {
			System.out.println("ERROR: No ResultSet returned...");
		}
		
		
		return result;
	} 

	
	protected ResultSet requestStringsWithParent(String child, String parentType, String parentFilter, String table) {
		assert (parentFilter != null);
		assert (table != null);
		assert (child != null);
		
		String query = "" + SELECT + child + FROM + table
						+ WHERE + parentType + EQL + parentFilter;
		
		ResultSet result = executeRequest(query);
		if (result == null) {
			System.out.println("ERROR: No ResultSet returned...");
		}
		
		return null;
	}

	
	private ResultSet executeRequest(String query) {
		assert (query != null);
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
			
		Statement s;
		try {
			s = c.createStatement();
		} catch (SQLException e) {
			System.out.println("ERROR: Creating statement not possible!");
			e.printStackTrace();
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
		
		whConnections.returnConnectionToPool(c);
		
		return re;
	}
}
