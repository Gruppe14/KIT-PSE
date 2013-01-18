package what;

/**
 * The abstract class ChartVisitor represents a Visitor
 * for a ChartHost/Petra.<br>
 * 
 * For the interaction of tools, like {@see DataPreparer} ... and a {@see ChartHost} 
 * the visitor pattern is used. Thereby a ChartVisitor fulfills the role of a visitor
 * in the pattern.
 * 
 * 
 * @author Jonathan, PSE Gruppe 14
 *
 */
public abstract class ChartVisitor {

	abstract public boolean visitOneDim(OneDimChart host);
	
	abstract public boolean visitTwoDim(TwoDimChart host);
	
	abstract public boolean visitThreeDim(ThreeDimChart host);
	
	static public ChartVisitor getInstance() {
		return null;
	}
}
