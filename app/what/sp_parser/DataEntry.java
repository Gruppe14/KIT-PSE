package what.sp_parser;

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
	
	private Object[] info;
	
	
	protected DataEntry(int size) {
		info = new Object[size];
	}
	
	
	
	/**
	 * @return the info
	 */
	public Object getInfo(int i) {
		return info[i];
	}

	
	/**
	 * @param info the info to set
	 */
	public void setInfo(Object info, int i) {
		this.info[i] = info;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataEntry [info=" + Arrays.toString(info)
				+ "]";
	}

	
	
	
	
}
