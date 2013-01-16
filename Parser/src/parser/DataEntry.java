package parser;

import java.util.Arrays;
import java.util.Date;

/**
 * 
 * The DataEntry stores the data which will be written into the warehouse. The given attributes are representative
 * for possible data.
 * 
 * @author Alex
 *
 */
public class DataEntry {
	
	private Date date;
	
	private String ip;
	
	private String city;
	
	private String country;
	
	private Object[] info;
	
	
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the info
	 */
	public Object getInfo(int i) {
		return info[i];
	}

	/**
	 * @param date the date to set
	 */
	protected void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param ip the ip to set
	 */
	protected void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @param city the city to set
	 */
	protected void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param country the country to set
	 */
	protected void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param info the info to set
	 */
	protected void setInfo(Object info, int i) {
		this.info[i] = info;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataEntry [date=" + date + ", ip=" + ip + ", city=" + city
				+ ", country=" + country + ", info=" + Arrays.toString(info)
				+ "]";
	}

	
	
	
	
}
