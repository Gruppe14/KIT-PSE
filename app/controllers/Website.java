package controllers;

import java.io.File;

import org.codehaus.jackson.JsonNode;

import play.mvc.*;


public class Website extends Controller {

	/**
	 * method to render the index site with all available chart types
	 * @return returns the html index site
	 */
    public static Result index() {
    	return ok(views.html.index.render(ChartIndex.getInstance().getCharts()));
    }
    /**
     * method to dynamically return a chart site depending on the chartName
     * @param chartName the name of the requested chart
     * @return returns a valid http response, a website
     */
    public static Result chartType(String chartName) {
    	return ok(views.html.abstractChart.render(chartName));
    }
    /**
     * method to dynamically return a chart javascript depending on the chartName
     * @param chartName the name of the requested chart
     * @return returns a valid http response, a javascript
     */
    public static Result chartJS(String chartName) {
    	return ok(views.html.chartJS.render(chartName)).as("application/javascript");
    }
    
    /**
     * requests static resources for a chart
     * @param chartName the chart name
     * @param file the path to the file requested
     * @return returns the file
     */
    public static Result chartStatics(String chartName, String file) {
    	return ok(new File("./charts/" + chartName + "/" + file));
    }
    
    /**
     * method to process chart requests
     * @return returns the needed chart data
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result chartRequest() {
    	JsonNode json = request().body().asJson();
    	String chart = json.findPath("chart").getTextValue();
        return ok(new File("./example/charts/" + chart + ".json")).as("application/json");
    }
    
    /**
     * change the language and redirect to current path with new language
     * @param path the current path
     * @return returns the same page in chosen language
     */
    public static Result changeLanguage(String path) {
    	Localize.changeLanguage();
    	return redirect(path);
    }
    
}
