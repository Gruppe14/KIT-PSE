package web;

import web.controllers.routes;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Class to help check if someone is logged in or not.
 * 
 * @author Lukas Ehnle, PSE Gruppe 14
 *
 */
public class AdminAuth extends Security.Authenticator {
	
	/**
	 * Retrieves the username from the session.
	 * @param ctx the request to check for username
	 * @return returns username or null
	 */
	public String getUsername(Context ctx) {
		String username = ctx.session().get("username");
		if (username == null) {
			return null;
		}
		return username;
	}
	
	/**
	 * alternative result if user is not valid.
	 * @param ctx the request to check for authorization
	 * @return Result
	 */
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Website.adminLogin());
	}
}
