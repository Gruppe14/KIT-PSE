package what.sp_dataMediation;

import java.sql.*;
import java.util.Vector;

class WHConnectionManager {
	
    private static final String databaseUrl = "jdbc:mysql://localhost:3306/WHATDataWarehouse";
    private static final String userName = "masteWHAT";
    private static final String password = "whatUP";
    private static final int MAX_POOL_SIZE = 5;

    Vector<Connection> connectionPool = new Vector<Connection>();
    
    public WHConnectionManager() {
        initialize();
    }

    private void initialize() {
        //Here we can initialize all the information that we need
        initializeConnectionPool();
    }

    private void initializeConnectionPool()  {
    	
        while(!checkIfConnectionPoolIsFull()) {
            System.out.println("Connection Pool is NOT full. Proceeding with adding new connections");

            connectionPool.addElement(createNewConnectionForPool());
        }
        
        System.out.println("Connection Pool is full.");
    }

    private synchronized boolean checkIfConnectionPoolIsFull() {
        
        return !(connectionPool.size() <  MAX_POOL_SIZE);
    }

    //Creating a connection
    private Connection createNewConnectionForPool() {
        Connection connection = null;
      
        try {        	
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(databaseUrl, userName, password);
            System.out.println("Connection: "+connection);
        } catch(SQLException sqle) {
            System.err.println("SQLException: "+sqle);
            return null;
        } catch(ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException: "+cnfe);
            return null;
        }

        return connection;
    }

    public synchronized Connection getConnectionFromPool() {
        Connection connection = null;

        //Check if there is a connection available. 
        // There are times when all the connections in the pool may be used up
        if(connectionPool.size() > 0) {
            connection = (Connection) connectionPool.firstElement();
            connectionPool.removeElementAt(0);
        }
        //Giving away the connection from the connection pool
        return connection;
    }

    protected synchronized void returnConnectionToPool(Connection connection) {
        //Adding the connection from the client back to the connection pool
        connectionPool.addElement(connection);
    }


}