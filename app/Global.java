import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings{
	public void onStop(Application app) {
		System.out.println("hahaha it works!");
	}
}
