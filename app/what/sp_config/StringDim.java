package what.sp_config;

import java.util.TreeSet;

/**
 * A DimRow dimension only for Strings.
 * 
 * @author Jonathan, PSE Gruppe 14
 * @see StringRow
 * @see StrinMapRow
 */
public class StringDim extends DimRow {

	/** The trees with the content in the warehouse for this dimension. */
 	private TreeSet<DimKnot> trees = new TreeSet<DimKnot>(); 
 	
 	/**
	 * Adds a DimKnot to the collection of trees.
	 * 
	 * @param dk DimKnot to be added
	 * @return whether adding was successful
	 */
	public boolean addTree(DimKnot dk) {
		if (dk == null) {
			throw new IllegalArgumentException();
		}
		
		return trees.add(dk);		
	}
	
	
	/**
	 * Returns the tree of Strings of this DimRow if it exists.
	 * 
	 * @return the tree of Strings of this DimRow
	 */
	public TreeSet<DimKnot> getStrings() {
		return trees;
	}
 	
}
