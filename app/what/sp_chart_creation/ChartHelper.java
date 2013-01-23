package what.sp_chart_creation;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import what.JSON_Helper;

public class ChartHelper {

	public static final String JSON_COUNT = "# of accesses";
	public static final String JSON_COUNT_SQL = "count(*)";
	
	public static final String JSON_FILTER = "filter";
	
	public static final String JSON_CHART_TYPE = "chart";
	
	public static final String JSON_X = "x";
	public static final String JSON_Y = "y";
	public static final String JSON_MEASURE = "measures";
	
	public static final String JSON_ALL = "all";
	public static final String JSON_PARENT = "parent";

	// -- BUILDING CHARTHOST -- BUILDING CHARTHOST -- BUILDING CHARTHOST --
	protected static DimChart getChartHost(String path) {
		assert (path != null);
		
		// gets the file
		File chartFile = JSON_Helper.getJSONFile(path);
		if (chartFile == null) { 
			System.out.println("ERROR: File path incorrect: " + path);
			return null;
		}
		
		// gets the content of the file
		String jsonContent = JSON_Helper.getJSONContent(chartFile);
		if (jsonContent == null) { 
			System.out.println("ERROR: Chart file empty for " + path);
			return null;
		}
		
		// get json object
		JSONObject json;
		try {
			json = new JSONObject(jsonContent);
		} catch (JSONException e) {
			System.out.println("ERROR: Creating chart host failed for path:\n " + path);
			e.printStackTrace();
			return null;
		}
		
		// determin whether it's a one or two dimensional chart
		DimChart chart = getDimChart(json);
		
		
		if (chart == null) {
			System.out.println("ERROR: Creating chart host failed...");
		}
		
		return null;
	}


	private static DimChart getDimChart(JSONObject json) {
		assert (json != null);

		String x = null;
		String chart_type = null;
		String measure = null;
		JSONArray filters = null;
		try {
			x = json.getString(JSON_X);
			chart_type = json.getString(JSON_CHART_TYPE);
			measure = json.getString(JSON_MEASURE);
			filters = json.getJSONArray(JSON_FILTER);
		} catch (JSONException e) {
			System.out.println("ERROR: Getting field in json file failed!");
			e.printStackTrace();
			return null;
		}
		
		if (measure.equalsIgnoreCase(JSON_COUNT)) {
			measure = JSON_COUNT_SQL;
		}
		
		HashMap<String, TreeSet<String>> filterSets = new HashMap<String, TreeSet<String>>();
		
		TreeSet<String> xFilter = getFilters(x, json);
		
		
		DimChart chart;
		if (json.has(JSON_Y)) {
			chart = getTwoDimChart(json);
		} else {
			chart = getOneDimChart(json);
		}
		return null;
	}


	private static TreeSet<String> getFilters(String s, JSONObject json) {
		assert (s != null);
		assert (json != null);
		
		// get JSON Array with strings to filter for
		JSONArray values;
		try {
			values = json.getJSONArray(s);
		} catch (JSONException e) {
			System.out.println("ERROR: Getting filter array for " + s + " not possible!");
			e.printStackTrace();
			return null;
		} 
		
		int length = values.length();
		for (int i = 0; i < length; i++) {
			
		}
		
		
		return null;
	}


	private static DimChart getOneDimChart(JSONObject json) {
		
		return null;
	}


	private static TwoDimChart getTwoDimChart(JSONObject json) {
		assert (json != null);
		
		
		
		
		return null;
	}
}

