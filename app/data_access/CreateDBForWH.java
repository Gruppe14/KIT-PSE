package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDBForWH {
	
    private String Username;
    private String Password;
    private String databaseName;
    private String Driver = "com.mysql.jdbc.Driver";
    private String URL = "jdbc:mysql://localhost:3306/";
    private Connection conn;
	
	
	public CreateDBForWH(String databaseName, String Username, String Password) {
		
		this.databaseName = databaseName;
		this.Username = Username;
		this.Password = Password;
		this.makeConnection();
		
	}
	
	public boolean makeConnection() {
		
		try {
            Class.forName(this.Driver).newInstance();
            this.conn = DriverManager.getConnection(this.URL + this.databaseName,
                    this.Username, this.Password);
            return true;
        } catch (Exception e) {
        
           return false;
        }
		
	}
	
	
	public boolean insertINTable(int year, int month, int day, int hour, int min, int sec,
			                       String ip, String servername, String databasename,
			                       float elapsed_time, float cpu_time, int rows,
			                       String city, String country
			                       ) {
		
          try {
    	 
    	        Statement stmt = (Statement) this.conn.createStatement();
			
		   String query = "INSERT INTO db_dim(databasename, servername) " +
					       "VALUES('"+databasename+"', '"+servername+"') " ;
                   String query1 = "INSERT INTO location_dim(ipLocation, country, city) " +
					       "VALUES('"+ip+"', '"+country+"', '"+city+"') " ;
		   String query2 = "INSERT INTO query_entry(elapsed_time, cpu_time, rows_entry) " +
					       "VALUES("+elapsed_time+", "+cpu_time+", "+rows+") ";
		   String query3 = "INSERT INTO time_dim(year, month, day_t, hour, min, sec) " +
					       "VALUES("+year+", "+month+","+day+", "+hour+", "+min+", "+sec+") ";
			
			      stmt.executeUpdate(query);
			      stmt.executeUpdate(query1);
			      stmt.executeUpdate(query2);
			      stmt.executeUpdate(query3);
			      stmt.close();
		return true;
			      
	    } catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return false;
	    }
		
	}
	
	public void insertIDs(int time_id, int location_id, int db_id) {
		
		 try {
	    	 
	    	       Statement stmt = (Statement) this.conn.createStatement();
	    	       String query2 = "INSERT INTO query_entry(time_id, location_id, db_id)" +
	    			        "VALUES("+time_id+", "+location_id+", "+db_id+")" ;
	    	       stmt.executeUpdate(query2);
	    	 
		 } catch (SQLException e2) {
				// TODO Auto-generated catch block
	        	e2.printStackTrace();
		 }
	    	 
	}
	
	public Connection getConnection() {
		return this.conn;
	}
	
	 

}
