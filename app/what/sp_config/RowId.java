package what.sp_config;


public enum RowId {
	INT(ConfigHelper.TYPE_INT), DOUBLE(ConfigHelper.TYPE_DOUBLE), 
	LOCATION(ConfigHelper.TYPE_LOCATION), STRING(ConfigHelper.TYPE_STRING), 
	STRINGMAP(ConfigHelper.TYPE_STRINGMAP), DUMMY(ConfigHelper.TYPE_DUMMY);

	private final String id;

	private RowId(String id) {
		this.id = id;
	}
	
	protected String getName() {
		return this.id;
	}

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
	
	private boolean equals(String s) {
		assert (s != null);
		
		return id.equals(s);			
	}
}
