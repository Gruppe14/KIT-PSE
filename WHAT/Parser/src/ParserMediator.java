import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
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
	private static int POOLSIZE_PARSING = 10;
	
	/**
	 * Number of threads used for loading tasks.
	 */
	private static int POOLSIZE_LOADING = 5;
	
	/**
	 * Number of threads combined.
	 */
	private static int POOLSIZE = (POOLSIZE_PARSING + POOLSIZE_LOADING);
	
	private int finishedTasks = 0;
	
	private Logfile usedFile = null;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(POOLSIZE);
	
	private Task tasks[];
	
	public LinkedList<String> errors = new LinkedList<String>();
	
	/**
	 * Creates a new <code>threadPool</code> with <code>POOLSIZE_PARSING</code> objects of the type 
	 * <code>ParsingTask</code> and <code>POOLSIZE_LOADING</code> objects of the type <code>LoadingTask</code>.
	 * Those objects are created and inserted in <code>tasks</code>, which is an array for objects of type 
	 * <code>Task</code>.
	 */
	private boolean createThreadPool() {
		
		tasks = new Task[POOLSIZE];
		
		if (POOLSIZE_PARSING < 1) {
			error(Messages.getString("Error.10"));
			return false;
		}
		
		if (POOLSIZE_LOADING < 1) {
			error(Messages.getString("Error.20"));
			return false;
		}
		
		for (int i = 0; i < POOLSIZE_PARSING; i++) {
			tasks[i] = new ParsingTask(this, i);
		}
		
		for (int i = 0; i < POOLSIZE_LOADING; i++) {
			tasks[i + POOLSIZE_PARSING] = new LoadingTask(this);
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
		
		
		for (int i = 0; i < POOLSIZE; i++) {
			try {
				threadPool.submit(tasks[i]);
			} catch(RejectedExecutionException e) {
				error(Messages.getString("Error.30P1") + i + Messages.getString("Error.30P2")); 
			} catch(NullPointerException e) {
				error(Messages.getString("Error.40P1") + i + "Error.40P2"); 
			}
		}
		
		while (true) {
			if (finishedTasks == POOLSIZE) {
				threadPool.shutdown();
				return true;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					error(Messages.getString("ParserMediator.6"));
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
		errors.add(err);
		System.out.println(err);		
		System.exit(1);
	}

	/**
	 * This method is called when a task is finished. If it hits the poolsize the parser is shut down.
	 */
	protected void increaseFT() {
		finishedTasks++;		
	}
	
	
	

}
