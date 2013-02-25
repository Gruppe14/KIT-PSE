package what.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import what.Facade;
import what.sp_parser.ParserMediator;

public class ParserTest {
	
	// file names
	private static final String SMALL_PARSING1 = "1k-01TestParsing.csv";
	private static final String SMALL_PARSING2 = "1k-02TestParsing.csv";
	private static final String BIG_PARSING1 = "10k-01TestParsing.csv";
	private static final String FLAWED = "Flawed-TestParsing.csv";
	private static final String MISTAKE1 = "Mistake-01TestParsing.csv";
	private static final String MISTAKE2 = "Mistake-02TestParsing.csv";
	private static final String MISTAKE3 = "Mistake-03TestParsing.csv";
	private static final String EMPTY = "EmptyLine-TestParsing.csv";
	private static final String TEN = "10Lines-TestParsing.csv";
	
	// control
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
	
<<<<<<< HEAD
	// -- MISTAKES -- MISTAKES -- MISTAKES -- MISTAKES -- MISTAKES --
=======
	@Test
	public void smallParseTestStandardSize() {
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\result10.csv"));
	}
	
>>>>>>> 7b1e2757f2b4f06945535dc7c7e9c942788d4192
	@Test
	public void testIntegerMistake() {
		assertFalse(f.parseLogFile(getPathFor(MISTAKE1)));
	}
	
	@Test
	public void testMissingPart() {
		assertFalse(f.parseLogFile(getPathFor(MISTAKE2)));
	}
	
	@Test
	public void testEmptyPart() {
		assertFalse(f.parseLogFile(getPathFor(MISTAKE3)));
	}
	
	@Test
	public void smallParseTestEmptyLine() {
		assertFalse(f.parseLogFile(getPathFor(EMPTY)));
	}
	
	@Test
	public void flawedFile() {
		assertFalse(f.parseLogFile(getPathFor(FLAWED)));
		//flawedFile.csv is a copy of result10.csv with a spelling mistake in the type.
	}
	
	// -- POOL SIZE -- POOL SIZE -- POOL SIZE -- POOL SIZE --
	@Test
	public void smallParseTestSize10() {
		pm.setPoolsizeParsing(10);
		assertTrue(f.parseLogFile(getPathFor(TEN)));
	}
	
	@Test
	public void smallParseTestTooBig() {
		pm.setPoolsizeParsing(100);
		assertFalse(f.parseLogFile(getPathFor(TEN)));
	}
	
	@Test
	public void mediumParseTest50Tasks() {
		pm.setPoolsizeParsing(50);
		assertTrue(f.parseLogFile(getPathFor(SMALL_PARSING2)));
	}
	
	// -- JUST PARSING -- JUST PARSING -- JUST PARSING -- JUST PARSING --
	@Test
	public void smallParseTestStandardSize() {
		assertTrue(f.parseLogFile(getPathFor(SMALL_PARSING1)));
	}
	
	@Test
	public void negativePoolsize() {
		pm.setPoolsizeParsing(-2);
		assertFalse(f.parseLogFile(getPathFor(TEN)));
	}
		
	@Test
	public void mediumParseTestStandardSize() {
		assertTrue(f.parseLogFile(getPathFor(SMALL_PARSING1)));
	}

	@Test
	public void bigParseTestStandardSize() {
		assertTrue(f.parseLogFile(getPathFor(BIG_PARSING1)));
	}
	
	@Test
	public void doubleParsing() {
		assertTrue(f.parseLogFile(getPathFor(SMALL_PARSING1)));
		assertTrue(f.parseLogFile(getPathFor(SMALL_PARSING2)));
	}
	
	@Ignore
	public void veryBigParseTestStandardSize() {
		assertTrue(f.parseLogFile("C:\\Users\\Alex\\Downloads\\resultDez.csv"));
		assertEquals(1226224, pm.getPoolsize());
	}

	// -- HELPER -- HELPER -- HELPER -- HELPER -- HELPER --
	private String getPathFor(String s) {
		return sourcePath + seperator + "example\\" + s;
	}
}
