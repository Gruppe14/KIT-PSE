package what.sp_config;

/**
 * A DimRow dimension for the time.
 * 
 * @author Jonathan, PSE Gruppe 14
 *
 */
public class TimeDimension extends DimRow {
	
	/** Length of the stored time. */
	public static final int TIME_WEBPAGE_LENGTH = 3;
	
	/** Number of rows. Minimal and maximal. */
	public static final int TIME_MIN_MAX = 2;

	/** Length of the stored time. */
	public static final int TIME_LENGTH = 6;
	
	/** 
	 * The time of the time dimension in the warehouse.<br>
	 * First row = from<br>
	 * Second row = to<br>
	 *  
	 */
	private int[][] time;


	/**
	 * Returns the minimal and maximal time.<br>
	 * First row = from<br>
	 * Second row = to<br>
	 * 
	 * @return first time of this dimension
	 */
	public int[][] getMinMaxTime() {
		return time;
	}

	/**
	 * Sets the minimal time of this time dimension.
	 * 
	 * @param from the minimal time 
	 */
	public void setMinMaxTime(int[][] minMaxTime) {
		assert (minMaxTime.length == 2);
		assert (minMaxTime[0].length == TIME_WEBPAGE_LENGTH);
		
		this.time = minMaxTime;
	}

}
