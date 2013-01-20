package sp_dataMediation;

import what.sp_config.ConfigWrap;

public class DataMediator {
	
	/** Configuration on which this ChartMediator works on */
	private ConfigWrap config;

	public DataMediator(ConfigWrap config) {
		this.config = config;
	}
	
	/**
	 * Organizes things after data got uploaded.<br>
	 * Sets the String sets in the DimRows in the config and
	 * tells the OLAP Cubes to prae-compute it's data new.
	 * @return
	 */
	public boolean organizeData() {
		if (!config.computeDimRows()) {
			System.out.println("Computing a DimRow failed!");
			return false;
		}
		
		// TODO set the right class which organizes this... may be this class itself, but i don't know any methods which it may call yet
		/*if (someClassHere.computeOlapCubes()) {
			System.out.println("Computing OLAP cubes failed!");
			return false;
		}
		*/
		
		return true;
	}
	
	

}
