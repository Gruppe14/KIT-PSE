package appTier.sb_config;

public abstract class ConfigEntry {

	
	/**
	 * name stands for the name of this measure or type overall. 
	 * Examples are: "location", "elapsed time", "time", "security level", ...
	 */
	private String name;

	public ConfigEntry(){
		
	}
	
	
	public String getName() {
		return name;
	}
	
	
}
