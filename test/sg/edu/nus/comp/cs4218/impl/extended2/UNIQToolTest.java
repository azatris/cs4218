package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;

public class UNIQToolTest {
	private IUniqTool uniqTool;
	
	@Before
	public void before() {
		String[] args = {"uniq"};
		uniqTool = new UniqTool(args);
	}

	@After
	public void after() {
		uniqTool = null;
	}
	
	/**
	 * test getUnique method
	 * @Corrected assertEquals("",uniqTool.getUnique(false, input4)); suppose to be assertEquals(input4,uniqTool.getUnique(false, input4));
	 */
	@Test
	public void getUniqueTest() {
		String input1 = "ab cd ef";
		String input4 = " AB   cd ef";
		assertEquals(input1,uniqTool.getUnique(true, input1));
		assertEquals(input4,uniqTool.getUnique(false, input4));
		assertEquals(input4,uniqTool.getUnique(true, input4));
	}
	
	/**
	 * test getUnique method for null
	 */
	@Test
	public void getUniqueForNullTest(){
		assertEquals("",uniqTool.getUnique(true, null));
	}
	
	/**
	 * test getUniqueSkipNum method for valid range
	 */
	@Test
	public void getUniqueSkipNumValidRangeTest(){
		String input1 = "a b c d e";
		String input2 = "b  b c d e";
		assertEquals(input1,uniqTool.getUniqueSkipNum(1,true, input1));
		assertEquals(input2,uniqTool.getUniqueSkipNum(1,false, input2));
	}
	
	/**
	 * test getUniqueSkipNum method for out of range
	 * @Corrected assertEquals("",uniqTool.getUniqueSkipNum(false, input2)); suppose to be assertEquals(input2,uniqTool.getUniqueSkipNum(false, input2));
	 */
	@Test
	public void getUniqueSkipNumInvalidRangeTest(){
		String input1 = "a b c d e";
		String input2 = "b  b c d e";
		assertEquals(input1,uniqTool.getUniqueSkipNum(100,true, input1));
		assertEquals(input2,uniqTool.getUniqueSkipNum(100,false, input2));
	}
	
	/**
	 * test getUniqueSkipNum method for null
	 */
	@Test
	public void getUniqueSkipNumForNullTest(){
		assertEquals("",uniqTool.getUniqueSkipNum(1,true, null));
	}
	
	//=======================Add additional test cases======================
	
	/**
	 * Test to read a file given a valid filename
	 * Method to be tested: readFile(String filename)
	 */
	@Test
	public void readFileValidFilenameTest() {
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			assertEquals(fileContent, UniqTool.readFile(tempFileName));
			tempFile.delete();
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

			String tempFileName = "invalidFilename";
			try {
				UniqTool.readFile(tempFileName);
				fail();
			} catch (IOException e) {
				//Exception Caught
			}	
	}
	
	/**
	 * Test to check the File Existence of Existing File
	 * Method to be tested: checkFileExistence(String filename)
	 */
	@Test
	public void checkFileExistenceForExistingFile(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			assertTrue(UniqTool.checkFileExistence(tempFileName));
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * Test to check the File Existence of NonExisting File
	 * Method to be tested: checkFileExistence(String filename)
	 */
	@Test
	public void checkFileExistenceForNonExistingFile(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			tempFile.delete();
			assertFalse(UniqTool.checkFileExistence(tempFileName));
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing simple command
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	
	@Test
	public void executionTest(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "a dummy file content \na dummy file content \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "a dummy file content \n The End\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with simple command ignoring case
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestIgnoringCase(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "A DUMMY FILE CONTENT\na dummy file content\n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-i",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "A DUMMY FILE CONTENT\n The End\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with simple command skipping 1 token
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestSkipping1Token(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "a dummy file content \nnot dummy file content \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f","1",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "a dummy file content \n The End\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with Simple Command Skipping 1 token ignoring case
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestSkipping1TokenIgnoringCase(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "a dummy file content \nNOT DUMMY FILE CONTENT \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-i","-f","1",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "a dummy file content \n The End\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with Illegal Option (Unknown Option)
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestIllegalOption(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "a dummy file content \nNOT DUMMY FILE CONTENT \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-z",tempFileName}); //-z is illegal option here
			tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with Illegal Skip Number (Zero)
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestIllegal0SkipNumber(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "a dummy file content \nNOT DUMMY FILE CONTENT \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f","0",tempFileName}); //-z is illegal option here
			tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with Illegal Option (Negative Number)
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestIllegalNegativeSkipNumber(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "a dummy file content \nNOT DUMMY FILE CONTENT \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f","-1",tempFileName}); //-z is illegal option here
			tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with Illegal Option (Non-Integer Skip Number)
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestIllegalNonIntegerSkipNumber(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			File tempFile = new File(tempFileName);
			String fileContent = "a dummy file content \nNOT DUMMY FILE CONTENT \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f","10a00",tempFileName}); //-z is illegal option here
			tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test get help
	 */
	@Test
	public void testGetHelp(){
		String textOfHelp = "Command Format - uniq [OPTIONS] [FILE]\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") use standard input\n"
				+ "OPTIONS\n"
				+ "f NUM : Skips NUM fields on each line before checking for uniqueness. Use a null\n"
				+ "string for comparison if a line has fewer than n fields. Fields are sequences of\n"
				+ "non-space non-tab characters that are separated from each other by at least one\n"
				+ "space or tab.\n"
				+ "-i : Ignore differences in case when comparing lines.\n"
				+ "-help : Brief information about supported options";
		String executionResult = uniqTool.getHelp();
		assertEquals(textOfHelp,executionResult);
		assertEquals(uniqTool.getStatusCode(),0);
	}
}

