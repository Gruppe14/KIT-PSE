package what.sp_chart_creation;

// java imports
import java.util.TreeSet;

// intern imports
import what.sp_config.DimKnot;
import what.sp_config.DimRow;
import what.sp_data_access.MySQLAdapter;

public class TimeFilter extends Filter {
	
	/** Start time for request */
	private final int[] from;
	
	/** End time for request */
	private final int[] to;

	private final static int[] noFilter = {-1,-1,-1,-1,-1};
	
	public TimeFilter(DimRow dimension, TreeSet<DimKnot> trees) {
		this(dimension, trees, noFilter, noFilter);
	}
	
	public TimeFilter(DimRow dimension, TreeSet<DimKnot> trees, int[] from, int[] to) {
		super(dimension, trees);
		
		assert ((from != null) && (from.length == 5));
		assert ((to != null) && (to.length == 5));
		
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
		
		for (int i = 0; i < 5; i++) {
			if ((from[i] > 0) || (to[i] > 0)) {
				noRes = false;
			}
		}
		
		return noRes;
	}
	
	// -- SQL GETTER -- SQL GETTER -- SQL GETTER -- SQL GETTER --
	@Override
	public String getTableQuery() {
			return getTable() + MySQLAdapter.AS + getTableNickName();
		
	}

	@Override
	public String getRestrictions() {
		if (hasTimeRestrictions()) {
			return "";
		}
		
		// (
		String query = MySQLAdapter.AND + MySQLAdapter.LBR; // (
		
		boolean and = false;
		for (int i = 0; i < 5; i++) {
			query += getTimeRestriction(from, i, and);
			query += getTimeRestriction(to, i, and);
		}
		
		
		// ) 
		query += MySQLAdapter.RBR;
		
		return query;
	}

	
	private String getTimeRestriction(int[] ary, int i, boolean and) {
		
		
		String query = "";
		
		if (ary[i] > 0) {
			if (and) {
				query += MySQLAdapter.AND;
			}
			
			and = true;
			query += getDimension().getRowAt(i).getColumnName() + MySQLAdapter.EQL + ary[i]; 
		}
		
		
		return query;
	}
	
}
