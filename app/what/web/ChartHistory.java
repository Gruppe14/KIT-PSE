package what.web;

import java.util.Date;
import java.util.LinkedList;

import org.json.JSONObject;

import play.api.templates.HtmlFormat;
import play.api.templates.Html;

//intern imports
import what.Facade;

/**
 * Static class providing some functions for displaying the chart history
 * @author Lukas Ehnle
 *
 */
public class ChartHistory {
	//static
	private static Facade f = Facade.getFacadeInstance();
	private static LinkedList<ChartHistory> instances = new LinkedList<>();
	//non-static
	private Date timestamp;
	private String uuid;
	private JSONObject[] history;
	
	ChartHistory() {
		this.timestamp = new Date();
	}
	
	public static Html historySummary(String uuid) {
		int num = f.getCurrentSizeOfHistory();
		ChartHistory tmp = new ChartHistory();
		tmp.uuid = uuid;
		tmp.history = new JSONObject[num];
		String html = "";
		for(int i = 0; i < num; i++) {
			tmp.history[i] = f.historyChart(i);
			
		}
		instances.add(tmp);
		return HtmlFormat.raw(html);
	}
	/**
	 * method to get the json object of one of the last chart requests
	 * @param uuid the uuid of the user that requested a history overview
	 * @param num the number of the history, which to return
	 * @return returns a json object to compute the chart or null
	 */
	public static JSONObject requestHistory(String uuid, int num) {
		//remove the object for this request, a new one is created upon overview request
		int i = instances.indexOf(uuid);
		if (i > -1) {
			JSONObject json = instances.get(i).history[num];
			instances.remove(i);
			//remove objects which are too old (15min), if someone left overview page without requesting history
			while(new Date().getTime() - instances.getFirst().timestamp.getTime() < 900000) {
				instances.removeFirst();
			}
			return json;
		}
		return null	;
	}
	
	@Override
	public boolean equals(Object elem) {
		if(elem instanceof ChartHistory) {
			return this.uuid.equals((String)elem);
		} else if(elem instanceof String){ 
			return this.uuid.equals(((ChartHistory)elem).uuid);
		} else {
			return false;
		}
	}
}
