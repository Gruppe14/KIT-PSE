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


	private static void splitTime(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		String[] str = pt.getSplitStr();
		
		//Setting Year, Month, Day and Hour of the SQL-Request.
		de.setYear(Integer.parseInt(str[0]));
		de.setMonth(Integer.parseInt(str[1]));
		de.setDay(Integer.parseInt(str[2]));
		de.setHour(Integer.parseInt(str[3]));
				
		
	}
	
	private static void splitServerInfo(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		String [] str = pt.getSplitStr();
		
		
	}
	
}
