package what.web;

import controllers.routes;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * class to help check if someone is logged in or not
 * @author Lukas Ehnle
 *
 */
public class AdminAuth extends Security.Authenticator{
	
	/**
	 * retrieves the username from the session
	 * @ctx the request to check for username
	 * @returns username or null
	 */
	public String getUsername(Context ctx) {
		String username = ctx.session().get("username");
		if (username==null) return null;
		return username;
	}
	/**
	 * alternative result if user is not valid
	 */
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Website.adminLogin());
	}
}
