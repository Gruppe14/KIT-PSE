package appTier.sb_config;

public class Measure extends ConfigEntry {

	/**
	 * scaleName describes the scale of this measure.
	 * For example elapse time may have "ms" or "*10^-3 s" for milliseconds.
	 */
	private String scaleName;

	
	
	/**
	 * Returns the scaleName of this Measure.
	 * 
	 * @return the scaleName of this Measure.
	 */
	public String getScale() {
		return scaleName;
	}
}
