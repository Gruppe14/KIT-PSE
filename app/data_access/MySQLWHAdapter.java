package data_access;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;



/*
 * hier, kann man query schicken 
 * 
 */

public class MySQLWHAdapter {
	
	private ResultSet set = null;
    private WareHouseConfiger wh;
    
    
	public MySQLWHAdapter () {
		
	  this.wh = new WareHouseConfiger();
		
	}
	
	
	public boolean requestDataFromWH(String x, String dim, String measure, HashMap<String, TreeSet<String>> filters) {
		/*
		String sql = "SELECT" + x + "," + measure + "FROM" + dim + "," + "query_entry " +
				     "WHERE ";
		
		HashMap hm = filters;
		
		Iterator i = hm.entrySet().iterator();
		Set  j;
		
		String sy = "(query_entry." + x + "_id = " + dim + x + "_id)" ;
		String k;
		
		while (i.hasNext()) {
			
			Map.Entry me = (Map.Entry) i.next();
			
			k = (String) me.getKey();
			
			sy = "(" + k;
			
			j = (Set) me.getValue();
			
			while (j.iterator().hasNext()) {
				
				sy += "=" + (String) j.iterator().next() + "OR" + k;
				
			}
			
			sy += ") AND ";
            
           			
		}
		
		sql += sy + " GROUB BY " + x;
		
		
		try {
			
			Statement stmt = (Statement) this.wh.getConnecationWH()).createStatement();
			this.set = (ResultSet) stmt.executeQuery(sql);
			
			this.wh.closeConnectionMySQL();
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
 */
		return false;
	}
	
	
	public ResultSet getResult() {
		
		return this.set;
	}
	

} 


















