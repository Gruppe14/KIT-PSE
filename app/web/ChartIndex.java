package web;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

import what.FileHelper;
import what.Printer;

/**
 * Singleton scanning all chart types available.
 * @author Lukas
 */
public class ChartIndex {
	/** the instance of chart index for this singleton class. */
	private static ChartIndex instance = null;
	/** an array containing the names of all available charts. */
	private String[] charts;
	/** a hashmap containing the thumbnail picture name of a chart. */
	private HashMap<String, String> thumbs;
	/** a hashset containing the names of charts that have a css file. */
	private HashSet<String> css;
	/** a hashmap containing the dimensions of a chart as read from the charts config file. */
	private HashMap<String, Integer> dims;
	/**
	 * on initialisation scans the charts directory for subdirectories a.k.a. chartTypes.
	 */
	private ChartIndex() {
		thumbs = new HashMap<>();
		css = new HashSet<>();
		dims = new HashMap<>();
		File chartDir = new File("./charts");
		File[] dirList = chartDir.listFiles(new FileFilter() {
			//accept only charts that have all components
			public boolean accept(File file) {
				if (file.isDirectory()) {
					boolean thumb = false;
					boolean js = false;
					boolean config = false;
					for (String s : file.list()) {
						if (s.matches("^(thumb)\\..*")) {
							thumb = true;
							thumbs.put(file.getName(), s);
						}
						if (s.equalsIgnoreCase(file.getName() + ".js")) {
							js = true;
						}
						if (s.equalsIgnoreCase(file.getName() + ".css")) {
							css.add(file.getName());
						}
						if (s.equalsIgnoreCase("config.json")) {
							String jsonString = FileHelper.getStringContent(new File(file, s));
							if (jsonString != null) {
								JSONObject json = null;
								try {
									json = new JSONObject(jsonString);
									int dim = json.getInt("dimensions");
									if (dim > 0 && dim < 3) {
										dims.put(file.getName(), dim);
										config = true;
									}
								} catch (JSONException e) {
									Printer.perror("Error with chart config");
								}
							}
						}
					}
					return thumb && js && config;
				}
				return false; 
			}
		});
		charts = new String[dirList.length];
		for (int i = 0; i < dirList.length; i++) {
			charts[i] = dirList[i].getName();
		}
	}
	
	/**
	 * get the instance of ChartIndex, initializes one, if not already done.
	 * @return returns the instance of ChartIndex
	 */
	public static ChartIndex getInstance() {
		if (instance == null) {
			instance = new ChartIndex();
		}
		return instance;
	}
	/**
	 * returns all names of valid charts.
	 * @return a string array with all the names
	 */
	public String[] getCharts() {
		return charts;
	}
	
	/**
	 * returns the file name of a thumbnail for the chart type.
	 * @param chart the chart name
	 * @return returns the file name
	 */
	public String getThumb(String chart) {
		return thumbs.get(chart);
	}
	
	/**
	 * returns wether a chart has a chart specific css file.
	 * @param chart the chart name
	 * @return returns true if a chart specific css file exists, false otherwise
	 */
	public boolean hasCss(String chart) {
		return css.contains(chart);
	}
	
	/**
	 * get the number of dimensions for a chart.
	 * @param chart the name of the chart
	 * @return returns the number of dimensions
	 */
	public int getDim(String chart) {
		return dims.get(chart);
	}
}
