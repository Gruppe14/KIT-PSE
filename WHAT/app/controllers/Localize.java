package controllers;
import play.i18n.*;

public class Localize {
	public static String get(String lang, String message) {
		String loc = Messages.get(Lang.forCode(lang),message);
		//default language if message not found
		if(message.equals(loc)) {
			loc = Messages.get(Lang.forCode("en"), message);
		}
		return loc;
	}
}
