import java.util.Arrays;

/**
 * 
 * The DataEntry stores the data which will be written into the warehouse. The given attributes are representative
 * for possible data.
 * 
 * @author Alex
 *
 */
public class DataEntry {
	
	private int hour;
	
	private int day;
	
	private int month;
	
	private int year;
	
	private int rows;
	
	private int elapsedTime;
	
	private int busyTime;
	
	private String ip;
	
	private String city;
	
	private String country;
	
	private String database;
	
	private String server;
	
	private Object[] additionalInfo = new Object[5];

	/**
	 * Protected constructor for a <code>DataEntry</code>.
	 */
	protected DataEntry() {
		//overwritten to be protected.
	}
	
	/**
	 * @return the hour
	 */
	protected int getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	protected void setHour(int hour) {
		this.hour = hour;
	}

	/**
	 * @return the day
	 */
	protected int getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	protected void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return the month
	 */
	protected int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	protected void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	protected int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	protected void setYear(int year) {
		this.year = year;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataEntry [hour=" + hour + ", day=" + day + ", month=" + month
				+ ", year=" + year + ", rows=" + rows + ", elapsedTime="
				+ elapsedTime + ", busyTime=" + busyTime + ", ip=" + ip
				+ ", city=" + city + ", country=" + country + ", database="
				+ database + ", server=" + server + ", additionalInfo="
				+ Arrays.toString(additionalInfo) + "]";
	}

	/**
	 * @return the rows
	 */
	protected int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	protected void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the elapsedTime
	 */
	protected int getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	protected void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the busyTime
	 */
	protected int getBusyTime() {
		return busyTime;
	}

	/**
	 * @param busyTime the busyTime to set
	 */
	protected void setBusyTime(int busyTime) {
		this.busyTime = busyTime;
	}

	/**
	 * @return the ip
	 */
	protected String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	protected void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the city
	 */
	protected String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	protected void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	protected String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	protected void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the database
	 */
	protected String getDatabase() {
		return database;
	}

	/**
	 * @param database the database to set
	 */
	protected void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @return the server
	 */
	protected String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	protected void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the additionalInfo
	 */
	protected Object[] getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	protected void setAdditionalInfo(Object[] additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	
	
	
	
}
