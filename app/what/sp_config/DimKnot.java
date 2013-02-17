package what.sp_config;

import java.util.TreeSet;

import what.sp_data_access.MySQLAdapter;

public class DimKnot {

	private final String value;
	
	private TreeSet<DimKnot> children;
	
	private final RowEntry row;

	public DimKnot(String value, RowEntry row) {
		super();
		this.value = value;
		this.row = row;
		
		children = new TreeSet<DimKnot>();
	}
	
	public void addChild(DimKnot child) {
		children.add(child);
	}
	
	public boolean hasChildren() {
		return (children.size() > 0);
	}
	
	public String getQuery() {
		// get one query part ( column name  = value
		String query = MySQLAdapter.LBR + getRow().getColumnName() + MySQLAdapter.EQL 
				+ MySQLAdapter.APOS + getValue() + MySQLAdapter.APOS;		
	    
		// get children's query part
		if (hasChildren()) {
			query += MySQLAdapter.AND ;
			
			DimKnot last = children.last();
			for (DimKnot dk : children) {
				query += dk.getQuery();
				if (dk != last) {
					query += MySQLAdapter.OR;
				}
				
			}
			
		}
		
		// end )
		query += MySQLAdapter.RBR;
		
		return query;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the row
	 */
	protected RowEntry getRow() {
		return row;
	}
	
}
