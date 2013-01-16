package parser;

import java.util.Date;

/**
 * 
 * The SplittingTool splits the <code>str</code> from its 
 * <code>parsingTask</code> and enters the splitted raw parts into its <code>DataEntry</code>.
 * It uses the following format:
 * SELECT YY, MM, DD, HH, clientIP, server, dbname, elapsed, busy, rows, up to 5 additional informations.
 * 
 * @author Alex
 *
 */
public class SplittingTool {
		
	
	protected static void split(ParsingTask pt) {
		splitTime(pt);
		splitServerInfo(pt);
	}

	@SuppressWarnings("deprecation")
	protected static boolean splitTime(ParsingTask pt) {
		
		
		
		DataEntry de = pt.getDe();
		String[] str = pt.getSplitStr();
		int year = -1, month = -1, day = -1, hour = -1, minute = -1, second = -1;
		
		
		try {
			year = Integer.parseInt(str[0]) - 1900;
		} catch (NumberFormatException e) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
			
			
		try {
			month = Integer.parseInt(str[1]);
		} catch (NumberFormatException e) {
			pt.getPm().increaseLinedel();
			return false;
		}
			
					
		try {
			day = Integer.parseInt(str[2]);
		} catch (NumberFormatException e) {
			pt.getPm().increaseLinedel();
			return false;
		}
					
			
		try {
			hour = Integer.parseInt(str[3]);
		} catch (NumberFormatException e) {
			pt.getPm().increaseLinedel();
			return false;
		}
						
			
		try {
			minute = Integer.parseInt(str[4]);
		} catch (NumberFormatException e) {
			pt.getPm().increaseLinedel();
			return false;
		}
			
			
			
		try {
			second = Integer.parseInt(str[5]);
		} catch (NumberFormatException e) {
			pt.getPm().increaseLinedel();
			return false;
		}
		
		de.setDate(new Date(year, month, day, hour, minute, second));
		
			
		return true;	
		
	}
	
	private static void splitServerInfo(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		String [] str = pt.getSplitStr();
		
		
		
	}
	
}
