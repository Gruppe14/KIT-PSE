package what;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.ChartIndex;
import controllers.Localize;
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
	//charts available, currently overhead, but with further config files may be needed
	private HashMap<String, Html> charts;
	
	/**
	 * private constructor because of singleton
	 * refer to getInstance
	 */
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
		ArrayList<DimRow> stringDim = new ArrayList<>();
		ArrayList<String> measures = new ArrayList<>();
		for (DimRow dim: dims) {
			if(dim.getName().equalsIgnoreCase("time")) {
				html += time();
				html += timeScale(1);
			} else if (dim.isStringDim()) {
				html += "<div>" + dim.toString() + "</div>";
				stringDim.add(dim);
			} else {
				measures.add(dim.getName());
			}
		}
		html += stringDimHtml(stringDim);
		html += measuresHtml(measures);
		html += "dims:" + stringDim.size();
		
		return HtmlFormat.raw(html);
	}
	
	/**
	 * method to create the option selections for dimensions
	 * @param dims the dimensions
	 * @return the html string
	 */
	private String stringDimHtml(ArrayList<DimRow> dims) {
		String html = "";
		for(DimRow dim: dims) {
			html += dim.getName();
		}
		return html;
	}
	
	/**
	 * creates the option selection for the measures
	 * @param measures the measures to add
	 * @return returns a html string with the measure selection
	 */
	private String measuresHtml(ArrayList<String> measures) {
		String html = "<div id=\"measures\" class=\"options\">" +
				"<div>" + Localize.get("filter.measures") +
				"</div><div class=\"type\">&nbsp;</div>" +
				"<div class=\"group list\">";
		for(String m: measures) {
			html += "<span>" + m + "</span>";
		}
		html += "</div></div>";
		return html;
	}
	/**
	 * method to add the time scale options
	 * @param dim wether only x or more dimensions are available
	 * @return returns the html string
	 */
	private String timeScale(int dim) {
		String html = "<div id=\"timescale\" class=\"options\"><div>" +
				Localize.get("time.scale") + "</div><div class=\"group type\">" +
				"<span class=\"x\">" + Localize.get("filter.x.axis") + 	"</span>";
		//if y can be chosen or not
		if(dim > 1) {
			html += "<span class=\"y\">" + Localize.get("filter.y.axis") + 	"</span>";
		}
		html += "<span class=\"filter\">" + Localize.get("filter.filter") + "</span></div>" +
				"<div class=\"group list\">";
		// maybe dynamic
		html += "<span>" + Localize.get("time.year") + "</span>";
		html += "<span>" + Localize.get("time.month") + "</span>";
		html += "<span>" + Localize.get("time.day") + "</span>";
		html += "<span>" + Localize.get("time.hour") + "</span>";
		
		html += "</div></div>";
		return html;
	}
	/**
	 * creates the option selection for time
	 * @return returns the html String for it
	 */
	private String time() {
		int[] year = {2009, 2012};
		String html = "<div id=\"time\">";
		String[] ft = {"From", "To"};
		for(String s: ft) {
			html += "<div>" + Localize.get("time." + s) + "<br /><div><span>" +
					Localize.get("time.year") + "</span><span id=\"year" + s + 
					"\">&nbsp;</span><div class=\"dropdown\"><span>---</span>";
			//add every year to selection
			for(int i = year[1]; i >= year[0]; i--) {
				html += "<span>" + i + "</span>";
			}
			html += "</div></div><div><span>" + Localize.get("time.month") +
					"</span><span id=\"month" + s + "\">&nbsp;</span>" +
					"<div class=\"dropdown\"><span>---</span>";
			//add every month
			for(int i = 1; i < 13; i++) {
				html += "<span>" + i + "</span>";
			}
			html += "</div></div><div><span>" + Localize.get("time.day") +
					"</span><input id=\"day" + s + "\" type=\"number\" " +
					"min=\"1\" max=\"31\" placeholder=\"1 - 31\"/></div>" +
					"<div><span>" + Localize.get("time.hour") + "</span>" +
					"<input id=\"hour" + s + "\" type=\"text\" " +
					"pattern=\"(^[0-9]|^[1][0-9]|^[2][1-3]):[0-5][0-9]$\" " +
					"placeholder=\"0:00\"/></div></div>";
		}
		html += "</div>";
		return	html;
	}
}
