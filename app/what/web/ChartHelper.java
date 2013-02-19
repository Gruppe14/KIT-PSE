package what.web;

// java imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

// intern imports
import controllers.ChartIndex;
import controllers.Localize;
import play.api.templates.HtmlFormat;
import play.api.templates.Html;
import what.Facade;
import what.sp_chart_creation.Measure;
import what.sp_config.*;

/**
 * class to create the HTML stuff from configuration for charts
 * @author Lukas Ehnle, Jonathan, PSE Gruppe 14
 *
 */
public class ChartHelper {
	
	private static final String SPAN = "<span>";
	private static final String NAPS = "</span>";
	
	private static final String DIV = "<div>";
	private static final String VID = "</div>";
	
	//instance of charthelper --> singleton
	private static HashMap<String, ChartHelper> instance = new HashMap<>();
	//current config
	private ArrayList<DimRow> dims;
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
	 * returns the options selection for a chart
	 * @param the chart name
	 * @return returns the option selection
	 */
	public static Html getOptions(String name) {
		String lan = Localize.language();
		if(!instance.containsKey(lan)) {
			instance.put(lan, new ChartHelper());
		}
		return instance.get(lan).charts.get(name);
	}
	
	//creates the option selection for a chart
	private Html createChart(String name) {
		ArrayList<DimRow> stringDim = new ArrayList<>();
		ArrayList<String> measures = new ArrayList<>();
		for (DimRow dim: dims) {
			//if time dimension, add time options + time scale
			if(dim.getName().equalsIgnoreCase("time")) {
				//if time do nothing is handled separately
			//if string dim add to list for later
			} else if (dim.isStringDim()) {
				stringDim.add(dim);
			//else add to measure list for later
			} else {
				measures.add(dim.getName());
			}
		}
		//create the html content
		String html = "";
		html += time();
		html += axis(stringDim, name);
		html += measuresHtml(measures) + "<br />";
		html += stringDimHtml(stringDim);
		
		return HtmlFormat.raw(html);
	}
	
	/**
	 * method that creates the selection for the axes
	 * @return returns the html string
	 */
	private String axis(ArrayList<DimRow> dims, String chart) {
		String html = "";
		String tmp = "";
		for(DimRow dim : dims) {
			tmp += "<span data=\"" + dim.getName() + "\">" + Localize.get("dim." + dim.getName()) + NAPS + "<div class=\"sub\">";
				for(int i = 0; i < dim.getSize(); i++){
					tmp += "<span data=\"" + dim.getNameOfLevel(i) + "\">" +
							Localize.get("dim." + dim.getName() + "." + dim.getNameOfLevel(i)) + NAPS;
				}
			tmp += VID;
		}
		for(int i = 0; i < ChartIndex.getInstance().getDim(chart); i++) {
			html += "<div id=\"" + (char)('x' + i) + "\" class=\"single options\">" + DIV + (char)('x' + i) + 
					"-" + Localize.get("filter.axis") + VID +
					"<div class=\"list\">";
			//for x axis add time
			if(i == 0) {
				html += timeScale();
			}
			html += tmp + VID + VID;
		}
		return html;
	}
	
	/**
	 * Creates the option selections for the String dimensions
	 * 
	 * @param dims the dimensions, only String dimensions allowed
	 * @return HTML String for this String dimension
	 */
	private String stringDimHtml(ArrayList<DimRow> dims) {
		assert (dims != null);
		
		String html = "";
		for(DimRow dim: dims) {
			String tmp = "<div id=\"" + dim.getName() + "\" class=\"options\">" + DIV +
					Localize.get("dim." + dim.getName()) + VID;
					
			assert (dim.isStringDim());
			
			//first level is build here because of dim list classes
			TreeSet<DimKnot> trees = dim.getStrings();
			
			tmp += "<div class=\"dim list\" data=\"" + trees.first().getRowName() + "\">";
			for (DimKnot dk : trees) {
				tmp += getHtmlForDimKnot(dk);	
			}
			tmp += VID + VID;
			
			html += tmp;
		}
		return html;
	}
	
