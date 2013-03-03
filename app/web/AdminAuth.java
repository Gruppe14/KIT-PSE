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
	 * Retrieves the user name from the session.
	 * 
	 * @param ctx the request to check for user name
	 * @return returns user name or null
	 */
	public String getUsername(Context ctx) {
		assert (ctx != null);
		
		String username = ctx.session().get("username");
		if (username == null) {
			return null;
		}
		return username;
	}
	
	/**
	 * alternative result if user is not valid.
	 * 
	 * @param ctx the request to check for authorization
	 * @return Result of this
	 */
	public Result onUnauthorized(Context ctx) {
		assert (ctx != null);
		
		return redirect(routes.Website.adminLogin());
	}
}
