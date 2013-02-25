package what.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import what.Facade;
import what.sp_config.ConfigWrap;
import what.sp_data_access.DBTableBuilder;

public class MySQLStuff {

	// is needed every time... the heart of the system
	private static Facade f;
	
	// is needed also nearly every time... the whatever...
	private static ConfigWrap confi;
	
		
	@BeforeClass
	public static void initialize() {
		f = Facade.getFacadeInstance();
		confi = f.getCurrentConfig();
	}
	
	@Test
	public void createDataBaseQueries() {
		assertNotNull(DBTableBuilder.getDataBaseQuery(confi) != null);		
	}

}
