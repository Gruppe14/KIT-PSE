package parser;

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
	
	static int zeroBusy = 0;
	static int zeroElapsed = 0;

	
	
	protected static void split(ParsingTask pt) {
		splitTime(pt);
		splitServerInfo(pt);
	}


	private static void splitTime(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		String[] str = pt.getSplitStr();
		
		//Setting Year, Month, Day and Hour of the SQL-Request.
		try {
			de.setYear(Integer.parseInt(str[0]));
		} catch (NumberFormatException e) {
			pt.getPm().error(Messages.getString("Error.101"));
		}
		
		try {
			de.setMonth(Integer.parseInt(str[1]));
		} catch (NumberFormatException e) {
			pt.getPm().error(Messages.getString("Error.102"));
		}
		
		try {
			de.setDay(Integer.parseInt(str[2]));
		} catch (NumberFormatException e) {
			pt.getPm().error(Messages.getString("Error.103"));
		}
		
		try {
			de.setHour(Integer.parseInt(str[3]));
		} catch (NumberFormatException e) {
			pt.getPm().error(Messages.getString("Error.104"));
		}
		
		
				
		
	}
	
	private static void splitServerInfo(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		String [] str = pt.getSplitStr();
		
		de.setIp(str[4]);
		de.setServer(str[5]);
		de.setDatabase(str[6]);
		
		try {
			de.setElapsedTime((int) (Double.parseDouble(str[7]) * 1000));
			if (de.getElapsedTime() == 0) {
				zeroElapsed++;
			}
		} catch (NumberFormatException e) {
			pt.getPm().error(Messages.getString("Error.107"));
		}
		
		try {
			de.setBusyTime((int) (Double.parseDouble(str[8]) * 1000));
			if (de.getBusyTime() == 0) {
				zeroBusy++;
			}
		} catch (NumberFormatException e) {
			pt.getPm().error(Messages.getString("Error.108"));
		}
		
		try {
			de.setRows(Integer.parseInt(str[9]));
		} catch (NumberFormatException e) {
			pt.getPm().error(Messages.getString("Error.109"));
		}
		
		if (str.length > 10) {
			if (str.length > 15) {
				pt.getPm().error(Messages.getString("Error.110"));
			}
			int max = str.length - 10;
			
			String[] toEnter = new String[5];
			
			for (int i = 0; i < max; i++) {
				toEnter[i] = str[i + 10]; 
			}
			
			de.setAdditionalInfo(toEnter);
			
		}
		
		
	}
	
}
