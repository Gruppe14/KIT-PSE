package web.controllers;


import play.mvc.Controller;
import play.mvc.Result;
import what.Facade;

/**
 * Class for test purposes only.
 * @author all members I guess
 *
 */
public class Test extends Controller {
	
	/**
	 * method for fast tests of newly written code.
	 * not intended for continuous tests.
	 * @return returns a http response to the webpage
	 */
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
		//new Facade();
		//Facade f = Facade.getFacadeInstance();
		//String sourcePath = System.getProperty("user.dir");
		//String seperator = System.getProperty("file.separator");
		//String pathJSONConfig = sourcePath + seperator + "conf\\ConfigSkyServer.json";
		//System.out.println("Was it?" + f.init(pathJSONConfig));
		
		// System.out.println("And if it's content is there:");
		// ConfigWrap confi = f.getCurrentConfig();
		// DBTableBuilder.getDataBaseQuery(confi);
		
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
