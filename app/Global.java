import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import play.Application;
import play.GlobalSettings;
import what.Facade;
import what.Printer;

public class Global extends GlobalSettings {
	
	public void onStart(Application app) {
		Printer.print("Welcome to WHAT!");
		try {
			java.awt.Desktop.getDesktop().browse(new URI("http://localhost:9000/"));
		} catch (IOException e) {
			Printer.pfail("IO while starting web page.");
		} catch (URISyntaxException e) {
			Printer.pfail("Creating URI while starting web page.");
		}
	}
	
	public void onStop(Application app) {
		// reset facade to release connections
		Facade.getFacadeInstance().reset();
		
		Printer.print("You close WHAT! We hope to see you again sometimes.");
	}
}
