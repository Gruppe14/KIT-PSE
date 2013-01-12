package parser;

import java.io.IOException;

import GeoIP.*;


public class GeoIPTool {
	
	private static LookupService cl;
	
	
	
	
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
	

	
	protected static String getCountry(ParsingTask pt) {
		
		
			try {
				return cl.getLocation(pt.getDe().getIp()).countryName;	
			} catch (NullPointerException e) {
				return null;
			}
				
	
					
		
	}



	public static String getCity(ParsingTask pt) {
		
		
			try {
				return cl.getLocation(pt.getDe().getIp()).city;
			} catch (NullPointerException e) {
				return null;
			}
		
		
		
		
	
	}
	
}
