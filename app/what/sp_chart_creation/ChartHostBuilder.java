package what.sp_chart_creation;

//java imports
import java.util.ArrayList;
import java.util.TreeSet;

//intern imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//intern imports
import what.Printer;
import what.JSONReader;
import what.sp_config.ConfigWrap;
import what.sp_config.DimKnot;
import what.sp_config.DimRow;
import what.sp_config.RowEntry;

/**
 * Helper class to create a DimChart from a given JSONObject.
 *  
 * @author Jonathan, PSE Gruppe 14
 * @see ChartMediator
 * @see DimChart
 */
public class ChartHostBuilder {
	/** JSON constant. */
	public static final String JSON_FILTER = "filter";
	
	/** JSON constant. */
	public static final String JSON_CHART_TYPE = "chart";
	
	/** JSON constant. */
	public static final String JSON_X = "x";
	/** JSON constant. */
	public static final String JSON_Y = "y";
	/** JSON constant. */
	public static final String JSON_X_DIM = "xDim";
	/** JSON constant. */
	public static final String JSON_Y_DIM = "yDim";
	
	/** JSON constant. */
	public static final String JSON_MEASURE = "measure";
	/** JSON constant. */
	public static final String JSON_AGGREGATION = "aggregation";
	
	/** JSON constant. */
	public static final String JSON_ALL = "all";
	
	/** JSON constant. */
	public static final String JSON_LVL = "level";
	/** JSON constant. */
	public static final String JSON_SELEC = "selected";
	/** JSON constant. */
	public static final String JSON_PARENT = "parent";
	
	/** JSON constant. */
	public static final String JSON_TIME = "time";
	/** JSON constant. */
	public static final String YEAR = "years";
	/** JSON constant. */
	public static final String MON = "months";
	/** JSON constant. */
	public static final String DAY = "days";
	/** JSON constant. */
	public static final String HOUR = "hours";
	/** JSON constant. */
	public static final String MIN = "mins";
	
	/** The configuration on which the work of this CharHostBuilder is based. */	
	private final ConfigWrap config;
	
	/**
	 * Constructor for a ChartHostBuilder with a given ConfigWrap.
	 * 
	 * @param config ConfigWrap on which the work wil be based
	 */
	protected ChartHostBuilder(ConfigWrap config) {
		assert (config != null);
		
		this.config = config;
	}

	// -- BUILDING CHARTHOST -- BUILDING CHARTHOST -- BUILDING CHARTHOST --
	/**
	 * Creates a DimChart from a JSON file.
	 * 
	 * @param json path for the JSON file
	 * @return a DimChart from a JSON file
	 */
	protected  DimChart getChartHost(JSONObject json) {
		assert (json != null);
		
		// TESTING TESTING TESTING
		//Printer.print(json.toString());

		DimChart chart = getDimChart(json);
		
		
		if (chart == null) {
			Printer.pfail("Creating chart host.");
			return null;
		}
		
		return chart;
	}

	/**
	 * Creates a DimChart from a given JSONObject.
	 * 
	 * @param json from which DimChart gets created
	 * @return a DimChart from a given JSONObject
	 */
	private  DimChart getDimChart(JSONObject json) {
		assert (json != null);
		
		// get a reader
		JSONReader reader = new JSONReader(json);

		// getting basic fields
		String chartType = reader.getString(JSON_CHART_TYPE);
		String x = reader.getString(JSON_X);		
		String xDim = reader.getString(JSON_X_DIM);
		String measure = reader.getString(JSON_MEASURE);
		String aggregation = reader.getString(JSON_AGGREGATION);
		if (chartType == null) {
			Printer.pproblem("Chart type not found.");
			chartType = ConfigWrap.NOT_AVAILABLE;
		}
		if ((x == null) || (xDim == null) || (measure == null) || (aggregation == null)) {
			Printer.pfail("Get basic field for DimChart.");
			return null;
		}
		
		// create the measure
		Measure msr = getMeasure(measure, aggregation);
		if (msr == null) {
			Printer.pfail("Get measure.");
			return null;
		}
		
		// create x-Filter
		Filter xFilter = getFilterFor(xDim, reader);
		if (xFilter == null) {
			Printer.pfail("Get x filter.");
			return null;
		}
		
		
		// create y-Filter
		boolean hasY = json.has(JSON_Y);

		String y = null;
		String yDim = null;
		Filter yFilter = null;
		if (hasY) {
			y = reader.getString(JSON_Y);
			yDim = reader.getString(JSON_Y_DIM);
			if ((y == null) || (yDim == null)) {
				Printer.pfail("Get y field for DimChart.");
				return null;
			}
			
			yFilter = getFilterFor(yDim, reader);
			if (yFilter == null) {
				Printer.pfail("Get y filter.");
				return null;
			}
		}
		
		// create other filters
		ArrayList<Filter> filters = getFilters(reader);
		if (filters == null) {
			Printer.pfail("Getting the filters");
			return null;
		}

		// create the DimCharts
		if (hasY) {
			return new TwoDimChart(chartType, x, xFilter, y, yFilter, msr, filters);
		} else {
			return new DimChart(chartType, x, xFilter, msr, filters);
		}
	}

