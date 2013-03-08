package what.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import play.test.FakeApplication;
import play.test.Helpers;

import what.Facade;

import what.Printer;


public class ConfigTest {
	
	// is needed every time... the heart of the system
	private static Facade f;
	// run a fakeApplication
	private static FakeApplication app;
	
	// strings for file tests
	private static String sourcePath;
	private static String separator;
		
	@BeforeClass
	public static void initialize() {
		app = Helpers.fakeApplication();
		Helpers.start(app);
		Printer.ptestclass("config tests");
		f = Facade.getFacadeInstance();
		sourcePath = System.getProperty("user.dir");
		separator = System.getProperty("file.separator");
	}
	
	@AfterClass
	public static void stopApp() {
		Helpers.stop(app);
	}
	
	private void resetFacade() {
		f.reset();
		f = Facade.getFacadeInstance();
	}
	
	// -- Configuration build -- Configuration build -- Configuration build --
	//>> file names
	private static final String CONFIG1 = "TestConfig1.json";
	private static final String CONFIG1_PARSING = "Config1-TestParsing.csv";
	private static final String CONFIG2 = "TestConfig2.json";
	private static final String WRONG1 = "WrongTestConfig1-NoIP.json";
	private static final String WRONG2 = "WrongTestConfig2-Time.json";
	private static final String WRONG3 = "WrongTestConfig3-NoMeasure.json";
	private static final String WRONG4 = "WrongTestConfig4-DimOrder.json";

	
	@Test
	public void configTest1() {
		Printer.ptestcase("Other config 1");
		assertTrue(f.init(getPathForExample(CONFIG1)));
		assertTrue(f.init(getPathForExample(CONFIG1)));
		assertTrue(f.parseLogFile(getPathForExample(CONFIG1_PARSING)));
		resetFacade();
	}
	
	@Test
	public void configTest2() {
		Printer.ptestcase("Other config 2");
		assertTrue(f.init(getPathForExample(CONFIG2)));
		resetFacade();
	}
	
	@Test
	public void configWrong1() {
		Printer.ptestcase("Config wrong 1");
		assertFalse(f.init(getPathForExample(WRONG1)));
		resetFacade();
	}
	
	@Test
	public void configWrong2() {
		Printer.ptestcase("Config wrong 2");
		assertFalse(f.init(getPathForExample(WRONG2)));
		resetFacade();
	}
	
	@Test
	public void configWrong3() {
		Printer.ptestcase("Config wrong 3");
		assertFalse(f.init(getPathForExample(WRONG3)));
		resetFacade();
	}
	
	@Test
	public void configWrong4() {
		Printer.ptestcase("Config wrong 4");
		assertFalse(f.init(getPathForExample(WRONG4)));
		resetFacade();
	}
	
	
	
	// -- HELPER -- HELPER -- HELPER -- HELPER -- HELPER --
	private String getPathForExample(String s) {
		return sourcePath + separator + "example" + separator + s;
	}


}
