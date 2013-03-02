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
	
	// -- Config build -- Config build -- Config build -- Config build --
	//>> file names
	private static final String CONFIG1 = "TestConfig1.json";
	private static final String CONFIG2 = "TestConfig2.json";
	
	
	
	// -- Facade -- Facade -- Facade -- Facade --
	// is tested via hand, nothing would work, if it wouldn't work
	
	// -- HELPER -- HELPER -- HELPER -- HELPER -- HELPER --
	private String getPathForExample(String s) {
		return sourcePath + separator + "example" + separator + s;
	}

    private String getPathForData(String s) {
        return sourcePath + separator + "data" + separator + s;
    }

}