	/**
	 * Returns a Measure for the given measure name,
	 * describing a RowEntry in the configuration and an aggregation.
	 * 
	 * @param measure name of a RowEntry describing a measure in the configuration
	 * @param aggregation aggregation for the measure
	 * @return a Measure for the given parameters or null if it fails
	 */
	private Measure getMeasure(String measure, String aggregation) {
		assert (measure != null);
		
		RowEntry row = config.getRowEntryFor(measure);
		if (row == null) {
			Printer.pfail("Getting row for measure: " + measure);
			return null;
		}
		
		return Measure.getMeasure(aggregation, row);
	}

	// -- FILTER -- FILTER -- FILTER -- FILTER -- FILTER -- FILTER --
	// filter summary method
	/**
	 * Returns a ArrayList of Filters extracted with the given
	 * JSONReader.
	 * 
	 * @param reader JSONReader containing the JSONObject 
	 * 			from which they get extracted
	 * @return a ArrayList of Filters extracted with the given
	 * 			JSONReader, null if it fails.
	 */
	private ArrayList<Filter> getFilters(JSONReader reader) {
		assert (reader != null);
		
		ArrayList<Filter> filters = new ArrayList<Filter>();
		
		JSONArray filterArray = reader.getJSONArray(JSON_FILTER);
		for (int i = 0, l = filterArray.length(); i < l; i++) {
			
			// try to get a object
			Object o = null;
			try {
				o = filterArray.get(i);
			} catch (JSONException e) {
				Printer.perror("Getting object from filter array.");
				return null;
			}
			
			// check whether it is a String
			if (!(o instanceof String)) {
				Printer.perror("Field in filter array is not a String.");
				return null;
			}
			
			// get the filter for this field
			String s = (String) o;
			Filter curFilter = getFilterFor(s, reader);	
			if (curFilter == null) {
				Printer.pfail("Getting filter for: " + s);
				return null;
			}
			
			filters.add(curFilter);
		}
		
		return filters;
	}
	
	// main filter creation method
	/**
	 * Returns a Filter for a name of a dimension and a reader.
	 * 
	 * @param name of a dimension
	 * @param reader JSONReader containing information
	 * @return  a Filter for a name of a dimension and a reader
	 */
	private Filter getFilterFor(String name, JSONReader reader) {
		assert (name != null);
		assert (reader != null);
		
		// getting the dimension of this filter
		DimRow dim = config.getDimRowFor(name);
		if (dim == null) {
			Printer.perror("Given filter name describes no dimension: " + name);
			return null;
		}		
		
		// special treatment for time
		if (name.equalsIgnoreCase(JSON_TIME)) {
			return getTimeFilter(name, dim, reader);
		}
		
		
		// get the filters 
		TreeSet<DimKnot> filterTree = getFilterTree(name, dim, reader);
		if (filterTree == null) {
			Printer.pfail("Geting filter tree for: " + name);
			return null;
		}
		
		return new Filter(dim, filterTree);
	}

