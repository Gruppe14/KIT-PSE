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
	private static final String STRTO = "STR_TO_DATE("; 
	/** MySQL constant string. */
	private static final String CAST = "CAST(";
	/** MySQL constant string. */
	private static final String ASCHAR = " AS CHAR),";
	/** MySQL constant string. */
	private static final String CONCAT = "CONCAT(";
	/** MySQL constant string. */
	private static final String DATE = "'%Y,%m,%d,%H,%i,%s') ";
	/** MySQL constant string. */
	private static final String CHARKOMMA = "',') ";
	/** MySQL constant string. */
	private static final String CHARKOMMAZERO = "',00') ";
	/** MySQL constant string. */
	private static final String KOMMA = ",";
	/** MySQL constant string. */
	private static final String KOMMAZERO = ",00',";
	/** MySQL constant string. */
	private static final String BETWEEN = " BETWEEN ";
	/** MySQL constant string. */
	private static final String AND = " AND ";
	/** MySQL constant string. */
	private static final String RBR = ") ";
	/** MySQL constant string. */
	private static final String APOS = "'";
	
	@Override
	public String getTableQuery() {
			return getTable() + MySQLAdapter.AS + getTableNickName();
		
	}

	@Override
	public String getRestrictions() {
		if (hasTimeRestrictions()) {
			return "";
		}
		
		DimRow d = getDimension();
		
		// STR_TO_DATE(CONCAT(CONCAT(CONCAT(CONCAT
		String restri = AND + STRTO + CONCAT + CONCAT + CONCAT + CONCAT;
		
		// year
		restri += CONCAT + CAST + d.getColumnNameOfLevel(0)
					+ ASCHAR + CHARKOMMA + KOMMA;
		
		// month
		restri += CONCAT + CAST + d.getColumnNameOfLevel(1)
					+ ASCHAR + CHARKOMMA + RBR + KOMMA;

		// day
		restri += CONCAT + CAST + d.getColumnNameOfLevel(2)
					+ ASCHAR + CHARKOMMA + RBR + KOMMA;
		
		// hour
		restri += CONCAT + CAST + d.getColumnNameOfLevel(3)
					+ ASCHAR + CHARKOMMA + RBR + KOMMA;
		
		// minute
		restri += CONCAT + CAST + d.getColumnNameOfLevel(4)
					+ ASCHAR + CHARKOMMAZERO + RBR + KOMMA;
		
		// '%Y,%m,%d,%H:%i') BETWEEN
		restri += DATE;
		
		
		// BETWEEN STR_TO_DATE( ...  AND STR_TO_DATE(... 
		String fromQuery = BETWEEN + STRTO + APOS;
		String toQuery = AND + STRTO + APOS;
			
		int i = 0;
		for (; i < (ChartHostBuilder.L - 1); i++) {
			// number,%Y,%m,%d,%H:%i')
			fromQuery += from[i] + KOMMA;
			toQuery += to[i] + KOMMA;
		}
		
		// minutes,00','
		fromQuery += from[i] + KOMMAZERO + DATE;
		toQuery += to[i] + KOMMAZERO + DATE;
			
		return restri + fromQuery + toQuery;
	}	
}
