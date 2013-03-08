package what.sp_data_access;

// sql imports
import java.sql.Connection;
import java.sql.SQLException;

// java imports
import play.db.DB;

//intern imports
import what.Printer;

/**
 * WHConnectionManager manages connections,
 * or better said just a pool of connections.
 * Was refactored using the connection management of play, but leaving some method names.
 * 
 * If you take one, please put it back.
 * 
 * @author Jonathan, Lukas, PSE Gruppe 14,   (Sridhar M. S. http://www.developer.com/java/data/article.php/3847901/
 * 										Implement-Java-Connection-Pooling-with-JDBC.htm)
 */
public class WHConnectionManager {
    // -- GET AND BRING BACK -- GET AND BRING BACK -- GET AND BRING BACK -- GET AND BRING BACK --
    /**
     * Returns a connection from the pool if one is available.
     * 
     * @return connection from the pool
     */
    protected Connection getConnectionFromPool() {
    	return DB.getConnection();
    }   
    
    /**
     * Returns a connection to the pool.
     * 
     * @param connection connection to be returned
     */
    protected void returnConnectionToPool(Connection connection) {
        //Adding the connection from the client back to the connection pool
//        connectionPool.addElement(connection);
       // Printer.print("Connection returned to pool. Size of pool: " + connectionPool.size());
    	try {
			connection.close();
		} catch (SQLException e) {
			Printer.pproblem("Returning connection to play pool.");
		}
    }
}