package what.sp_chart_creation;

import java.util.ArrayList;

//intern imports

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
	private Filter yFilter;
	
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
	protected TwoDimChart(String chartType, String x, Filter xFilter, String y, Filter yFilter, String measure, ArrayList<Filter> filters) {
		super(chartType, x, xFilter, measure, filters);
		
		assert (yFilter != null);
		assert (y != null);
		
		this.y = y;		
		this.yFilter = yFilter;
	}
	
	/**
	 * Returns the y-axis.
	 * 
	 * @return the y-axis
	 */
	protected String getY() {
		return y;
	}

	/**
	 * Returns the y Filter.
	 * 
	 * @return the y Filter
	 */
	protected Filter getYFilter() {
		return yFilter;
	}
}
