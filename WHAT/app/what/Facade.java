package appTier;

// java imports
import java.io.File;

// intern imports
import appTier.sb_config.ConfigDB;

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
 * @see ConfigDB
 */
public class Facade {

	/**
	 * Directs a parsing request to a ParserMediator.<br>
	 * 
	 * Referring to the given configuration (id), it directs the request of parsing
	 * a given log-file (path) to a ParserMediator.
	 * 
	 * @param id identifies the configuration/on which database is operated
	 * @param path path of the log file, which has to be parsed
	 * @return whether parsing this log file was successful, to a certain point;
	 * 			therefore {@linkplain ParserMediator}
	 * @see ParserMediator 
	 */
	public boolean parseLogFile(int id, String path) {
		if (id <= 0) {
			throw new IllegalArgumentException();
		} else if (path == null) {						 //TODO better check?
			throw new IllegalArgumentException();
		}
		
		// get the right configuration object
		ConfigDB confi = ConfigDB.getConfi(id);
		if (confi == null) {
			throw new IllegalArgumentException();
		}		
		
		// directs the right mediator
		ParserMediator mediator = ParserMediator.getParserMediator(confi);
		if (mediator == null) {
			throw new IllegalArgumentException(); //TODO other exception?
		}
		
		// directs the request and returns the status of it
		return mediator.parseLofFile(path);
	}
	
	
	/**
	 * Directs a chart request to a ChartMediator.<br>
	 * 
	 * Referring to a given configuration (id), it directs a request
	 * for a chart to a ChartMediator. 
	 * 
	 * @param id identifies the configuration/on which database is operated
	 * @param list list of options for the request
	 * @return a json-file which contains all information about the requested chart
	 */
	public File computeChart(int id, int list) { //TODO specify parameter list! what will it be?
		if (id <= 0) {
			throw new IllegalArgumentException();
		} else if (list == 0) { //TODO change!
			throw new IllegalArgumentException();
		}
				
		// get the right mediator
		ChartMediator mediator = getChartMediator(id); 
		if (mediator == null) {
			throw new IllegalArgumentException();
		}
		
		// direct request and receive file
		File json = mediator.computeChart(list);	
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
	 * @param id identifies the configuration/on which database is operated
	 * @param number number of the latest computed chart, range from 1 (latest) to 10 (oldest)
	 * @return the json-file of the requested chart, referring to the id and the number
	 */
	public File historyChart(int id, int number) {
		if (id <= 0) {
			throw new IllegalArgumentException();
		} else if ((number <= 0) || (number > 10)) { //TODO make range a global constant!
			throw new IllegalArgumentException();
		}
		
		// get the right mediator
		ChartMediator mediator = getChartMediator(id); 
		if (mediator == null) {
			throw new IllegalArgumentException();
		}
		
		// request the chart
		File histo = mediator.getHistoryChart(number);	
		if (histo == null) {
			//TODO error handling
		}
		
		return histo;
	}
	
	
	/**
	 * Returns the ChartMediator referring to the given id.
	 * 
	 * @param id identifies the configuration/on which database is operate
	 * @return the ChartMediator referring to the given id
	 */
	private ChartMediator getChartMediator(int id) {
		assert (id > 0);
		
		// get the right configuration object
		ConfigDB confi = ConfigDB.getConfi(id);
		if (confi == null) {
			throw new IllegalArgumentException(); //TODO other exception?
		}
		
		// get the right mediator
		ChartMediator mediator = ChartMediator.getChartMediator(confi);
		if (mediator == null) {
			throw new IllegalArgumentException();
		}
		
		return mediator;
	}
	
	
}
