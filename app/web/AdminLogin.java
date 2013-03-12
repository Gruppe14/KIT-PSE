package web;

import play.Configuration;
import play.Play;
import play.data.validation.Constraints.Required;
import web.controllers.Localize;

/**
 * Class to help with login form and validation.
 * 
 * @author Lukas Ehnle, PSE Gruppe 14
 *
 */
public class AdminLogin {
	/**
	 * the user name, as given to login form.
	 */
	@Required
	public String username;
	
	/**
	 * the password, as given to login form.
	 */
	@Required
	public String password;
	
	/**
	 * Checks if a login was correct.
	 * @return returns null or an error message
	 */
	public String validate() {
		Configuration conf = Play.application().configuration();
		if (username.equals(conf.getString("admin.name")) 
				&& password.equals(conf.getString("admin.password"))) {
			return null;
		}
		return Localize.get("admin.wrongLogin");
	}
}
