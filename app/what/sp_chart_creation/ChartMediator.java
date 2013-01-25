package what.sp_chart_creation;

// java imports
import java.util.LinkedList;

// JSON imports
import org.json.JSONException;
import org.json.JSONObject;

//intern imports
import what.sp_config.ConfigHelper;
import what.sp_config.ConfigWrap;
import what.sp_dataMediation.DataMediator;

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
 * @see DimChart
 */
public class ChartMediator {
	
	/** Configuration on which this ChartMediator works on */
	private ConfigWrap config;
	
	private DataMediator dataMedi;
	
	/** The stored history of computed charts of this ChartMediator */
	private LinkedList<DimChart> history;
	
	/**
	 * Public constructor for a ChartMediator.
	 * 
	 * @param confi config on which work bases
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
	}
	
// -------- Handling of a Chart request -------------------------------
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
		DimChart host = ChartHelper.getChartHost(json);
		System.out.println("Chart created!");
		if (host == null) {
			System.out.println("ERROR: Creating chart host failed!");
			return null;
		}
		
		computeFileFor(host);

		
		
		// add to history, may be improved
		addToHistory(host);
		
		
		return host.getJson();
	}

	/**
	 * Computes the file for the given chart and stores it in it.
	 * @param chart DimChart for which the 
	 */
	private void computeFileFor(DimChart chart) {
		assert (chart != null);
		
		if (chart instanceof TwoDimChart) {
			// TODO
		} else {
			String xKey = config.getTableKeyFor(chart.getxCategory());
			
			String x = chart.getX();
			
			// check if just a category is selected
			if (!(x.contains(ConfigHelper.ROW_TABLE))) {
				if (config.isCategorie(x)) {
					x = config.getHighestRowFor(x);
				}
			}
			
			
			
			

			JSONObject j = dataMedi.requestTwoDimJSON(x, chart.getxTable(), xKey, chart.getMeasure(), chart.getFilters());
			if (j == null) {
				System.out.println("ERROR: No JSONObject retruned for chart request.");
			}
			try {
				j.put("chartType", chart.getChartType());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			chart.setJSON(j);
			
		}
		
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
	public JSONObject getHistoryChart(int number) {
		if ((number <= 0) || (number > getSizeForHistory() )) {
			throw new IllegalArgumentException();
		}
		
		int l = history.size();
		
		// number to high, not enough charts in history!
		if (number > l) {
			System.out.println("ERROR: Not so many charts stored");
			return history.get(history.size()).getJson();
		}
				
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

