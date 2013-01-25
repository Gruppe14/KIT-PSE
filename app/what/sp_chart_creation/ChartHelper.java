package what.sp_chart_creation;

import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import what.JSON_Helper;

/**
 * Helper class
 * 
 * @author Jonathan, PSE
 *
 */
public class ChartHelper {

	public static final String JSON_COUNT = "# of accesses";
	public static final String JSON_COUNT_SQL = "count(*)";
	
	public static final String JSON_FILTER = "filter";
	
	public static final String JSON_CHART_TYPE = "chart";
	
	public static final String JSON_X = "x";
	public static final String JSON_Y = "y";
	public static final String JSON_MEASURE = "measures";
	
	public static final String JSON_ALL = "all";
	
	public static final String JSON_LVL = "level";
	public static final String JSON_SELEC = "selected";
	public static final String JSON_PARENT = "parent";
	
	public static final String JSON_TIMESCALE = "timescale";
	public static final String TIME = "time";
	public static final String YEAR = "years";
	public static final String MON = "months";
	public static final String DAY = "days";
	public static final String HOUR = "hours";
	public static final String MIN = "mins";
	
	
	// -- BUILDING CHARTHOST -- BUILDING CHARTHOST -- BUILDING CHARTHOST --
	/**
	 * Creates a DimChart from a JSON file.
	 * 
	 * @param json path for the JSON file
	 * @return a DimChart from a JSON file
	 */
	protected static DimChart getChartHost(JSONObject json) {
		assert (json != null);
		

		// determin whether it's a one or two dimensional chart
		DimChart chart = getDimChart(json);
		
		
		if (chart == null) {
			System.out.println("ERROR: Creating chart host failed...");
		}
		
		return chart;
	}

	//TODO change exception caching to .has(Stringblabla)
	/**
	 * Creates a DimChart from a given JSONObject.
	 * 
	 * @param json from which DimChart gets created
	 * @return a DimChart from a given JSONObject
	 */
	private static DimChart getDimChart(JSONObject json) {
		assert (json != null);

		System.out.println(json.toString());

		String x = null;
		String chartType = null;
		String measure = null;
		JSONArray filters = null;
		try {
			x = json.getString(JSON_X);
			chartType = json.getString(JSON_CHART_TYPE);
			measure = json.getString(JSON_MEASURE);
			filters = json.getJSONArray(JSON_FILTER);
		} catch (JSONException e) {
			System.out.println("ERROR: Getting field in json file failed!");
			e.printStackTrace();
			return null;
		}
		
		String xTable = "dim_"+ x;
		String xCategorie = x;
		
		if (measure.equalsIgnoreCase(JSON_COUNT)) {
			measure = JSON_COUNT_SQL;
		} else {
			measure = "sum(row_" + measure + ")"; //TODO not hard coded...
		}
		
		
		HashMap<String, TreeSet<String>> filterSets = new HashMap<String, TreeSet<String>>();
		
		// x filters
		if (x.equalsIgnoreCase(JSON_TIMESCALE)) {
			xTable = "dim_time";
			xCategorie = TIME;
			
			
			try {
				x = "row_" + json.getString(JSON_TIMESCALE);
			} catch (JSONException e) {
				System.out.println("ERROR: Getting time filter failed!");
				e.printStackTrace();
				return null;
			}
			
		//	filterSets;
			
		} else {
			
			TreeSet<String> xFilter = null;
			try {
				xFilter = getFilters(x, json);
				x = getLevel(x, json);
			} catch (JSONException e) {

			}
		
			if (xFilter != null ) {
				filterSets.put(x, xFilter);
			}
		}
		
		// reading other filters
		/*
		JSONArray ary = null;
		try {
			ary = json.getJSONArray(JSON_FILTER);
		} catch (JSONException e1) {
			System.out.println("ERROR: Getting filter array failed!");
			e1.printStackTrace();
		}
		TreeSet<String> filterStr = new TreeSet<String>();
		if (ary != null) {
			for (int i = 0, l = ary.length(); i < l; i++) {
				try {
					filterStr.add(ary.getString(i));
				} catch (JSONException e) {
					System.out.println("ERROR: Getting a filter failed!");
				}
			}
		} else {
			System.out.println("Filter array empty!");

		}
		
		for (String s : filterStr) {
			TreeSet<String> sFilter = null;
			try {
				sFilter = getFilters(s, json.getJSONObject(s));
				s = x = getLevel(s, json);
			} catch (JSONException e) {
				System.out.println("ERROR: Getting filter set failed for " + s);
			}
			
			if (sFilter != null ) {
				filterSets.put(s, sFilter);
			}
			
		}
		*/
		
		// reading time
		JSONObject timeObj = null;
		int[] start = {-1,-1,-1,-1,-1};
		int[] end = {-1,-1,-1,-1,-1};
		try {
			if (json.has(TIME)) {
				timeObj = json.getJSONObject(TIME);
				start = getDate(timeObj, 0);
				end = getDate(timeObj, 1);
			} 			
		} catch (JSONException e) {
			System.out.println("ERROR: Getting time field failed!");
		}	
		
		

		
		DimChart chart;
		if (json.has(JSON_Y)) {
			// it has to be ensured that y is not time!!
			String y = null;
			try {
				y = json.getString(JSON_Y);
			} catch (JSONException e) {
				// won't happen..
			}
			
			String yTable = "dim_" + y;
			String yCategorie = y;
			TreeSet<String> sFilter = null;
			try {
				sFilter = getFilters(y, json.getJSONObject(y));
				y = getLevel(y, json);
			} catch (JSONException e) {
				System.out.println("ERROR: Getting filter set failed for " + y);
			}
			
			if (sFilter != null ) {
				filterSets.put(y, sFilter);
			}
			
			chart = new TwoDimChart(chartType, x, xTable, xCategorie, y, yTable, yCategorie, measure, filterSets, start, end);
		} else {
			chart = new DimChart(chartType, x, xTable, xCategorie, measure, filterSets, start, end);
		}
		
		return chart;
	}





