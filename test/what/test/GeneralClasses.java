package what.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import what.Facade;

public class GeneralClasses {

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
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\wrongFile.txt"));
		assertFalse(f.init(sourcePath + seperator + "example\\wrongFile.txt"));
		resetFacade();
	}

	@Test
	public void nonexistentFile() {
		assertFalse(f.parseLogFile("nonexistentpath.csv"));
		assertFalse(f.parseLogFile("ahahaha what am I?"));
	}
	
	
}
