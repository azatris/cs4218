package sg.edu.nus.comp.cs4218.impl.fileutils;

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
	private static String testDataFileName;
	private static Properties prop;
	final private static String IP = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";
	
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
		String testData = prop.getProperty("testData");
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

	@Test
	public void executeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void getCountOfMatchingLinesJavaTest() {
		int result = grepTool.getCountOfMatchingLines("java", testDataFileName);
		assertEquals("Incorrect number of matched lines.", 8, result);
	}
	
	@Test
	public void getCountOfMatchingLinesIPTest() {
		int result = grepTool.getCountOfMatchingLines(IP, testDataFileName);
		assertEquals("Incorrect number of matched lines.", 3, result);
	}

	@Test
	public void getOnlyMatchingLinesJavaTest() {
		String correctResult = prop.getProperty("getOnlyMatchingLinesTestJava");
		String result = grepTool.getOnlyMatchingLines("java", testDataFileName);
		assertEquals("The right lines were not matched.", correctResult, result);
	}
	
	@Test
	public void getOnlyMatchingLinesIPTest() {
		String correctResult = prop.getProperty("getOnlyMatchingLinesTestIP");
		String result = grepTool.getOnlyMatchingLines(IP, testDataFileName);
		assertEquals("The right lines were not matched.", correctResult, result);
	}

	@Test
	public void getMatchingLinesWithTrailingContextJavaTest() {
		String correctResult = prop.getProperty("getMatchingLinesWithTrailingContextTestJava");
		String result = grepTool.getMatchingLinesWithTrailingContext(2, "java", testDataFileName);
		assertEquals("The right lines with trailing context were not matched.", correctResult, result);
	}
	
	@Test
	public void getMatchingLinesWithTrailingContextIPTest() {
		String correctResult = prop.getProperty("getMatchingLinesWithTrailingContextTestIP");
		String result = grepTool.getMatchingLinesWithTrailingContext(2, IP, testDataFileName);
		assertEquals("The right lines with trailing context were not matched.", correctResult, result);
	}

	@Test
	public void getMatchingLinesWithLeadingContextJavaTest() {
		String correctResult = prop.getProperty("getMatchingLinesWithLeadingContextTestJava");
		String result = grepTool.getMatchingLinesWithLeadingContext(2, "java", testDataFileName);
		assertEquals("The right lines with leading context were not matched.", correctResult, result);
	}
	
	@Test
	public void getMatchingLinesWithLeadingContextIPTest() {
		String correctResult = prop.getProperty("getMatchingLinesWithLeadingContextTestIP");
		String result = grepTool.getMatchingLinesWithLeadingContext(2, IP, testDataFileName);
		assertEquals("The right lines with leading context were not matched.", correctResult, result);
	}

	@Test
	public void getMatchingLinesWithOutputContextJavaTest() {
		String correctResult = prop.getProperty("getMatchingLinesWithOutputContextTestJava");
		String result = grepTool.getMatchingLinesWithOutputContext(1, "java", testDataFileName);
		assertEquals("The right lines with output context were not matched.", correctResult, result);
	}
	
	@Test
	public void getMatchingLinesWithOutputContextIPTest() {
		String correctResult = prop.getProperty("getMatchingLinesWithOutputContextTestIP");
		String result = grepTool.getMatchingLinesWithOutputContext(1, IP, testDataFileName);
		assertEquals("The right lines with output context were not matched.", correctResult, result);
	}

	@Test
	public void getMatchingLinesOnlyMatchingPartJavaTest() {
		String correctResult = prop.getProperty("getMatchingLinesOnlyMatchingPartTestJava");
		String result = grepTool.getMatchingLinesOnlyMatchingPart("java", testDataFileName);
		assertEquals("The right parts were not matched.", correctResult, result);
	}
	
	@Test
	public void getMatchingLinesOnlyMatchingPartIPTest() {
		String correctResult = prop.getProperty("getMatchingLinesOnlyMatchingPartTestIP");
		String result = grepTool.getMatchingLinesOnlyMatchingPart(IP, testDataFileName);
		assertEquals("The right parts were not matched.", correctResult, result);
	}

	@Test
	public void getNonMatchingLinesJavaTest() {
		String correctResult = prop.getProperty("getNonMatchingLinesTestJava");
		String result = grepTool.getNonMatchingLines("java", testDataFileName);
		assertEquals("The right non-matching lines were not found.", correctResult, result);
	}
	
	@Test
	public void getNonMatchingLinesIPTest() {
		String correctResult = prop.getProperty("getNonMatchingLinesTestIP");
		String result = grepTool.getNonMatchingLines(IP, testDataFileName);
		assertEquals("The right non-matching lines were not found.", correctResult, result);
	}

	@Test
	public void getHelpTest() {
		String correctResult = prop.getProperty("getHelpTest");
		String result = grepTool.getHelp();
		assertEquals("Help was printed correctly.", correctResult, result);
	}

}
