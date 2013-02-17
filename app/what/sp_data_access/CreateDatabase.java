package what.sp_data_access;

import java.util.ArrayList;
import java.util.HashMap;

import what.sp_config.ConfigWrap;
import what.sp_config.DimRow;

/**
 * The CreateDatabase class prints a mysql statement from a given config,
 * saying how the database has to look.
 * 
 * NOT FINISHED - WRONG SCHEMA - SEE DATA folder and TableCreationQuery.txt !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * @author Jonathan, Anas
 *
 */
public class CreateDatabase {
	
	private static String ID_PART = " INT AUTO_INCREMENT PRIMARY KEY";
	private CreateDatabase() {
		
	}
    
	/**
	 * Returns a query from a given config how the warehouse has to look and
	 * rows and tables have to be named.
	 * 
	 * @param config config for which a schema will be created
	 * @return statement describing the schema of the warehouse
	 */
    public static String getDataBaseQuery(ConfigWrap config) {
    	String query = "CREATE TABLE " + config.getFactTableName() + "(\n ";
    	
    	//query += "ID " + ID_PART + ", \n ";
    	
    	ArrayList<DimRow> dims = config.getDims();
    	
    	HashMap<String, String> keys = new HashMap<String, String>();
    	
    	ArrayList<String> dimQueries = new ArrayList<String>();
    	
    	for (DimRow d : dims) {
    		if (d.isDimension()) {
    			keys.put(d.getTableKey(), d.getDimTableName());
    			dimQueries.add(getTableQueryForDim(d));
    		} else {
    			query += d.getRowNameOfLevel(0) + " " + d.getTableTypeAt(0) + ",\n ";
    		}
    	}
    	
    	// add foreign keys
    	for (String k : keys.keySet()) {
    		query += "FOREIGN KEY (" + k + ") REFERENCES " + keys.get(k) + "(" + k + "),\n ";
    	}
    	
    	query = query.substring(0, query.length()-2);
    	
    	query += "); \n";
    	
    	String all = "";
    	for (String s : dimQueries) {
    		all += s;
    	}
    	
    	all += query;
    	
    	return all;
    }



	private static String getTableQueryForDim(DimRow d) {
		String query = "CREATE TABLE " + d.getDimTableName() + "(\n ";
		
		query += d.getTableKey() + ID_PART;
		
		for (int i = 0, l = d.getSize(); i < l; i++) {
			query += ",\n " + d.getRowNameOfLevel(i) + " " + d.getTableTypeAt(0);
		}
		
		query += "); \n\n";
		
		return query;
	}
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    /**
    private void createTimeTabele() {
    	
    	try {
    		
    		Statement stmt = (Statement) this.conn.createStatement();
    		
    		String table = 
    				       " CREATE TABLE IF NOT EXISTS  `time_dim` ( " +
    				       " `time_id` int (11), " +
    				       " `year` year (4), " +
    				       " `month` int (11), " +
    				       " `day` int (11), " +
    				       " `hour` int (11), " +
    				       " `min` int (11), " +
    				       " `sec` int (11) );" ;
    		
    		stmt.execute(table);
    		
    	} catch(SQLException e2) {
    		
    	}
    	
    }
    
    private void createDBTabele() {
    	
      try {
    		
    		Statement stmt = (Statement) this.conn.createStatement();
    		
    		String table = 
    				       " CREATE TABLE IF NOT EXISTS  `db_dim` ( " +
    				       " `db_i` int (11), " +
    				       " `databasename` varchar (765), " +
    				       " `servername` varchar (765) )";
    		
    		stmt.execute(table);
    		
    	} catch(SQLException e3) {
    		
    	}
    	
    }
    
    
    
    private void createLocationTabele () {
    	
     try {
    		
    		Statement stmt = (Statement) this.conn.createStatement();
    		
    		String table = 
    				       " CREATE TABLE IF NOT EXISTS location_dim ( " +
    				       " `location_id` int (11), " +
    				       " `country` varchar (765), " +
    				       " `city` varchar (765), " +
    				       ");" ;
    		
    		stmt.execute(table);
    		
    	} catch(SQLException e3) {
    		
    	}
    }
    
    private void createQueryEntryTabele () {
    	
        try {
       		
       		Statement stmt = (Statement) this.conn.createStatement();
       		
       		String table = 
       				       " CREATE TABLE IF NOT EXISTS query_entry ( " +
       				       " time_id int (11), " +
       				       " location_id int (11), " +
       				       " db_id int (11), " +
       				       " server_id int (11) " +
       				       " `elapse time` int (11), " +
    				       " `cpu time` int (11), " +
    				       " `rows` int (11)" +
       				       ");" ;
       		
       		stmt.execute(table);
       		
       	} catch(SQLException e3) {
       		
       	}
     }
    
    private void closeConnection() {
		
		if (this.conn != null) {
            try {
                this.conn.close();
            } catch (Exception e) {
            }
		}
			
    }
    
    
    */
}



