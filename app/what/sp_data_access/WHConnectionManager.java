package what.sp_data_access;

// sql imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// java imports
import java.util.Vector;

//intern imports
import what.Printer;

/**
 * WHConnectionManager manages connections,
 * or better said just a pool of connections.
 * 
 * If you take one, please put it back.
 * 
 * @author Jonathan, PSE Gruppe 14,   (Sridhar M. S. http://www.developer.com/java/data/article.php/3847901/
 * 										Implement-Java-Connection-Pooling-with-JDBC.htm)
 */
public class WHConnectionManager {
	
	/** MySQL path constant. */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/WHATDataWarehouse";
    /** MySQL name constant. */
    private static final String USER_NAME = "masteWHAT";
    /** MySQL password constant. */
    private static final String PW = "whatUP";
    /** Pool size constant. */
    private static final int MAX_POOL_SIZE = 8;

    /** Connection pool. */
    private Vector<Connection> connectionPool = new Vector<Connection>();
    
    /** Connection pool non auto-committing connections. */
    private Vector<Connection> poolNoAutoCommitting = new Vector<Connection>();
    
    /**
     * Protected constructor for a WHConnectionMangaer.
     */
    protected WHConnectionManager() {
        initialize();
    }

    // -- INIT -- INIT -- INIT -- INIT -- INIT -- INIT -- INIT --
    /**
     * Initializes the pool.
     */
    private void initialize() {
        //Here we can initialize all the information that we need
        initializeConnectionPool();
    }

    /**
     * Initializes the pool.
     */
    private void initializeConnectionPool()  {
    	
    	int i = 0;
    	
        while ((!checkIfConnectionPoolIsFull()) && (i < MAX_POOL_SIZE * MAX_POOL_SIZE)) {
        	Connection c = createNewConnectionForPool();
        	if (c != null) {
        		connectionPool.addElement(c);
        	}
        	
        	//Printer.ptest("Normal pool size: " + connectionPool.size());
        	i++;
        }
        
        Printer.ptest("Connection Pool is full.");
        
        i = 0;
        while ((!checkIfNotAutoComPoolIsFull()) && (i < MAX_POOL_SIZE * MAX_POOL_SIZE)) {
        	Connection cs = createNewNoAutoComCon();
        	if (cs != null) {
        		poolNoAutoCommitting.addElement(cs);
        	}
        	
        	//Printer.ptest("No auto committing pool size: " + poolNoAutoCommitting.size());
        	i++;
        }
        
        Printer.ptest("Connection Pool is full.");
    }

    /**
     * Returns whether the connection pool is full.
     * 
     * @return whether the connection pool is full
     */
    private synchronized boolean checkIfConnectionPoolIsFull() {
        
        return !(connectionPool.size() <  MAX_POOL_SIZE);
    }

    /**
     * Creates a new connection for the pool.
     * 
     * @return new connection for the pool
     */
    private Connection createNewConnectionForPool() {
        Connection connection = null;
      
        try {        	
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER_NAME, PW);
        } catch (SQLException sqle) {
        	Printer.perror("SQLException: " + sqle);
            return null;
        } catch (ClassNotFoundException cnfe) {
            Printer.perror("ClassNotFoundException: " + cnfe);
            return null;
        }

        return connection;
    }
  
    /**
     * Returns whether the no auto-committing connection pool is full.
     * 
     * @return whether the no auto-committing connection pool is full
     */
    private synchronized boolean checkIfNotAutoComPoolIsFull() {
        
        return !(poolNoAutoCommitting.size() <  MAX_POOL_SIZE);
    }

    /**
     * Creates a new no auto-committing connection for the pool.
     * 
     * @return new connection for the pool
     */
    private Connection createNewNoAutoComCon() {
        Connection connection = null;
      
        try {        	
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER_NAME, PW);
            connection.setAutoCommit(false);
            //Printer.print("Connection: " + connection);
        } catch (SQLException sqle) {
        	Printer.perror("SQLException: " + sqle);
            return null;
        } catch (ClassNotFoundException cnfe) {
            Printer.perror("ClassNotFoundException: " + cnfe);
            return null;
        }

        return connection;
    }
      
    // -- GET AND BRING BACK -- GET AND BRING BACK -- GET AND BRING BACK -- GET AND BRING BACK --
    /**
     * Returns a connection from the pool if one is available.
     * 
     * @return connection from the pool
     */
    protected synchronized Connection getConnectionFromPool() {
        Connection connection = null;

        //Check if there is a connection available. 
        // There are times when all the connections in the pool may be used up
        if (connectionPool.size() > 0) {
            connection = (Connection) connectionPool.firstElement();
            connectionPool.removeElementAt(0);
        }
        //Giving away the connection from the connection pool
        // Printer.print("Connection taken from pool. Size of pool: " + connectionPool.size());
        return connection;
    }   
    
    /**
     * Returns a connection to the pool.
     * 
     * @param connection connection to be returned
     */
    protected synchronized void returnConnectionToPool(Connection connection) {
        //Adding the connection from the client back to the connection pool
        connectionPool.addElement(connection);
       // Printer.print("Connection returned to pool. Size of pool: " + connectionPool.size());
    }
    
    /**
     * Returns a no auto-committing connection from the pool if one is available.
     * 
     * @return no auto-committing connection from the pool
     */
    protected synchronized Connection getNoAutoComConnection() {
        Connection connection = null;

        //Check if there is a connection available. 
        // There are times when all the connections in the pool may be used up
        if (poolNoAutoCommitting.size() > 0) {
            connection = (Connection) poolNoAutoCommitting.firstElement();
            poolNoAutoCommitting.removeElementAt(0);
        }
        //Giving away the connection from the connection pool
        // Printer.print("Connection taken from pool. Size of pool: " + connectionPool.size());
        return connection;
    }   
    
    /**
     * Returns a no auto-committing connection to the pool.
     * 
     * @param connection no auto-committing connection to be returned
     */
    protected synchronized void returnNoAutoComConnection(Connection connection) {
        //Adding the connection from the client back to the connection pool
    	poolNoAutoCommitting.addElement(connection);
       // Printer.print("Connection returned to pool. Size of pool: " + connectionPool.size());
    }

    // -- RELEASE -- RELEASE -- RELEASE -- RELEASE -- RELEASE --
    /**
     * Closes all connections in the pools.
     */
    protected synchronized void releaseConnections() {
    	for (Connection c : connectionPool) {
    		try {
				c.close();
			} catch (SQLException e) {
				Printer.pproblem("Closing connection of normal pool.");
			}
    	}
    	
    	for (Connection c : poolNoAutoCommitting) {
    		try {
				c.close();
			} catch (SQLException e) {
				Printer.pproblem("Closing connection of no auto committing pool.");
			}
    	}
    	
    	Printer.ptest("Connections released.");
    }
    
    
    
}