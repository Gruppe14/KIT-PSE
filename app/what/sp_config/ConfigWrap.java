package what.sp_config;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 

public class ConfigWrap {
	
	/** Name of the data base for which this ConfigWrap is */
	private String dbName;
	
	/** Version of this ConfigWrap for a database. */
	private String version; // not necessary yet, 
						   // but i see potential and so it may be better to be in from the beginning
	
	/** The entries in order of the appearance in the log file */
	private RowEntry[] entries; 

	/**
	 * Private constructur as the building is solved with a static factory.
	 */
	private ConfigWrap() {
		
	}
	
	// -- CONSTRUCTION -- CONSTRUCTION -- CONSTRUCTION -- CONSTRUCTION -- 
	/**
	 * Static factory for ConfiWraps.<br>
	 * Builds up ConfigWraps from a given configuration file. 
	 * 
	 * @param path the path of the configuration file 
	 * 		  from witch this ConfigWrap will be build up
	 * @return a ConfigWrap build up from the given file
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static ConfigWrap buildConfig(String path) throws IOException, JSONException {
		if (path == null) {
			throw new IllegalArgumentException();
		}
				
		File configFile = ConfigHelper.getConfigFile(path);
		
		String jsonContent = ConfigHelper.getJSONContent(configFile);
		
		JSONObject json = new JSONObject(jsonContent);
		
		ConfigWrap confi = buildConfig(json);
		
		return confi;
	}
		
	
	private static ConfigWrap buildConfig(JSONObject json) throws JSONException {
		assert (json != null);
		
		ConfigWrap confi = new ConfigWrap();
		
		confi.dbName = json.getString(ConfigHelper.DB_NAME);
		confi.version = json.getString(ConfigHelper.VERSION);
		
		JSONArray rows = json.getJSONArray(ConfigHelper.FIELDS);
		
		confi.entries = new RowEntry[rows.length()];
		
		buildConfigEntries(confi, rows);
		
		return confi;
	}

	private static void buildConfigEntries(ConfigWrap confi, JSONArray jsRows) throws JSONException {
		assert (confi != null);
		assert (jsRows != null);
	
		for (int i = 0, l = jsRows.length(); i < l; i++) {
			JSONObject jso = (JSONObject) jsRows.get(i); //TODO protect this, check this
			confi.addEntry(jso, i);
		}
		
		
	}



	
	
	private void addEntry(JSONObject jso, int i) {
		// TODO Auto-generated method stub
		
	}

	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	public int getSize() {
		return entries.length;
	}
	
	public RowEntry getEntryAt(int i) {
		if ((i < 0) || (i >= entries.length)) {
			throw new IllegalArgumentException();
		}
		
		return entries[i];
	}

	public String getNameOfDB() {
		return dbName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String verson) {
		this.version = verson;
	}
	


	
}
