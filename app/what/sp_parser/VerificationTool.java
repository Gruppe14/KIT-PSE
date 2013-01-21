package what.sp_parser;

import java.util.Calendar;

import java.util.Date;

import what.sp_config.*;



public class VerificationTool {
	
	
	
	protected static boolean checkConfig(Logfile lf) {
		return checkTimeConfig(lf) && checkIpConfig(lf) && checkOtherConfig(lf);
		
		
	}
	
	private static boolean checkIpConfig(Logfile lf) {
		return lf.getPm().getConfig().getEntryAt(6).getClass().getName().contains("IpLocationRow");
	}



	private static boolean checkOtherConfig(Logfile lf) {

		ConfigWrap cw = lf.getPm().getConfig();
		
		
		for (int i = 7; i < cw.getSize(); i++) {
			if(!cw.getEntryAt(i).getClass().getSuperclass().getName().contains("RowEntry")) {
				return false;
			}
		}
		
		
		return true;
	}



	protected static boolean checkTimeConfig(Logfile lf) {
		
		ConfigWrap cw = lf.getPm().getConfig();
		
		String[] typeSplit = lf.getType().split(",");
		
		int i = 0;
		while (i < 6) {
			
			if (!cw.getEntryAt(i).getLogId().toLowerCase().equals(typeSplit[i].toLowerCase())) {
				return false;
			}
			i++;
		}
		return true;
	}

}
