package sg.edu.nus.comp.cs4218.impl.extended2;

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

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;

public class PASTEToolTest {
	private IPasteTool pasteTool;
	private IPasteTool pasteToolForExecute;
	
	private static String testData1;
	private static String testData2;
	private static String testData3;
	private static String testDataFileName1;
	private static String testDataFileName2;
	private static String testDataFileName3;
	
	private static Properties prop;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Loading the test data from the properties file.
		prop = new Properties();
		try {
			prop.load(new FileInputStream("test.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		
		testData1 = prop.getProperty("testData1");
		testData2 = prop.getProperty("testData2");
		testData3 = prop.getProperty("testData3");
		
		testDataFileName1 = prop.getProperty("testDataFileName1");
		testDataFileName2 = prop.getProperty("testDataFileName2");
		testDataFileName3 = prop.getProperty("testDataFileName3");

		// Creating files to store the test data.
		File testfile = new File(testDataFileName1);
		if (!testfile.isFile()) {
			PrintWriter writer = new PrintWriter(testDataFileName1, "UTF-8");
			writer.println(testData1);
			writer.close();
		}
		testfile = new File(testDataFileName2);
		if (!testfile.isFile()) {
			PrintWriter writer = new PrintWriter(testDataFileName2, "UTF-8");
			writer.println(testData2);
			writer.close();
		}
		testfile = new File(testDataFileName3);
		if (!testfile.isFile()) {
			PrintWriter writer = new PrintWriter(testDataFileName3, "UTF-8");
			writer.println(testData3);
			writer.close();
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Files.delete(Paths.get(testDataFileName1));
		Files.delete(Paths.get(testDataFileName2));
		Files.delete(Paths.get(testDataFileName3));
	}
	
	@Before
	public void before() {
		pasteTool = new PASTETool(new String[]{"paste"});
	}

	@After
	public void after() {
		pasteTool = null;
	}
	
	
	/**
	 * Test pasteSerial method
	 * @CORRECTED 
	 */
	@Test
	public void pasteSerialTest() {
		String[] input = {"1","2","3","4","5","6"};
		String output = "1\t2\t3\t4\t5\t6\n";
		assertEquals(output, pasteTool.pasteSerial(input));
	}
	
	
	/**
	 * Test pasteUseDelimiter method
	 * @CORRECTED 
	 */
	@Test
	public void pasteUseDelimiterTest(){
		String[] input = {"1","2","3","4","5","6"};
		String output = "1\n2\n3\n4\n5\n6\n";
		assertEquals(output, pasteTool.pasteUseDelimiter("|",input));		
	}
	
	/**
	 * Tests the core functionality of getHelp
	 */
	@Test
	public void testGetHelp() {
		String correctResult = prop.getProperty("getPasteHelpTest");
		String result = pasteTool.getHelp();
		assertEquals("Help was not printed correctly.", correctResult, result);
	}
	
	/**
	 * Tests the functionality of getHelp from execute
	 */
	@Test
	public void testExecuteHelp() {
		pasteToolForExecute = new PASTETool(new String[]{"paste", "-help"});
		String correctResult = prop.getProperty("getPasteHelpTest");
		String result = pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Help was not printed.", correctResult, result);
	}
	
	/**
	 * Tests the default no-flag case of paste
	 * uses pasteUseDelimiter with no delimiters (as empty string)
	 */
	@Test
	public void testExecuteDefault() {
		pasteToolForExecute = new PASTETool(new String[]{"paste", testDataFileName1, testDataFileName2, testDataFileName3});
		String correctResult = prop.getProperty("testExecuteDefault");
		String result = pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Default case result is incorrect.", correctResult, result);
	}
	
	/**
	 * Tests the default no-flag case of paste with no arguments
	 */
	@Test
	public void testExecuteDefaultNoArguments() {
		pasteToolForExecute = new PASTETool(new String[]{"paste"});
		pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotNull("Default case with no arguments does not return correct status code.", pasteToolForExecute.getStatusCode());
	}
	
	/**
	 * Tests the "single file at a time" case of paste
	 * uses pasteSerial
	 */
	@Test
	public void testExecuteS() {
		pasteToolForExecute = new PASTETool(new String[]{"paste", "-s", testDataFileName1, testDataFileName2, testDataFileName3});
		String correctResult = prop.getProperty("testExecuteS");
		String result = pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Single-file case result is incorrect.", correctResult, result);
	}
	
	/**
	 * Tests the "single file at a time" case of paste with just one file
	 * uses pasteSerial
	 */
	@Test
	public void testExecuteSOneFile() {
		pasteToolForExecute = new PASTETool(new String[]{"paste", "-s", testDataFileName1});
		String correctResult = prop.getProperty("testExecuteSOneFile");
		String result = pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Single-file case result is incorrect.", correctResult, result);
	}
	
	/**
	 * Tests the "single file at a time" case of paste with no arguments
	 */
	@Test
	public void testExecuteSNoArguments() {
		pasteToolForExecute = new PASTETool(new String[]{"paste", "-s"});
		pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotNull("Paste with -s flag does not return correct status code.", pasteToolForExecute.getStatusCode());
	}
	
	/**
	 * Tests the single delimiter case of paste
	 * uses pasteUseDelimiter
	 */
	@Test
	public void testExecuteDSingle() {
		pasteToolForExecute = new PASTETool(new String[]{"paste", "-d", ",", testDataFileName1, testDataFileName2, testDataFileName3});
		String correctResult = prop.getProperty("testExecuteDSingle");
		String result = pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Single delimiter case result is incorrect.", correctResult, result);
	}
	
	/**
	 * Tests the multiple delimiters case of paste (one char is one delimiter)
	 * uses pasteUseDelimiter
	 */
	@Test
	public void testExecuteDMultiple() {
		pasteToolForExecute = new PASTETool(new String[]{"paste", "-d", ",./", testDataFileName1, testDataFileName2, testDataFileName3});
		String correctResult = prop.getProperty("testExecuteDMultiple");
		String result = pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Multiple delimiter case result is incorrect.", correctResult, result);
	}
	
	/**
	 * Tests the multiple delimiters case of paste with no arguments
	 */
	@Test
	public void testExecuteDNoArguments() {
		pasteToolForExecute = new PASTETool(new String[]{"paste", "-d", ",./"});
		pasteToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotNull("Paste with -d flag does not return correct status code.", pasteToolForExecute.getStatusCode());
	}
	

}