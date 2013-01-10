/**
 * This is the superclass for <code>ParsingTask</code> and <code>LoadingTask</code> and shouldn't be used.
 * @author Alex
 *
 */
public class Task implements Runnable {

	/**
	 * The run-method for Task. Should be protected but Runnable doesn't want that.
	 */
	@Override
	public void run() {
		//doNothing();
		System.out.println("ERROR #0: Don't run the superclass Task");
		/*
		 * This error won't be added to errors because Task doesn't know the ParserMediator 
		 * and because this error is caused by a major fuckup which shouldn't happen anyway.
		 */
		
	}

}