	// first level
	/**
	 * Returns a set of DimKnots for a key in a JSONObject,
	 * a DimRow and a reader.
	 * 
	 * @param key in the JSONObject of the reader
	 * @param dim DimRow of the DimKnots
	 * @param reader JSONReader containing information
	 * @return a set of DimKnots for a key in a JSONObject
	 */
	private TreeSet<DimKnot> getFilterTree(String key, DimRow dim, JSONReader reader) {
		assert (key != null);
		assert (reader != null);
		
		// get JSON object with strings to filter for
		JSONObject values = reader.getJSONObject(key);
		if (values == null) {
			Printer.pfail("Getting the filter field.");
			return null;
		}
		
		
		TreeSet<DimKnot> filterSet = new TreeSet<DimKnot>();

		// get the filter field
		JSONObject obj = reader.getJSONObject(key);
		
		JSONReader subReader = new JSONReader(obj);
		
		
		// get the level on which we work and determine the RowEntry for it
		String level = subReader.getString(JSON_LVL);
		if (level == null) {
			Printer.pfail("Getting level field.");
		}
		RowEntry row = dim.getRowEntryFor(level);
		if (row == null) {
			Printer.pfail("Getting RowEntry for given level: " + level);
			return null;
		}
		
		// get the select array
		Object o = subReader.getObject(JSON_SELEC);
		if (o == null) {
			Printer.pfail("Getting JSONArray of filters for: " + JSON_SELEC);
			return null;
		}
			
		if (o instanceof String) { // all selected?
			String sel = (String) o;
			if (sel.equalsIgnoreCase(JSON_ALL)) { 
				// ALL is selected! >> return empty set to show that nothing has to be filtered
				return filterSet;
			} else {
				Printer.perror("Illegal statement for select: " + sel);
				return null;
			}
		} else if (o instanceof JSONArray) { 
			// it as a object so now the first level and then recursive things start
			JSONArray array = (JSONArray) o;
			
			// get all DimKnot of the first level
			DimKnot cur = null;
			for (int i = 0, l = array.length(); i < l; i++) {
				
				try {
					cur = getDimKnot(array.get(i), dim, row);
				} catch (JSONException e) {
					Printer.pfail("Getting a object from " + JSON_SELEC + " array.");
					return null;
				}
				
				if (cur == null) {
					Printer.pfail("Getting a DimKnot of first level.");
					return null;
				} 
				filterSet.add(cur);
			}
								
	
		} else {
			Printer.perror("Illegal statement for the filter, neither String nor JSONObject.");
		}
			
		return filterSet;
	}

	// deeper level
	/**
	 * Returns a DimKnot for a given dimension,
	 * an object, which may be an array containing other Knots
	 * or the name of the searched Knot.
	 * Row is the actual RowEntry of the searched DimKnot.
	 * 
	 * @param obj containing name and possible children Knots
	 * @param dim dimension of all this Knots
	 * @param row RowEntry of the requested DimKnot
	 * @return a DimKnot for a RowEntry and a Object containing name
	 * and possible children
	 */
	private static DimKnot getDimKnot(Object obj, DimRow dim, RowEntry row) {
		assert (obj != null);
		assert (dim != null);
		assert (row != null);
		
		// case it is a leaf
		if (obj instanceof String) {
			String name = (String) obj;
			return new DimKnot(name, row);
		}
		
		// case it is not a JSONObject -> error
		if (!(obj instanceof JSONObject)) {
			Printer.perror("Object in filter array was neither String nor JSONObject.");
		}
		
		// case it is a knot
		JSONObject json = (JSONObject) obj;
		JSONReader reader = new JSONReader(json);
		
		String value = reader.getString(JSON_PARENT);
		if (value == null) {
			Printer.pfail("Getting field for the value of the DimKnot for: " + JSON_PARENT);
		}
		DimKnot dk = new DimKnot(value, row);
		
		// determine the level of the children
		String level = reader.getString(JSON_LVL);
		if (level == null) {
			Printer.pfail("Getting level field.");
		}
		RowEntry childRow = dim.getRowEntryFor(level);
		if (childRow == null) {
			Printer.pfail("Getting RowEntry for given level: " + level);
		}
		
		// get children array
		JSONArray array = reader.getJSONArray(value);
		if (array == null) {
			Printer.pfail("Getting JSONArray of filters for: " + value);
		}
		
		
		for (int i = 0, l = array.length(); i < l; i++) {
			DimKnot child = null;
			try {
				child = getDimKnot(array.get(i), dim, childRow);
			} catch (JSONException e) {
				Printer.pfail("Getting a object from " + value + " array.");
			}
			
			if (child == null) {
				Printer.pfail("Getting a DimKnot of deeper level.");
				return null;
			} 
			
			// add the found child to the current DimKnot
			dk.addChild(child);
		}	
		
	
		return dk;
	}

