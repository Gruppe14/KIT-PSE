package what.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import play.test.FakeApplication;
import play.test.Helpers;
import what.Facade;
import what.Printer;
import what.sp_parser.ParserMediator;

//@Ignore
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
	private static final String TEN = "10lines-TestParsing.csv";
	
	// control
	private static Facade f;
	private static ParserMediator pm;
	
	// run a fakeApplication
	private static FakeApplication app;
	
	// path Strings
	private static String sourcePath;
	private static String seperator;
	
	
	@BeforeClass
	public static void initialize() {
		Printer.ptestclass("parser tests");
		
		app = Helpers.fakeApplication();
		Helpers.start(app);
		
		f = Facade.getFacadeInstance();
		pm = f.getParserMediator();
		
		sourcePath = System.getProperty("user.dir");
		seperator = System.getProperty("file.separator");
	}
	
	@Before
	public void resetFacade() {
		f.reset();
		f = Facade.getFacadeInstance();
		pm = f.getParserMediator();
	}
	
	@AfterClass
	public static void stopApp() {
		Helpers.stop(app);
	}
	
	
	// -- MISTAKES -- MISTAKES -- MISTAKES -- MISTAKES -- MISTAKES --
	@Test
	public void testIntegerMistake() {
		Printer.ptestcase("Parser mistake");
		assertFalse(f.parseLogFile(getPathFor(MISTAKE1)));
	}
	
	@Test
	public void testMissingPart() {
		Printer.ptestcase("Parser mistake");
		assertFalse(f.parseLogFile(getPathFor(MISTAKE2)));
	}
	
	@Test
	public void testEmptyPart() {
		Printer.ptestcase("Parser mistake");
		assertFalse(f.parseLogFile(getPathFor(MISTAKE3)));
	}
	
	@Test
	public void smallParseTestEmptyLine() {
		Printer.ptestcase("Parser empty");
		assertTrue(f.parseLogFile(getPathFor(EMPTY)));
	}
	
	@Test
	public void flawedFile() {
		Printer.ptestcase("Parser flawed");
		assertFalse(f.parseLogFile(getPathFor(FLAWED)));
		//flawedFile.csv is a copy of result10.csv with a spelling mistake in the type.
	}
	
	// -- POOL SIZE -- POOL SIZE -- POOL SIZE -- POOL SIZE --
	@Test
	public void smallParseTestSize10() {
		Printer.ptestcase("Parser pool 10");
		pm.setPoolsizeParsing(10);
		assertTrue(f.parseLogFile(getPathFor(TEN)));
	}
	
	@Test
	public void smallParseTestSize2() {
		Printer.ptestcase("Parser pool 2");
		pm.setPoolsizeParsing(2);
		assertTrue(f.parseLogFile(getPathFor(TEN)));
	}
	
	@Ignore
	@Test
	public void smallParseTestTooBig() {
		Printer.ptestcase("Parser pool 100");
		pm.setPoolsizeParsing(100);
		assertFalse(f.parseLogFile(getPathFor(TEN)));
	}
	
	@Test
	public void mediumParseTest10Tasks() {
		Printer.ptestcase("Parser pool 10");
		pm.setPoolsizeParsing(10);
		assertTrue(f.parseLogFile(getPathFor(SMALL_PARSING2)));
	}
	
	@Ignore
	@Test
	public void negativePoolsize() {
		Printer.ptestcase("Parser pool negative");
		pm.setPoolsizeParsing(-2);
		assertFalse(f.parseLogFile(getPathFor(TEN)));
	}
	
	// -- JUST PARSING -- JUST PARSING -- JUST PARSING -- JUST PARSING --
	@Test
	public void smallParseTestStandardSize() {
		Printer.ptestcase("Parser tiny");
		assertTrue(f.parseLogFile(getPathFor(TEN)));
	}
			
	@Ignore
	@Test
	public void mediumParseTestStandardSize() {
		Printer.ptestcase("Parser small");
		assertTrue(f.parseLogFile(getPathFor(SMALL_PARSING1)));
	}

	@Ignore
	@Test
	public void bigParseTestStandardSize() {
		Printer.ptestcase("Parser normal");
		assertTrue(f.parseLogFile(getPathFor(BIG_PARSING1)));
	}
	
	//@Ignore
	@Test
	public void doubleParsing() {
		Printer.ptestcase("Parser two");
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
		return sourcePath + seperator + "example" + seperator + s;
	}
	
}
