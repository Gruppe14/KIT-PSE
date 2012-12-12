package controllers;
import play.mvc.*;
import play.mvc.Http.RequestBody;
import play.i18n.*;

import java.util.List;

public class Localize extends Controller{
	private static List<Lang> available = Lang.availables();
	//default language
	private static Lang standard = Lang.forCode("en");
	
	//static class
	private Localize(){
	}
	
	/**
	 * method to get a localized string
	 * @param language the language in which the string will be
	 * @param message the key of the string to localize
	 * @return returns a localized string in the given language or if the language is not supported 
	 * the string in the standard language
	 */
	public static String get(String message) {
		String loc = Messages.get(Lang.forCode(language()),message);
		//default language if message not found
		if(message.equals(loc)) {
			loc = Messages.get(standard, message);
		}
		return loc;
	}
	
	/**
	 * Method to get all available translations
	 * @return returns a string array with abbreviations e.g. "de" or "en" of the available languages
	 */
	public static String[] getAvailables() {
		String[] tmp = new String[available.size()];
		for(int i = 0; i < tmp.length; i++) {
			tmp[i] = available.get(i).code();
		}
		return tmp;
	}
	/**
	 * method to automatically localize the page in the best language
	 * @return returns a language in string form e.g. "en" or "de"
	 * if language was chosen manually that language is returned, else the preferred languages
	 * as sent by the browser are checked if any fit the available languages and that language is returned
	 * else the standard language is returned.
	 */
	public static String language() {
		String lang = session("lang");
		if(lang == null){
	    	List<Lang> want = request().acceptLanguages();
	    	for (Lang w: want) {
	    		if(available.contains(w)) {
	    			session("lang", w.code());
	    			lang = w.code();
	    			break;
	    		}
	    	}
	    	if(lang == null) {
	    		lang = standard.code();
	    	}
		}
    	return lang;
	}
	/**
	 * method to change the previous language to a selected
	 */
	public static void changeLanguage(){
    	RequestBody body = request().body();
    	if(body.asFormUrlEncoded() != null && body.asFormUrlEncoded().containsKey("lang")) {
	    	session("lang", request().body().asFormUrlEncoded().get("lang")[0]);
    	}
    }
}
