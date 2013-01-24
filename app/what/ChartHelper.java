package what;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.ChartIndex;
import play.api.templates.HtmlFormat;
import play.api.templates.Html;
import what.sp_config.*;




/**
 * class to create the html stuff from config for charts
 * @author Lukas Ehnle
 *
 */
public class ChartHelper {
	//instance of charthelper --> singleton
	private static ChartHelper instance = null;
	//current config
	private  ArrayList<DimRow> dims;
	//charts available
	private HashMap<String, Html> charts;
	
	// private because singleton
	private ChartHelper () {
		dims = Facade.getFacadeIstance().getCurrentConfig().getDims();
		charts = new HashMap<>();
		for(String chart : ChartIndex.getInstance().getCharts()) {
			charts.put(chart, createChart(chart));
		}
	}
	
	/**
	 * If already initialized returns instance, else initializes ChartHelper
	 * @return returns the only instance
	 */
	public static ChartHelper getInstance() {
		if(ChartHelper.instance == null) {
			ChartHelper.instance = new ChartHelper();
		}
		return ChartHelper.instance;
	}
	
	/**
	 * returns the options selection for a chart
	 * @param the chart name
	 * @return returns the option selection
	 */
	public Html getOptions(String name) {
		return charts.get(name);
	}
	
	//creates the option selection for a chart
	private Html createChart(String name) {
		String html = "";
		ArrayList<DimRow> trueDim = new ArrayList<>();
		ArrayList<String> measures = new ArrayList<>();
		for (DimRow dim: dims) {
			html += "<div>" + dim.toString() + "</div>";
			if(dim.getName().equalsIgnoreCase("time")) {
				html += timeDim();
			} else if (dim.isDimension()) {
				trueDim.add(dim);
			} else {
				
				measures.add(dim.getName());
			}
		}
		html += measuresHtml(measures);
		html += "dims:" + trueDim.size() + " measures:" + measures.size();
		
		return HtmlFormat.raw(html);
	}
	
	//create the options for all measures
	private String measuresHtml(ArrayList<String> measures) {
		String html = "";
		
		return html;
	}
	// the time options not scale
	private String timeDim() {
		String html = "";
		
		return	html;
	}
}
