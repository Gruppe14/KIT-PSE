package what.sp_data_access;

// java imports
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.TreeSet;

// JSON imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import what.Printer;
import what.sp_chart_creation.DimChart;

/**
 * Helper class providing methods 
 * to transform result sets into specific other things
 * 
 * @author Jonathan, PSE
 * @see MySQLAdapter
 */
public class DataChanger {
	
	private static final String ATT1 = "attribute1";
	private static final String ATT2 = "attribute2";
	//private static final String ATT3 = "attribute3";
	private static final String DATA = "data";

	/**
	 * Transforms a ResultSet into a TreeSet of Strings.
	 *  
	 * @param re ResultSet from which data will be extracted
	 * @return TreeSet of Strings extracted from the ResultSet
	 */
	protected static TreeSet<String> getTreeSetFromRS(ResultSet re) {
		assert (re != null);
		
		TreeSet<String> strings = new TreeSet<String>();
		
		try {
			while (re.next()) {
				strings.add(re.getString(1));
			}
		} catch (SQLException e) {
			Printer.perror("Reading from ResultSet.");
			return null;
		}
		
		return strings;
	}

	/**
	 * Returns a JSONObject containing a array of pairs from x and measure
	 * extracted from the given ResultSet.
	 * Also it contains the identifier for this to variables.
	 * 
	 * @param re ResultSet containing the information
	 * @param chart second column in the ResultSet
	 * @return a JSONObject containing a array of pairs from x and measure
	 * 			extracted from the given ResultSet
	 */
	protected static JSONObject getOneDimJSONFromResultSet(ResultSet re, DimChart chart) {
		assert (re != null);
		assert (chart != null);
		
		String m = chart.getMeasure();
		String x = chart.getX();
		
		// create JSONObject and put first variables
		JSONObject json = new JSONObject();
		try {
			json.put(ATT1, x);
			json.put(ATT2, chart);
		} catch (JSONException e) {
			Printer.perror("Putting into JSONObject.");
			return null;
		}
		
		JSONArray aray = null;
		HashSet<JSONObject> sum = new HashSet<JSONObject>();
		
		try {
			while (re.next()) {
				JSONObject a = new JSONObject();
				a.put(x, re.getString(1));
				a.put(m, re.getDouble(2));
				sum.add(a);
			}
			aray = new JSONArray(sum);
			json.put(DATA, aray);
		} catch (SQLException e) {
			Printer.perror("Reading from ResultSet.");
			return null;
		} catch (JSONException e) {
			Printer.perror("Putting into JSONObject.");
			return null;
		}
		
		
		return json;
	}

}
