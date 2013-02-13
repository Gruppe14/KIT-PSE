package what.sp_parser;

public class WatchDogTimer {
	
	private int watchTimeSeconds;
	
	private static WatchDogTimer wdt = new WatchDogTimer();
	
	
	
	private WatchDogTimer() {
		//singleton
	}
	
	
	private int[] lastTimes;
	private boolean[] workedInLastSecond;
	
	
	protected void initialize(ParserMediator pm) {
		lastTimes = new int[pm.getPoolsize()];
		workedInLastSecond = new boolean[lastTimes.length];
		watchTimeSeconds = pm.getWatchTime();
		
		for (int i = 0; i < lastTimes.length; i++) {
			lastTimes[i] = 0;
			workedInLastSecond[i] = false;
		}
		
	}
	
	protected void check(ParserMediator pm) {
		for (int i = 0; i < lastTimes.length; i++) {
			if (workedInLastSecond[i]) {
				lastTimes[i] = 0;
			} else {
				lastTimes[i]++;
				if (lastTimes[i] >= watchTimeSeconds) {
					System.out.println("Thread " + i + " wird gekillt");
					pm.resetThread(i);
					lastTimes[i] = 0;
				}
			}
			workedInLastSecond[i] = false;
		}
	}
	
	protected void addWork(int i) {
		workedInLastSecond[i] = true;
	}


	protected static WatchDogTimer getInstance() {
		return wdt;		
	}

}
