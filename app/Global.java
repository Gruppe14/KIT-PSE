import play.Application;
import play.GlobalSettings;
import what.Facade;
import what.Printer;

public class Global extends GlobalSettings {
		
	public void onStart(Application app) {
		Printer.print("Welcome to WHAT!");
		
	}
	
	public void onStop(Application app) {
		// reset facade to release connections
		Facade.getFacadeInstance().reset();
		
		Printer.print("WHAT has been closed! We hope to see you again sometimes.");
	}
}
