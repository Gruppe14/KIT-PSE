package what.sp_config;

/**
 * <p>A RowEntry describes a element in the log file and
 * a row in the warehouse.<br>
 * Therefore it stores some information like type, name, logId
 * and whether it is in a dimension or not.</p>
 * 
 * This is the abstract parent class for specific RowEntries.
 * 
 * @author Jonathan, PSE Gruppe 14
 * @version 1.0
 *
 * @see ConfigWrap
 */
public abstract class RowEntry {
	

	// -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES -- ATTRIBUTES --
	/**
	 * The RowId of this RowEntry to determine the type without using instanceOf().
	 * @see RowId
	 */
	private final RowId id;
	
	/**
	 * The name of this RowEntry.<br>
	 * Examples are: "location", "elapsed time", "time", "security level", ...	
	 */
	private final String name;
	
	/**
	 * The logId of this RowEntry, like "yy" or "yyyy" for year.
	 */
	private final String logId;
	
	/**
	 * The level in a dimension for this RowEntry.<br>
	 * Should be zero if not a dimension. 1 stands for top level.
	 */
	private final int level;
	
	/**
	 * The category or dimension name of this RowEntry.<br>
	 *  Should be "none" if it's not part of a dimension.
	 */
	private final String category;
	
	/**
	 * The scale stands for the String on the axis in a chart for this RowEntry.<br>
	 * Examples are: "ms", "#rows", "database", ...
	 */
	private final String scale;
	
	// -- CONSTRUCTOR -- CONSTRUCTOR -- CONSTRUCTOR -- CONSTRUCTOR --
	/**
	 * Constructor for a RowEntry.
	 * 
	 * @param name the name
	 * @param logId the logId
	 * @param lvl the level
	 * @param category the category
	 * @param scale the scale String
	 * @param id the RowId
	 */
	protected RowEntry(String name, String logId, int lvl, String category, String scale, RowId id) {
		assert (name != null);
		assert (logId != null);
		assert (category != null);
		assert (scale != null);
		assert (lvl < 0);
		
		this.name = name;
		this.logId = logId;
		this.level = lvl;
		this.category = category;
		this.scale = scale;
		
		this.id = id;
	}
	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the logId.
	 * 
	 * @return the logId
	 */
	public String getLogId() {
		return logId;
	}

	/**
	 * Returns the level.
	 * 
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Returns the category.
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the scale String.
	 * 
	 * @return the scale
	 */
	public String getScale() {
		return scale;
	}

	/**
	 * Returns the RowId.
	 * 
	 * @return the RowId
	 */
	public RowId getId() {
		return id;
	} 
	
	// -- OVERRIDES -- OVERRIDES -- OVERRIDES -- OVERRIDES --
	@Override
	public String toString() {
		return "RowEntry [id=" + id + ", name=" + name + ", logId=" + logId
				+ ", level=" + level + ", category=" + category + ", scale="
				+ scale + "]\n";
	}
}
