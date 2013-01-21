package what.sp_parser;

import java.util.ResourceBundle;


public class Messages {
	
	private static String language = "messages_en";

	public static String getString(String string) {
		
		ResourceBundle bundle = ResourceBundle.getBundle(language); 
		return bundle.getString(string);
	}
	
	public static void setLanguage(String string) {
		if (string.toLowerCase().equals("german")) {
			language = "messages_de";
		} 
		
		if (string.toLowerCase().equals("english")) {
			language = "messages_en";
		}
		
		if (string.toLowerCase().equals("greek")) {
			language = "messages_gr";
		}
		
	}

}
