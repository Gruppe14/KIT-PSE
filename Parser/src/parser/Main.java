package parser;


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
		pc.addEntry(new IntegerEntry("yy"));
		pc.addEntry(new IntegerEntry("mm"));
		pc.addEntry(new IntegerEntry("dd"));
		pc.addEntry(new IntegerEntry("hh"));
		pc.addEntry(new IntegerEntry("mi"));
		pc.addEntry(new IntegerEntry("ss"));
		pc.addEntry(new StringEntry("clientIP", str));
		
		String[] servers = { "SDSS3J" , "SDSS3D" };
		
		pc.addEntry(new StringEntry("server", servers));
		
		String[] databases = { "BESTDR1" , "BESTDR7" , "BESTDR8" , "BESTDR9" };
		
		pc.addEntry(new StringEntry("dbname", databases));
		pc.addEntry(new DoubleEntry("elapsed"));
		pc.addEntry(new DoubleEntry("busy"));
		pc.addEntry(new IntegerEntry("rows"));
		pc.addEntry(new StringEntry("statement", str));
		
		
		
		pm1.parseLogFile("C:/Users/Alex/Downloads/resultMonat.csv", pc);

	}

}
