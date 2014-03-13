package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GrepToolTest {
	private GrepTool grepTool;
	private GrepTool grepToolForExecute;
	private static String testDataFileName;
	private static String testData;
	private static Properties prop;
	final private static String IP_PATTERN = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Loading the test data from the properties file.
		prop = new Properties();
		try {
			prop.load(new FileInputStream("test.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		testDataFileName = prop.getProperty("testDataFileName");
		testData = prop.getProperty("testData");
		// Creating a file to store the test data.
		File testfile = new File(testDataFileName);
		if (!testfile.isFile()) {
			PrintWriter writer = new PrintWriter(testDataFileName, "UTF-8");
			writer.println(testData);
			writer.close();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Files.delete(Paths.get(testDataFileName));
	}

	@Before
	public void setUp() throws Exception {
		grepTool = new GrepTool(new String[]{"does", "not", "matter"});
	}

	/**
	 * Test getCountOfMatchingLines method with a normal word pattern
	 */
	@Test
	public void testGetCountOfMatchingLinesJava() {
		int result = grepTool.getCountOfMatchingLines("java", testData);
		assertEquals("Incorrect number of matched lines.", 8, result);
	}
	
	/**
	 * Test getCountOfMatchingLines method with an IP pattern
	 */
	@Test
	public void testGetCountOfMatchingLinesIP() {
		int result = grepTool.getCountOfMatchingLines(IP_PATTERN, testData);
		assertEquals("Incorrect number of matched lines.", 3, result);
	}

	/**
	 * Test getOnlyMatchingLines method with a normal word pattern
	 */
	@Test
	public void testGetOnlyMatchingLinesJava() {
		String correctResult = prop.getProperty("getOnlyMatchingLinesTestJava");
		String result = grepTool.getOnlyMatchingLines("java", testData);
		assertEquals("The right lines were not matched.", correctResult, result);
	}
	
	/**
	 * Test getOnlyMatchingLines method with an IP pattern
	 */
	@Test
	public void testGetOnlyMatchingLinesIP() {
		String correctResult = prop.getProperty("getOnlyMatchingLinesTestIP");
		String result = grepTool.getOnlyMatchingLines(IP_PATTERN, testData);
		assertEquals("The right lines were not matched.", correctResult, result);
	}

	/**
	 * Test getMatchingLinesWithTrailingContext method with a normal word pattern
	 */
	@Test
	public void testGetMatchingLinesWithTrailingContextJava() {
		String correctResult = prop.getProperty("getMatchingLinesWithTrailingContextTestJava");
		String result = grepTool.getMatchingLinesWithTrailingContext(2, "java", testData);
		assertEquals("The right lines with trailing context were not matched.", correctResult, result);
	}
	
	/**
	 * Test getMatchingLinesWithTrailingContext method with an IP pattern
	 */
	@Test
	public void testGetMatchingLinesWithTrailingContextIP() {
		String correctResult = prop.getProperty("getMatchingLinesWithTrailingContextTestIP");
		String result = grepTool.getMatchingLinesWithTrailingContext(2, IP_PATTERN, testData);
		assertEquals("The right lines with trailing context were not matched.", correctResult, result);
	}

	/**
	 * Test getMatchingLinesWithLeadingContext method with a normal word pattern
	 */
	@Test
	public void testGetMatchingLinesWithLeadingContextJava() {
		String correctResult = prop.getProperty("getMatchingLinesWithLeadingContextTestJava");
		String result = grepTool.getMatchingLinesWithLeadingContext(2, "java", testData);
		assertEquals("The right lines with leading context were not matched.", correctResult, result);
	}
	
	/**
	 * Test getMatchingLinesWithLeadingContext method with an IP pattern
	 */
	@Test
	public void testGetMatchingLinesWithLeadingContextIP() {
		String correctResult = prop.getProperty("getMatchingLinesWithLeadingContextTestIP");
		String result = grepTool.getMatchingLinesWithLeadingContext(2, IP_PATTERN, testData);
		assertEquals("The right lines with leading context were not matched.", correctResult, result);
	}

	/**
	 * Test getMatchingLinesWithOutputContext method with a normal word pattern
	 */
	@Test
	public void testGetMatchingLinesWithOutputContextJava() {
		String correctResult = prop.getProperty("getMatchingLinesWithOutputContextTestJava");
		String result = grepTool.getMatchingLinesWithOutputContext(1, "java", testData);
		assertEquals("The right lines with output context were not matched.", correctResult, result);
	}
	
	/**
	 * Test getMatchingLinesWithOutputContext method with an IP pattern
	 */
	@Test
	public void testGetMatchingLinesWithOutputContextIP() {
		String correctResult = prop.getProperty("getMatchingLinesWithOutputContextTestIP");
		String result = grepTool.getMatchingLinesWithOutputContext(1, IP_PATTERN, testData);
		assertEquals("The right lines with output context were not matched.", correctResult, result);
	}
	
	/**
	 * Test getMatchingLinesOnlyMatchingPart method with a Java pattern
	 */
	@Test
	public void testGetMatchingLinesOnlyMatchingPartJava() {
		String correctResult = prop.getProperty("getMatchingLinesOnlyMatchingPartTestJava");
		String result = grepTool.getMatchingLinesOnlyMatchingPart("java", testData);
		assertEquals("The right parts were not matched.", correctResult, result);
	}
	
	/**
	 * Test getMatchingLinesOnlyMatchingPart method with an IP pattern
	 */
	@Test
	public void testGetMatchingLinesOnlyMatchingPartIP() {
		String correctResult = prop.getProperty("getMatchingLinesOnlyMatchingPartTestIP");
		String result = grepTool.getMatchingLinesOnlyMatchingPart(IP_PATTERN, testData);
		assertEquals("The right parts were not matched.", correctResult, result);
	}

	/**
	 * Test getNonMatchingLines method with a Java pattern
	 */
	@Test
	public void testGetNonMatchingLinesJava() {
		String correctResult = prop.getProperty("getNonMatchingLinesTestJava");
		String result = grepTool.getNonMatchingLines("java", testData);
		assertEquals("The right non-matching lines were not found.", correctResult, result);
	}
	
	/**
	 * Test getNonMatchingLines method with an IP pattern
	 */
	@Test
	public void testGetNonMatchingLinesIP() {
		String correctResult = prop.getProperty("getNonMatchingLinesTestIP");
		String result = grepTool.getNonMatchingLines(IP_PATTERN, testData);
		assertEquals("The right non-matching lines were not found.", correctResult, result);
	}

	/**
	 * Tests whether the help returned from getHelp is the correct string
	 */
	@Test
	public void testGetHelp() {
		String correctResult = prop.getProperty("getHelpTest");
		String result = grepTool.getHelp();
		assertEquals("Help was not printed correctly.", correctResult, result);
	}
	
	/**
	 *  Tests execute of grep with 0 arguments
	 */
	@Test
	public void testExecuteArguments0ReturnsNull() {
		grepToolForExecute = new GrepTool(new String[]{});
		String result = grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNull("Execution runtime should never reach this point", result);
	}
	
	/**
	 *  Tests status code of execute of grep with 0 arguments
	 */
	@Test
	public void testExecuteArguments0StatusCode() {
		grepToolForExecute = new GrepTool(new String[]{});
		grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals(grepToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 *  Tests execute of grep with 1 argument
	 */
	@Test
	public void testExecuteArguments1ReturnsNull() {
		grepToolForExecute = new GrepTool(new String[]{"grep"});
		String result = grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNull("Grep must have at least 1 argument", result);
	}
	
	/**
	 *  Tests status code of execute of grep with 1 argument
	 */
	@Test
	public void testExecuteArguments1StatusCode() {
		grepToolForExecute = new GrepTool(new String[]{"grep"});
		grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals(grepToolForExecute.getStatusCode(), 0);
	}

	/**
	 *  Tests execute of grep with 2 arguments
	 */
	@Test
	public void testExecuteArguments2() {
		grepToolForExecute = new GrepTool(new String[]{"grep", "help"});
		String correctResult = prop.getProperty("getHelpTest");
		String result = grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Help was printed.", correctResult, result);
	}
	
	/**
	 *  Tests execute of grep with 3 arguments
	 */
	@Test
	public void testExecuteArguments3() {
		grepToolForExecute = new GrepTool(new String[]{"grep", IP_PATTERN, testDataFileName});
		String correctResult = prop.getProperty("getOnlyMatchingLinesTestIP");
		String result = grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("The lines were not matched.", correctResult, result);
	}
	
	/**
	 *  Tests execute of grep with 4 arguments
	 */
	@Test
	public void testExecuteArguments4() {
		grepToolForExecute = new GrepTool(new String[]{"grep", "-c", IP_PATTERN, testDataFileName});
		String result = grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Incorrect number of matched lines.", Integer.toString(3), result);
	}
	
	/**
	 *  Tests execute of grep with 5 arguments
	 */
	@Test
	public void testExecuteArguments5() {
		grepToolForExecute = new GrepTool(new String[]{"grep", "-C", "1", IP_PATTERN, testDataFileName});
		String correctResult = prop.getProperty("getMatchingLinesWithOutputContextTestIP");
		String result = grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("The lines with output context were not matched.", correctResult, result);
	}
	
	/**
	 *  Tests the status code of execute of grep with more than 5 arguments
	 */
	@Test
	public void testExecuteArgumentsMoreThan5StatusCode() {
		grepToolForExecute = new GrepTool(new String[]{
				"grep", "-C", "4", IP_PATTERN, testDataFileName, "dummyArgument"});
		grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals(grepToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 *  Tests execute with stdin (from pipe)
	 */
	@Test
	public void testExecuteWithStdin() {
		grepToolForExecute = new GrepTool(new String[]{"grep", IP_PATTERN, "-"});
		String correctResult = prop.getProperty("getOnlyMatchingLinesTestIP");
		String result = grepToolForExecute.execute(Paths.get(".").toFile(), testData);
		assertEquals("The lines were not matched.", correctResult, result);
	}
	
	/**
	 *  Tests execute with stdin (from pipe) with excessive number of arguments
	 */
	@Test
	public void testExecuteWithStdinWithTooManyArguments() {
		grepToolForExecute = new GrepTool(new String[]{"grep", IP_PATTERN, "-", "dummyArgument"});
		grepToolForExecute.execute(Paths.get(".").toFile(), testData);
		assertNotEquals(grepToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 *  Tests execute with an incorrect tool
	 */
	@Test
	public void testExecuteWithIncorrectTool() {
		grepToolForExecute = new GrepTool(new String[]{"cat", IP_PATTERN, testDataFileName});
		grepToolForExecute.execute(Paths.get(".").toFile(), testData);
		assertNotEquals(grepToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 *  Tests execute with an invalid tool
	 */
	@Test
	public void testExecuteWithInvalidTool() {
		grepToolForExecute = new GrepTool(new String[]{"dog", IP_PATTERN, testDataFileName});
		grepToolForExecute.execute(Paths.get(".").toFile(), testData);
		assertNotEquals(grepToolForExecute.getStatusCode(), 0);
	}
}
