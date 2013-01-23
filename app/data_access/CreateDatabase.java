package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/*
 * das klasse ist fuer Adminbereich
 * Er kann neue database mit neue tabellen
 * aber die ist noch nicht fertig
 */

public class CreateDatabase {
	
    private String Username;
    private String Password;
    private String Driver = "com.mysql.jdbc.Driver";
    private String URL;
    private String newName;
    private Connection conn;
    
    
    public CreateDatabase(String newName, String username, String password) {
    	
    	this.Username = username;
    	this.Password = password;
    	this.newName  = newName;
    	this.URL = "jdbc:mysql://localhost:3306/olapwhat";
    	this.makeConnection();
    	this.createDatabaseName();
    	this.createTimeTabele();
    	this.createDBTabele();
    	this.createLocationTabele ();
    	this.createQueryEntryTabele ();
    	this.closeConnection();
    	
    }
    
    private boolean makeConnection() {
		
    	try {
                 Class.forName(this.Driver);
                 this.conn = DriverManager.getConnection(this.URL,
                           this.Username, this.Password);
                 return true;
                 
            } catch (Exception e) {
                  System.out.println("No Connection are allowd!");
                  return false;
            }
		
    }
    
    private boolean createDatabaseName() {
    	
       try {
				    
    	                Statement stmt = (Statement) this.conn.createStatement();
			
			String query = "CREATE DATABASE " + this.newName;

			stmt.executeUpdate(query);
			      
			      this.closeConnection();
			      this.URL = "jdbc:mysql://localhost:3306/" + this.newName;
			      this.makeConnection();
			      
			      return false;
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			return false;
			
		}
    	
    }
    
    
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
    
    
   /* 
    public static void main(String[] args) {
    	
    	
		
	   } */
    
}