	// -- TIME FILTER -- TIME FILTER -- TIME FILTER -- TIME FILTER --
	/**
	 * Returns a TimeFilter for the given dimension, key string and a reader.
	 * 
	 * @param time key in a JSONObject
	 * @param dim Dimension of the time
	 * @param reader JSONReader
	 * @return a TimeFilter for the given dimension, key string and a reader
	 */
	private TimeFilter getTimeFilter(String time, DimRow dim, JSONReader reader) {
		assert (time != null);
		assert (dim != null);
		assert (reader != null);
		
		// get the empty filters 
		TreeSet<DimKnot> filterTree = new TreeSet<DimKnot>();
		
		// get the filter field
		Object timeObj = reader.getObject(JSON_TIME);		
		if (timeObj instanceof String) { // all selected?
				String sel = (String) timeObj;
				if (sel.equalsIgnoreCase(JSON_ALL)) { 
				// ALL is selected! >> return empty set to show that nothing has to be filtered
					return new TimeFilter(dim, filterTree);
				} else {
					Printer.perror("Illegal statement for select: " + sel);
					return null;
				}
		} else if (timeObj instanceof JSONObject) {		
		
			// get the time scale field
			JSONObject timeObject = (JSONObject) timeObj;
		
			// get start and end
			int[] from = {0, 0, 0, 0, 0};
			int[] to = {0, 0, 0, 0, 0};
		
			int[] cur = getDate(timeObject, 0);
			if (cur != null) {
				from = cur;
			}
		
			cur = getDate(timeObject, 1);
			if (cur != null) {
				to = cur;
			}
					
			return new TimeFilter(dim, filterTree, from, to);
		} else {
			Printer.perror("Illegal statement for the filter, neither String nor JSONObject.");
			return null;
		}
		
	}
	
	/** Constant number in array for a date segment. */
	private static final int Y = 0;
	/** Constant number in array for a date segment. */
	private static final int M = 1;
	/** Constant number in array for a date segment. */
	private static final int D = 2;
	/** Constant number in array for a date segment. */
	private static final int H = 3;
	/** Constant number in array for a date segment. */
	private static final int MI = 4;
	/** Constant number as length of the time array. */
	protected static final int L = 5;
	
	/**
	 * Extracts the time array for a given JSONObject
	 * and 'from' for i = 0 or 'to' for i = 1.
	 * 
	 * @param timeObj JSONObject
	 * @param i 0 for from, 1 for to
	 * @return time array
	 */
	private int[] getDate(JSONObject timeObj, int i) {
		assert ((i == 0) || (i == 1));

		// get a reader
		JSONReader reader = new JSONReader(timeObj);
			
		// get a array
		int[] ary = new int[L];
		
		try {
			if (timeObj.has(YEAR)) {
				ary[Y] = reader.getJSONArray(YEAR).getInt(i);
			} else {
				ary[Y] = 0;
			}
			if (timeObj.has(MON)) {
				ary[M] = reader.getJSONArray(MON).getInt(i);
			} else {
				ary[M] = 0;
			}
			if (timeObj.has(DAY)) {
				ary[D] = reader.getJSONArray(DAY).getInt(i);
			} else {
				ary[D] = 0;
			}
			if (timeObj.has(HOUR)) {
				ary[H] = reader.getJSONArray(HOUR).getInt(i);
			} else {
				ary[H] = 0;
			}
			if (timeObj.has(MIN)) {
				ary[MI] = reader.getJSONArray(MIN).getInt(i);
			} else {
				ary[MI] = 0;
			}
		} catch (JSONException e) {
			Printer.perror("Getting a time field.");
			return null;
		}

		return ary;
	}

}

