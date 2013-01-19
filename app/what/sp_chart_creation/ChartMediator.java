package what.sp_chart_creation;

// java imports
import java.io.File;
import java.util.LinkedList;
import java.util.TreeSet;

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
 * @see Petra
 */
public class ChartMediator {
	
	/* Cache of ChartMediators */
	private static TreeSet<ChartMediator> mediatorCache = new TreeSet<ChartMediator>();
	
	/* Configuration on which this ChartMediator works on */
	private ConfigWrap config;
	
	/* The stored history of computed charts of this ChartMediator */
	private LinkedList<Petra> history;
	
// -------- ChartMediator creation and fly-weight pattern -------------
	/**
	 * Private constructor to ensure no duplicated objects
	 * for the same configuration.
	 * 
	 * @param confi configuration of the database 
	 * 		  on which this ChartMeditator operates
	 */
	public ChartMediator(ConfigWrap confi) {
		if (confi == null) {
			throw new IllegalArgumentException();
		}
		
		history = new LinkedList<Petra>(); 
		
		this.config = confi;
	}
	
	/**
	 * Static factory machine for ChartMediators.<br>
	 * 
	 * This factory ensures that no duplicates, referring to configurations,
	 * are produced. This is necessary for saving the latest
	 * chart request results.
	 * 
	 * @param confi
	 * @return
	 *
	public static ChartMediator getChartMediator(ConfigWrap confi) {
		if (confi == null) {
			throw new IllegalArgumentException();
		}
		
		// looks if it is already cached
		for (ChartMediator m : mediatorCache) {
			if (m.getConfig()  == confi) {
				return m;
			}
		}
		
		//TODO better error handling!
		ChartMediator medi = new ChartMediator(confi);
		
		
		return medi;
	}*/

	/**
	 * Returns the configuration for the database of this ChartMediator.
	 * 
	 * @return the ConfigWrap of this ChartMediator
	 */
	private ConfigWrap getConfig() {
		return config;
	}

	

// -------- Handling of a Chart request -------------------------------
	/**
	 * Computes a chart for the given parameters.<br>
	 * 
	 * Returns the file of the computed chart. Also it saves the chart
	 * in the history.
	 * 
	 * @param list list of parameters $%&/()
	 * @return the computed chart for the given parameters
	 */
	public File computeChart(int list) {
		if (list < 0) { //TODO change when list is correct
			throw new IllegalArgumentException();
		}
		
		// get a chart host
		Petra host = getChartHost(list);
		
		// manage visits
		if(!manageVisits(host)) {
			throw new RuntimeException(); //TODO better exception or error managing
		}
		
		// add to history, may be improved
		addToHistory(host);
		
		
		return host.getJSON();
	}

	
	

	private Petra getChartHost(int list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Manages the visits to the chart host {@linkplain Petra}.
	 * 
	 * @param host a chart host
	 * @return whether the visits where successful
	 * @see Petra
	 */
	private boolean manageVisits(Petra host) {
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

	

	// -------- Handling a request for a history chart --------------------
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
			throw new IndexOutOfBoundsException();
		}
				
		return history.get(l - number).getJSON();
	}

	/**
	 * Adds a chart host to the history.
	 * 
	 * @param toSave chart host that has to be saved
	 */
	private void addToHistory(Petra toSave) {
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

