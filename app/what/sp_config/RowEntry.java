package what.sp_config;

public abstract class RowEntry {
	
	private final RowId id;
	
	/**
	 * name stands for the name of this measure or type overall. 
	 * Examples are: "location", "elapsed time", "time", "security level", ...
	 */
	private final String name;
	
	private final String logId;
	
	private final int level;
	
	private final String categorie;
	
	private final String scale;
	
	protected RowEntry(String name, String logId, int lvl, String categorie, String scale, RowId id) {
		assert (name != null);
		assert (logId != null);
		assert (categorie != null);
		assert (scale != null);
		assert (lvl <= 0);
		
		this.name = name;
		this.logId = logId;
		this.level = lvl;
		this.categorie = categorie;
		this.scale = scale;
		
		this.id = id;
	}
	
	
	public String getName() {
		return name;
	}
	
	public String getLogId() {
		return logId;
	}

	public int getLevel() {
		return level;
	}

	public String getCategorie() {
		return categorie;
	}

	public String getScale() {
		return scale;
	}

	public RowId getId() {
		return id;
	} 
}
