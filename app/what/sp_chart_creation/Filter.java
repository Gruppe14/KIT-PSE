package what.sp_chart_creation;

import java.util.TreeSet;

import what.sp_config.DimKnot;
// intern imports
import what.sp_config.DimRow;
import what.sp_data_access.MySQLAdapter;

/**
 * This class represents a filter for a chart request.<br>
 * It contains restrictions for a query.
 * 
 * @author Jonathan, PSE Group 14
 * @see DimChart
 * @see MySQLAdapter
 */
public class Filter {

	/** SQL static string to create a table nick name. */
	private static final String ID = "ID";
	
	/** The dimension on which this filter runs. */
	private final DimRow dimension;
	
	/** The nick name for a table with restrictions: (...) as tableId. */
	private final String tableNickName;
	
	/** The trees of restrictions for the dimension. */
	private TreeSet<DimKnot> trees;
	
	/** 
	 * Boolean to say whether everything from this dimension is requested.
	 * Special treatment for axis, which are filters, but may not filter anything.
	 */
	private final boolean all;

	/**
	 * Public constructor for a Filter.<br>
	 * Creates a Filter from a given table, 
	 * it's corresponding table key and
	 * the restrictions itself.
	 * 
	 * @param table table in which will be filtered
	 * @param tableKey the corresponding tableKey to the table
	 * @param trees DimKnots containing the restrictions for this filter;
	 * 			if the Set is empty, nothing is filtered
	 */
	public Filter(DimRow dimension, TreeSet<DimKnot> trees) {
		if (dimension == null) {
			throw new IllegalArgumentException();
		} if (trees == null) {
			throw new IllegalArgumentException();
		}
		
		this.dimension = dimension;
		this.trees = trees;
		
		// check whether all is requested
		if (trees.size() == 0) {
			this.all = true;
		} else {
			this.all = false;
		}
		
		this.tableNickName = setTableId(dimension.getDimTableName());
		
	}

	// -- SETTER -- SETTER -- SETTER -- SETTER -- SETTER -- 
	/**
	 * Private helper class which creates a table nick name
	 * from a given table name.
	 * 
	 * @param table table name from which a nick name will be created
	 * @return table nick name from a given table name
	 */
	private String setTableId(String table) {
		return table + ID;
	}
	
	// -- GETTER -- GETTER -- GETTER -- GETTER -- GETTER -- 
	/**
	 * Returns the category of this Filter.
	 * 
	 * @return the category of this Filter
	 */
	public String getCategory() {
		return dimension.getName();
	}
	
	/**
	 * Returns the table of this Filter.
	 * 
	 * @return the table of this Filter
	 */
	protected String getTable() {
		return dimension.getDimTableName();
	}
	
	/**
	 * Returns the tableId of this Filter.
	 * 
	 * @return the tableId of this Filter
	 */
	protected String getTableNickName() {
		return tableNickName;
	}
	
	/**
	 * Returns the tableKey of this Filter.
	 * 
	 * @return the tableKey of this Filter
	 */
	public String getTableKey() {
		return dimension.getTableKey();
	}

	/**
	 * Returns whether something has to be filtered.
	 * 
	 * @return the whether something has to be filtered
	 */
	protected boolean noFilter() {
		return all;
	}
	
	/**
	 * Returns the level of the row described by the given String.
	 * 
	 * @param s name of the row which level is requested
	 * @return  the level of the row described by the given String
	 */
	protected int getLevelOf(String s) {
		assert (s != null);
		
		return dimension.getLevelOfRow(s);
	}
	
	/**
	 * Returns the dimension of this Filter.
	 * 
	 * @return the dimension of this Filter
	 */
	protected DimRow getDimension() {
		return dimension;
	}
	
	// -- SQL GETTER -- SQL GETTER -- SQL GETTER -- SQL GETTER --
	/**
	 * Returns the query part for the table key.<br>
	 * E.g. "dim_typeID.type_key".
	 * 
	 * @return the query part for the table key
	 */
	public String getKeyQuery() {
		return getTableNickName() + MySQLAdapter.DOT + getTableKey();
	}

	/**
	 * Returns the query from part for this table.<br>
	 * E.g.: (SELECT * FROM dim_type WHERE ... restrictions ...) AS dim_typeID
	 * 
	 * @return the query from part for this table
	 */
	public String getTableQuery() {
		// returns the query part, if all is requested
		if (noFilter()) {
			return getTable() + MySQLAdapter.AS + getTableNickName();
		}
		
		// ( SELECT *
		String query = MySQLAdapter.LBR + MySQLAdapter.SELECT +  MySQLAdapter.ALL;
		
		// FROM table WHERE
		query += MySQLAdapter.FROM + getTable() + MySQLAdapter.WHERE;
		
		// restrictions
		query += MySQLAdapter.LBR; // (
		DimKnot last = trees.last();
		
		for (DimKnot dk : trees) {
			
			query += dk.getQuery();
			
			// more restrictions?
			if (last != dk) {
				query += MySQLAdapter.OR;
			}
		}
		query += MySQLAdapter.RBR; // )
		
		// AS table nick name
		query +=  MySQLAdapter.AS + getTableNickName();
		
		return query;
	}


	
}
