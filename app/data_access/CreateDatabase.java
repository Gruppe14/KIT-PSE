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
    
    
    public CreateDatabase(String username, String password) {
    	
    	this.Username = username;
    	this.Password = password;
    	this.URL = "jdbc:mysql://localhost:3306/olapwhat";
    	
    	this.makeConnection();
    	
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
    
    public void createDatabaseName(String name) {
    	
       try {
				    
    	    Statement stmt = (Statement) this.conn.createStatement();
			
			String query = "CREATE DATABASE " + name;

			stmt.executeUpdate(query);
			      
			      this.newName = name;
		    	  this.newDBUrlName();
			      this.closeConnection();
			      this.makeConnection();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("iam hier");
			
		}
    	
    }
    
    private void newDBUrlName() {
    	
    	this.URL = "jdbc:mysql://localhost:3306/" + this.newName;
    }
    
    public void createTimeTabele() {
    	
    	try {
    		
    		Statement stmt = (Statement) this.conn.createStatement();
    		
    		String table = 
    				       " create table `time_dim` ( " +
    				       " `time_id` int (11), " +
    				       " `year` year (4), " +
    				       " `month` int (11), " +
    				       " `day` int (11), " +
    				       " `hour` int (11), " +
    				       " `elapse time` int (11), " +
    				       " `cpu time` int (11), " +
    				       " `rows time` int (11) " +
    				       " );" ;
    		
    		stmt.execute(table);
    		
    	} catch(SQLException e2) {
    		
    	}
    	
    }
    
    public void createDBTabele() {
    	
      try {
    		
    		Statement stmt = (Statement) this.conn.createStatement();
    		
    		String table = 
    				       " create table IF NOT EXISTS `db_dim` ( " +
    				       " `db_i` int (11), " +
    				       " `database` varchar (765), " +
    				       " `elapse time` int (11), " +
    				       " `cpu time` int (11), " +
    				       " `rows` int (11));" ;
    		
    		stmt.execute(table);
    		
    	} catch(SQLException e3) {
    		
    	}
    	
    }
    
    public void createServerTabele () {
    	
       try {
    		
    		Statement stmt = (Statement) this.conn.createStatement();
    		
    		String table = 
    				       " create table IF NOT EXISTS `server_dim` ( " +
    				       " `server_id` int (11), " +
    				       " `type` varchar (765), " +
    				       " `elapse time` int (11), " +
    				       " `cpu time` int (11), " +
    				       " `rows` int (11)" +
    				       ");";
    		
    		stmt.execute(table);
    		
    	} catch(SQLException e3) {
    		
    	}
    	
    }
    
    
    public void createLocationTabele () {
    	
     try {
    		
    		Statement stmt = (Statement) this.conn.createStatement();
    		
    		String table = 
    				       " CREATE TABLE  location_dim ( " +
    				       " `location_id` int (11), " +
    				       " `country` varchar (765), " +
    				       " `city` varchar (765), " +
    				       " `elapse time` int (11), " +
    				       " `cpu time` int (11), " +
    				       " `rows` int (11)" +
    				       ");" ;
    		
    		stmt.execute(table);
    		
    	} catch(SQLException e3) {
    		
    	}
    }
    
    public void createQueryEntryTabele () {
    	
        try {
       		
       		Statement stmt = (Statement) this.conn.createStatement();
       		
       		String table = 
       				       " CREATE TABLE IF NOT EXISTS query_entry ( " +
       				       " time_id int (11), " +
       				       " location_id int (11), " +
       				       " db_id int (11), " +
       				       " server_id int (11) " +
       				       ");" ;
       		
       		stmt.execute(table);
       		
       	} catch(SQLException e3) {
       		
       	}
       }
    
    public void closeConnection() {
		
		if (this.conn != null) {
            try {
                this.conn.close();
            } catch (Exception e) {
            }
        }
		
	}
    
    
   /* 
    public static void main(String[] args) {
    	CreateDatabase g = new CreateDatabase("root", "password");
    	
		
	   } */
    
}
