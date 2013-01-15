package controllers;

import play.mvc.*;

public class Website extends Controller {

    public static Result index() {
    	return ok(views.html.index.render(ChartIndex.getInstance().getCharts()));
    }
    
    public static Result ChartType(String type) {
    	return ok(views.html.abstractChart.render(type));
    }
    
    public static Result changeLanguage(String path) {
    	Localize.changeLanguage();
    	return redirect(path);
    }

}
