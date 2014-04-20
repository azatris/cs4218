package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;

public class WCToolTest {
	private IWcTool wcTool;
	private String fileContent;
	private File tempFile;
	private String tempFileName;

	@Before
	public void before() {
		
		String[] args = {"wc"};
		wcTool = new WcTool(args);
		try {
		//Create a temp file and input some dummy content on it
		tempFileName = "dummyfile";
		tempFile = new File(tempFileName);
		fileContent = "This is just a dummy file content \n The End\n";
		DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
		out.writeBytes(fileContent);
		out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void after() {
		wcTool = null;
		tempFile.delete();
	}

	/**
	 * test getCharacterCount method with string
	 */
	@Test
	public void getCharacterCountTest() {
		String input = "Test this"; 
		assertEquals("9", wcTool.getCharacterCount(input));
	}

	/**
	 * test getCharacterCount method with string having a newline
	 */
	@Test
	public void getCharacterCountStringWithNewlineTest() {
		String input = "Test this\n"; 
		assertEquals("10", wcTool.getCharacterCount(input));
	}

	/**
	 * test getCharacterCount method with empty string
	 */
	@Test
	public void getCharacterCountEmptyStringTest() {
		String input = "";// empty string
		assertEquals("0", wcTool.getCharacterCount(input));
	}


	/**
	 * test getWordCountTest, String with newline
	 */
	@Test
	public void getWordCountTest() {

		String input = "\n Test 4 3 \n"; 
		assertEquals("3", wcTool.getWordCount(input));

	}
	
	/**
	 * test getWordCountTest for empty string
	 */
	@Test
	public void getWordCountForEmptyString() {
		String input = "";
		assertEquals("0", wcTool.getWordCount(input));
	}

	/**
	 * test getWordCountTest for null string
	 */
	@Test
	public void getWordCountForNullTest() {
		String input = null;
		assertEquals("0", wcTool.getWordCount(input));
	}


	/**
	 * Test getNewLineCount with string with newline
	 */
	@Test
	public void getNewLineCountTest() {
		String input = "Test this\n\n\n"; // with new line character
		assertEquals("3", wcTool.getNewLineCount(input));
	}

	/**
	 * Test getNewLineCount with null string
	 */
	@Test
	public void getNewLineCountForNullTest() {
		String input = null;
		assertEquals("0", wcTool.getNewLineCount(input));
	}
	
	/**
	 * Test getNewLineCount with null string
	 */
	@Test
	public void getCharacterCountForNullTest() {
		String input = null;
		assertEquals("0", wcTool.getCharacterCount(input));
	}

	//==========================Add additional test cases============================
	/**
	 * Test to read a file given a valid filename
	 * Method to be tested: readFile(String filename)
	 */
	@Test
	public void readFileValidFilenameTest() {
		try {
			assertEquals(fileContent, WcTool.readFile(tempFileName));
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * Test to read a file given a valid filename
	 * Method to be tested: readFile(String filename)
	 */
	@Test
	public void readFileInvalidFilenameTest() {
			String invalidFileName = "invalidFilename";
			try {
				WcTool.readFile(invalidFileName);
				fail();
			} catch (IOException e) {
				//Exception Caught
			}
		
	}
	
	/**
	 * Test executing simple command
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTest(){
			WcTool tempWcTool = new WcTool(new String[]{"wc",tempFileName});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedCharCount = tempWcTool.getCharacterCount(fileContent);
			String expectedWordCount = tempWcTool.getWordCount(fileContent);
			String expectedNewLineCount = tempWcTool.getNewLineCount(fileContent);
			String expectedResult = "   Character: " + expectedCharCount + "   Word: " + expectedWordCount + "   New Line: " + expectedNewLineCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	

	/**
	 * Test executing simple command from stdin
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestFromStdin(){
			String stdin = "This is just a dummy file content \n The End\n";
			WcTool tempWcTool = new WcTool(new String[]{"wc","-"});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), stdin);
			String expectedCharCount = tempWcTool.getCharacterCount(fileContent);
			String expectedWordCount = tempWcTool.getWordCount(fileContent);
			String expectedNewLineCount = tempWcTool.getNewLineCount(fileContent);
			String expectedResult = "   Character: " + expectedCharCount + "   Word: " + expectedWordCount + "   New Line: " + expectedNewLineCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test executing with non existing file
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithNonExistingFile(){
			tempFile.delete(); // delete the temp file, so it doesn't texist
			WcTool tempWcTool = new WcTool(new String[]{"wc",tempFileName});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test executing simple command with filename missing
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void executionTestWithFilenameMissing(){
			WcTool tempWcTool = new WcTool(new String[]{"wc"});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test constructing with null arguments
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void constructionTestWithNullArguments(){
		WcTool tempWcTool = new WcTool(null);
		assertEquals(127, tempWcTool.getStatusCode());


	}
	
	/**
	 * Test constructing with empty arguments
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void constructionTestWithEmptyArguments(){
			WcTool tempWcTool = new WcTool(new String[]{""});
			assertEquals(127, tempWcTool.getStatusCode());

	}
	
	/**
	 * Test constructing with first argument is not wc
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void constructionTestWithFirstArgumentIsNotWc(){
			WcTool tempWcTool = new WcTool(new String[]{"ls"});
			assertEquals(127, tempWcTool.getStatusCode());

	}
	
	/**
	 * Test executing with M option
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithMOption(){
			WcTool tempWcTool = new WcTool(new String[]{"wc","-m",tempFileName});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedCharCount = tempWcTool.getCharacterCount(fileContent);
			String expectedResult = "   Character: " + expectedCharCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test executing with M option from stdin
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithMOptionFromStdin(){
			String stdin = "This is just a dummy file content \n The End\n";
			WcTool tempWcTool = new WcTool(new String[]{"wc","-m","-"});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), stdin);
			String expectedCharCount = tempWcTool.getCharacterCount(fileContent);
			String expectedResult = "   Character: " + expectedCharCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test executing with W option
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithWOption(){
			WcTool tempWcTool = new WcTool(new String[]{"wc","-w",tempFileName});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedWordCount = tempWcTool.getWordCount(fileContent);
			String expectedResult = "   Word: " + expectedWordCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test executing with W option from stdin
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithWOptionFromStdin(){
		String stdin = "This is just a dummy file content \n The End\n";
			WcTool tempWcTool = new WcTool(new String[]{"wc","-w","-"});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), stdin);
			String expectedWordCount = tempWcTool.getWordCount(fileContent);
			String expectedResult = "   Word: " + expectedWordCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test executing with L option
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithLOption(){
			WcTool tempWcTool = new WcTool(new String[]{"wc","-l",tempFileName});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedNewLineCount = tempWcTool.getNewLineCount(fileContent);
			String expectedResult = "   New Line: " + expectedNewLineCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test executing with L option from stdin
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithLOptionFromStdin(){
			String stdin = "This is just a dummy file content \n The End\n";
			WcTool tempWcTool = new WcTool(new String[]{"wc","-l","-"});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), stdin);
			String expectedNewLineCount = tempWcTool.getNewLineCount(fileContent);
			String expectedResult = "   New Line: " + expectedNewLineCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test executing with M, W, and L option
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithMWLOption(){
			WcTool tempWcTool = new WcTool(new String[]{"wc","-m", "-w","-l",tempFileName});
			String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedCharCount = tempWcTool.getCharacterCount(fileContent);
			String expectedWordCount = tempWcTool.getWordCount(fileContent);
			String expectedNewLineCount = tempWcTool.getNewLineCount(fileContent);
			String expectedResult = "   Character: " + expectedCharCount + "   Word: " + expectedWordCount + "   New Line: " + expectedNewLineCount;
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempWcTool.getStatusCode());
	}
	
	
	/**
	 * Test executing with Illegal Option
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithIllegalOption(){
			WcTool tempWcTool = new WcTool(new String[]{"wc","-z",tempFileName}); //-z is illegal option
			tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempWcTool.getStatusCode());
	}
	
	
	/**
	 * Test executing with too many arguments
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithTooManyArguments(){
			WcTool tempWcTool = new WcTool(new String[]{"wc","-m","-w", "-l", "-m", "-w", "-l", tempFileName}); 
			tempWcTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempWcTool.getStatusCode());
	}
	
	/**
	 * Test getting Help
	 * Method to be tested: getHelp()
	 */
	@Test
	public void testGetHelp(){
		String textOfHelp = "Command Format - wc [OPTIONS] [FILE]\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") use standard input\n"
				+ "OPTIONS\n"
				+ "-m : Print only the character counts\n"
				+ "-w : Print only the word counts\n"
				+ "-l : Print only the newline counts\n"
				+ "-help : Brief information about supported options";
		WcTool tempWcTool = new WcTool(new String[]{"wc","-help"});
		String executionResult = tempWcTool.execute(new File(System.getProperty("user.dir")), null);
		
		assertEquals(textOfHelp,executionResult);
		assertEquals(tempWcTool.getStatusCode(),0);
	}
}
