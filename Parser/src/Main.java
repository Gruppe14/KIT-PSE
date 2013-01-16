import parser.*;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//SELECT YY, MM, DD, HH, MI, SS, clientIP, server, dbname, elapsed, busy, rows, statement
		ParserMediator pm1 = new ParserMediator();
		ParserConfig pc = new ParserConfig(13);
		String[] str = new String[1];
		str[0] = "nothingToCompare";
		pc.addEntry(new ParserConfigEntry("yy", "Integer", str));
		pc.addEntry(new ParserConfigEntry("mm", "Integer", str));
		pc.addEntry(new ParserConfigEntry("dd", "Integer", str));
		pc.addEntry(new ParserConfigEntry("hh", "Integer", str));
		pc.addEntry(new ParserConfigEntry("mn", "Integer", str));
		pc.addEntry(new ParserConfigEntry("ss", "Integer", str));
		pc.addEntry(new ParserConfigEntry("clientIP", "String", str));
		
		String[] servers = { "SDSS3J" , "SDSS3D" };
		
		pc.addEntry(new ParserConfigEntry("server", "String", servers));
		
		String[] databases = { "BESTDR1" , "BESTDR7" , "BESTDR8" , "BESTDR9" };
		
		pc.addEntry(new ParserConfigEntry("dbname", "String", databases));
		pc.addEntry(new ParserConfigEntry("elapsed", "Double", str));
		pc.addEntry(new ParserConfigEntry("busy", "Double" , str));
		pc.addEntry(new ParserConfigEntry("rows", "Integer", str));
		pc.addEntry(new ParserConfigEntry("statement", "String", str));
		
		
		
		pm1.parseLogFile("C:/Users/Alex/Downloads/result.csv", pc);

	}

}
