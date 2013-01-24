package what.sp_config;

import java.util.ArrayList;
import java.util.Set;

public class DimRow {

	private ArrayList<RowEntry> rows = new ArrayList<RowEntry>();
	
	private Object strings;
	
	private static final String CITY = "city";
	private static final String COUNTRY = "country";
	private static final String LOCATION = "location";
	
	private static final DimRow LOCATION_DIM_INSTANCE; 
	static {
		LOCATION_DIM_INSTANCE = new DimRow();
		LOCATION_DIM_INSTANCE.add(new StringRow(COUNTRY, "", 1, LOCATION, COUNTRY, null));
		LOCATION_DIM_INSTANCE.add(new StringRow(CITY, "", 2, LOCATION, CITY, null));
	}
		
	// -- SETTER -- SETTER -- SETTER -- SETTER -- SETTER --
 	protected void add(RowEntry rowEntry) {
		if (rowEntry == null) {
			throw new IllegalArgumentException();
		}
		
		rows.add(rowEntry);		
	}
	
	

	public boolean setStrings() {
		if (!isStringDim()) {
			return true;
		}
		
		int len = rows.size();
		RowId id = rows.get(0).getId();
		
		if (len == 1) {
			if (id == RowId.STRING) {
				// TODO query against warehouse, which values exist
			} else if (id == RowId.STRINGMAP) {
				StringMapRow cur = (StringMapRow) rows.get(0);
				strings = cur.getTopicStrings();
				return true;
			}
		} else {
			//TODO build trees
		}
		
		
		return true;
	}
	
	
	// -- CHECKER -- CHECKER -- CHECKER -- CHECKER -- CHECKER --
	public boolean isDimension() {
		if (!isNotEmpty()) {
			return false;
		}
		RowEntry re = rows.get(0);
		
		return ((re.getLevel() > 0) || (re.getId().equals(RowId.STRING)) || (re.getId().equals(RowId.STRINGMAP)));
	}
	
	
	public boolean isStringDim() {
		if (!isNotEmpty()) {
			return false;
		}
		
		RowId cur = rows.get(0).getId();
		return ((cur == RowId.STRING) || (cur == RowId.STRINGMAP));
	}
	
	private boolean isNotEmpty() {
		return (getSize() > 0);
	}
	
	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	public int getSize() {
		return rows.size();
	}
		
	public String getName() {
		if (!isNotEmpty()) {
			return null;
		}
		
		return rows.get(0).getCategory();
	}
	
	public String getScaleAt(int i) {
		if (getSize() <= i) {
			throw new IllegalArgumentException();
		}
		
		return rows.get(i).getScale();
	}

	public Object getStrings() {
		if (!isStringDim()) {
			return null;
		}
		
		return strings;
	}
	
	public String getTableName() {
		return ConfigHelper.DIM_TABLE + getName();
	}
	
	public String getTableKey() {
		return getName() + ConfigHelper.KEY_TABLE;
	}
	
	public String getRowNameOfLevel(int i) {
		if ((i < 0) || (i >= getSize())) {
			throw new IllegalArgumentException();
		}
		
		return "row_" + rows.get(i).getName();
	}
	
	public String getNameOfLevel(int i) {
		if ((i < 0) || (i >= getSize())) {
			throw new IllegalArgumentException();
		}
		
		return rows.get(i).getName();
	}
	
	public static DimRow getLocationDim() {
		return LOCATION_DIM_INSTANCE;
	}
	
	public String getTableTypeAt(int i) {
		if ((i < 0) || (i >= getSize() )) {
			throw new IllegalArgumentException();
		}
			
		return rows.get(i).getTableType();
	}
	
	// -- OVERRIDES -- OVERRIDES -- OVERRIDES -- OVERRIDES --	
	@Override
	public String toString() {
		return "DimRow [name= " + getName() +", size= " + getSize() + "]";
	}
	



	
}
