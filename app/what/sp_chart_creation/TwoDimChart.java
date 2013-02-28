package what.sp_chart_creation;

// java imports
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

// JSON imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// intern imports
import what.Printer;
import what.sp_data_access.MySQLAdapter;

/**
 * A chart with 2 dimensions and a measure.<br>
 * Extends a chart with 1 dimension. 
 * 
 * @author Jonathan, PSE Group 14
 * @see DimChart
 */
public class TwoDimChart extends DimChart {
	
	/** Constant for a position in the result set. */
	private static final int RESULTSET_POSITION_MEASURE = 1;
	/** Constant for a position in the result set. */
	private static final int RESULTSET_POSITION_X = 2;
	/** Constant for a position in the result set. */
	private static final int RESULTSET_POSITION_Y = 3;
	
	/** y axis of this TwoDimChart. */
	private String y;
	
	/** Name of the table of the y-axis of this TwoDimChart. */
	private Filter yFilter;
	
	/**
	 * Protected constructor for a 2 dimension chart.
	 * 
	 * @param chartType chart type like bubble chart, ...
	 * @param x x-axis
	 * @param xFilter Filter for the x-axis
	 * @param y y-axis
	 * @param yFilter Filter for the y-axis
	 * @param measure Measure
	 * @param filters a collection of filters
	 */
	protected TwoDimChart(String chartType, String x, Filter xFilter, String y, Filter yFilter, 
							Measure measure, ArrayList<Filter> filters) {
		super(chartType, x, xFilter, measure, filters);
		
		assert (yFilter != null);
		assert (y != null);
		
		this.y = y;		
		this.yFilter = yFilter;
	}
	
	/**
	 * Returns the y-axis.
	 * 
	 * @return the y-axis
	 */
	protected String getY() {
		return y;
	}

	/**
	 * Returns the y Filter.
	 * 
	 * @return the y Filter
	 */
	protected Filter getYFilter() {
		return yFilter;
	}

	/**
	 * Returns the column of y in the warehouse.
	 * 
	 * @return the column of y in the warehouse
	 */
	private String getYColumn() {
		return getYFilter().getDimension().getRowEntryFor(getY()).getColumnName();
	}
	
	@Override
	public String getSelect() {
		return super.getSelect() + MySQLAdapter.KOMMA + getYColumn();
	}
	
	@Override
	public String getTableQuery() {
		return super.getTableQuery() + MySQLAdapter.KOMMA + getYFilter().getTableQuery();
	}
	
	@Override
	public String getKeyRestrictions(String facttableShort) {
		if (facttableShort == null) {
			throw new IllegalArgumentException();
		}
		
		String restri = super.getKeyRestrictions(facttableShort);
		
		// ft.
		String ft = facttableShort.trim() + MySQLAdapter.DOT;
			
		// x
		restri += MySQLAdapter.AND + ft + getYFilter().getTableKey() + MySQLAdapter.EQL + getYFilter().getKeyQuery();
		

				
		return restri;
	}
	
	@Override
	public String getRestrictions() {
		String restri = super.getRestrictions();
		
		return restri + getYFilter().getRestrictions();
	}

	@Override
	public String getGroupBy() {
		return super.getGroupBy() +  MySQLAdapter.KOMMA + getYColumn();
	}

	@Override
	public  boolean createJSONFromResultSet(ResultSet re) {
		assert (re != null);
		

		// create JSONObject and put first variables
		JSONObject json = new JSONObject();
		String x = getX();
		String y = getY();
		String m = getMeasure().getName();
		try {
			json.put(ATT1, x);
			json.put(ATT2, y);
			json.put(ATT3, m);
		} catch (JSONException e) {
			Printer.perror("Putting into JSONObject.");
			return false;
		}
		
		// reading from ResultSet
		JSONArray aray = null;
		HashSet<JSONObject> sum = new HashSet<JSONObject>();
		try {
			while (re.next()) {
				JSONObject a = new JSONObject();
				a.put(x, re.getString(RESULTSET_POSITION_X));
				a.put(y, re.getString(RESULTSET_POSITION_Y));
				a.put(m, re.getDouble(RESULTSET_POSITION_MEASURE));
				sum.add(a);
			}
			aray = new JSONArray(sum);
			json.put(DATA, aray);
		} catch (SQLException e) {
			Printer.perror("Reading from ResultSet.");
			return false;
		} catch (JSONException e) {
			Printer.perror("Putting into JSONObject.");
			return false;
		}
		
		// additional information
		if (!(putAdditionalInformation(json))) {
			Printer.pproblem("Putting addiotnal information into JSONObject.");
			return false;
		}
		
		// success (?!)
		Printer.psuccess("Creating JSON for chart from ResultSet.");
		setJSON(json);
		return true;
	}
}
