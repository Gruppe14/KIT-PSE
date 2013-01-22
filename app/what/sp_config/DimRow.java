package what.sp_config;

import java.util.ArrayList;

public class DimRow {

	private ArrayList<RowEntry> rows = new ArrayList<RowEntry>();
	
	private Object strings;
		
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
		
		return (rows.get(0).getLevel() > 0);
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
		
		return (isDimension()) ? rows.get(0).getCategory() : rows.get(0).getName();
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
	
	
	// -- OVERRIDES -- OVERRIDES -- OVERRIDES -- OVERRIDES --	
	@Override
	public String toString() {
		return "DimRow [name= " + getName() +", size= " + getSize() + "]";
	}
}
