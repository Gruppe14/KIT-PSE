package what.sp_config;

public abstract class RowEntry {

	
	/**
	 * name stands for the name of this measure or type overall. 
	 * Examples are: "location", "elapsed time", "time", "security level", ...
	 */
	private String name;

	public RowEntry(String name) {
		assert (name != null);
		
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
	
}
