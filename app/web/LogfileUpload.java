package web;

import play.data.validation.Constraints.Required;
import web.controllers.Localize;
import what.Facade;

import java.io.File;

/**
 * class to support form validation in logfilePath uploads.
 * 
 * @author Lukas Ehnle, PSE Gruppe 14
 *
 */
public class LogfileUpload {
	
	/**
	 * path to the log file as given in the logFileUpload form.
	 */
	@Required
	public String pathToLogfile;
	
	/**
	 * method to check if the uploaded path exists on the server side.
	 * 
	 * @return returns null or an error message.
	 */
	public String validate() {
		if (pathToLogfile != null && new File(pathToLogfile).exists()) {
			//start parser
			Facade.getFacadeInstance().parseLogFile(pathToLogfile);
			return null;
		}
		return Localize.get("admin.uploadErr");
	}
}
