package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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

import sg.edu.nus.comp.cs4218.extended2.ISortTool;

public class SORTToolTest {

	private static ISortTool sortTool;
	private ISortTool sortToolForExecute;
	
	private static Properties prop;
	
	private static String testData1;
	private static String testData2;
	private static String testData3;
	private static String testDataFileName1;
	private static String testDataFileName2;
	private static String testDataFileName3;

	@BeforeClass
	public static void executeThisBeforeClass() throws IOException {
		// Loading the test data from the properties file.
		prop = new Properties();
		try {
			prop.load(new FileInputStream("test.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}

		// creating testFile of unsorted order
		File myFile1 = new File("unSortFile.txt");
		myFile1.createNewFile();
		writeFile("unSortFile.txt", "zzz\r\nbbb\r\naaa\r\nggg\r\nfff");

		// creating testFile of sorted order
		File myFile2 = new File("sortFile.txt");
		myFile2.createNewFile();
		writeFile("sortFile.txt", "aaa\r\nbbb\r\nccc\r\nddd\r\neee");
		
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

	@Before
	public void before() throws IOException {
		sortTool = new SORTTool(new String[]{"sort", "dummyOptions", "dummyFileName"});

	}

	@After
	public void after() {
		sortTool = null;
	}

	@AfterClass
	public static void executeThisAfterClass() throws IOException {

		File file1 = new File("sortFile.txt");
		if (file1.exists()) {
			file1.delete();
		}

		File file2 = new File("unSortFile.txt");
		if (file2.exists()) {
			file2.delete();
		}
		
		Files.delete(Paths.get(testDataFileName1));
		Files.delete(Paths.get(testDataFileName2));
		Files.delete(Paths.get(testDataFileName3));
	}
	
//	/**
//	 * test sortFile method on sorted file
//	 * @CORRECTED incorrect test case as sortFile takes multiple files as input
//	 */
//	@Test
//	public void sortFileTestForSortedFile() {
//		String result = sortTool.sortFile("sortFile.txt");
//		assertEquals(result, "aaa\nbbb\nccc\nddd\neee\n");
//	}

//	/**
//	 * test sortFile method on unsorted file
//	 * @CORRECTED incorrect test case as sortFile takes multiple files as input
//	 */
//	@Test
//	public void sortFileTestForUnsortedFile() {
//		String result = sortTool.sortFile("unSortFile.txt");
//		assertEquals(result, "aaa\nbbb\nfff\nggg\nzzz\n");
//	}

	/**
	 * test checkIfSorted method on sorted file
	 * @CORRECTED unintentional typo
	 */
	@Test
	public void checkIfSortedTestForUnsortedFile() {
		String result = sortTool.checkIfSorted("unSortFile.txt");
		assertEquals(result, "sort: unSortFile.txt:2 disorder: bbb\n");
	}

	//test checkIfSorted method on unsorted file
	@Test
	public void checkIfSortedTestForSortedFile() {
		String result = sortTool.checkIfSorted("sortFile.txt");
		assertEquals(result, "");
	}
		
	public static void writeFile(String fileName, String s) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write(s);
		out.close();
	}
	
	/**
	 * Tests the core functionality of getHelp
	 */
	@Test
	public void testGetHelp() {
		String correctResult = prop.getProperty("getSortHelpTest");
		String result = sortTool.getHelp();
		assertEquals("Help was not printed correctly.", correctResult, result);
	}
	
	/**
	 * Tests the functionality of getHelp from execute
	 */
	@Test
	public void testExecuteHelp() {
		sortToolForExecute = new SORTTool(new String[]{"sort", "-help"});
		String correctResult = prop.getProperty("getSortHelpTest");
		String result = sortToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Help was not printed.", correctResult, result);
	}
	
	/**
	 * Tests the default no-flag case of sort on multiple files
	 * uses sortFile
	 */
	@Test
	public void testSortMultiple() {
		sortToolForExecute = new SORTTool(new String[]{"sort", testDataFileName1, testDataFileName2, testDataFileName3});
		String correctResult = prop.getProperty("testSortMultiple");
		String result = sortToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Incorrect sort result, sorting multiple files.", correctResult, result);
	}
	
	/**
	 * Tests the default no-flag case of sort on multiple files with stdin
	 * uses sortFile
	 */
	@Test
	public void testSortMultipleWithStdin() {
		sortToolForExecute = new SORTTool(new String[]{"sort", testDataFileName1, testDataFileName2, testDataFileName3, "-"});
		String correctResult = prop.getProperty("testSortMultipleWithStdin");
		String result = sortToolForExecute.execute(Paths.get(".").toFile(), ".\n,\n/");
		assertEquals("Incorrect sort result, sorting multiple files with stdin.", correctResult, result);
	}
	
	/**
	 * Tests the -c flag case of sort on multiple files
	 * uses checkIfSorted
	 */
	@Test
	public void testCheckIfSortedMultiple() {
		sortToolForExecute = new SORTTool(new String[]{"sort", "-c", testDataFileName1, testDataFileName2, testDataFileName3});
		sortToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals("Extra operand not allowed with -c.", 0, sortToolForExecute.getStatusCode());
	}
	
	/**
	 * Tests the -c flag case of sort on multiple files with stdin
	 * uses checkIfSorted
	 */
	@Test
	public void testCheckIfSorted() {
		sortToolForExecute = new SORTTool(new String[]{"sort", "-c", testDataFileName3});
		String correctResult = prop.getProperty("testCheckIfSorted");
		String result = sortToolForExecute.execute(Paths.get(".").toFile(), null);
		assertEquals("Incorrect sort check.", correctResult, result);
	}
}
