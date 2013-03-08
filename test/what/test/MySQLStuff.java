package what.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;
import play.test.Helpers;

import what.Facade;
import what.sp_config.ConfigWrap;
import what.sp_data_access.DBTableBuilder;

public class MySQLStuff {

	// is needed every time... the heart of the system
	private static Facade f;
	
	// run a fakeApplication
	private static FakeApplication app;
	
	// is needed also nearly every time... the whatever...
	private static ConfigWrap confi;
	
		
	@BeforeClass
	public static void initialize() {
		app = Helpers.fakeApplication();
		Helpers.start(app);
		f = Facade.getFacadeInstance();
		confi = f.getCurrentConfig();
	}
	
	@AfterClass
	public static void stopApp() {
		Helpers.stop(app);
	}
	
	@Test
	public void createDataBaseQueries() {
		assertNotNull(DBTableBuilder.getDataBaseQuery(confi) != null);		
	}

}