	/**
	 * Returns the HTML part for a DimKnot and it's children.
	 * 
	 * @param dk DimKnot for which the HTML part is requested
	 * @return the HTML part for a DimKnot and it's children
	 */
	private String getHtmlForDimKnot(DimKnot dk) {
		assert (dk != null);
		
		String tmp = SPAN + dk.getValue() + NAPS;
		
		if (dk.hasChildren()) {
			tmp += "<div class=\"sub\" data=\"" + dk.getChildren().first().getRowName() + "\">";
			for (DimKnot child : dk.getChildren()) {
				tmp += getHtmlForDimKnot(child);
			}
			
			tmp += VID;
		}
		
		

		return tmp;
	}
	

	/**
	 * creates the option selection for the measures
	 * @param measures the measures to add
	 * @return returns a html string with the measure selection
	 */
	private String measuresHtml(ArrayList<String> measures) {
		String html = "<div id=\"measures\" class=\"single options\">" +
				DIV + Localize.get("filter.measures") +
				VID + "<div class=\"list\">";
		for(String m: measures) {
			html += "<span data=\"" + m + "\">" + Localize.get("measure." + m) + NAPS + "<div class=\"sub\">";
			for(String s: Measure.getAggregations()) {
				html += "<span data=\"" + s + "\">" + Localize.get("aggregation." + s) + NAPS;
			}
			html += VID;
			
		}
		html += VID + VID;
		return html;
	}
	
	/**
	 * method to add the time scale options
	 * @param dim wether only x or more dimensions are available
	 * @return returns the html string
	 */
	private String timeScale() {
		String html = "<span data=\"time\">" + Localize.get("time") + NAPS + "<div class=\"sub\">";
		// maybe dynamic
		html += "<span data=\"year\">" + Localize.get("time.year") + NAPS;
		html += "<span data=\"month\">" + Localize.get("time.month") + NAPS;
		html += "<span data=\"day\">" + Localize.get("time.day") + NAPS;
		html += "<span data=\"hour\">" + Localize.get("time.hour") + NAPS;
		
		html += VID + "\n";
		return html;
	}
	
	/**
	 * creates the option selection for time
	 * @return returns the html String for it
	 */
	private String time() {
		int[] year = {2011, 2012};
		String html = "<div id=\"timescale\">";
		String[] ft = {"From", "To"};
		for(String s: ft) {
			html += DIV + Localize.get("time." + s) + "<br />" + DIV + SPAN +
					Localize.get("time.year") + "</span><span id=\"year" + s + 
					"\">&nbsp;" + NAPS + "<div class=\"dropdown\"><span>---</span>";
			//add every year to selection
			for(int i = year[1]; i >= year[0]; i--) {
				html += SPAN + i + NAPS;
			}
			html += VID + VID + DIV + SPAN + Localize.get("time.month") +
					NAPS + "<span id=\"month" + s + "\">&nbsp;" + NAPS +
					"<div class=\"dropdown\">" + SPAN + "---" + NAPS;
			//add every month
			for(int i = 1; i < 13; i++) {
				html += SPAN + i + NAPS;
			}
			html += VID + VID + DIV + SPAN + Localize.get("time.day") +
					NAPS + "<input id=\"day" + s + "\" type=\"number\" " +
					"min=\"1\" max=\"31\" placeholder=\"1 - 31\"/></div> " +
					DIV + SPAN + Localize.get("time.hour") + NAPS +
					"<input id=\"hour" + s + "\" type=\"text\" " +
					"pattern=\"(^[0-9]|^[1][0-9]|^[2][1-3]):[0-5][0-9]$\" " +
					"placeholder=\"0:00\"/>" + VID + VID;
		}
		html += VID;
		return	html;
	}
}
