package what.sp_parser;

public class WatchDogTimer {
	
	/**
	 * This number states after how many seconds of idleness a thread gets resetted.
	 */
	private int watchTimeSeconds;
	
	/**
	 * The singleton-watchdogtimer
	 */
	private static WatchDogTimer wdt = new WatchDogTimer();
	
	
	
	private WatchDogTimer() {
		//singleton
	}
	
	// Helper arrays.
	private int[] lastTimes;
	private boolean[] workedInLastSecond;
	
	/**
	 * Initializes the watchdogtimer.
	 * @param pm the PparserMediator
	 */
	protected void initialize(ParserMediator pm) {
		lastTimes = new int[pm.getPoolsize()];
		workedInLastSecond = new boolean[lastTimes.length];
		watchTimeSeconds = pm.getWatchTime();
		
		for (int i = 0; i < lastTimes.length; i++) {
			lastTimes[i] = 0;
			workedInLastSecond[i] = false;
		}
		
	}
	
	/**
	 * This method is used every 1000ms and is used to check, if every thread worked until the last 
	 * time check was called.
	 * @param pm the parsermediator
	 */
	protected void check(ParserMediator pm) {
		for (int i = 0; i < lastTimes.length; i++) {
			if (workedInLastSecond[i]) {
				lastTimes[i] = 0;
			} else {
				lastTimes[i]++;
				if (lastTimes[i] >= watchTimeSeconds) {
					pm.resetThread(i);
					lastTimes[i] = 0;
				}
			}
			workedInLastSecond[i] = false;
		}
	}
	
	/**
	 * This method is used when a thread is finished with a line.
	 * @param i the number of thread
	 */
	protected void addWork(int i) {
		workedInLastSecond[i] = true;
	}

	/**
	 * Returns the singleton of the WatchDogTimer
	 * @return the singleton WatchDogTimer
	 */
	protected static WatchDogTimer getInstance() {
		return wdt;		
	}

}
