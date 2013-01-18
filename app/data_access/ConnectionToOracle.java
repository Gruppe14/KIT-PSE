package data_access;

import oracle.olapi.data.source.DataProvider;
import oracle.olapi.session.UserSession;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDataSource;

import java.sql.SQLException;

public class ConnectionToOracle {
	
	private OracleConnection conn = null;
	private OracleDataSource dataSource = null;
	private DataProvider dataprovider;
	
	public ConnectionToOracle() {
		
		try
		{
		         this.dataSource = new OracleDataSource();
		         this.dataSource.setURL(props.getProperty("url"));
		         this.dataSource.setUser(props.getProperty("username"));
		         this.dataSource.setPassword(props.getProperty("password");
		         this.conn = (OracleConnection) this.dataSource.getConnection();
		         
		} catch(SQLException e) {
			
		            System.out.println("Connection attempt failed. " + e);
		}
		
	}
	
	public void createUserSession() {
		
		this.dataprovider = new DataProvider();
		
		try {
			
		     UserSession session = this.dataprovider.createSession(conn);
		     
		} catch(SQLException e) {
			
		      System.out.println("Could not create a UserSession. " + e);
		      
		}
	}
	
	public DataProvider getDataProvider() {
		
		return this.dataprovider;
	}

}
