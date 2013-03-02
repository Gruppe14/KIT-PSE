package what.sp_parser;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;


import web.controllers.Localize;
import what.Printer;
import what.sp_config.ConfigWrap;
import what.sp_data_access.DataMediator;


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
	 * The pool size of parsingTasks.
	 */
	private int poolsize = STANDARD_PS;
	
	/**
	 * The standard pool size.
	 */
	private static final int STANDARD_PS = 5;
	
	/**
	 * The maximum pool size.
	 */
	private static final int MAX_POOLSIZE = 50;
	
	/**
	 * This variable saves how many tasks have been finished. it is used to 
	 * shut down the threadPool when all threads are finished
	 */
	private int finishedTasks = 0;
	
	/**
	 * This variable indicates how many lines have been deleted due to mistakes.
	 */	
	private int linesDeleted = 0;

	/**
	 * This variable indicates after how much idle time a thread gets killed. (seconds)
	 */
	private int watchTime = 2;
	
	/**
	 * This variable indicates how many percent of the lines have to get uploaded correctly.
	 */
	private static final double CORRECT = 70;
	
	/**
	 * Constant variable for hundred.
	 */
	private static final double HUNDRED = 100;

	/**
	 * Constant variable for thousand.
	 */
	private static final int THOUSAND = 1000;
	
	/**
	 * The WatchDogTimer.
	 */
	private WatchDogTimer wdt = WatchDogTimer.getInstance();
	
	/**
	 * Represents the used log file.
	 */
	private Logfile usedFile = null;
	
	/**
	 * The thread-pool which contains all objects of the class ParsingTask.
	 */
	private ExecutorService threadPool;
	
	/**
	 * An array of all tasks.
	 */
	private ParsingTask[] tasks;
	
	/**
	 * The used configuration.
	 */
	private ConfigWrap cw;
	
	/**	
	 * The DataMediator which loads the data into the warehouse.
	 */
	private DataMediator loader;
	
	/**
	 * The error which occurred.
	 */
	private String error;
	
	/**
	 * True if a fatalError occurred. Program will shut down.
	 */
	private boolean fatalError = false;
	
	/**
	 * Constructor for a new ParserMediator.
	 * @param confi - the used config
	 * @param dataMedi - the DataMediator which loads the data into the warehouse
	 */
	public ParserMediator(ConfigWrap confi, DataMediator dataMedi) {
		if (confi == null) {
			throw new IllegalArgumentException();
		} else if (dataMedi == null)  {
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
	 * @return false if an error occurred
	 */
	private boolean createThreadPool() {
				
		if (poolsize <= 1) {
			error(Localize.getString("Error.10"));
			return false;
		}		
		
		if (poolsize > MAX_POOLSIZE) {
			error(Localize.getString("Error.20"));
			return false;
		}

		tasks = new ParsingTask[poolsize];
						
		for (int i = 0; i < poolsize; i++) {
			tasks[i] = new ParsingTask(this, i);
		}
		
		threadPool = Executors.newFixedThreadPool(poolsize);
		
		return true;
	}
	
	/**
	 * This method starts the actual parsing. It creates a new <code>Logfile</code> with @param path and
	 * sets the <code>usedFile</code> to the path. Then it creates a <code>ThreadPool</code> like stated 
	 * in <code>createThreadPool</code> and submits all those threads via 
	 * <code>java.util.Concurrent.ThreadPool</code>
	 * @return true, after parsing is finished
	 */
	public boolean parseLogFile(String path) {
		assert (path != null);
		
		finishedTasks = 0;
		
		Printer.ptest("Start parsing log file: " + path);
		
		//Initialization for Logfile, ThreadPool and GeoIPTool.
		
		usedFile = new Logfile(path, this);
		
					
		if (fatalError) {
			this.reset();
			return false;
		}
		
		usedFile.setPm(this);

		if (!createThreadPool()) {
			this.reset();
			return false;
		}
		
		if (!GeoIPTool.setUpIpTool(this)) {
			this.reset();
			return false;
		}
		

		wdt.initialize(this);
		
		//Submits all threads to the pool and starts them.
		for (int i = 0; i < poolsize; i++) {
			try {
				threadPool.submit(tasks[i]);
			} catch (RejectedExecutionException e) {
				error(Localize.getString("Error.30P1") + " " + i + " " + Localize.getString("Error.30P2")); 
			} catch (NullPointerException e) {
				error(Localize.getString("Error.40P1") + " " + i + " " + Localize.getString("Error.40P2")); 
			}
			
			if (fatalError) {
				threadPool.shutdown();
				this.reset();
				return false;
			}			
		}
		
		
		
		// Checks all 1000ms if all tasks are finished or if there was a fatal error. Returns true if 
		// all tasks are finished and false, if there was a fatal error.
		while (true) {
			if (finishedTasks >= poolsize) {
				System.out.println("lines: " + usedFile.getLines());
				threadPool.shutdown();
				boolean toReturn = enoughLinesSubmitted();
				this.reset();
				return toReturn;
			} else {
				try {
					Thread.sleep(THOUSAND);
				} catch (InterruptedException e) {
					error(Localize.getString("Error.80"));
				}
				
				if (fatalError) {
					this.reset();
					return false;
				}
			}
			
			if (finishedTasks < poolsize) {
				wdt.check(this);
			}
		}
		

	}
	
	
	/**
	 * This method checks if enough lines are submitted correctly. Parsing only returns true if more than 
	 * CORRECT % of the lines were in fact correct
	 * @return true if parsing was successful
	 */
	private boolean enoughLinesSubmitted() {
		
		return ((double) usedFile.getLines() * (double) (CORRECT / HUNDRED) <= (usedFile.getLines() - linesDeleted));
						
	}

	/**
	 * This method resets the ParserMediator.
	 */
	private void reset() {
		finishedTasks = 0;
		linesDeleted = 0;
		try {
			usedFile.close();
		} catch (IOException e) {
			Printer.perror("on closing Logfile");
		}
		usedFile = null;
		tasks = null;
		error = null;
		fatalError = false;
		threadPool = null;	
		
	}


	/**
	 * This method reads a line from <code>usedFile</code>.
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
		Printer.print(err);	
		error = err;
		fatalError = true;
	}

	/**
	 * This method is called when a task is finished. If it hits the poolsize the parser is shut down.
	 * @param pt the ParsingTask which is finished.
	 */
	protected void increaseFT(ParsingTask pt) {
		
		finishedTasks++;	
		Printer.print("Task " + pt.getNumber() + " finished - now finished: " + finishedTasks);
	}
	
	/**
	 * This method is called when a line gets deleted. It sends out a warning to the standard output.
	 */
	protected void increaseLinedel() {
		
		linesDeleted++;
		
		Printer.pproblem(Localize.getString("Warning.10P1") + " " + linesDeleted + " "
				+ Localize.getString("Warning.10P2"));
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
	 * @return the loader
	 */
	public DataMediator getLoader() {
		return loader;
	}

	/**
	 * @return the poolsize
	 */
	public int getPoolsize() {
		return poolsize;
	}

	/**
	 * @return the watchtime
	 */
	public int getWatchTime() {
		return watchTime;
	}

	/**
	 * @return the watchdogtimer
	 */
	public WatchDogTimer getWatchDog() {
		return wdt;		
	}
	
	/**
	 * Returns the used logfile.
	 * @return the used logfile
	 */
	public Logfile getLogfile() {
		return usedFile;
	}

	/**
	 * This method resets the thread with the number i.
	 * @param i the thread to be resetted
	 */
	protected void resetThread(int i) {
		ParsingTask newTask = new ParsingTask(this, i);
		threadPool.submit(newTask);
		tasks[i] = newTask;
		Printer.pproblem(Localize.getString("Warning.20"));
	}

	/**
	 * This method returns the number of finished tasks.
	 * @return the number of finished tasks
	 */
	public int getFinishedTasks() {
		return finishedTasks;
	}
	
	/**
	 * Returns the error which occurred.
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	
}
