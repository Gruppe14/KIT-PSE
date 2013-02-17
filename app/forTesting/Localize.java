package forTesting;

import java.util.ResourceBundle;

public class Localize {

	public static String getString(String str) {
		return ResourceBundle.getBundle("messages_en").getString(str);
	}
}
