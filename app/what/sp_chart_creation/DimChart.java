package what.sp_chart_creation;

import java.util.HashMap;
import java.util.TreeSet;

import org.json.JSONObject;


public class DimChart {
	
	private JSONObject json;

	private String chartType;
	
	private String x;
	private String xTable;
	private String xCategorie;
	
	private String measure;
	
	private int[] from;
	
	private int[] to;
	
	private HashMap<String, TreeSet<String>> filters;
	
	protected DimChart(String chartType, String x, String xTable, String xCategorie,String measure, HashMap<String, TreeSet<String>> filterSets, int[] start, int[] end) {
		assert (chartType != null);
		assert (x != null);
		assert (xTable != null);
		assert (xCategorie != null);
		assert (measure != null);
		assert (filterSets != null);
		assert ((start != null) && (start.length == 5));
		assert ((end != null) && (end.length == 5));
		
		this.chartType = chartType;
		this.x = x;
		this.xTable= xTable;
		this.xCategorie = xCategorie;
		this.measure = measure;
		this.filters = filterSets;
		this.from = start;
		this.to = end;		
	}

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

	public void setJSON(JSONObject j) {
		this.json = j;
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
	public String getxCategorie() {
		return xCategorie;
	}


	
		
}
