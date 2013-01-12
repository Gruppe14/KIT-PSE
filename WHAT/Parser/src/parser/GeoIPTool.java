package parser;

import java.io.IOException;

import GeoIP.*;


public class GeoIPTool {
	
	/**
	 * The lookupservice which looks for country and city of every request.
	 */
	private static LookupService cl;
	
	//Cache for the last IP and location.
	private static Location lastLoc = null;
	private static String lastIp = null;
		
	/**
	 * Sets up the IpTool and creates a new lookupService
	 * @param pm the ParserMediator which will use this tool
	 */
	protected static void setUpIpTool(ParserMediator pm) {
		
		String dir = System.getProperty("user.dir");
		String seperator = System.getProperty("file.separator");
		
		
		String lookup = dir + seperator + "geoLiteCity.dat";
		
		
		try {
			
			cl = new LookupService(lookup);
		} catch (IOException e) {
			pm.error(Messages.getString("Error.120"));
		}
	}
	
	/**
	 * Adds the location info to the IP of a certain dataEntry
	 * @param pt the parsing-task which parsed the dataEntry
	 */
	protected static void getLocationInfo(ParsingTask pt) {

		try {		
			Location loc;
			if (pt.getDe().getIp().equals(lastIp)) {
				loc = lastLoc;
			} else {
				loc = cl.getLocation(pt.getDe().getIp());
			}
			pt.getDe().setCountry(loc.countryName);	
			pt.getDe().setCity(loc.city);
			
			lastLoc = loc;
			lastIp = pt.getDe().getIp();
			
			
		} catch (NullPointerException e) {
			//doNothing() - will be handled while verifying.
		}
	}
}