	private static int[] getDate(JSONObject timeObj, int i) {
		assert ((i == 0) || (i == 1));
		
		int[] date = new int[5];
		try {
			if (timeObj.has(YEAR)) {
				date[0] = timeObj.getJSONArray(YEAR).getInt(i);
			} else {
				date[0] = -1;
			}
			if (timeObj.has(MON)) {
				date[1] = timeObj.getJSONArray(MON).getInt(i);
			} else {
				date[1] = -1;
			}
			if (timeObj.has(DAY)) {
				date[2] = timeObj.getJSONArray(DAY).getInt(i);
			} else {
				date[2] = -1;
			}
			if (timeObj.has(HOUR)) {
				date[3] = timeObj.getJSONArray(HOUR).getInt(i);
			} else {
				date[3] = -1;
			}
			if (timeObj.has(MIN)) {
				date[4] = timeObj.getJSONArray(MIN).getInt(i);
			} else {
				date[4] = -1;
			}
		} catch (JSONException e) {
			System.out.println("ERROR: Getting time failed!");
			e.printStackTrace();
			return null;
		}

		return date;
	}


	private static TreeSet<String> getFilters(String s, JSONObject json) throws JSONException {
		assert (s != null);
		assert (json != null);
		
		// get JSON Array with strings to filter for
		JSONObject values;
		try {
			values = json.getJSONObject(s);
		} catch (JSONException e) {
			System.out.println("ERROR: Getting filter array for " + s + " not possible!");
			e.printStackTrace();
			return null;
		} 
		
		TreeSet<String> filterSet = new TreeSet<String>();
		int length = values.length();
		for (int i = 0; i < length; i++) {
			s = values.getString(JSON_LVL);	
			Object selected = values.getJSONArray(JSON_SELEC);
			
			
			if (selected instanceof String) {
				String sel = (String) selected;
				if (sel.equalsIgnoreCase(JSON_ALL)) {
					return null;
				} else {
					System.out.println("ERROR: Illegal statement for select: " + sel);
				}
			} else if (selected instanceof JSONArray) {
				JSONArray array = (JSONArray) selected;
				TreeSet<String> content = getSelectContent(array, s);
				if (content != null) {
					filterSet.addAll(content);
				}
				
			} else {
				System.out.println("ERROR: Illegal statement for select, neither string nor array.");
			}
		}
		
		
		return filterSet;
	}


	private static TreeSet<String> getSelectContent(JSONArray array, String curLvl) {
		assert (array != null);
		assert (curLvl != null);
		
		 TreeSet<String> result = new  TreeSet<String>();
		
		try {
			if (array.get(0) instanceof String) {
				for (int i = 0, l = array.length(); i < l; i ++) {
					result.add(array.getString(i));
				}
				return result;
			} else if (array.get(0) instanceof JSONObject) {
				for (int i = 0, l = array.length(); i < l; i ++) {
					JSONObject obj = array.getJSONObject(i);
					curLvl = obj.getString(JSON_LVL);
					String parent = obj.getString(JSON_PARENT);
					JSONArray newArray = obj.getJSONArray(parent);
					result.addAll(getSelectContent(newArray, curLvl));
				}
				return result;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("ERROR: Reading filter array failed.");
		}
	
		return null;
	}

	private static String getLevel(String s, JSONObject json) {
		assert (s != null);
		assert (json != null);
		
		// get JSON Array with strings to filter for
		JSONObject values;
		try {
			values = json.getJSONObject(s);
		} catch (JSONException e) {
			System.out.println("ERROR: Getting level for " + s + " not possible!");
			e.printStackTrace();
			return null;
		} 
		
		int length = values.length();
		for (int i = 0; i < length; i++) {
			Object selected = null;
			try {
				s = values.getString(JSON_LVL);
				selected = values.getJSONArray(JSON_SELEC);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			if (selected instanceof String) {
				String sel = (String) selected;
				if (sel.equalsIgnoreCase(JSON_ALL)) {
					return s;
				} else {
					System.out.println("ERROR: Illegal statement for select: " + sel);
				}
			} else if (selected instanceof JSONArray) {
				JSONArray array = (JSONArray) selected;
				s = getLevelDeeper(array, s);

				
			} else {
				System.out.println("ERROR: Illegal statement for select, neither string nor array.");
			}
		}
		
		return s;
	}

	private static String getLevelDeeper(JSONArray array, String s) {
		assert (array != null);
		assert (s != null);
		
		try {
			if (array.get(0) instanceof String) {
				return s;
			} else if (array.get(0) instanceof JSONObject) {
				for (int i = 0, l = array.length(); i < l; i ++) {
					JSONObject obj = array.getJSONObject(i);
					s = obj.getString(JSON_LVL);
					String parent = obj.getString(JSON_PARENT);
					JSONArray newArray = obj.getJSONArray(parent);
					s = getLevelDeeper(newArray, s);
				}
				return s;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("ERROR: Reading filter array failed.");
		}
	
		return s;
	}
}

