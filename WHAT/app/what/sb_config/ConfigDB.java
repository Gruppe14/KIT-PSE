package appTier.sb_config;
 
import java.io.File;
import java.util.TreeSet;

public class ConfigDB {
	private int id;
	
	

	private File originFile;
	
	private String parseLine;
	
	private TreeSet<ConfigEntry> entries; 

	private static TreeSet<ConfigDB> confCache = new TreeSet<ConfigDB>();
	
	
	private ConfigDB() {
		// private Constructor, as objects are controlled with static factory machine and fly-weights
	}
	
	public static ConfigDB getConfi(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException();
		}
		
		for (ConfigDB c : confCache) {
			//TODO better solution, don't do with int!!!!
			if (c.getId()  == id) {
				return c;
			}
		}
		
		//TODO better error handling!
		ConfigDB conf = configDBFactory(id);
		
		if (conf == null) {
			throw new IllegalArgumentException();
		}
		
		return conf;
	}
	
	private static ConfigDB configDBFactory(int id) {
		assert (id > 0);
		
		ConfigDB conf = new ConfigDB();
		//TODO integrate tool which reads the configuration file
		return conf;
	}

	private int getId() {
		return id;
	}
	private File getOriginFile() {
		return originFile;
	}
	
	
}
