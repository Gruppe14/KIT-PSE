package controllers;

import java.io.File;
import java.io.FileFilter;
/**
 * Singleton scanning all chart types available
 * @author Lukas
 */
public class ChartIndex {
	private static ChartIndex instance = null;
	private String[] charts;
	//on initialisation scans the charts directory for subdirectories a.k.a. chartTypes
	private ChartIndex() {
		File chartDir = new File("./charts");
		File[] dirList = chartDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		charts = new String[dirList.length];
		for(int i = 0; i < dirList.length; i++) {
			charts[i] = dirList[i].getName();
		}
	}
	public static ChartIndex getInstance() {
		if(instance == null) {
			instance = new ChartIndex();
		}
		return instance;
	}
	public String[] getCharts() {
		return charts;
	}
}
