package what.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import what.Facade;
import what.sp_parser.ParserMediator;

public class ParserTest {
	
	private static Facade f;
	private static ParserMediator pm;
	private static String sourcePath;
	private static String seperator;
	
	@BeforeClass
	public static void initialize() {
		f = Facade.getFacadeIstance();
		sourcePath = System.getProperty("user.dir");
		seperator = System.getProperty("file.separator");
	}
	
	@Before
	public void resetFacade() {
		f.reset();
		f.init(sourcePath + seperator + "conf\\ConfigurationFile.json");
		pm = f.getParserMediator();
	}
	
	@Testsmal
	public void smallParseTestStandardSize() {
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\result10.csv"));
	}
	
	@Test
	public void testIntegerMistake() {
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\fileWithMistake1.csv"));
	}
	
	@Test
	public void testMissingPart() {
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\fileWithMistake2.csv"));
	}
	
	@Test
	public void testEmptyPart() {
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\fileWithMistake3.csv"));
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
	public void smallParseTestEmptyLine() {
		assertTrue(f.parseLogFile(sourcePath + seperator + "example\\result10EmptyLine.csv"));
	}
	
	@Test
	public void negativePoolsize() {
		pm.setPoolsizeParsing(-2);
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\result10.csv"));
	}
	
	@Test
	public void nonexistentFile() {
		assertFalse(f.parseLogFile("nonexistentpath.csv"));
	}
	
	@Test
	public void wrongFile() {
		assertFalse(f.parseLogFile(sourcePath + seperator + "example\\wrongFile.txt"));
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
