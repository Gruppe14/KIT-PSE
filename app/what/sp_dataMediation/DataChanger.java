package what.sp_dataMediation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataChanger {
	
	private static final String ATT1 = "attribute1";
	private static final String ATT2 = "attribute2";
	private static final String DATA = "data";


	public static TreeSet<String> getTreeSetFromRS(ResultSet re) {
		TreeSet<String> strings = new TreeSet<String>();
		
		try {
			while (re.next()) {
				strings.add(re.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Reading from result set failed.");
			e.printStackTrace();
		}
		
		return strings;
	}

	public static JSONObject getFileFromResultSet(ResultSet re, String x, String measure) {
		JSONObject json = new JSONObject();
		
		try {
			json.put(ATT1, x);
			json.put(ATT2, measure);
		} catch (JSONException e) {
			System.out.println("ERROR: JSON failure.");
			e.printStackTrace();
		}
		
		JSONArray aray = null;
		HashSet<JSONObject> sum = new HashSet<JSONObject>();
		
		try {
			while (re.next()) {
				JSONObject a = new JSONObject();
				a.put(x, re.getString(1));
				a.put(measure, re.getDouble(2));
				sum.add(a);
			}
			aray = new JSONArray(sum);
			json.put(DATA, aray);
		} catch (SQLException e) {
			System.out.println("ERROR: ResultSet failure.");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("ERROR: JSON failure.");
			e.printStackTrace();
		}
		
		
		return json;
	}

}
