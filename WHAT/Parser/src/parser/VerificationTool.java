package parser;
import java.util.Calendar;
import java.util.Date;



public class VerificationTool {
	
	protected static boolean verify(ParsingTask pt) {
		
		
		return (verifyTime(pt) && verifyServerInfo(pt));
		
	}

	@SuppressWarnings("deprecation")
	private static boolean verifyTime(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		Calendar cal = Calendar.getInstance();
		Date dateNow = cal.getTime();
		
		if (de.getYear() < 2001 || de.getYear() > dateNow.getYear() + 1900) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
		if (de.getMonth() < 1 || de.getMonth() > 12) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
		if (de.getDay() < 1 || de.getDay() > 31) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
		if (de.getHour() < 0 || de.getHour() > 23) {
			pt.getPm().increaseLinedel();
			return false;
		}
	
		return true;
	}

	private static boolean verifyServerInfo(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		
		if (de.getRows() < 0) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
		if (de.getElapsedTime() < 0) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
		if (de.getBusyTime() < 0) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
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
