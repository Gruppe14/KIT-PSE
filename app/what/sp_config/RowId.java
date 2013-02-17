package what.sp_config;

import what.JSONReader;

/**
 * Enumeration class wrapping Strings for
 * specific row types, making it easier
 * to determine the type of RowEntries, while
 * reading from the JSON file and later.
 * 
 * @author Jonathan, PSE Gruppe 14
 * @see RowEntry
 * @see JSONReader
 */
public enum RowId {
	
	INT(RowEntry.TYPE_INT), DOUBLE(RowEntry.TYPE_DOUBLE), 
	LOCATION(RowEntry.TYPE_LOCATION), STRING(RowEntry.TYPE_STRING), 
	STRINGMAP(RowEntry.TYPE_STRINGMAP), DUMMY(RowEntry.TYPE_DUMMY);

	/** The String defining this RowId. */
	private final String id;

	/**
	 * Private enumeration Constructor.
	 * 
	 * @param id String defining the RowId.
	 */
	private RowId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the name of this RowId.
	 * 
	 * @return the name of this RowId
	 */
	protected String getName() {
		return this.id;
	}

	/**
	 * Returns the RowId to which the given String refers.  
	 * 
	 * @param type String referring to the RowId, as it's name
	 * @return the RowId to which the given String refers
	 */
	protected static RowId getRowIdByString(String type) {
		assert (type != null);
		
		if (INT.equals(type)) {
			return INT;
		} else if (DOUBLE.equals(type)) {
			return  DOUBLE;
		} else if (LOCATION.equals(type)) {
			return  LOCATION;
		} else if (STRING.equals(type)) {
			return  STRING;
		} else if (STRINGMAP.equals(type)) {
			return  STRINGMAP;
		} else if (DUMMY.equals(type)) {
			return  DUMMY;
		} 
					
		return null;
	}
	
	/**
	 * Private equal method comparing a given String
	 * with the name of this RowId.
	 * 
	 * @param s String to compare with
	 * @return whether the given String equals the name of this RowId
	 */
	private boolean equals(String s) {
		assert (s != null);
		
		return id.equals(s);			
	}
}
