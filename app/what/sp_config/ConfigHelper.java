package what.sp_config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigHelper {
	
	protected static final String DB_NAME = "Name";
	protected static final String VERSION = "Version";
	protected static final String FIELDS = "Fields";

	protected static final String ATRI_TYPE = "Type";
	protected static final String TYPE_INT = "INT";
	protected static final String TYPE_DOUBLE = "DOUBLE";
	protected static final String TYPE_STRING = "STRING";
	protected static final String TYPE_LOCATION = "IP";
	protected static final String TYPE_STRINGMAP = "STRINGMAP";
	protected static final String TYPE_DUMMY = "NULL";

	protected static final String ATRI_CAT = "Categorie";
	protected static final String ATRI_LVL = "Level";
	protected static final String ATRI_NAME = "Name";
	protected static final String ATRI_LOG = "LogId";
	protected static final String ATRI_SCL = "Scale";
	
	protected static final String ATRI_STRINGS = "List";
	protected static final String STRING_NAME = "ParentName";

	
	
	private ConfigHelper() {
		// Method class, not there to be instanced
	}
	
	protected static File getConfigFile(String path) throws IOException {
		assert (path != null);
		
		File configFile = new File(path);
		
		if (!configFile.exists() || !configFile.isFile()) {
			throw new IOException();
		} else if (!configFile.exists() || !configFile.canRead()) {
			throw new IOException();
		}
		
		return configFile;
	}

	protected static String getJSONContent(File configFile) throws IOException, FileNotFoundException {
		assert (configFile != null);
		
		// I hope standard readers will do, otherwise we have to specify some for ourselves		
		Reader in = new FileReader(configFile);
		BufferedReader bIn = new BufferedReader(in);
		
		String content = readFile(bIn);
		
		if (content == null) {
			throw new IllegalArgumentException();
		}
		
		return content;
	}

	private static String readFile(BufferedReader bIn) throws IOException {
		String cur;
		String content = "";
		while ((cur = bIn.readLine()) != null) {
			content = content + cur;
		}
		
		return content;
	}
	
	protected static RowEntry getEntry(JSONObject jso) throws JSONException {
		assert (jso != null);

		String type = jso.getString(ATRI_TYPE);
		RowId id = RowId.getRowIdByString(type);
		
		if (id == null) {
			return null;	// TODO have to change this...
		}
		
		RowEntry re = getEntryById(id, jso);
		
			
		return re;		
	}

	private static RowEntry getEntryById(RowId id, JSONObject jso) throws JSONException {
		assert (id != null);
		assert (jso != null);
		
		String name = jso.getString(ATRI_NAME);
		String logId = jso.getString(ATRI_LOG);
		String cat = jso.getString(ATRI_CAT);
		String scale = jso.getString(ATRI_SCL);
		int lvl = jso.getInt(ATRI_LVL);
				
		switch (id) {
		case INT : 
			return new IntRow(name, logId, lvl, cat, scale);
		case DOUBLE :
			return new DoubleRow(name, logId, lvl, cat, scale);
		case STRING : 
			Set<String> strings = getStrings(jso);
			return new StringRow(name, logId, lvl, cat, scale, strings);
		case LOCATION :
			return new IpLocationRow(name, logId, lvl, cat, scale);
		case STRINGMAP :
			HashMap<String, Set<String>> maps = getMap(jso);
			return new StringMapRow(name, logId, lvl, cat, scale, maps);
		case DUMMY :
			return DummyRow.getInstance();
		default :
			//throw new JSONException("No matching row found.");
		}
			
		return null;
	}

	private static HashMap<String, Set<String>> getMap(JSONObject jso) throws JSONException {
		assert (jso != null);
		
		HashMap<String, Set<String>> maps = new HashMap<String, Set<String>>();
		
		JSONArray contents = jso.getJSONArray(ATRI_STRINGS);
		
		for (int i = 0, l = contents.length(); i < l; i++) {
			JSONObject obj = (JSONObject) contents.get(i);
			String key = obj.getString(STRING_NAME);
			Set<String> values = getStrings(obj);
			maps.put(key, values);
		}
		
		return maps;
	}

	private static TreeSet<String> getStrings(JSONObject jso) throws JSONException {
		assert (jso != null);
		
		JSONArray contents = jso.getJSONArray(ATRI_STRINGS);
		TreeSet<String> strings = new TreeSet<String>();
		for (int i = 0, j = contents.length(); i < j; i++) {
			strings.add(contents.getString(i));
		}
		
		return strings;
	}


}
