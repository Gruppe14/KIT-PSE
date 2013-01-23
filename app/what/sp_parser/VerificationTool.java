package what.sp_parser;

import what.sp_config.*;


/**
 * This tool checks, if the <code>ConfigWrap</code> and the type of the csv - file are identical.
 * @author Alex
 *
 */
public class VerificationTool {

	/**
	 * Checks, if <code>ConfigWrap</code> and the type of the logfile are identical.
	 * @param lf the logfile which has the type saved.
	 * @return true if it is identical, false otherwise
	 */
	protected static boolean checkConfig(Logfile lf) {
		
		ConfigWrap cw = lf.getPm().getConfig();
		
		String[] typeSplit = lf.getType().split(",");
		
		int i = 0;
		while (i < cw.getSize()) {
			
			//actual check for being identical
			if (!cw.getEntryAt(i).getLogId().toLowerCase().equals(typeSplit[i].toLowerCase())) {
				return false;
			}
			i++;
		}
		return true;
	}

}
