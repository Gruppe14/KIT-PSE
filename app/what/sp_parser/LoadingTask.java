package what.sp_parser;


public class LoadingTask extends Task implements Runnable {

	ParserMediator pm;

	protected LoadingTask(ParserMediator pm) {
		this.pm = pm;
	}

	public void run() {
		//System.out.println("LoadingTask");
		pm.increaseFT();
		return;

	}
}
