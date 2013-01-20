package what.sp_parser;

import java.util.Calendar;

import java.util.Date;

import what.sp_config.*;



public class VerificationTool {
	
	protected static boolean verify(ParsingTask pt) {
		
		
		return (verifyTime(pt) && verifyServerInfo(pt));
		
	}
	
	

	private static boolean verifyTime(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		Calendar cal = Calendar.getInstance();
		Date dateNow = cal.getTime();
		
	
		return true;
	}
	
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

	private static boolean verifyServerInfo(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		
		
		if (!verifyIP(pt)) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
		return true;
	}

	public static boolean verifyIP(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		
		if (de.getCity() == null) {
			return false;
		}
		
		if (de.getCountry() == null) {
			return false;
		}

		return true;		
	}
}
