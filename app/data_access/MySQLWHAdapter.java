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
		
		String sql = "SELECT " + x + ", " + measure + "\nFROM " + dim + ", " + "query_entry\n" +
				     "WHERE ";
		
		Map <String, TreeSet<String>>  map = filters;
		
		String sy = "(query_entry." + x + "_id = " + dim + "." + x + "_id) \n" ;

		for (String k : map.keySet()) {
		    
			sy += "AND (" + k;
			Iterator i = filters.get(k).iterator();
			
			while (i.hasNext()) {
				
				sy +=  "." + dim + " = " + " '" + (String) i.next() + "' ";
				
				if (i.hasNext()) {
					sy += " OR " + k;
				}
				
			} 
			
			sy += ")\n" ;
            
           			
		} 
		
		sql += sy + "GROUB BY " + x;
		
		
		try {
			
			Statement stmt = (Statement) this.wh.getConnecationWH().createStatement();
			this.set = (ResultSet) stmt.executeQuery(sql);
			
			this.wh.closeConnectionMySQL();
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		} 
		
		
		

	}
	
	
	public ResultSet getResult() {
		
		return this.set;
	}
	

} 


















