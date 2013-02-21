package what.test;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunner {

	Result resultParser = JUnitCore.runClasses(ParserTest.class);
	Result resultMySQL = JUnitCore.runClasses(MySQLStuff.class);
}
