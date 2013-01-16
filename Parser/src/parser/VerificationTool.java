package parser;
import java.util.Calendar;
import java.util.Date;



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
	
	protected static boolean checkTimeConfig(Logfile lf) {
		
		ParserConfig pc = lf.getPm().getPc();
		
		String[] typeSplit = lf.getType().split(",");
		
		int i = 0;
		while (i < 6) {
			if (!pc.getEntryAt(i).getName().toLowerCase().equals(typeSplit[i].toLowerCase())) {
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
