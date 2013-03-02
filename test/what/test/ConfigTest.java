package what.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import what.Facade;

import what.Printer;


public class ConfigTest {
	
	// is needed every time... the heart of the system
	private static Facade f;
	
	// strings for file tests
	private static String sourcePath;
	private static String separator;
		
	@BeforeClass
	public static void initialize() {
		f = Facade.getFacadeInstance();
		sourcePath = System.getProperty("user.dir");
		separator = System.getProperty("file.separator");
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
	
	// -- HELPER -- HELPER -- HELPER -- HELPER -- HELPER --
	private String getPathForExample(String s) {
		return sourcePath + separator + "example" + separator + s;
	}

    private String getPathForData(String s) {
        return sourcePath + separator + "data" + separator + s;
    }

}
