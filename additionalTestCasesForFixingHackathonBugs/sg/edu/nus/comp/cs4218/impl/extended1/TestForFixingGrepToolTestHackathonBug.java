package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestForFixingGrepToolTestHackathonBug {

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
	 * BUG_ID: 41
	 * Fix location 1: Line 334, GrepTool.java
	 * Fix location 2: Line #, CommTool.Java
	 * Fix location 3: Line #, CommTool.Java
	 */
	@Test
	public void testExecuteWithNonExistingFile() {
		grepToolForExecute = new GrepTool(new String[]{"grep", IP_PATTERN, "abcdefg.prr"});
		grepToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals(grepToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 * BUG_ID: 43
	 * Fix location 1: Line 334, GrepTool.java
	 */
	@Test
	public void testGrepExecuteCountMatchingLines() {
		grepToolForExecute = new GrepTool(new String[]{"grep", "-c", "l3", "a.txt"});
		int result = Integer.valueOf(grepToolForExecute.execute(Paths.get(".").toFile(), null));
		assertEquals(1, result);
	}

}
