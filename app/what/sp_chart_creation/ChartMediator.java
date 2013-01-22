package what.sp_chart_creation;

// java imports
import java.io.File;
import java.util.LinkedList;
import java.util.TreeSet;

import what.JSON_Helper;
// intern imports
import what.sp_config.ConfigWrap;

/**
 * A ChartMediator mediates a chart request.<br>
 * 
 * When a chart is requested, this class handles the process
 * which follows the visitor pattern. 
 * 
 * @author Jonathan, PSE Gruppe 14
 * @version 1.0
 *
 * @see ChartVisitor
 * @see OneDimChart
 */
public class ChartMediator {
	
	/** Configuration on which this ChartMediator works on */
	private ConfigWrap config;
	
	/** The stored history of computed charts of this ChartMediator */
	private LinkedList<OneDimChart> history;
	

	public ChartMediator(ConfigWrap confi) {
		if (confi == null) {
			throw new IllegalArgumentException();
		}
		
		history = new LinkedList<OneDimChart>(); 
		
		this.config = confi;
	}
	
	

// -------- Handling of a Chart request -------------------------------
	/**
	 * Computes a chart for the given parameters.<br>
	 * 
	 * Returns the file of the computed chart. Also it saves the chart
	 * in the history.
	 * 
	 * @param path list of parameters $%&/()
	 * @return the computed chart for the given parameters
	 */
	public File computeChart(String path) {
		if (path == null) { 
			throw new IllegalArgumentException();
		}
		
		// get a chart host
		OneDimChart host = getChartHost(path);
		
		// manage visits
		if(!manageVisits(host)) {
			System.out.println("ERROR: A visit failed!");
		}
		
		// add to history, may be improved
		addToHistory(host);
		
		
		return host.getJSON();
	}

	
	

	// -- BUILDING CHARTHOST -- BUILDING CHARTHOST -- BUILDING CHARTHOST --
	private OneDimChart getChartHost(String path) {

		// gets the file
		File configFile = JSON_Helper.getJSONFile(path);
				
		// gets the content of the file
		String jsonContent = JSON_Helper.getJSONContent(configFile);
		
		
		
		
		return null;
	}
	
	
	
	
	/**
	 * Manages the visits to the chart host {@linkplain OneDimChart}.
	 * 
	 * @param host a chart host
	 * @return whether the visits where successful
	 * @see OneDimChart
	 */
	private boolean manageVisits(OneDimChart host) {
		assert (host != null);
		
		if (!host.accept(ChartDataRequester.getInstance())) {
			return false;
		}
		
		if (!host.accept(DataPreparer.getInstance())) {
			return false;
		}
		
		if (!host.accept(JsonWriter.getInstance())) {
			return false;
		}
		
		return true;
	}

	

	// -- REQUEST HISTORY -- REQUEST HISTORY -- REQUEST HISTORY --
	/**
	 * Returns the json-file of a requested chart from the history.<br>
	 * 
	 * Referring to the given number a chart from the history is returened.
	 * Thereby the a lower number indicates a later chart.
	 * E.g. 1 stands for the newest one, 6 for the 6 latest. 
	 * 
	 * @param number number of the latest computed chart, range from 1 (latest) to 10 (oldest)
	 * @return the json-file of the requested chart from the history, referring to the number
	 */
	public File getHistoryChart(int number) {
		if ((number <= 0) || (number > getSizeForHistory() )) {
			throw new IllegalArgumentException();
		}
		
		int l = history.size();
		
		// number to high, not enough charts in history!
		if (number > l) {
			System.out.println("ERROR: Not so many charts stored");
			return history.get(history.size()).getJSON();
		}
				
		return history.get(number).getJSON();
	}

	/**
	 * Adds a chart host to the history.
	 * 
	 * @param toSave chart host that has to be saved
	 */
	private void addToHistory(OneDimChart toSave) {
		assert (toSave != null);
		
		// ensures that history doesn't get bigger than wanted
		if (history.size() >= getSizeForHistory()) {
			history.removeLast();
		}
		
		// adds it to history
		history.addFirst(toSave);
	}

	/**
	 * Returns the number of charts stored in the history.
	 * @return the number of charts stored in the history
	 */
	private int getSizeForHistory() {
		// TODO global constant
		return 10;
	}

}

