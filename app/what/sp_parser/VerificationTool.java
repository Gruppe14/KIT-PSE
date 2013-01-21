package what.sp_parser;

import what.sp_config.*;



public class VerificationTool {

	protected static boolean checkConfig(Logfile lf) {
		
		ConfigWrap cw = lf.getPm().getConfig();
		
		String[] typeSplit = lf.getType().split(",");
		
		int i = 0;
		while (i < cw.getSize()) {
			
			if (!cw.getEntryAt(i).getLogId().toLowerCase().equals(typeSplit[i].toLowerCase())) {
				return false;
			}
			i++;
		}
		return true;
	}

}
