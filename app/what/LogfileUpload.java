package what;

import controllers.Localize;
import play.data.validation.Constraints.Required;

import java.io.File;

/**
 * class to support form validation in logfilePath uploads
 * @author Lukas Ehnle
 *
 */
public class LogfileUpload {
	@Required
	public String pathToLogfile;
	
	public String validate() {
		
		/**
		 * method to check if the uploaded path exists on the serverside
		 * @return returns null or an error message
		 */
		if (new File(pathToLogfile).exists()) {
			//start parser
			Facade.getFacadeIstance().parseLogFile(pathToLogfile);
			return null;
		}
		return Localize.get("admin.wrongPath");
	}
}
