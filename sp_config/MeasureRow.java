package what.sp_config;

public abstract class MeasureRow extends RowEntry {

	/**
	 * scaleName describes the scale of this measure.
	 * For example elapse time may have "ms" or "*10^-3 s" for milliseconds.
	 */
	private String scaleName;

	public MeasureRow(String name, String scaleName) {
		super(name);
		this.scaleName = scaleName;
	}
	
	/**
	 * Returns the scaleName of this Measure.
	 * 
	 * @return the scaleName of this Measure.
	 */
	public String getScale() {
		return scaleName;
	}
}
