package what.sp_data_access;

import java.util.ArrayList;

import what.Printer;
import what.sp_config.ConfigWrap;
import what.sp_config.DimRow;

/**
 * The DBTableBuilder class provides a method to
 * build a MySQL statement from a given configuration.
 * 
 * @author Jonathan, PSE Gruppe 14
 *
 */
public final class DBTableBuilder {
	
	// Strings to create and attributes
	/** MySQL short constant. */
	private static final String CREATE = "CREATE TABLE IF NOT EXISTS ";
	/** MySQL short constant. */
	private static final String UNIQUE = " UNIQUE ";
	/** MySQL short constant. */
	private static final String INT = "INT(5)";
	
	// Strings for ordering
	/** MySQL short constant. */
	private static final String LBR = " ( ";
	/** MySQL short constant. */
	private static final String KOMMA = ",  ";
	/** MySQL short constant. */
	private static final String RBR = " ) ";
	/** MySQL short constant. */
	private static final String SPACE = " ";
	/** MySQL short constant. */
	private static final String SEMI = ";";
	
	/**
	 * Private Constructor as this is a helper class
	 * providing just static methods.
	 */
	private DBTableBuilder() {
		throw new AssertionError();
	}
    
	/**
	 * Returns an array of Strings containing MySQL queries for creating
	 * the fact table and the dimension tables for a given configuration. 
	 * 
	 * @param config configuration for which the Strings will be created
	 * @return array of Strings describing the table schema of the warehouse as query
	 */
    public static String[] getDataBaseQuery(ConfigWrap config) {
    	assert (config != null);
    	
		int dimCounter = 0;
		
		String[] creates = new String[config.getNumberOfDimsWitoutRows() + 1];
		// query for the fact table
		String fact = CREATE + config.getFactTableName() + LBR;
		
		
		ArrayList<DimRow> dims = config.getDims();
		// get last to set "," at right positions
		DimRow lastDim = dims.get(dims.size() - 1); 
		for (DimRow d : dims) {
			if (d.isDimension()) { // case it's a dimension
				dimCounter++;
				String curDim = CREATE + d.getDimTableName() + LBR;
				
				// add all rows to the trunk
				for (int i = 0, l = d.getSize(); i < l; i++) {
					curDim += d.getColumnNameOfLevel(i) + SPACE 
							+ d.getRowAt(i).getTableType() + KOMMA;
				}
				
				
				// end this trunk: key + ) + VALUES
				curDim += d.getTableKey() + SPACE + INT + UNIQUE + RBR + SEMI;
				// add to trunks
				creates[dimCounter] = curDim;
	
				// store key row of this dimension to the fact trunk
				fact += d.getTableKey() + SPACE + INT;				
			} else { // case it is fact
				fact += d.getColumnNameOfLevel(0) + SPACE + d.getRowAt(0).getTableType();	
			}
			
			// not last one -> ,
			if (!d.equals(lastDim)) {
				fact += KOMMA;
			}
		
		}
		
		// end fact trunk
		fact += RBR + SEMI;
		
		// add the fact trunk
		creates[0] = fact;

		
		// TEST
		/*for (int i = 0, j = creates.length; i < j; i++) {
			Printer.ptest("Create query " + i + ": " + creates[i]);
		}*/
		
		return creates;
    }

}



