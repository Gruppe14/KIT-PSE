package what;

/**
 * This class provides static methods
 * to print something to the console or elsewhere.<br>
 * 
 * Provides methods for normal printing, errors, failures,
 * problems and success.<br>
 * 
 * If the output should be printed somewhere else, just
 * this class has to be changed. 
 * 
 * @author Jonathan, PSE Grupp 14
 *
 */
public class Printer {

	/**
	 * Utility class, no construcotr needed.
	 */
	private Printer() {
		throw new AssertionError();
	}
	
	/**
	 * Prints the given String (and a new line).
	 * 
	 * @param s String to be printed
	 */
	public static void print(String s) {
		if (s == null) {
			perror("Something to be printed and a wrong given String to the print method!");
		}
		System.out.println(s);
	}
	
	/**
	 * Prints an error message for the given String (and a new line).
	 * 
	 * @param s String to be printed with an error message
	 */
	public static void perror(String s) {
		if (s == null) {
			perror("Something which went wrong and a wrong given String to the perror method!");
		}
		print("ERROR: " + s); // or better System.err.println(..) ?
	}
	
	/**
	 * Prints an failure message for the given String (and a new line).
	 * 
	 * @param s String to be printed with a failure message
	 */
	public static void pfail(String s) {
		if (s == null) {
			perror("Something which failed and a wrong given String to the pfail method!");
		}
		print("FAILURE: " + s);
	}
	
	/**
	 * Prints an problem message for the given String (and a new line).
	 * 
	 * @param s String to be printed with an problem message
	 */
	public static void pproblem(String s) {
		if (s == null) {
			perror("A problem occured and a wrong given String to the pproblem method!");
		}
		print("PROBLEM: " + s);
	}
	
	/**
	 * Prints an success message for the given String (and a new line).
	 * 
	 * @param s String to be printed with a success message
	 */
	public static void psuccess(String s) {
		if (s == null) {
			perror("Something which succeeded and a wrong given String to the psuccess method!");
		}
		print("SUCCESS: " + s);
	}

	/**
	 * Prints an test message for the given String (and a new line).
	 * 
	 * @param s String to be printed with a test message
	 */
	public static void ptest(String s) {
		if (s == null) {
			perror("Something which succeeded and a wrong given String to the ptest method!");
		}
		print("TEST: " + s);
	}

	
	/**
	 * Prints an test case message for the given String (and a new line).
	 * 
	 * @param s String to be printed with a test case message
	 */
	public static void ptestcase(String s) {
		if (s == null) {
			perror("Something which succeeded and a wrong given String to the ptest method!");
		}
		print("\nTEST CASE >>> " + s);
		
	}
	
}
