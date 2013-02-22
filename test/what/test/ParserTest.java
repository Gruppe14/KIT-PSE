package what.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import what.Facade;
import what.sp_parser.ParserMediator;

public class ParserTest {
	
	private static final String SMALL_PARSING1 = "01TestParsing1k.csv";
	private static final String SMALL_PARSING2 = "02TestParsing1k.csv";
	private static final String PARSING1 = "01TestParsing10k.csv";
	
	private static Facade f;
	private static ParserMediator pm;
	
	// path Strings
	private static String sourcePath;
	private static String seperator;
	
	
	@BeforeClass
	public static void initialize() {
		f = Facade.getFacadeIstance();
		pm = f.getParserMediator();
		
		sourcePath = System.getProperty("user.dir");
		seperator = System.getProperty("file.separator");
	}
	
	@Before
	public void resetFacade() {
		f.reset();
		f = Facade.getFacadeIstance();
		pm = f.getParserMediator();
	}
	
	@Test
	public void smallParseTestStandardSize() {
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\" + SMALL_PARSING1));
	}
	
	@Test
	public void smallParseTestSize10() {
		pm.setPoolsizeParsing(10);
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\result10.csv"));
	}
	
	@Test
	public void smallParseTestTooBig() {
		pm.setPoolsizeParsing(100);
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\result10.csv"));
	}
	
	@Test
	public void negativePoolsize() {
		pm.setPoolsizeParsing(-2);
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\result10.csv"));
	}
		
	@Test
	public void flawedFile() {
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\flawedFile.csv"));
		//flawedFile.csv is a copy of result10.csv with a spelling mistake in the type.
	}
	
	@Test
	public void mediumParseTestStandardSize() {
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\result1000.csv"));
	}
	
	@Test
	public void mediumParseTest50Tasks() {
		pm.setPoolsizeParsing(50);
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\result1000.csv"));
	}
	
	@Test
	public void bigParseTestStandardSize() {
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\resultDay.csv"));
	}
	
	@Test
	public void doubleParsing() {
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\result1000.csv"));
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\result1000.csv"));
	}
	
	@Ignore
	public void veryBigParseTestStandardSize() {
		assertTrue(f.parseLogFile("C:\\Users\\Alex\\Downloads\\resultDez.csv"));
		assertEquals(1226224, pm.getPoolsize());
	}

}
