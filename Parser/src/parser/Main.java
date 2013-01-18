package parser;

import what.sp_config.*;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//SELECT YY, MM, DD, HH, MI, SS, clientIP, server, dbname, elapsed, busy, rows, statement
		
		
		
		
		ConfigWrap cw = new ConfigWrap(13);
		//ParserConfig pc = new ParserConfig(13);
		cw.addEntry(new IntRow("yy", "year"));
		cw.addEntry(new IntRow("mm", "month"));
		cw.addEntry(new IntRow("dd", "day"));
		cw.addEntry(new IntRow("hh", "hour"));
		cw.addEntry(new IntRow("mi", "minute"));
		cw.addEntry(new IntRow("ss", "second"));
		cw.addEntry(new IpLocationRow("clientIP"));
		
		//String[] servers = { "SDSS3J" , "SDSS3D" };
		
		cw.addEntry(new StringRow("server", "Server"));
		
		//String[] databases = { "BESTDR1" , "BESTDR7" , "BESTDR8" , "BESTDR9" };
		
		cw.addEntry(new StringRow("dbname", "Database"));
		cw.addEntry(new DoubleRow("elapsed", "s"));
		cw.addEntry(new DoubleRow("busy", "s"));
		cw.addEntry(new IntRow("rows", "*"));
		cw.addEntry(new StatementRow("statement", "FROM"));
		

		ParserMediator pm1 = new ParserMediator(cw);
		
		
		
		pm1.parseLogFile("C:/Users/Alex/Downloads/resultMonat.csv");

	}

}
