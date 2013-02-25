package what.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import what.Facade;

public class GeneralClasses {

	// file names
	private static final String NONSENSE = "ahahaha what am I?";
	private static final String WRONG = "WrongAmI.txt";
	
	// is needed every time... the heart of the system
	private static Facade f;
	
	// strings for file tests
	private static String sourcePath;
	private static String seperator;
		
	@BeforeClass
	public static void initialize() {
		f = Facade.getFacadeIstance();
		sourcePath = System.getProperty("user.dir");
		seperator = System.getProperty("file.separator");
	}
	
	private void resetFacade() {
		f.reset();
		f = Facade.getFacadeIstance();
	}
	
	// -- FileHelper -- FileHelper -- FileHelper -- FileHelper --
	@Test
	public void wrongFile() {
		assertFalse(f.parseLogFile(getPathFor(WRONG)));
		assertFalse(f.init(getPathFor(WRONG)));
		resetFacade();
	}

	@Test
	public void nonexistentFile() {
		assertFalse(f.parseLogFile(getPathFor("nonexistentpath.csv")));
		assertFalse(f.parseLogFile(getPathFor(NONSENSE)));
	}
	
	// -- HELPER -- HELPER -- HELPER -- HELPER -- HELPER --
	private String getPathFor(String s) {
		return sourcePath + seperator + "example\\" + s;
	}
	
}
