package what.sp_parser;

import java.util.ArrayList;

import java.util.Date;

import what.sp_config.ConfigWrap;
import what.sp_config.RowEntry;
import what.sp_config.StringMapRow;

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
		
	
	protected static boolean split(ParsingTask pt) {
		
		return (splitTime(pt) && splitOthers(pt) && splitStatement(pt));
	
	}

	private static boolean splitStatement(ParsingTask pt) {
		
		String[] split = pt.getSplitStr();
		ConfigWrap conf = pt.getPm().getConfig();
		
	
			
		StringMapRow row = (StringMapRow) conf.getEntryAt(conf.getSize() - 1);
				
		for (String str : row.getCompareTo()) {
			if (pt.getStr().toLowerCase().contains(str.toLowerCase())) {
				pt.getDe().setInfo(row.isTopicTo(str), conf.getSize() - 8);						
				return true;
			} 
		}				
	

		pt.getDe().setInfo("other", conf.getSize() - 8);
		return true;
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
			month = Integer.parseInt(str[1]) - 1;
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
	
	private static boolean splitOthers(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		String [] str = pt.getSplitStr();
		ConfigWrap cw = pt.getPm().getConfig();
		
		for (int i = 7; i < pt.getPm().getConfig().getSize() - 1; i++) {
			if (cw.getEntryAt(i).getClass().getName().contains("StringRow")) {
				de.setInfo(str[i], i - 7);
			} else if (cw.getEntryAt(i).getClass().getName().contains("DoubleRow")) {
				try {
					de.setInfo(Double.parseDouble(str[i]), i - 7);
				} catch (NumberFormatException e) {
					pt.getPm().error(Messages.getString("Error.107"));
				}
				
			} else if (cw.getEntryAt(i).getClass().getName().contains("IntRow")) {
				try {
					de.setInfo(Integer.parseInt(str[i]), i - 7);
				} catch (NumberFormatException e) {
					pt.getPm().error(Messages.getString("Error.108"));
				}
				
			} else {
				return false;
			}
		}
		return true;
		
		
		
	}
	
}
