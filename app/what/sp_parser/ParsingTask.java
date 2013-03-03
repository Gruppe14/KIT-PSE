package what.sp_parser;

/**
 * 
 * ParsingTasks are tasks created by the ParserMediator. They receive a line from the log file and use  
 * SplittingTool, VerifyTool and LocationTool to create a DataEntry. 
 * 
 * @author Alex, PSE Gruppe 14
 * @see ParserMediator
 * @see DataEntry
 */
public class ParsingTask implements Runnable {
	
	/**
	 * The string which is parsed.
	 */
	private String str;
	
	/**
	 * The number of this task.
	 */
	private int number;
	
	/**
	 * String split at commas.
	 */
	private String[] splitStr;
	
	/**
	 * The ParserMediator which controls this task.
	 */
	private ParserMediator pm;
	
	/**
	 * The used DataEntry.
	 */
	private DataEntry de;
	
	/**
	 * This is the constructor for a new <code>ParsingTask</code>.
	 * 
	 * @param pm the ParserMediator which is connected to this <code>ParsingTask</code>
	 * @param number the number used to identify the task
	 */
	protected ParsingTask(ParserMediator pm, int number) {
		this.pm = pm;
		this.number = number;
	}


	/**
	 * The run-method for a ParsingTask. It reads a new line from the <code>Logfile</code>
	 * and creates a <code>DataEntry</code> which will be filled and verified using various tools.
	 * It repeats this process until the Logfile is finished.
	 * Should be protected, but Runnable doesn't allow that.
	 */
	public void run() {
		while (true) {
			str = pm.readLine();
			if (str == null) {
				pm.increaseFT(this);
				return;
			} else {
				splitStr = str.split(",");

				de = new DataEntry(pm.getConfig().getNumberOfRows() + 1);
				if (splitStr.length >= pm.getConfig().getNumberOfRows() 
						//&& (splitStr[pm.getConfig().getNumberOfRows() - 1].toLowerCase().startsWith("select") 
						//|| (splitStr[pm.getConfig().getNumberOfRows() - 1].toLowerCase().startsWith("\"select"))
						//|| (splitStr[pm.getConfig().getNumberOfRows() - 1].toLowerCase().startsWith("exec")))
						&& (SplittingTool.split(this))) {
					GeoIPTool.getLocationInfo(this);
							
					pm.getWatchDog().addWork(number);
										
					boolean success = pm.getLoader().loadEntry(de);
					//Printer.ptest("Loading DataEntry was successful: " + success);
					if (!success) {
						pm.increaseLinedel();
					}
				} else {
					pm.increaseLinedel();
				}
				
			}		
		}	
	}
	
	/**
	 * @return the pm
	 */
	protected ParserMediator getPm() {
		return pm;
	}


	/**
	 * @return the splitStr
	 */
	protected String[] getSplitStr() {
		return splitStr;
	}



	/**
	 * @return the DataEntry
	 */
	protected DataEntry getDe() {
		return de;
	}


	/**
	 * @param str the string to set
	 */
	protected void setStr(String str) {
		this.str = str;
	}

	/**
	 * @return the string which is parsed
	 */
	protected String getStr() {
		return str;
	}
	
	/**
	 * @return the number of this task
	 */
	protected int getNumber() {
		return number;
	}
	
}
