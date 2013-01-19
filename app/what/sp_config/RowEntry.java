package what.sp_config;

public abstract class RowEntry {
	
	public static final String ATRI_TYPE = "Type";
	public static final String TYPE_INT = "INT";
	public static final String TYPE_DOUBLE = "DOUBLE";
	public static final String TYPE_STRING = "STRING";
	public static final String TYPE_LOCATION = "IP";
	public static final String TYPE_STRINGTREE = "STRINGTREE";
	public static final String TYPE_DUMMY = "NULL";

	public static final String ATRI_CAT = "Categorie";
	public static final String ATRI_LVL = "Level";
	public static final String ATRI_NAME = "Name";
	public static final String ATRI_LOG = "LogId";
	
	
	/**
	 * name stands for the name of this measure or type overall. 
	 * Examples are: "location", "elapsed time", "time", "security level", ...
	 */
	private String name;
	
	private String logId;

	public RowEntry(String name) {
		assert (name != null);
		
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	
	
	public String getLogId() {
		return logId;
	}





	private enum Id {
		
		INT("INT"), DOUBLE("DOUBLE"), LOCATION("IP"), STRING("STRING"), STRINGTREE("STRINGTREE"), DUMMY("NULL");
		
		private final String id;
		
		private Id(String id) {
			this.id = id;
			
		}
		
	}
	
}
