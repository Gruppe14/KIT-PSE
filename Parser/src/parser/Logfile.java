package parser;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/** 
 * 
 * The LogFile class is the gateway between parser and a log file - it contains the path of the logfile 
 * and an integer  which saves how many lines have been read from this file. 
 * It can read single lines from the logfile and return them to the ParserMediator.
 * 
 * @author Alex
 *
 */

public class Logfile {

	
	/**
	 * Number of lines which are already read from the file.
	 */
	private int lines;
	
	/**
	 * The type of the csv-file.
	 */
	private String type;
	
	/**
	 * The file-object at <code>path</code>
	 */
	private File file;
	
	/**
	 * The ParserMediator which is connected to the <code>Logfile</code>
	 */
	private ParserMediator pm;
	
	/**
	 * InputStreams and Reader for reading the <code>file</code>
	 */
	private FileInputStream fstream = null;
	private DataInputStream in = null;
	private BufferedReader br = null;
	
	/**
	 * @return the ParserMediator
	 */
	protected ParserMediator getPm() {
		return pm;
	}

	/**
	 * @param pm the ParserMediator to set
	 */
	protected void setPm(ParserMediator pm) {
		this.pm = pm;
	}

	/**
	 * @return the number of lines which are already read
	 */
	protected int getLines() {
		return lines;
	}

	/**	
	 * @return The type of the logfile
	 */
	protected String getType() {
		return type;
	}

	/**
	 * The constructor for a new <code>Logfile</code>. It sets the @param path, 
	 * searches the <code>File</code> at this path and creates a file-object. 
	 * Then it sets the InputStreams and the BufferedReader and sets the <code>type</code>
	 * to the first line of the file.
	 */
	protected Logfile(String path, ParserMediator pm) {
		this.pm = pm;
		this.lines = 0;
		try {
		this.file = new File(path);
		} catch (NullPointerException e) {
			pm.error(Messages.getString("Error.50"));
		}
		try {
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			pm.error(Messages.getString("Error.60P1") + path + Messages.getString("Error.60P2"));
		}
		in = new DataInputStream(fstream);
		br = new BufferedReader(new InputStreamReader(in));
		try {
			type = br.readLine();
			if (!type.startsWith("YY,MM,DD,HH,clientIP,server,dbname,elapsed,busy,rows")) {
				pm.error(Messages.getString("Error.90"));
			}
			System.out.println(type);
		} catch (IOException e) {
			if (!file.canRead()) {
				pm.error(Messages.getString("Error.71"));
			} else {
				pm.error(Messages.getString("Error.73"));
			}
			
		}
		
	}
	
	private long first;
	/**
	 * Returns the next line from <code>file</code> and iterates <code>lines</code>.
	 * @return the next line from <code>file</code>
	 */
	protected synchronized String readLine() {
		String str = null;
		try {
			if (lines == 0) {
				first = System.currentTimeMillis();
			}
			if (lines % 10000 == 0) {
				System.out.println(System.currentTimeMillis() - first);
				first = System.currentTimeMillis();
			}
			str = br.readLine();
		} catch (IOException e) {
			if (!file.canRead()) {
				pm.error(Messages.getString("Error.72P1") + lines + " " + Messages.getString("Error.72P2"));
			} else {
				pm.error(Messages.getString("Error.74P1") + lines + " " + Messages.getString("Error.74P2"));
			}
		}
		lines++;
		
		return str;
	}
	
}
