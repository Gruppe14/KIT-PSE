package controllers;

import play.mvc.*;

public class Website extends Controller {

    public static Result index(String lang) {
    	return ok(views.html.index.render(lang));
    }
    public static Result histo() {
    	return ok(views.html.charts.bubblechart.bubblechart.render());
    }
    
    public static Result test() {
    	return ok(views.html.chartsindex.render(ChartIndex.getInstance().getCharts()));
    }
    
    public static Result getChartType(String type) {
    	System.out.println(type);
    	return ok();
    }

}
