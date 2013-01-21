package what.sp_parser;

import java.util.ResourceBundle;


public class Messages {

	public static String getString(String string) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("messages_en"); 
		
		return bundle.getString(string);
	}

}
