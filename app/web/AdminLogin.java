package web;

import play.data.validation.Constraints.Required;
import web.controllers.Localize;

/**
 * Class to help with login form and validation.
 * @author Lukas Ehnle
 *
 */
public class AdminLogin {
	/**
	 * the username, as given to login form.
	 */
	@Required
	public String username;
	
	/**
	 * the password, as given to login form.
	 */
	@Required
	public String password;
	
	/**
	 * method to check if a login was correct.
	 * @return returns null or an error message
	 */
	public String validate() {
		if (username.equals("test") && password.equals("secret")) {
			return null;
		}
		return Localize.get("admin.wrongLogin");
	}
}
