package what.sp_chart_creation;

//java imports
import java.util.HashMap;
import java.util.TreeSet;

/**
 * A chart with 2 dimensions and a measure.<br>
 * Extends a chart with 1 dimension. 
 * 
 * @author Jonathan, PSE Group 14
 * @see DimChart
 */
public class TwoDimChart extends DimChart {
	
	/** y axis of this TwoDimChart */
	private String y;
	
	/** name of the table of the y-axis of this TwoDimChart */
	private String yTable;
	
	/** category of the y axis of this TwoDimChart */
	private String yCategory;

	/**
	 * Protected constructor for a 2 dimension chart.
	 * 
	 * @param chartType chart type like bubble chart, ...
	 * @param x x-axis
	 * @param xTable table of x-axis
	 * @param xCategory category of x-axis
	 * @param y y-axis
	 * @param yTable table of y-axis
	 * @param yCategory category of y-axis
	 * @param measure measures like sum(*), rows, ...
	 * @param filterSets a Map containing filters
	 * @param start start time for request
	 * @param end end time for request
	 */
	protected TwoDimChart(String chartType, String x, String xTable, String xCategory, String y, String yTable, String yCategory, String measure, HashMap<String,TreeSet<String>> filterSets, int[] start, int[] end) {
		super(chartType, x, xTable, xCategory, measure, filterSets, start, end);
		this.y = y;		
		this.yCategory = yCategory;
		this.yTable = yTable;

	}
	
	/**
	 * Returns the y-axis table.
	 * 
	 * @return the yTable
	 */
	protected String getyTable() {
		return yTable;
	}

	/**
	 * Returns the y-axis category.
	 * 
	 * @return the yCategory
	 */
	protected String getYCategory() {
		return yCategory;
	}

	/**
	 * Returns the y-axis.
	 * 
	 * @return the y-axis
	 */
	protected String getY() {
		return y;
	}

}
