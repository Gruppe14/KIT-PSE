package what.sp_chart_creation;

// java imports
import java.util.LinkedList;

// JSON imports
import org.json.JSONObject;

//intern imports
import what.Printer;
import what.sp_config.ConfigWrap;
import what.sp_data_access.DataMediator;

/**
 * A ChartMediator mediates a chart request.<br>
 * 
 * When a chart is requested, this class handles the process
 * which follows the visitor pattern. 
 * 
 * @author Jonathan, PSE Gruppe 14
 *
 * @see ChartVisitor
 * @see DimChart
 */
public class ChartMediator {
	
	/** Configuration on which this ChartMediator works on. */
	private ConfigWrap config;
	
	/** The DataMediator with which this ChartMediator works. */
	private DataMediator dataMedi;
	
	/** The ChartHostBuilder with which this ChartMediator works. */
	private ChartHostBuilder builder;
	
	/** The size of the history. */
	private int size_history = 10;
	
	/** The maximal size of the history. */
	private int MAX_SIZE_FOR_HISTORY = 100;
	
	/**
	 * Sets the size for the history.
	 * 
	 * @param size_history Size for the history length;
	 * must be between 1 and 100
	 */
	public void setMaxSizeForHistory(int size_history) {
		if ((size_history > 0) && (size_history < MAX_SIZE_FOR_HISTORY)) {
			this.size_history = size_history;
		}
		
	}

	/** The stored history of computed charts of this ChartMediator. */
	private LinkedList<DimChart> history;
	
	/**
	 * Public constructor for a ChartMediator.
	 * 
	 * @param confi configuration on which work bases
	 * @param dataMedi DataMediator to which it must connect
	 */
	public ChartMediator(ConfigWrap confi, DataMediator dataMedi) {
		if (confi == null) {
			throw new IllegalArgumentException();
		} else if (dataMedi == null) {
			throw new IllegalArgumentException();
		} 
		
		history = new LinkedList<DimChart>(); 
		
		this.config = confi;
		this.dataMedi = dataMedi;
		
		this.builder = new ChartHostBuilder(config);
	}
	
	// -- CHART REQUEST -- CHART REQUEST -- CHART REQUEST -- CHART REQUEST --
	/**
	 * Computes a chart for the given parameters.<br>
	 * 
	 * Returns the file of the computed chart. Also it saves the chart
	 * in the history.
	 * 
	 * @param json list of parameters $%&/()
	 * @return the computed chart for the given parameters
	 */
	public JSONObject computeChart(JSONObject json) {
		if (json == null) { 
			throw new IllegalArgumentException();
		}
		
		// get a chart host
		DimChart chart = builder.getChartHost(json);
		if (chart == null) {
			Printer.pfail("Creating chart host.");
			return null;
		} else {
			Printer.psuccess("Creating chart host.");
			//Printer.ptest(chart.toString());
		}
		
		// test integrity
		if (!isCorrectChart(chart)) {
			Printer.psuccess("Chart host contains unknown attributes.");
			return null;
		}
		
		// compute the JSON chart for it
		if (computeJSONFor(chart)) {
			addToHistory(chart);
			//Printer.ptest(chart.getJson().toString());
			return chart.getJson();
		}

		return null;
	}

	/**
	 * Checks whether the given chart is of integrity.
	 * 
	 * @param chart chart to check
	 * @return whether it is of integritry
	 */
	private boolean isCorrectChart(DimChart chart) {
		assert (chart != null);
		
		if (!(Filter.checkIntegrityOfFilter(chart.getXFilter()))) {
			Printer.perror("Incorrect x-filter " + chart.getXFilter().getCategory() 
					+ ", contains bad material.");
			return false;
		}
		
		for (Filter f : chart.getFilters()) {
			if (!(Filter.checkIntegrityOfFilter(chart.getXFilter()))) {
				Printer.perror("Incorrect filter " + f.getCategory() 
						+ ", contains bad material.");
				return false;
			}
		}
		
		
		if (chart instanceof TwoDimChart) {
			TwoDimChart twoChart = (TwoDimChart) chart;
			if (!(Filter.checkIntegrityOfFilter(twoChart.getYFilter()))) {
				Printer.perror("Incorrect y-filter " + twoChart.getYFilter().getCategory() 
						+ ", contains bad material.");
				return false;
			}
		}
		
		
		return true;
	}

	/**
	 * Computes the JSONObject for the given chart and stores it in it.
	 * 
	 * @param chart DimChart for which the JSONObject will be created
	 * @return whether it was successful
	 */
	private boolean computeJSONFor(DimChart chart) {
		assert (chart != null);
		
		if (!(dataMedi.requestDimJSON(chart))) {
			Printer.pfail("Executing chart request.");
			return false;
		}
		
		return true;		
	}

	// -- REQUEST HISTORY -- REQUEST HISTORY -- REQUEST HISTORY --
	/**
	 * Returns the JSONObject of a requested chart from the history.<br>
	 * 
	 * Referring to the given number a chart from the history is returned.
	 * Thereby the a lower number indicates a later chart.
	 * E.g. 1 stands for the newest one, 6 for the 6 latest. 
	 * 
	 * @param number number of the latest computed chart, range from 1 (latest) to 10 (oldest)
	 * @return the JSONObject of the requested chart from the history, referring to the number
	 */
	public JSONObject getHistoryChart(int number) {
		if ((number < 0) || (number >= getMaxSizeOfHistory())) {
			throw new IllegalArgumentException();
		}
		
		int l = getCurrentSizeOfHistory();
		
		// number to high, not enough charts in history!
		if (number >= l) {
			Printer.pproblem("Not so many charts stored.");
			return null;
		}
				
		// for testing
		//Printer.ptest("History chart number: " + number 
		//				+ "\n" + history.get(number).toString());
		
		return history.get(number).getJson();
	}

	/**
	 * Adds a chart host to the history.
	 * 
	 * @param toSave chart host that has to be saved
	 */
	private void addToHistory(DimChart toSave) {
		assert (toSave != null);
		
		// ensures that history doesn't get bigger than wanted
		if (history.size() >= getMaxSizeOfHistory()) {
			history.removeLast();
		}
		
		// adds it to history
		history.addFirst(toSave);
	}

	/**
	 * Returns the maximal number of charts possible to store in the history.
	 * 
	 * @return the maximal number of charts possible to store in the history
	 */
	public int getMaxSizeOfHistory() {
		return size_history;
	}

	/**
	 * Returns the current number of charts stored in the history.
	 * 
	 * @return the current number of charts stored in the history
	 */
	public int getCurrentSizeOfHistory() {
		return  history.size();
	}
}
 
