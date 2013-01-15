package appTier;

import java.io.File;

import appTier.sb_config.ConfigDB;

abstract class Petra {
	
	private ConfigDB confi;
	
	private File json;
	
	public Petra(ConfigDB confi) {
		this.confi = confi;
		
	}

	public File getJSON() {
		return json;
	}
	
	abstract public boolean accept(ChartVisitor v);
	
}
