package what.sp_parser;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import what.sp_config.ConfigWrap;


/**
 * 
 * ParserMediator is the 'main'-Class of the Parser. It creates and administers a thread-pool,
 * which contains several tasks.
 * The ParserMediator also contains the entryBuffer for finished DataEntries and it saves which log file 
 * is used.
 * 
 * @author Alex
 *
 */

public class ParserMediator {
	
	/**
	 * Number of threads used for parsing.
	 */
	private int poolsizeParsing = 1;
	
	
	/**
	 * Number of threads used for loading tasks.
	 */
	private int poolsizeLoading = 5;
	
	/**
	 * Number of threads combined.
	 */
	private int poolsize = (poolsizeParsing + poolsizeLoading);
	
	/**
	 * true if verification shall be done
	 */
	private boolean verify = true;
	
	/**
	 * This variable saves how many tasks have been finished. it is used to 
	 * shut down the threadPool when all threads are finished
	 */
	private int finishedTasks = 0;
	
	/**
	 * This variable indicates how many lines have been deleted due to mistakes
	 */	
	private int linesDeleted = 0;
	
	private Logfile usedFile = null;
		
	private ExecutorService threadPool = Executors.newFixedThreadPool(poolsize);
	
	private Task tasks[];
	
	private ConfigWrap cw;
	
	public ParserMediator(ConfigWrap cw) {
		this.cw = cw;
	}
	
		
	/**
	 * Creates a new <code>threadPool</code> with <code>POOLSIZE_PARSING</code> objects of the type 
	 * <code>ParsingTask</code> and <code>POOLSIZE_LOADING</code> objects of the type <code>LoadingTask</code>.
	 * Those objects are created and inserted in <code>tasks</code>, which is an array for objects of type 
	 * <code>Task</code>.
	 */
	private boolean createThreadPool() {
		
		tasks = new Task[poolsize];
		
		if (poolsizeParsing < 1) {
			error(Messages.getString("Error.10"));
			return false;
		}
		
		if (poolsizeLoading < 1) {
			error(Messages.getString("Error.20"));
			return false;
		}
		
		for (int i = 0; i < poolsizeParsing; i++) {
			tasks[i] = new ParsingTask(this);
		}
		
		for (int i = 0; i < poolsizeLoading; i++) {
			tasks[i + poolsizeParsing] = new LoadingTask(this);
		}
		
		return true;
	}
	
	/**
	 * This method starts the actual parsing. It creates a new <code>Logfile</code> with @param path and
	 * sets the <code>usedFile</code> to @param path. Then it creates a <code>ThreadPool</code> like stated 
	 * in <code>createThreadPool</code> and submits all those threads via 
	 * <code>java.util.Concurrent.ThreadPool</code>
	 * @return true, after parsing is finished
	 */
	public boolean parseLogFile(String path) {
		
		usedFile = new Logfile(path, this);
		usedFile.setPm(this);

		if (!createThreadPool()) {
			return false;
		}
		
		GeoIPTool.setUpIpTool(this);
		
		
		for (int i = 0; i < poolsize; i++) {
			try {
				threadPool.submit(tasks[i]);
			} catch(RejectedExecutionException e) {
				error(Messages.getString("Error.30P1") + " " + i + " " + Messages.getString("Error.30P2")); 
			} catch(NullPointerException e) {
				error(Messages.getString("Error.40P1") + " " + i + " " + Messages.getString("Error.40P2")); 
			}
		}
		
		while (true) {
			if (finishedTasks == poolsize) {
				System.out.println("lines: " + usedFile.getLines());
				threadPool.shutdown();
				return true;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					error(Messages.getString("Error.80"));
				}
			}
		}
		

	}
	
	/**
	 * This method reads a line from <code>usedFile</code>
	 * @return the next line from <code>usedFile</code>
	 */
	protected String readLine() {
		return usedFile.readLine();
	}
	
	/**
	 * If the parser got an error somewhere this method will be used and @param err will be printed out
	 * and added to the <code>LinkedList<String> errors</code>.
	 * @param err
	 */
	protected void error(String err) {
		System.out.println(err);		
		System.exit(1);
	}

	/**
	 * This method is called when a task is finished. If it hits the poolsize the parser is shut down.
	 */
	protected void increaseFT() {
		finishedTasks++;		
		System.out.println("finished" + finishedTasks);
	}
	
	/**
	 * This method is called when a line gets deleted. It sends out a warning to the standard output
	 *  for every 1000 deleted lines.
	 */

	protected void increaseLinedel() {
		linesDeleted++;
		
	//	if (linesDeleted % 1000 == 0) {
			System.out.println(Messages.getString("Warning.10P1") + linesDeleted + " " + Messages.getString("Warning.10P2"));
	//	}
		
	}

	/**
	 * @return the verify
	 */
	protected boolean getVerify() {
		return verify;
	}

	/**
	 * @return the config
	 */
	protected ConfigWrap getConfig() {
		return cw;
	}

	/**
	 * @param verify the verify to set
	 */
	public void setVerify(boolean verify) {
		this.verify = verify;
	}
	
	/**
	 * @param poolsizeParsing the poolsizeParsing to set
	 */
	public void setPoolsizeParsing(int poolsizeParsing) {
		this.poolsizeParsing = poolsizeParsing;
	}

	/**
	 * @param poolsizeLoading the poolsizeLoading to set
	 */
	public void setPoolsizeLoading(int poolsizeLoading) {
		this.poolsizeLoading = poolsizeLoading;
	}
	
	
	

}
