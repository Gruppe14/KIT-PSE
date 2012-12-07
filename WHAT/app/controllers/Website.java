package controllers;

import play.mvc.*;

public class Website extends Controller {

    public static Result index() {
    	return ok(views.html.index.render(Localize.language(), ChartIndex.getInstance().getCharts()));
    }
    
    public static Result ChartType(String type) {
    	return ok(views.html.abstractChart.render(Localize.language(), type));
    }
    
    public static Result changeLanguage(String path) {
    	Localize.changeLanguage();
    	return redirect(path);
    }

}
