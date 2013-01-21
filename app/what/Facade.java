package what;

// java imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// org.json imports
import org.json.JSONException;


// intern imports
import what.sp_config.ConfigWrap;
import what.sp_config.DimRow;
import what.sp_parser.ParserMediator;
import what.sp_chart_creation.ChartMediator;
import what.sp_dataMediation.DataMediator;

/**
 * This class Facade represents a facade.<br>
 * 
 * More precisely, it is the facade for the application tier, 
 * with the task to receive all requests or calls from the 
 * web server tier.
 * 
 * @author Jonathan, PSE Gruppe 14
 * @version 1.0
 *
 * @see ChartMediator
 * @see ParserMediator
 * @see ConfigWrap
 */
public class Facade {
	
	/**	Current Configuration on which all work is based with this Facade. */
	private ConfigWrap currentConfig;
	
	/** ParserMediator to which work is directed from this Facade. */
	private ParserMediator parsMedi;
	
	/** ChartMediator to which work is directed from this Facade. */
	private ChartMediator chartMedi;
	
	/** DataMediator to which work is directed from this Facade. */
	private DataMediator dataMedi;
	 
	
	// -- INIT -- RESET -- INIT -- RESET -- INIT --
	/**
	 * Initializes this facade with a ConfigWrap, what is necessary
	 * to post requests to it.
	 * 
	 * @param path of the configuration file (.json)
	 * @return whether initialization was successful
	 */
	public boolean init(String path) {
		if (path == null) {						 
			throw new IllegalArgumentException();
		}
		
		ConfigWrap config = null;
		
		// tries to build a new ConfigWrap
		try {
			config = ConfigWrap.buildConfig(path);
		} catch (JSONException e) {
			System.out.println("Building ConfigWrap with the given path failed"
								+ "because something went wrong with JSON!");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.out.println("Building ConfigWrap with the given path failed "
								+ "because of an IO failure!");
			e.printStackTrace();
			return false;
		}
		
		currentConfig = config;
		
		parsMedi = new ParserMediator(config);
		chartMedi = new ChartMediator(config);	
		
		return true;
	}
	
	/**
	 * Checks whether the facade and the ConfigWrap have been initialized.
	 * 
	 * @return whether the facade and the ConfigWrap have been initialized
	 */
	private boolean isInitialized() {
		return ((parsMedi != null) && (chartMedi != null));
	}
	
	/**
	 * Resets the facade.
	 */
	public void reset() {
		currentConfig = null;
		parsMedi = null;
		chartMedi = null;
	}
	
	// -- REQUESTS -- REQUESTS -- REQUESTS -- -- REQUESTS --
	/**
	 * Directs a parsing request to a ParserMediator.<br>
	 * 
	 * Referring to the given configuration (id), it directs the request of parsing
	 * a given log-file (path) to a ParserMediator.
	 * 
	 * @param path path of the log file, which has to be parsed
	 * @return whether parsing this log file was successful, to a certain point;
	 * 			therefore {@linkplain ParserMediator}
	 * @see ParserMediator 
	 */
	public boolean parseLogFile(String path) {
		if (path == null) {						 
			throw new IllegalArgumentException();
		}
		
		if (!isInitialized()) {
			//throw new NotImplementedException(); //TODO better exception
		}
		
		// directs the request
		if (!parsMedi.parseLogFile(path)) {
			System.out.println("Parsing failed!");
			return false;		
		}
		
		// set up data after parsing
		if (!dataMedi.organizeData()) {
			System.out.println("Setting up data after parsing failed!");
			return false;
		}
		
		return true;
	}

	/**
	 * Directs a chart request to a ChartMediator.<br>
	 * 
	 * Referring to a given configuration (id), it directs a request
	 * for a chart to a ChartMediator. 
	 * 
	 * @param list list of options for the request
	 * @return a json-file which contains all information about the requested chart
	 */
	public File computeChart(int list) { //TODO specify parameter list! what will it be?
		if (list == 0) { //TODO change!
			throw new IllegalArgumentException();
		}

		if (!isInitialized()) {
			//throw new NotImplementedException(); //TODO better exception
		}
		
		// direct request and receive file
		File json = chartMedi.computeChart(list);	
		
		if (json == null) {
			//TODO error handling
		}
		
		return json;
	}
	
	/**
	 * Request a old chart request from the history of a ChartMediator.<br>
	 * 
	 * Referring to a given configuration (id), it requests a chart from the history
	 * of a ChartMediator. Thereby it requests the one, indicated by the given number.
	 * E.g. 1 stands for the newest one, 6 for the 6 latest. 
	 * 
	 * @param number number of the latest computed chart, range from 1 (latest) to 10 (oldest)
	 * @return the json-file of the requested chart, referring to the id and the number
	 */
	public File historyChart(int id, int number) {
		if (id <= 0) {
			throw new IllegalArgumentException();
		} else if ((number <= 0) || (getSizeOfHistory() > 10)) { 
			throw new IllegalArgumentException();
		}
		
		
		// request the chart
		File histo = chartMedi.getHistoryChart(number);	
		if (histo == null) {
			System.out.println("Config not initialized => no chart requests allowed!");
			
		}
		
		return histo;
	}
	
	/**
	 * Returns the dimensions and rows.
	 * 
	 * @return the dimensions and rows
	 */
	public ArrayList<DimRow> getDimensions() {
		if (!isInitialized()) {
			//throw new NotImplementedException(); //TODO better exception
		}
		
		ArrayList<DimRow> dims = currentConfig.getDims(); 
			
		return dims;		
	}
	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 	
	/**
	 * Returns the number of charts stored in the history.
	 * 
	 * @return the number of charts stored in the history
	 */
	private int getSizeOfHistory() {
		// TODO Auto-generated method stub
		return 10;
	}


	/**
	 * @return the currentConfig
	 */
	public ConfigWrap getCurrentConfig() {
		return currentConfig;
	}


	
}
