package what.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import what.Facade;
import what.FileHelper;
import what.JSONReader;

//JSON imports
import org.json.JSONArray;
import org.json.JSONObject;

public class GeneralClasses {
	
	// is needed every time... the heart of the system
	private static Facade f;
	
	// strings for file tests
	private static String sourcePath;
	private static String seperator;
		
	@BeforeClass
	public static void initialize() {
		f = Facade.getFacadeInstance();
		sourcePath = System.getProperty("user.dir");
		seperator = System.getProperty("file.separator");
	}
	
	private void resetFacade() {
		f.reset();
		f = Facade.getFacadeInstance();
	}
	
	// -- FileHelper -- FileHelper -- FileHelper -- FileHelper --
	//>> file names
	private static final String NONSENSE = "ahahaha what am I?";
	private static final String WRONG = "WrongAmI.txt";
	@Test
	public void wrongFile() {
		assertFalse(f.parseLogFile(getPathFor(WRONG)));
		assertFalse(f.init(getPathFor(WRONG)));
		resetFacade();
	}

	@Test
	public void nonexistentFile() {
		assertNull(FileHelper.getFile(getPathFor("nonexistentpath.csv")));
		assertNull(FileHelper.getFile(getPathFor(NONSENSE)));
	}
	
	
	// -- JSONReader -- JSONReader -- JSONReader -- JSONReader --
	//>> file names and JSON keys
	private static final String JSON = "JSONTest.txt";
	private static final String INT = "testInt";
	private static final String STRING = "testString";
	private static final String OBJECT = "testObject";
	private static final String ARRAY = "testArray";

	
	@Test
	public void jsonReaderAllInOne() {
		File f = FileHelper.getFile(getPathFor(JSON));
		String s = FileHelper.getStringContent(f);
		assertNotNull(s);
		
		// get the object and reader
		JSONObject o = JSONReader.getJSONObjectForString(s);
		assertNotNull(o);
		JSONReader r = new JSONReader(o);
		
		// basics reads		
		assertTrue(42 == r.getInt(INT));
		assertNotNull(r.getString(STRING));
		assertNotNull(r.getJSONObject(OBJECT));
		assertNotNull(r.getObject(OBJECT));
		
		// test on array
		JSONArray a = r.getJSONArray(ARRAY);
		assertNotNull(a);
		assertNotNull(JSONReader.getJSONObjectFromArray(a, 1));
		assertNotNull(JSONReader.getStringFromArray(a, 0));
		
	}
	
	// -- Facade -- Facade -- Facade -- Facade --

	
	// -- HELPER -- HELPER -- HELPER -- HELPER -- HELPER --
	private String getPathFor(String s) {
		return sourcePath + seperator + "example\\" + s;
	}
	
}
