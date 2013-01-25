package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import play.mvc.Controller;
import play.mvc.Result;
import what.Facade;
import what.sp_config.ConfigWrap;
import what.sp_config.DimRow;
import what.sp_dataMediation.CreateDatabase;
import what.sp_parser.DataEntry;
 
// Class for test purposes only
public class Test extends Controller {
	public static Result test() {
		/**
		 * add your methods to test here !!
		 * if you run play, any information printed (System.out.println, etc)
		 * will show up on the commandLine where you started the play server
		 */
		//go to localhost:9000/TEST to run the methods added here
		/**
		 * please don't upload changes made here to git,
		 * so all members can use this file for their own purposes 
		 */
		

		//System.out.println("This is a test for checking whether reading the config file is successful:");

		Facade f = Facade.getFacadeIstance();//new Facade();
		String sourcePath = System.getProperty("user.dir");
		String seperator = System.getProperty("file.separator");
		//String pathJSONConfig = sourcePath + seperator + "conf\\ConfigSkyServer.json";
		//System.out.println("Was it?" + f.init(pathJSONConfig));
		
		// System.out.println("And if it's content is there:");
		 ConfigWrap confi = f.getCurrentConfig();
		 System.out.println(confi.toString());
		 for (DimRow d : confi.getDims()) {
			 if (d.isStringDim()) {
				 System.out.println(d.getStrings() != null);
			 }	 
		 }
		
		//f.upload(DataEntry.getOne());
		
				
		/* 
		 * Uncomment first for 32k lines, second for 10 lines and third for 1k lines.
		 */
		//System.out.println(f.parseLogFile(sourcePath + seperator + "example\\resultDay.csv"));
		//f.parseLogFile(sourcePath + seperator + "example\\result10.csv");
		//f.parseLogFile(sourcePath + seperator + "example\\result1000.csv");
		//System.out.println("Parsing finished");
		return ok("Please look on your console for output of your code");
	}
	
}
