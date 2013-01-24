package what.sp_parser;

import java.util.Arrays;

/**
 * 
 * The DataEntry stores the data which will be written into the warehouse. The given attributes are representative
 * for possible data.
 * 
 * @author Alex
 *
 */
public class DataEntry implements Comparable<DataEntry> {
	
	/**
	 * The informations which are saved in a DataEntry
	 */
	private Object[] info;
	
	/**
	 * Protected constructor, because only ParsingTask should create new DataEntry-Objects.
	 * @param size the size of the info-array
	 */
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

	@Override
	public String toString() {
		return "DataEntry [info=" + Arrays.toString(info)
				+ "]";
	}



	@Override
	public int compareTo(DataEntry o) {
		
		for (int i = 0; i < info.length; i++) {
			if (!info[i].equals(o.getInfo(i))) {
				return info[i].hashCode() - o.getInfo(i).hashCode();
			}
		}
		
		return 0;

		
	}

	
	
	
	
}
