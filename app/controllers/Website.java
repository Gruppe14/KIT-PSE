package controllers;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import org.json.JSONException;
import org.json.JSONObject;

import play.mvc.Security;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import play.data.Form;

import what.Facade;
import what.Printer;
import what.web.AdminAuth;
import what.web.AdminLogin;
import what.web.ChartHelper;
import what.web.ChartHistory;
import what.web.LogfileUpload;

/**
 * Class that handles all http requests. the routes redirect a request to one of the methods
 * of this class. This class may handle the request itself or call other classes and methods.
 * all methods return a http response to the client
 * @author Lukas Ehnle
 *
 */
public class Website extends Controller {
	

	/**
	 * the form needed for admin login.
	 */
	private static Form<AdminLogin> form = form(AdminLogin.class);
	/**
	 * the form needed for logfilePathUpload.
	 */
	private static Form<LogfileUpload> log = form(LogfileUpload.class);

	/**
	 * method to render the index site with all available chart types.
	 * @return returns the html index site
	 */
    public static Result index() {
    	return ok(views.html.index.render());
    }
    /**
     * method to dynamically return a chart site depending on the chartName.
     * @param chartName the name of the requested chart
     * @return returns a valid HTTP response, a web page
     */
    public static Result chartType(String chartName) {
    	return ok(views.html.abstractChart.render(chartName));
    }
    /**
     * method to dynamically return a chart JavaScript depending on the chartName.
     * @param chartName the name of the requested chart
     * @return returns a valid HTTP response, a JavaScript
     */
    public static Result chartJS(String chartName) {
    	return ok(views.html.chartJS.render(chartName)).as("application/javascript");
    }
    
    /**
     * requests static resources for a chart.
     * @param chartName the chart name
     * @param file the path to the file requested
     * @return returns the file
     */
    public static Result chartStatics(String chartName, String file) {
    	return ok(new File("./charts/" + chartName + "/" + file));
    }
    
    /**
     * method to process chart requests.
     * @return returns the needed chart data
     */
    //TolerantText because ContentType is JSON
    @BodyParser.Of(BodyParser.TolerantText.class)
    public static Result requestChart() {
    	try {
			JSONObject json = new JSONObject(request().body().asText());
			json = Facade.getFacadeInstance().computeChart(json);
			if (json != null) {
				return ok(json.toString());
			}
    	} catch (JSONException e) {
    		Printer.pproblem("JSON request from web page");
    	}
    	return internalServerError("Something went wrong :(");
    }
    
    /**
     * method to mirror SVG back to download or convert SVG to PNG.
     * image type via POST value of format
     * SVG data via value of SVG
     * 
     * @return returns the chart as SVG/PNG or an serverError
     */
    public static Result downloadChart() {
    	Map<String, String[]> body = request().body().asFormUrlEncoded();
    	if (body != null && body.containsKey("svg")) {
    		String svg;
    		String name = "chart";
    		if (body.containsKey("name") && !body.get("name")[0].equals("")) {
    			name = body.get("name")[0];
    		}
			try {
				svg = URLDecoder.decode(body.get("svg")[0], "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return internalServerError("Something went wrong :(");
			}
    		if (body.containsKey("format") && body.get("format")[0].equals("svg")) {
    			response().setHeader("Content-Disposition", "attachment; filename=\"" + name + ".svg\"");
    			return ok(svg).as("image/svg+xml");
    		} else if (body.containsKey("format") && body.get("format")[0].equals("png")) {
    			PNGTranscoder t = new PNGTranscoder();
    			//white background instead of transparent
    			t.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.white);
    			TranscoderInput in = new TranscoderInput(new StringReader(svg));
    			ByteArrayOutputStream png = new ByteArrayOutputStream();
    			TranscoderOutput out = new TranscoderOutput(png);
    			try {
					t.transcode(in, out);
				} catch (TranscoderException e) {
					e.printStackTrace();
					return internalServerError("Something went wrong :(");
				}
    			try {
					png.flush();
					png.close();
				} catch (IOException e) {
					e.printStackTrace();
					return internalServerError("Something went wrong :(");
				}
    			response().setHeader("Content-Disposition", "attachment; filename=\"" + name + ".png\"");
    			return ok(png.toByteArray()).as("image/png");
    		}
    	}
    	return internalServerError("Something went wrong :(");
    }
   
    /**
     * Returns a chart history for a uuid provided in session information
     * and a history number.
     * 
     * @param num the number for the history
     * @return returns a JSON response or an internal server error
     */
    public static Result requestHistory(String num) {
    	String uuid = session("uuid");
    	if (uuid != null) {
    		JSONObject json = ChartHistory.requestHistory(uuid, Integer.parseInt(num));
    		if (json != null) {
    			return ok(json.toString());
    		}
    	}
		return noContent();
    }
    
    /**
     * method to return a web page containing an overview of the last chart requests.
     * 
     * @return returns a HTML page with the overview
     */
    public static Result historyOfCharts() {
    	String uuid = session("uuid");
    	if (uuid == null) {
    	    uuid = java.util.UUID.randomUUID().toString();
    	    session("uuid", uuid);
    	}
    	return ok(views.html.main.render(Localize.get("hist.title"), 
    			ChartHelper.getStyle(), ChartHistory.historySummary(uuid)));
    }
    
    /**
     * Changes the language and redirect to current path with new language.
     * @return returns the same page in chosen language
     */
    public static Result changeLanguage() {
    	RequestBody body = request().body();
    	if (body.asFormUrlEncoded() != null && body.asFormUrlEncoded().containsKey("lang")) {
	    	session("lang", request().body().asFormUrlEncoded().get("lang")[0]);
    	}
    	return redirect(body.asFormUrlEncoded().get("path")[0]);
    }
    
    /**
     * display login form.
     * @return returns a http response with the form page
     */
    public static Result adminLogin() {
    	return ok(views.html.login.render(form));
    }
    
    /**
     * method to validate the login form.
     * @return returns the admin page or a badrequest
     */
    public static Result login() {
		Form<AdminLogin> filledForm = form.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.login.render(filledForm));
		}
		session().put("username", filledForm.get().username);
		return redirect(routes.Website.admin());
	}
    
    /**
     * method to display admin page if authorized.
     * @return returns admin page or error
     */
    @Security.Authenticated(AdminAuth.class)
	public static Result admin() {
		return ok(views.html.adminPage.render(log));
	}
    
    /**
     * method to pass new log file path to the parser.
     * see what.LogfileUpload
     * @return returns to the admin page
     */
    @Security.Authenticated(AdminAuth.class)
    public static Result logfile() {
    	Form<LogfileUpload> filledForm = log.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.adminPage.render(filledForm));
		} 
    	return ok(views.html.adminPage.render(log));
    }
    
    
    /**
     * method to log out from admin.
     * @return returns to index
     */
    public static Result logout() {
		session().clear();
		return redirect(routes.Website.index());
	}
}
