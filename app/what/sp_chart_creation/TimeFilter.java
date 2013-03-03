package what.sp_chart_creation;

// java imports
import java.util.TreeSet;

// intern imports
import what.sp_config.DimKnot;
import what.sp_config.DimRow;
import what.sp_data_access.MySQLAdapter;

/**
 * TimeFilter as child of a Filter
 * is the class storing information what
 * is requested referring to the time dimension.
 * Provides methods to create MySQL statements 
 * for restriction parts.
 * 
 * @author Jonathan, PSE Gruppe 14
 * @see Filter
 */
public class TimeFilter extends Filter {
	
	/** Start time for request. */
	private final int[] from;
	
	/** End time for request. */
	private final int[] to;

	/** Constant array signaling no filtering. */
	private static final int[] NO_FILTER = {-1, -1, -1, -1, -1};
	
	/**
	 * Constructor for a TimeFilter with given
	 * dimension.
	 * 
	 * @param dimension DimRow
	 * @param trees should be empty
	 */
	public TimeFilter(DimRow dimension, TreeSet<DimKnot> trees) {
		this(dimension, trees, NO_FILTER, NO_FILTER);
	}
	
	/**
	 * Constructor for a TimeFilter with given
	 * dimension, start and end time.
	 * 
	 * @param dimension DimRow
	 * @param trees should be empty
	 * @param from time from
	 * @param to time to
	 */
	public TimeFilter(DimRow dimension, TreeSet<DimKnot> trees, int[] from, int[] to) {
		super(dimension, trees);
		assert ((trees != null) && (trees.size() == 0));
		
		assert ((from != null) && (from.length == ChartHostBuilder.L));
		assert ((to != null) && (to.length == ChartHostBuilder.L));
		
		this.from = from;
		this.to = to;
	}
	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the start time.
	 * 
	 * @return start time of the request
	 */
	protected int[] getFrom() {
		return from;
	}

	/**
	 * Returns the end time.
	 * 
	 * @return end time of the request
	 */
	protected int[] getTo() {
		return to;
	}
	
	/**
	 * Returns whether there are time restrictions.
	 * 
	 * @return whether there are time restrictions
	 */
	private boolean hasTimeRestrictions() {
		boolean noRes = true;
		
		for (int i = 0; i < ChartHostBuilder.L; i++) {
			if ((from[i] > 0) || (to[i] > 0)) {
				noRes = false;
			}
		}
		
		return noRes;
	}
	
	// -- SQL GETTER -- SQL GETTER -- SQL GETTER -- SQL GETTER --
	/** MySQL constant string. */
	private static final String STRTO = "STR_TO_DAT("; 
	/** MySQL constant string. */
	private static final String CAST = "CAST(";
	/** MySQL constant string. */
	private static final String CONCAT = "CONCAT(";
	/** MySQL constant string. */
	private static final String DATE = "'%d,%m,%Y'";
	/** MySQL constant string. */
	private static final String CHARKOMMA = "','";
	/** MySQL constant string. */
	private static final String BETWEEN = "BETWEEN";
	/** MySQL constant string. */
	private static final String AND = "AND";
	
	
	@Override
	public String getTableQuery() {
			return getTable() + MySQLAdapter.AS + getTableNickName();
		
	}

	@Override
	public String getRestrictions() {
		if (hasTimeRestrictions()) {
			return "";
		}
		
		//String restri =
		
		
		// (
		String query = MySQLAdapter.AND + MySQLAdapter.LBR; // (
		String fromQuery = MySQLAdapter.LBR;
		String toQuery = MySQLAdapter.LBR;
			
		boolean andFrom = false;
		boolean andTo = false;
		
		for (int i = 0; i < ChartHostBuilder.L; i++) {
			if (from[i] > 0) {
				if (andFrom) {
					fromQuery += MySQLAdapter.AND;
				} else {
					andFrom = true;
				}
			}
			fromQuery += getTimeRestriction(from, 0, i);
			
			
			if (to[i] > 0) {
				if (andTo) {
					toQuery += MySQLAdapter.AND;
				} else {
					andTo = true;
				}
			}
			toQuery += getTimeRestriction(to, 1, i);
		}
		// ) 
		fromQuery += MySQLAdapter.RBR;
		toQuery += MySQLAdapter.RBR;
		
		
		if (andFrom) {
			query += fromQuery;
		}
		if (andFrom && andTo) {
			query += MySQLAdapter.OR;
		}
		if (andTo) {
			query += toQuery;
		}
		
		// ) 
		query += MySQLAdapter.RBR;
		
		return query;
	}

	/**
	 * Returns the query restriction part for the given
	 * position i in the given array.
	 * 
	 * @param ary array 
	 * @param i position in array
	 * @param and whether and should be part of the query
	 * @return the query restriction part for the given
	 * position i in the given array
	 */
	private String getTimeRestriction(int[] ary, int fromTo, int i) {
		assert ((fromTo == 0) || (fromTo == 1));
		assert (ary != null);
		assert ((i >= 0) && (i < ary.length));
				
		String query = "";
		
		if (ary[i] > 0) {
			
			query += getDimension().getRowAt(i).getColumnName();
			if (fromTo == 0) {
				query += MySQLAdapter.MORE; 
			} else if (fromTo == 1) {
				query += MySQLAdapter.LESS;
			}
			query += ary[i]; 
		}
		
		
		return query;
	}
	
}
