package data_access;


import what.sp_parser.DataEntry;
//import what.sp_config.ConfigWrap;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class WareHouseConfiger {
	
	static private CreateDBForWH db;
	//private ConfigWrap config;
	
	public WareHouseConfiger() {
		
		this.db = new CreateDBForWH("whatwarehouse", "root", "password");
		
	}
	
	
	
	
	
	public void buildWareHouse(DataEntry loadMe) {
	

				this.db.insertINTable(
						(int) loadMe.getInfo(0), (int) loadMe.getInfo(1), (int) loadMe.getInfo(2),
						
						(int) loadMe.getInfo(3), (int) loadMe.getInfo(4), (int) loadMe.getInfo(5),
						
						(String) loadMe.getInfo(6), (String) loadMe.getInfo(7), (String) loadMe.getInfo(8),
						
						(float) loadMe.getInfo(9), (float) loadMe.getInfo(10), (int) loadMe.getInfo(11), (String) loadMe.getInfo(12), 
						
						(String) loadMe.getInfo(13), (String) loadMe.getInfo(14)
						);
				
	}
	
        
        
        public void closeConnectionMySQL() throws SQLException {
        	
        	WareHouseConfiger.db.getConnection().close();
        	
        }
        
        public Connection getConnecationWH() {
        	return this.db.getConnection();
        }
	 

}
