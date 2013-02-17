package what.sp_chart_creation;

// JSON imports
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import what.Printer;
// intern imports

/**
 * A chart with 1 dimension and a measure.<br>
 * 
 * @author Jonathan, PSE Group 14
 * @see ChartMediator
 */
public class DimChart {
	
	/** JSONObject storing this DimCharts representation for D3 */
	private JSONObject json;

	/** The chart type of this DimChart. */
	private final String chartType;
	
	/** x axis of this DimChart */
	private final String x;
	
	/** name of the table of the x-axis of this DimChart */
	private final Filter xFilter;
		
	/** measure of this DimChart */
	private final String measure;
	
	/** Map of filters. */
	private final ArrayList<Filter> filters;
	
	/**
	 * Protected constructor for a 2 dimension chart.
	 * 
	 * @param chartType chart type like bubble chart, ...
	 * @param x x-axis
	 * @param xTable table of x-axis
	 * @param xCategorie category of x-axis
	 * @param measure measures like sum(*), rows, ...
	 * @param filterSets a Map containing filters
	 * @param start start time for request
	 * @param end end time for request
	 */
	protected DimChart(String chartType, String x, Filter xFilter, String measure, ArrayList<Filter> filters) {
		
		assert (chartType != null);
		assert (x != null);
		assert (xFilter != null);
		assert (measure != null);
		assert (filters != null);
			
		this.chartType = chartType;
		this.x = x;
		this.xFilter= xFilter;
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
	 * Sets the JSONObject for this DimChart
	 * @param j
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
	 * Returns the chart type of this DimChart.
	 * 
	 * @return the chart type of this DimChart
	 */
	protected String getChartType() {
		return chartType;
	}

	/**
	 * Returns the x-axis.
	 * 
	 * @return the x-axis
	 */
	public String getX() {
		return x;
	}

	/**
	 * Returns the x Filter.
	 * 
	 * @return the x Filter
	 */
	public Filter getXFilter() {
		return xFilter;
	}
	
	/**
	 * Returns the column of x in the warehouse.
	 * 
	 * @return the column of x in the warehouse
	 */
	public String getXColumn() {
		return x; // TODO
	}
	
	/**
	 * Returns the measure of this DimChart.
	 * 
	 * @return the measure
	 */
	public String getMeasure() {
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
		
		// TODO anything more to put?
		
		try {
			j.put("chartType", getChartType());
		} catch (JSONException e) {
			Printer.pproblem("Putting chart type into JSONObject.");
			return false;
		}
		
		return true;
	}





}
