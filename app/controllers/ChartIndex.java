package controllers;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import what.FileHelper;
import what.Printer;

/**
 * Singleton scanning all chart types available
 * @author Lukas
 */
public class ChartIndex {
	private static ChartIndex instance = null;
	private String[] charts;
	//thumbs
	private HashMap<String, String> thumbs;
	//dimensions for charts
	private HashMap<String, Integer> dims;
	//on initialisation scans the charts directory for subdirectories a.k.a. chartTypes
	private ChartIndex() {
		thumbs = new HashMap<>();
		dims = new HashMap<>();
		File chartDir = new File("./charts");
		File[] dirList = chartDir.listFiles(new FileFilter() {
			//accept only charts that have all components
			public boolean accept(File file) {
				if(file.isDirectory()) {
					boolean thumb = false;
					boolean css = false;
					boolean js = false;
					boolean config = false;
					for(String s : file.list()) {
						if(s.matches("^(thumb)\\..*")) {
							thumb = true;
							thumbs.put(file.getName(), s);
						}
						if(s.equalsIgnoreCase(file.getName() + ".js")) {
							js = true;
						}
						if(s.equalsIgnoreCase(file.getName() + ".css")) {
							css = true;
						}
						if(s.equalsIgnoreCase("config.json")) {
							String jsonString = FileHelper.getStringContent(new File(file, s));
							if(jsonString != null){
								JSONObject json = null;
								try {
									json = new JSONObject(jsonString);
									int dim = json.getInt("dimensions");
									if(dim > 0 && dim < 3) {
										dims.put(file.getName(), dim);
										config = true;
									}
								} catch (JSONException e) {
									Printer.perror("Error with chart config");
								}
							}
						}
					}
					return thumb && css && js && config;
				}
				return false; 
			}
		});
		charts = new String[dirList.length];
		for(int i = 0; i < dirList.length; i++) {
			charts[i] = dirList[i].getName();
		}
	}
	
	/**
	 * method to get the instance of ChartIndex, initializes one, if not already done
	 * @return returns the instance of ChartIndex
	 */
	public static ChartIndex getInstance() {
		if(instance == null) {
			instance = new ChartIndex();
		}
		return instance;
	}
	/**
	 * method that returns all names of valid charts
	 * @return a string array with all the names
	 */
	public String[] getCharts() {
		return charts;
	}
	
	/**
	 * Method that returns the file name of a thumbnail for the chart type
	 * @param chart the chart name
	 * @return returns the file name
	 */
	public String getThumb(String chart) {
		return thumbs.get(chart);
	}
	
	/**
	 * method to get the number of dimensions for a chart
	 * @param chart the name of the chart
	 * @return returns the number of dimensions
	 */
	public int getDim(String chart) {
		return dims.get(chart);
	}
}
