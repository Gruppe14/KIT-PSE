package controllers;

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
    public static Result ChartType(String chartName) {
    	return ok(views.html.abstractChart.render(chartName));
    }
    /**
     * method to dynamically return a chart javascript depending on the chartName
     * @param chartName the name of the requested chart
     * @return returns a valid http response, a javascript
     */
    public static Result ChartJS(String chartName) {
    	return ok(views.html.charts.chartJS.render(chartName)).as("application/javascript");
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
