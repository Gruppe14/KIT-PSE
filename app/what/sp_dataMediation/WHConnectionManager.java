package what.sp_dataMediation;

// sql imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// java imports
import java.util.Vector;

/**
 * WHConnectionManager manages connections,
 * or better said just a pool of connections.
 * 
 * If you take one, plz put it back.
 * 
 * @author Jonathan,  (Sridhar M. S. http://www.developer.com/java/data/article.php/3847901/Implement-Java-Connection-Pooling-with-JDBC.htm)
 */
public class WHConnectionManager {
	
    private static final String databaseUrl = "jdbc:mysql://localhost:3306/WHATDataWarehouse";
    private static final String userName = "masteWHAT";
    private static final String password = "whatUP";
    private static final int MAX_POOL_SIZE = 8;

    Vector<Connection> connectionPool = new Vector<Connection>();
    
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
    	
        while(!checkIfConnectionPoolIsFull()) {
            System.out.println("Connection Pool is NOT full. Proceeding with adding new connections");

            connectionPool.addElement(createNewConnectionForPool());
        }
        
        System.out.println("Connection Pool is full.");
    }

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
        if(connectionPool.size() > 0) {
            connection = (Connection) connectionPool.firstElement();
            connectionPool.removeElementAt(0);
        }
        //Giving away the connection from the connection pool
        // System.out.println("Connection taken from pool. Size of pool: " + connectionPool.size());
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
       //  System.out.println("Connection returned to pool. Size of pool: " + connectionPool.size());
    }
}