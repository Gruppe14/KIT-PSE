package what.sp_parser;



import what.sp_config.ConfigWrap;
import what.sp_config.StringMapRow;

/**
 * 
 * The SplittingTool splits the <code>str</code> from its 
 * <code>parsingTask</code> and enters the splitted raw parts into its <code>DataEntry</code>.
 * It requires the date to be at the first place, then the IP, then all other informations and the statement as the last argument,
 * because the statement can contain commas and endOfLines.
 * 
 * @author Alex
 *
 */
public class SplittingTool {
		
	
	protected static boolean split(ParsingTask pt) {
		
		return (splitTime(pt) && splitOthers(pt) && splitStatement(pt));
	
	}

	private static boolean splitStatement(ParsingTask pt) {
		
		
		ConfigWrap conf = pt.getPm().getConfig();
				
		return conf.getEntryAt(conf.getSize() - 1).split(pt.getDe(), pt.getStr(), conf.getSize() - 1);
				
	}

	protected static boolean splitTime(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		String[] str = pt.getSplitStr();
		
		for (int i = 0; i < 6; i++) {
			
			if (!pt.getPm().getConfig().getEntryAt(i).split(de, str[i], i)) {
				return false;
			}
		}		
		
		return true;		
	}
	
	private static boolean splitOthers(ParsingTask pt) {
		
		DataEntry de = pt.getDe();
		String [] str = pt.getSplitStr();
		ConfigWrap cw = pt.getPm().getConfig();
		
		
		for (int i = 7; i < pt.getPm().getConfig().getSize() - 1; i++) {
			cw.getEntryAt(i).split(de, str[i], i);
		}
		return true;		
	}
	
	
}
	
