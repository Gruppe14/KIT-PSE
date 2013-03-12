package what.sp_chart_creation;

// JSON imports
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

// JSON imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//intern imports
import what.Printer;
import what.sp_data_access.MySQLAdapter;

/**
 * A chart with 1 dimension and a measure.<br>
 * 
 * @author Jonathan, PSE Group 14
 * @see ChartMediator
 */
public class DimChart {
	
	/** Constant for a position in the result set. */
	private static final int RESULTSET_POSITION_MEASURE = 1;
	/** Constant for a position in the result set. */
	private static final int RESULTSET_POSITION_X = 2;
	
	/** Constant name to put in JSONObject. */
	protected static final String ATT1 = "attribute1";
	/** Constant name to put in JSONObject. */
	protected static final String ATT2 = "attribute2";
	/** Constant name to put in JSONObject. */
	protected static final String ATT3 = "attribute3";
	/** Constant name to put in JSONObject. */
	protected static final String DATA = "data";
	/** Constant name to put in JSONObject. */
	protected static final String CHART = "chartType";
	/** Constant name to put in JSONObject. */
	protected static final String MEASURE_ATT = "measureAttribute";
	
	/** Constant name to put in JSONObject. */
	protected static final String SCATTERPLOT = "scatterplot";
	
	/** JSONObject storing this DimCharts representation for D3. */
	private JSONObject json;

	/** The chart type of this DimChart. */
	private final String chartType;
	
	/** x axis of this DimChart. */
	private final String x;
	
	/** name of the table of the x-axis of this DimChart. */
	private final Filter xFilter;
		
	/** Measure of this DimChart. */
	private final Measure measure;
	
	/** List of filters. */
	private final ArrayList<Filter> filters;
	
	/**
	 * Protected constructor for a 1 dimension chart.
	 * 
	 * @param chartType chart type like bubble chart, ...
	 * @param x the name of the x-axis
	 * @param xFilter Filter for the x-axis
	 * @param measure Measure of this chart
	 * @param filters Collection of Filters for the other dimensions
	 */
	protected DimChart(String chartType, String x, Filter xFilter, Measure measure, ArrayList<Filter> filters) {
		assert (chartType != null);
		assert (x != null);
		assert (xFilter != null);
		assert (measure != null);
		assert (filters != null);
			
		this.chartType = chartType;
		this.x = x;
		this.xFilter = xFilter;
		this.measure = measure;
		this.filters = filters;	
	}
	
	// -- CHECKER -- CHECKER -- CHECKER -- CHECKER -- CHECKER -- 
	/**
	 * Returns whether the chart JSONObject is exists.
	 * 
	 * @return whether the chart JSONObject is exists
	 */
	protected boolean hasJSON() {
		return (json != null);
	}
	
	// -- SETTER -- SETTER -- SETTER -- SETTER -- SETTER -- 
	/**
	 * Sets the JSONObject for this DimChart.
	 * 
	 * @param j the JSONObject of this chart
	 */
	protected void setJSON(JSONObject j) {
		this.json = j;
	}
	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the chart JSONObject of this DimChart.
	 * May be null if not initialized yet.
	 * 
	 * @return the chart JSONObject of this DimChart 
	 */
	protected JSONObject getJson() {
		return json;
	}

	/**
	 * Returns the select statement of this chart.
	 * 
	 * @return the select statement of this chart
	 */
	public String getSelect() {
		return getMeasure().getMeasureSelect() + MySQLAdapter.KOMMA + getXColumn();
	}
	
	/**
	 * Returns the from/join statement of this chart.<br>
	 * e.g.: "dim_time as timeID, filter1_table as f1" 
	 * 
	 * @return the from/join statement of this chart
	 */
	public String getTableQuery() {
		return getXFilter().getTableQuery() + getFilterSelect();
	}
	
