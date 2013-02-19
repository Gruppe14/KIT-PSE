package what.sp_config;

// java imports
import java.util.TreeSet;

// intern imports
import what.sp_data_access.MySQLAdapter;

/**
 * This class represents a entry in the warehouse of
 * a specific column. It also stores all the knots
 * of his children.
 * 
 * @author Jonathan, PSE Gruppe 14
 * @see RowEntry
 * @see DimRow
 */
public class DimKnot implements Comparable<DimKnot> {

	/** The value, a String, of this knot. */
	private final String value;
	
	/** The children of this knot. */
	private TreeSet<DimKnot> children;
	
	/** The row from which this DimKnit is. */
	private final RowEntry row;

	/**
	 * Public constructor for a DimKnot
	 * with it's value and the referring RowEntry.
	 * 
	 * @param value String value of this knot
	 * @param row RowEntry to which this knot belongs
	 */
	public DimKnot(String value, RowEntry row) {
		super();
		this.value = value;
		this.row = row;
		
		children = new TreeSet<DimKnot>();
	}
	
	/**
	 * Adds a child to this knot.
	 * 
	 * @param child DimKnot as child to be added
	 */
	public void addChild(DimKnot child) {
		children.add(child);
	}
	
	/**
	 * Returns whether this knot as children.
	 * 
	 * @return whether this knot as children
	 */
	public boolean hasChildren() {
		return (children.size() > 0);
	}
	
	/**
	 * Returns the query as WHERE condition for this knot
	 * and all his children.
	 * 
	 * @param table the table of this knot for the query "table."
	 * @return the query as WHERE condition for this knot
	 * and all his children
	 */
	public String getQuery(String table) {
		// get one query part ( column name  = value
		String query = MySQLAdapter.LBR + table + MySQLAdapter.DOT
				+ getRow().getColumnName() + MySQLAdapter.EQL 
				+ MySQLAdapter.APOS + getValue() + MySQLAdapter.APOS;		
	    
		// get children's query part
		if (hasChildren()) {
			query += MySQLAdapter.AND + MySQLAdapter.LBR;
			
			DimKnot last = children.last();
			for (DimKnot dk : children) {
				query += dk.getQuery(table);
				if (dk != last) {
					query += MySQLAdapter.OR;
				}
				
			}
			
			query +=  MySQLAdapter.RBR;	
		}
		
		// end )
		query +=  MySQLAdapter.RBR;
		
		return query;
	}

	/**
	 * Returns the value of this knot.
	 * 
	 * @return the value of this knot
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the name of the row of this knot.
	 * 
	 * @return the name of the row of this knot
	 */
	public String getRowName() {
		return row.getName();
	}

	/**
	 * Returns a Collection of the children of this knot.
	 * 
	 * @return a Collection of the children of this knot
	 */
	public TreeSet<DimKnot> getChildren() {
		return children;
	}

	
	/**
	 * Returns the row of this knot.
	 * 
	 * @return the row of this knot
	 */
	private RowEntry getRow() {
		return row;
	}
	
	@Override
	public int compareTo(DimKnot o) {
		return this.getValue().compareTo(o.getValue());
	}

	@Override
	public String toString() {
		return "\n>> >> Knot[" + value + ", row="
				+ row.getName() + ", children=" + children + "]";
	}
	
}
