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
	/**
	 * Returns the query from part for this table.<br>
	 * E.g.: (SELECT * FROM dim_type WHERE ... restrictions ...) AS dim_typeID
	 * 
	 * @return the query from part for this table
	 */
	public String getTableQuery() {
		if (hasTimeRestrictions()) {
			return super.getTableQuery();
		}
		
		// ( SELECT *
		String query = MySQLAdapter.LBR + MySQLAdapter.SELECT +  MySQLAdapter.ALL;
		
		// FROM table WHERE
		query += MySQLAdapter.FROM + getTable() + MySQLAdapter.WHERE + MySQLAdapter.LBR;
		
		// hard coded time restrictions
		boolean and = false;
		for (int i = 0; i < 5; i++) {
			query += getRestriction(from, i, and);
			query += getRestriction(to, i, and);
		}
		
		
		// ) AS table nick name
		query += MySQLAdapter.RBR + MySQLAdapter.AS + getTableNickName();
		
		return query;
	}

	private String getRestriction(int[] ary, int i, boolean and) {
		String query = "";
		
		if (ary[i] > 0) {
			if (and) {
				query += MySQLAdapter.AND;
			}
			
			query += getDimension().getRowAt(i).getColumnName() + MySQLAdapter.EQL + ary[i]; 
		}
		
		
		return query;
	}

	
}
