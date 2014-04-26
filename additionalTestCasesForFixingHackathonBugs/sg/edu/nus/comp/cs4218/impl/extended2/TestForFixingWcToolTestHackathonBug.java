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

public class TestForFixingWcToolTestHackathonBug {
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


	//==========================Add additional test cases============================
	
	
	/**
	 * Test getNewLineCount with null string
	 * Fix Bug #35, WcTool.java line 183
	 */
	@Test
	public void getCharacterCountForNullTest() {
		String input = null;
		assertEquals("0", wcTool.getCharacterCount(input));
	}
	
	/**
	 * Test executing simple command from stdin
	 * Method to be tested: execute(File workingDir, String stdin)
	 * Fix Bug #36, WcTool.java, line 65
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
	
}