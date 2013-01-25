package what.sp_chart_creation;

// java imports
import java.util.HashMap;
import java.util.TreeSet;

import org.json.JSONObject;

/**
 * A chart with 1 dimension and a measure.<br>
 * 
 * @author Jonathan, PSE Group 14
 */
public class DimChart {
	
	/** JSONObject storing this DimCharts representation for D3 */
	private JSONObject json;

	/** The chart type of this DimChart. */
	private String chartType;
	
	/** x axis of this DimChart */
	private String x;
	
	/** name of the table of the x-axis of this DimChart */
	private String xTable;
	
	/** category of the x axis of this DimChart */
	private String xCategory;
	
	/** measure of this DimChart */
	private String measure;
	
	/** start time for request */
	private int[] from;
	
	/** end time for request */
	private int[] to;
	
	/** Map of filters. */
	private HashMap<String, TreeSet<String>> filters;
	
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
	protected DimChart(String chartType, String x, String xTable, String xCategory,String measure, HashMap<String, TreeSet<String>> filterSets, int[] start, int[] end) {
		assert (chartType != null);
		assert (x != null);
		assert (xTable != null);
		assert (xCategory != null);
		assert (measure != null);
		assert (filterSets != null);
		assert ((start != null) && (start.length == 5));
		assert ((end != null) && (end.length == 5));
		
		this.chartType = chartType;
		this.x = x;
		this.xTable= xTable;
		this.xCategory = xCategory;
		this.measure = measure;
		this.filters = filterSets;
		this.from = start;
		this.to = end;		
	}
	
	/**
	 * Sets the JSONObject for this DimChart
	 * @param j
	 */
	protected void setJSON(JSONObject j) {
		this.json = j;
	}
	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * @return the json
	 */
	protected JSONObject getJson() {
		return json;
	}

	/**
	 * @return the chartType
	 */
	protected String getChartType() {
		return chartType;
	}

	/**
	 * @return the x
	 */
	protected String getX() {
		return x;
	}

	/**
	 * @return the measure
	 */
	protected String getMeasure() {
		return measure;
	}

	/**
	 * @return the from
	 */
	protected int[] getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	protected int[] getTo() {
		return to;
	}

	/**
	 * @return the filters
	 */
	protected HashMap<String, TreeSet<String>> getFilters() {
		return filters;
	}

	

	/**
	 * @return the xTable
	 */
	public String getxTable() {
		return xTable;
	}

	/**
	 * @return the xCategorie
	 */
	public String getxCategory() {
		return xCategory;
	}


	
		
}
