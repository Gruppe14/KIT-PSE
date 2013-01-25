package what.sp_chart_creation;

import java.util.HashMap;
import java.util.TreeSet;


public class TwoDimChart extends DimChart {
	
	private String y;
	private String yTable;
	private String yCategorie;

	public TwoDimChart(String chartType, String x, String xTable, String xCategorie, String y, String yTable, String yCategorie, String measure, HashMap<String,TreeSet<String>> filterSets, int[] start, int[] end) {
		super(chartType, x, xTable, xCategorie, measure, filterSets, start, end);
		this.y = y;		
		this.yCategorie = yCategorie;
		this.yTable = yTable;

	}
	
	/**
	 * @return the yTable
	 */
	protected String getyTable() {
		return yTable;
	}

	/**
	 * @return the yCategorie
	 */
	protected String getyCategorie() {
		return yCategorie;
	}

	protected String getY() {
		return y;
	}

}
