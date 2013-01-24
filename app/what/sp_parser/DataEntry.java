package what.sp_parser;

import java.util.Arrays;
import java.util.TreeSet;

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

	// just for testing:
	public static DataEntry ONE = new DataEntry(14);
	public static DataEntry TWO = new DataEntry(14);
	public static DataEntry THREE = new DataEntry(14);
	static {
		ONE.setInfo(2012, 0);
		ONE.setInfo(12, 1);
		ONE.setInfo(24, 2);
		ONE.setInfo(13, 3);
		ONE.setInfo(37, 4);
		ONE.setInfo(31, 5);
		ONE.setInfo("Germany", 6);
		ONE.setInfo("Birkenfeld", 7);
		ONE.setInfo("Server3", 8);
		ONE.setInfo("DB7", 9);
		ONE.setInfo(0.1, 10);
		ONE.setInfo(3.4, 11);
		ONE.setInfo(4, 12);
		ONE.setInfo("Photografie", 13);
		
		TWO.setInfo(2011, 0);
		TWO.setInfo(11, 1);
		TWO.setInfo(23, 2);
		TWO.setInfo(13, 3);
		TWO.setInfo(3, 4);
		TWO.setInfo(0, 5);
		TWO.setInfo("Germany", 6);
		TWO.setInfo("Ruhekarls", 7);
		TWO.setInfo("Server6", 8);
		TWO.setInfo("DB7", 9);
		TWO.setInfo(0.91, 10);
		TWO.setInfo(3.43, 11);
		TWO.setInfo(1, 12);
		TWO.setInfo("Aha", 13);
		
		
		THREE.setInfo(2012, 0);
		THREE.setInfo(12, 1);
		THREE.setInfo(24, 2);
		THREE.setInfo(13, 3);
		THREE.setInfo(39, 4);
		THREE.setInfo(33, 5);
		THREE.setInfo("Quasiland", 6);
		THREE.setInfo("Nebenan", 7);
		THREE.setInfo("Ser3", 8);
		THREE.setInfo("DBase8", 9);
		THREE.setInfo(0.1, 10);
		THREE.setInfo(3.0, 11);
		THREE.setInfo(100, 12);
		THREE.setInfo("Gesichter", 13);

	}
	public static TreeSet<DataEntry> getTests() {
		TreeSet<DataEntry> bla = new TreeSet<DataEntry>();
		bla.add(ONE);
		//bla.add(TWO);
		//bla.add(THREE);
		return bla;
	}



	@Override
	public int compareTo(DataEntry o) {
		
		for (int i = 0; i < info.length; i++) {
			if (!info[i].equals(o.getInfo(i))) {
				if (info[i] != null && o.getInfo(i) != null) {
					return info[i].hashCode() - o.getInfo(i).hashCode();
				}
			}
		}
		
		return 0;

		
	}

	
	public static DataEntry getOne() {
		return TWO;
	}
}
