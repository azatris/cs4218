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

public class AdditionalUNIQToolTest {
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



	//==========================Add additional test cases=========================

	/**
	 * Helper function to create dummy file
	 */

	public File createDummyFile(String fileName, String fileContent) throws IOException{
		File tempFile = new File(fileName);
		DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
		out.writeBytes(fileContent);
		out.close();
		return tempFile;
	}
	/**
	 * Test to read a file given a valid filename
	 * Method to be tested: readFile(String filename)
	 */
	@Test
	public void readFileValidFilenameTest() {
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "This is just a dummy file content \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);
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
	 * Test constructing tool with null arguments
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void constructionTestWithNullArguments(){
			UniqTool tempUniqTool = new UniqTool(null);
			assertEquals(127, tempUniqTool.getStatusCode());

	}
	
	/**
	 * Test constructing tool with empty arguments
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void constructionTestWithEmptyArguments(){
			UniqTool tempUniqTool = new UniqTool(new String[]{""});
			assertEquals(127, tempUniqTool.getStatusCode());

	}
	
	/**
	 * Test constructing with first argument is not uniq
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void constructionTestWithFirstArgumentIsNotUniq(){
			UniqTool tempUniqTool = new UniqTool(new String[]{"ls"});
			assertEquals(127, tempUniqTool.getStatusCode());

	}
	
	/**
	 * Test executing simple command with filename missing
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void executionTestWithFilenameMissing(){
			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq"});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
	}
	/**
	 * Test executing simple command from stdin
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void executionTestFromStdin(){
		//Create a temp file and input some dummy content on it
		String stdin = "a dummy file content \na dummy file content \n The End\n";
		UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-"});
		String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), stdin);
		String expectedResult = "a dummy file content \n The End\n";
		assertEquals(expectedResult, executionResult);
		assertEquals(0, tempUniqTool.getStatusCode());

	}
	
	/**
	 * Test executing with non existing file
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void executionTestWithNonExistingFile(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a dummy file content \na dummy file content \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);
			tempFile.delete();
			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq",});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * Test executing simple command with Windows File Separator
	 * Method to be tested: execute(File workingDir, String stdin)
	 */

	@Test
	public void executionTestWithWindowsLineSeparator(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a dummy file content \r\na dummy file content \r\n The End\r\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "a dummy file content \r\n The End\r\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}

	
	/**
	 * Test executing with simple command skipping 1 token multiple spaces after the 1st token
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestSkipping1TokenMultipleSpacesAfterFirstToken(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a   dummy file content \nnot  \tdummy file content \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f","1",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "a   dummy file content \n The End\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * Test executing with simple command skipping mutiple tokens
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestSkippingMultipleTokens(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a dummy file content \nnot dummy directory content \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f","3",tempFileName});
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
	 * Test executing with simple command skipping multiple tokens with one line has token less than skip number
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestSkippingMultipleTokensOneLineLessThanSkipNUm(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a dummy file content \n\n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f","3",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "a dummy file content \n\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * Test executing with simple command skipping multiple tokens with leading space
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestSkippingMultipleTokensLeadingSpace(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "\t\ta dummy file content \n\t\tnot dummy directory content \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f","3",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "\t\ta dummy file content \n The End\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * Test executing with simple command skipping mutiple tokens ignoring case
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestSkippingMultipleTokensIgnoringCase(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a dummy file content \nnot dummy directory content \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-i","-f","3",tempFileName});
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
	 * Test executing with simple command skipping multiple tokens with leading space ignoring case
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestSkippingMultipleTokensLeadingSpaceIgnoringCase(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "\t\ta dummy file content \n\t\tnot dummy directory content \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-i","-f","3",tempFileName});
			String executionResult = tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			String expectedResult = "\t\ta dummy file content \n The End\n";
			assertEquals(expectedResult, executionResult);
			assertEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}


	
	/**
	 * Test executing with F Option but No Skip Number
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestIllegalFOptionNoSkipNumber(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a dummy file content \nNOT DUMMY FILE CONTENT \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-f",tempFileName}); 
			tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with skip number but doesn't activate f option
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithSkipNumberButNoFOption(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a dummy file content \nNOT DUMMY FILE CONTENT \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-i","-3",tempFileName}); 
			tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test executing with too many arguments
	 * Method to be tested: execute(File workingDir, String stdin)
	 */
	@Test
	public void executionTestWithTooManyArguments(){
		try {
			//Create a temp file and input some dummy content on it
			String tempFileName = "dummyfile";
			String fileContent = "a dummy file content \nNOT DUMMY FILE CONTENT \n The End\n";
			File tempFile = createDummyFile(tempFileName, fileContent);

			UniqTool tempUniqTool = new UniqTool(new String[]{"uniq","-i","-i", "-i", "-f", "3", tempFileName}); 
			tempUniqTool.execute(new File(System.getProperty("user.dir")), null);
			assertNotEquals(0, tempUniqTool.getStatusCode());
			tempFile.delete();
		} catch (IOException e) {
			fail();
		}
	}

}
