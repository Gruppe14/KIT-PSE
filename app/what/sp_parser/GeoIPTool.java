package what.sp_parser;

import java.io.IOException;


import web.controllers.Localize;
import what.sp_parser.sp_GeoIp.Location;
import what.sp_parser.sp_GeoIp.LookupService;

/**
 * This tool adds the City and Country of a request to a dataEntry.
 * It includes GeoLite data created by MaxMind, available from http://www.maxmind.com. It uses the
 * MaxMind Java-API, which is a little bit changed for speed-up.
 * @author Alex
 *
 */
public class GeoIPTool {
	
	/**
	 * The lookup service which looks for country and city of every request.
	 */
	private static LookupService cl;
	
	/**
	 * Private constructor, utility class.
	 */
	private GeoIPTool() {
		//private constructor
	}

	/**
	 * Cache for the last location.
	 */
	private static Location lastLoc = null;
	
	/**
	 * Cache for the last ip.
	 */
	private static String lastIp = null;
	
	/**
	 * Position of IP in the line.
	 */
	private static final int POSITION_IP = 6;
	
	/**
	 * Position of country in the dataEntry.
	 */
	private static final int POSITION_CITY = 7;
	
	/**
	 * Position of city in the dataEntry.
	 */
	private static final int POSITION_COUNTRY = 6;
			
	/**
	 * Sets up the IpTool and creates a new lookupService.
	 * 
	 * @param pm the ParserMediator which will use this tool
	 * @return false if an error occurred
	 */
	protected static boolean setUpIpTool(ParserMediator pm) {
		
		String dir = System.getProperty("user.dir");
		String seperator = System.getProperty("file.separator");
		String lookup = dir + seperator + "data" + seperator + "GeoLiteCity.dat";
				
		
		try {
			cl = new LookupService(lookup);
		} catch (IOException e) {
			pm.error(Localize.getString("Error.120"));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Adds the location info to the IP of a certain dataEntry.
	 * @param pt the parsing-task which parsed the dataEntry
	 */
	protected static void getLocationInfo(ParsingTask pt) {

		try {		
			
									
			Location loc;
			
			//Looks if the IP is the same as the one which started the last request, uses the same location info
			//if this is true. If not, it uses the LookupService to create a new Location-object loc.
			if (pt.getSplitStr()[POSITION_IP].equals(lastIp)) {
				loc = lastLoc;
			} else {
				loc = cl.getLocation((String) pt.getSplitStr()[POSITION_IP]);
			}
			
			
			if (loc.countryName.toLowerCase().contains("proxy")) {
				loc.countryName = ParserMediator.UNDEFINED_VALUE;
			}
			if (loc.city.toLowerCase().contains("proxy")) {
				loc.city = ParserMediator.UNDEFINED_VALUE;
			}
			
			//Sets country and city to their spots of the DataEntry.
			pt.getDe().setInfo(loc.countryName.trim(), POSITION_COUNTRY);	
			pt.getDe().setInfo(loc.city.trim(), POSITION_CITY);

			if (pt.getDe().getInfo(POSITION_COUNTRY) == null) {
			    pt.getDe().setInfo(ParserMediator.UNDEFINED_VALUE, POSITION_COUNTRY);
			}
			 if (pt.getDe().getInfo(POSITION_CITY) == null) {
			    pt.getDe().setInfo(ParserMediator.UNDEFINED_VALUE, POSITION_CITY);
			 }
			
			lastLoc = loc;
			lastIp = (String) pt.getSplitStr()[POSITION_IP];
					
			
		} catch (NullPointerException e) {
			 //cl.getLocation(String str) throws a NullPointerException when it doens't find any location
			 //to a certain ip. catching it is the easiest way to deal with this problem.
			pt.getDe().setInfo(ParserMediator.UNDEFINED_VALUE, POSITION_CITY);
			pt.getDe().setInfo(ParserMediator.UNDEFINED_VALUE, POSITION_COUNTRY);
		}
	}
}
