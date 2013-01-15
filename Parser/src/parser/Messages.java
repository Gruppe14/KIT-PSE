package parser;
import java.util.Enumeration;
import java.util.ResourceBundle;


public class Messages {

	public static String getString(String string) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("Messages\\messages_en"); 
		
		return bundle.getString(string);
	}

}
