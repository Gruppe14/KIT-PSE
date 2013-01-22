package data_access;


import java.sql.ResultSet;



/*
 * hier, kann man query schicken 
 * 
 */

public class MySQLWHAdapter {
	

	public MySQLWHAdapter () {
		
		new WareHouseConfiger();
		
	}
	
	
	public ResultSet requestDataFromWH(String columne, String table, String filter) {
		
		return null;
	}

} 
