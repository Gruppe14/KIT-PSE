package what;

import java.io.File;

import what.sp_config.ConfigWrap;

abstract class Petra {
	
	private ConfigWrap confi;
	
	private File json;
	
	public Petra(ConfigWrap confi) {
		this.confi = confi;
		
	}

	public File getJSON() {
		return json;
	}
	
	abstract public boolean accept(ChartVisitor v);
	
}
