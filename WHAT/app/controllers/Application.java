package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok(oldindex.render("Your new application is ready."));
  }

    public static Result histo() {
	return ok(histogramm.render());
    }

    public static Result newindex() {
	return ok(index.render());
    }
  
}