	/**
	 * Returns the key restrictions for this chart.<br>
	 * e.g.: ft.xkey = xtable.xkey and ft.f1key = f1.table=f1key 
	 * 
	 * @param facttableShort the short of the fact table for: "ft.key"
	 * @return the key restrictions for this chart
	 */
	public String getKeyRestrictions(String facttableShort) {
		if (facttableShort == null) {
			throw new IllegalArgumentException();
		}
		
		// ft.
		String ft = facttableShort.trim() + MySQLAdapter.DOT;
			
		// x
		String restri = ft + getXFilter().getTableKey() + MySQLAdapter.EQL + getXFilter().getKeyQuery();
		
		// filters
		for (Filter f : getFilters()) {
			restri += MySQLAdapter.AND + ft + f.getTableKey() + MySQLAdapter.EQL + f.getKeyQuery();
		}
				
		return restri;
	}
	
	/**
	 * Returns the restrictions of this chart leaded by an AND.
	 * 
	 * @return the restrictions of this chart leaded by an AND
	 */
	public String getRestrictions() {
		String restri = getXFilter().getRestrictions();

		for (Filter f : getFilters()) {
			restri += f.getRestrictions();
		}
		
		return restri;
	}
	
	/**
	 * Returns the group by statement of this chart.
	 * 
	 * @return  the group by statement
	 */
	public String getGroupBy() {
		if (chartType.equalsIgnoreCase(SCATTERPLOT)) {
			return "";
		}
		return MySQLAdapter.GROUPBY + getXColumn();
	}
	
	// GETTER >> private or protected
	/**
	 * Returns the chart type of this DimChart.
	 * 
	 * @return the chart type of this DimChart
	 */
	private String getChartType() {
		return chartType;
	}

	/**
	 * Returns the x-axis.
	 * 
	 * @return the x-axis
	 */
	protected final String getX() {
		return x;
	}

	/**
	 * Returns the x Filter.
	 * 
	 * @return the x Filter
	 */
	protected Filter getXFilter() {
		return xFilter;
	}
	
	/**
	 * Returns the column of x in the warehouse.
	 * 
	 * @return the column of x in the warehouse
	 */
	private String getXColumn() {
		return getXFilter().getDimension().getRowEntryFor(getX()).getColumnName();
	}
	
	/**
	 * Returns the measure of this DimChart.
	 * 
	 * @return the measure
	 */
	protected Measure getMeasure() {
		return measure;
	}
	
	/**
	 * Returns the array of Filters from this DimChart.
	 * 
	 * @return the array of Filters
	 */
	public ArrayList<Filter> getFilters() {
		return filters;
	}

	/**
	 * Returns the select statement for the filters.
	 * 
	 * @return the select statement for the filters
	 */
	private String getFilterSelect() {
		String select = "";
		for (Filter f : getFilters()) {
			select += MySQLAdapter.KOMMA + f.getTableQuery();
		}
		return select;
	}
	
	// -- WORKING -- WORKING -- WORKING -- WORKING -- WORKING -- 
	/**
	 * Puts all additional information needed in the given JSONObject,
	 * which should be the chart JSONObject for this chart.
	 * 
	 * @param j JSONObject (chart) in which more information will be put
	 * @return whether putting information was successful
	 */
	public boolean putAdditionalInformation(JSONObject j) {
		assert (j != null);
			
		try {
			j.put(CHART, getChartType());
			j.put(MEASURE_ATT, getMeasure().getAggregation());
		} catch (JSONException e) {
			Printer.pproblem("Putting chart type into JSONObject.");
			return false;
		}
		
		return true;
	}

	/**
	 * Creates the JSONObject for this chart from
	 * the given result set.
	 * 
	 * @param re ResultSet containing the information
	 * @return whether it was successful
	 */
	public  boolean createJSONFromResultSet(ResultSet re) {
		assert (re != null);
		

		// create JSONObject and put first variables
		JSONObject json = new JSONObject();
		String x = getX();
		String m = getMeasure().getName();
		try {
			json.put(ATT1, x);
			json.put(ATT2, m);
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

	@Override
	public String toString() {
		return "DimChart [chartType=" + getChartType()  
				+ ", measure=" + measure.getMeasureSelect()
				+ ", x=" + x + ",  xFilter=" + xFilter 
				+ ", filters=" + filters + "]";
	}
}
