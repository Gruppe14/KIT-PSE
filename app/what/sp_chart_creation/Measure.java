package what.sp_chart_creation;

import java.util.ArrayList;

import what.Printer;
import what.sp_config.RowEntry;

public class Measure {
	
	public static final String COUNT = "#";

	public static final String COUNT_SQL = "count(*)";
	
	public static final String SUM = "SUM";
	
	public static final String MAX = "MAX";
	
	public static final String AVG = "AVG";
	
	/* Left and right bracket. */
	private static final String LBR = "(";
	private static final String RBR = ") ";
	
	private static ArrayList<String> aggregations = new ArrayList<String>();
	
	static {
		aggregations.add(COUNT);
		aggregations.add(SUM);
		aggregations.add(MAX);
		aggregations.add(AVG);
		
	}
	
	private String aggregation;
	
	private RowEntry row;

	private Measure(String aggregation, RowEntry row) {
		assert (aggregation != null);
		assert (row != null);
		
		this.row = row;
		this.aggregation = aggregation;
	}

	public static Measure getMeasure(String aggregation, RowEntry row) {
		if (aggregation == null) {
			throw new IllegalArgumentException();
		}
		String agg = getRightAggregation(aggregation);
		if (agg == null) {
			Printer.perror("Aggregation is not legal.");
			return null;
		}
		
		if ((row == null) && (agg != COUNT_SQL)) {
				throw new IllegalArgumentException();
		}

		return new Measure(agg, row);
	}
	
	private static String getRightAggregation(String agg) {
		assert (agg != null);
		
		if (agg.equalsIgnoreCase(COUNT_SQL)) {
			return COUNT_SQL;
		} else if (agg.equalsIgnoreCase(SUM)) {
			return SUM;
		} else if (agg.equalsIgnoreCase(MAX)) {
			return MAX;
		} else if (agg.equalsIgnoreCase(AVG)) {
			return AVG;
		}
			
		return null;
	}

	protected String getMeasureSelect() {
		if (getAggregation() == COUNT_SQL) {
			return COUNT_SQL ;
		} else if (getAggregation() == SUM) {
			return SUM + LBR + getMeasureRow() + RBR;
		} else if (getAggregation() == MAX) {
			return MAX + LBR + getMeasureRow() + RBR;
		} else if (getAggregation() == AVG) {
			return AVG + LBR + getMeasureRow() + RBR;
		} 
			
		Printer.pfail("Finding a fitting measure.");		
		
		return null;
	}
	
	/**
	 * @return the aggregation
	 */
	private String getAggregation() {
		return aggregation;
	}

	protected String getMeasureRow() {
		return row.getColumnName();
	}
	
	protected String getName() {
		return row.getName();
	}
	
	public ArrayList<String> getAggregations() {
		return aggregations;
	}
	
}
