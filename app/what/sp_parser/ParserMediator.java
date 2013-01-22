package what.sp_parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import what.sp_config.ConfigWrap;
import what.sp_dataMediation.DataMediator;


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
	 * The poolsize of parsingTasks.
	 */
	private int poolsize = 10;
	
	
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
	
	private ParsingTask tasks[];
	
	private ConfigWrap cw;
	
	private DataMediator loader;
	
	private boolean fatalError = false;
	
	public ParserMediator(ConfigWrap confi, DataMediator dataMedi) {
		if (confi == null) {
			throw new IllegalArgumentException();
		}
		
		this.cw = confi;
		this.loader = dataMedi;
	}
	
		
	/**
	 * Creates a new <code>threadPool</code> with <code>poolsize</code> objects of the type 
	 * <code>ParsingTask</code>.
	 * Those objects are created and inserted in <code>tasks</code>, which is an array for objects of type 
	 * <code>ParsingTask</code>.
	 */
	private boolean createThreadPool() {
		
		tasks = new ParsingTask[poolsize];
		
		if (poolsize < 1) {
			error(Messages.getString("Error.10"));
			return false;
		}
						
		for (int i = 0; i < poolsize; i++) {
			tasks[i] = new ParsingTask(this);
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
		
		if (fatalError) {
			return false;
		}
		
		usedFile.setPm(this);

		if (!createThreadPool()) {
			return false;
		}
		
		if (!GeoIPTool.setUpIpTool(this)) {
			return false;
		}
		
		
		for (int i = 0; i < poolsize; i++) {
			try {
				threadPool.submit(tasks[i]);
			} catch(RejectedExecutionException e) {
				error(Messages.getString("Error.30P1") + " " + i + " " + Messages.getString("Error.30P2")); 
			} catch(NullPointerException e) {
				error(Messages.getString("Error.40P1") + " " + i + " " + Messages.getString("Error.40P2")); 
			}
			
			if (fatalError) {
				threadPool.shutdown();
				return false;
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
				
				if (fatalError) {
					return false;
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
		fatalError = true;
	}

	/**
	 * This method is called when a task is finished. If it hits the poolsize the parser is shut down.
	 */
	protected void increaseFT() {
		finishedTasks++;		
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
	 * @return the config
	 */
	protected ConfigWrap getConfig() {
		return cw;
	}

	
	/**
	 * @param poolsize the poolsize to set
	 */
	public void setPoolsizeParsing(int poolsize) {
		this.poolsize = poolsize;
	}


	/**
	 * @param loader the loader to set
	 */
	public void setLoader(DataMediator loader) {
		this.loader = loader;
	}

	
	
	
	

}
