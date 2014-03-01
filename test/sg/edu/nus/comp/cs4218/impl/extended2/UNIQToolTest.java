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
import sg.edu.nus.comp.cs4218.helper.MessageHelper;

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
	
	
	//test getUnique method
	/**
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
	
	//test getUnique method for null
	@Test
	public void getUniqueForNullTest(){
		assertEquals("",uniqTool.getUnique(true, null));
	}
	
	//test getUniqueSkipNum method for valid range
	@Test
	public void getUniqueSkipNumValidRangeTest(){
		String input1 = "a b c d e";
		String input2 = "b  b c d e";
		assertEquals(input1,uniqTool.getUniqueSkipNum(1,true, input1));
		assertEquals(input2,uniqTool.getUniqueSkipNum(1,false, input2));
	}
	
	//test getUniqueSkipNum method for out of range
	/**
	 * @Corrected assertEquals("",uniqTool.getUniqueSkipNum(false, input2)); suppose to be assertEquals(input2,uniqTool.getUniqueSkipNum(false, input2));
	 */
	@Test
	public void getUniqueSkipNumInvalidRangeTest(){
		String input1 = "a b c d e";
		String input2 = "b  b c d e";
		assertEquals(input1,uniqTool.getUniqueSkipNum(100,true, input1));
		assertEquals(input2,uniqTool.getUniqueSkipNum(100,false, input2));
	}
	
	//test getUniqueSkipNum method for null
	@Test
	public void getUniqueSkipNumForNullTest(){
		assertEquals("",uniqTool.getUniqueSkipNum(1,true, null));
	}
	
	//Add additional test cases
	//Test reading File
	@Test
	public void readFileTest() {
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content \n The End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			assertEquals(fileContent, UniqTool.readFile(tempFileName));
			tempFile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Check whether an existing file is considered as exist
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Check whether a non-existing file is considered as not exist
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Test executing a simple command
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Test executing a simple command ignoring case
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Test executing a simple command skipping 1 token
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Test executing a simple command skipping 1 token ignoring case
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Test executing a simple command with illegal option
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Test executing a simple command with 0 skip number (illegal)
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Test executing a simple command with negative skip number (illegal)
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Test executing a simple command with Non-Integer skip number (illegal)
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

