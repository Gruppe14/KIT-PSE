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
	
	
	public void insertINTable(int year, int month, int day, int hour, int min, int sec,
			                       String ip, String server, String db,
			                       float elapsed_time, float cpu_time, int rows, String type,
			                       String city, String country
			                       ) {
		
          this.insertDim_Location(country, city);
          this.insertDim_server_db(server, db);
          this.insertDim_type(type);
          this.insertInTime(year, month, day, hour, min, sec);
          this.insertMeasure(elapsed_time, cpu_time, rows, type);
          
		
	}
	
	
	private void insertDim_Location(String country, String city) {
		   
		
		   String sql  = "INSERT INTO dim_location(country, city)" +
		   		         "VALUES('"+country+"', '"+city+"');";
		   String q    = "INSERT INTO fact_skyserver(location_key) " +
                         "SELECT location_key FROM dim_location " +
                         "WHERE city = '"+city+"' AND country = '"+country+"' LIMIT 1;";
		   
		   try {
			   
			Statement stmt = (Statement) this.conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.executeUpdate(q);
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error1");
		}
	}
	
	private void insertInTime(int year, int month, int day, int hour, int min, int sec) {
		
		try {
			Statement stmt = (Statement) this.conn.createStatement();
			
			String sql = "INSERT INTO time_dim(year, month, day_t, hour, min, sec) " +
				         "VALUES('"+year+"', '"+month+"', '"+day+"', '"+hour+"', '"+min+"', '"+sec+"') ";
			
			String q  =  "INSERT INTO fact_skyserver(time_key)" +
					     "SELECT time_key FROM dim_time " +
                         "WHERE  year = '"+year+"' LIMIT 1;";
			
			stmt.executeUpdate(sql);
			stmt.execute(q);
		    stmt.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error2");
		}
		
	}
	
    private void insertDim_server_db(String server, String db) {
		
		try {
			
			Statement stmt = (Statement) this.conn.createStatement();
			
			String sql     = "INSERT INTO dim_server_db(row_server, row_db) " +
				             "VALUES('"+server+"', '"+db+"') ";
			
			String q       = "INSERT INTO fact_skyserver(server_db_key)" +
				             "SELECT time_key FROM dim_time " +
                             "WHERE  row_server = '"+server+"' AND  + row_database = '"+db+"' LIMIT 1;";
			
			
			stmt.executeUpdate(sql);
			stmt.execute(q);
		    stmt.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error3");
		}
		
	}
    
   private void insertDim_type(String type) {
		
		try {
			
			Statement stmt = (Statement) this.conn.createStatement();
			
			String sql     = "INSERT INTO dim_type(row_type) " +
				             "VALUES('"+type+"') ";
			
			String q       = "INSERT INTO fact_skyserver(type_key)" +
				             "SELECT dim_type FROM dim_time " +
                             "WHERE  row_type = '"+type+"' LIMIT 1;";
			
			
			stmt.executeUpdate(sql);
			stmt.execute(q);
		    stmt.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error4");
		}
		
	}
	
	private void insertMeasure(float elapsed_time, float cpu_time, int rows, String type) {
		
       try {
			
			Statement stmt = (Statement) this.conn.createStatement();
			
			String sql     = "INSERT INTO fact_skyserver(row_elapsed, row_busy, row_rows, row_type) " +
				             "VALUES('"+elapsed_time+"', '"+cpu_time+"', '"+rows+"', '"+type+"') ";
			
			
			stmt.executeUpdate(sql);
		    stmt.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error5");
		}

	}
	
	public Connection getConnection() {
		return this.conn;
	}
	
	 

}
