package sg.edu.nus.comp.cs4218.integration;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class IntegrationTestingTwoOrMorePipes {

	File workingDirectory = new File(System.getProperty("user.dir"));
	String tempFileName = "dummyfile";
	File tempFile;

	//Setup dummy file before test execution
	@Before
	public void before() {
		try {
			tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nA separator for uniq\nThis is just a dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}

	//Delete the dummy file after each test execution
	@After
	public void after() {
		tempFile.delete();
	}

	/**
	 * Test combination of sort, uniq, wc and cat in this order
	 * Sort, sort the content of a dummy file
	 * Uniq, get unique line from the result of the sort
	 * Wc, count the char, word, and new line character from the input of the uniq
	 * Cat, pass the output of wc to stdout
	 */
	@Test
	public void testSortAndUniqAndWcAndCat(){
		SORTTool sortTool = new SORTTool(new String[]{"sort",tempFileName});
		UniqTool uniqTool = new UniqTool(new String[]{"uniq","-"});
		WcTool wcTool = new WcTool(new String[]{"wc","-"});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		ITool[] toolCollections = {sortTool,uniqTool,wcTool,catTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = "   Character: 63   Word: 13   New Line: 3";
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}
	
	/**
	 * Test combination of uniq, wc, and cat in this order
	 * Uniq, get unique line from the content of a dummy file
	 * Wc, count the char, word, and new line character from the input of the uniq
	 * Cat, pass the output of wc to stdout
	 */
	@Test
	public void testUniqAndWcAndCat(){
		UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
		WcTool wcTool = new WcTool(new String[]{"wc","-"});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		ITool[] toolCollections = {uniqTool,wcTool,catTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = "   Character: 97   Word: 20   New Line: 4";
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test combination of uniq, sort, and cat in this order
	 * Uniq, get unique line from the content of a dummy file
	 * Sort, sort the result of the uniq
	 * Cat, pass the output of sort to stdout
	 */
	@Test
	public void testUniqAndSortAndCat(){
		UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
		SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		ITool[] toolCollections = {uniqTool,sortTool,catTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = "A separator for uniq\nThe End\nThis is just a dummy file content\nThis is just a dummy file content\n";
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test Combination of Sort, Uniq, and Cat in this order
	 * Sort, sort the content of the dummy file
	 * Uniq, get unique line from the output of the sort
	 * Cat, pass the output of uniq to stdout
	 */
	@Test
	public void testSortAndUniqAndCat(){
		SORTTool sortTool = new SORTTool(new String[]{"sort",tempFileName});
		UniqTool uniqTool = new UniqTool(new String[]{"uniq","-"});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		ITool[] toolCollections = {sortTool,uniqTool,catTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = "A separator for uniq\nThe End\nThis is just a dummy file content\n";
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test Combination of Cat, Wc, and another Wc in this order
	 * Cat, pass the content of dummy file to stdout
	 * Wc, count the number of character, word, and new line from the result of cat
	 * Wc, count the number of character, word, and new line from previous wc result
	 */
	@Test
	public void testCatAndWcAndWc(){
		CatTool catTool = new CatTool(new String[]{"cat",tempFileName});
		WcTool wcTool1 = new WcTool(new String[]{"wc","-"});
		WcTool wcTool2 = new WcTool(new String[]{"wc","-"});
		ITool[] toolCollections = {catTool,wcTool1,wcTool2};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = "   Character: 42   Word: 7   New Line: 0";
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test Combination of Uniq, Sort, and Paste in this order
	 * Uniq, get unique line from the content of a dummy file
	 * Sort, sort the result of the uniq
	 * Paste, paste the result from uniq to stdout (since it is only 1 file, its behaviour supposed to be the same with cat)
	 */
	@Test
	public void testUniqAndSortAndPaste(){
		UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
		SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
		PASTETool pasteTool = new PASTETool(new String[]{"paste","-"});
		ITool[] toolCollections = {uniqTool,sortTool,pasteTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = "A separator for uniq\nThe End\nThis is just a dummy file content\nThis is just a dummy file content\n";
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test Combination of Pwd, Cat, and Paste in this order
	 * Pwd, get current abs path working directory
	 * Cat, pass the abs path of current working directory to stdout
	 * Paste, paste the result from cat to stdout (since it is only 1 file, its behaviour supposed to be the same with cat)
	 */
	@Test
	public void testPwdAndCatAndPaste(){
		PWDTool pwdTool = new PWDTool(new String[]{"pwd"});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		PASTETool pasteTool = new PASTETool(new String[]{"paste","-"});
		ITool[] toolCollections = {pwdTool,catTool,pasteTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = pwdTool.execute(workingDirectory,null) + '\n';
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}
	
	/**
	 * Test Combination of Pwd, Cat and Cut in this order
	 * Pwd, get current abs path working directory
	 * Cat, pass the abs path of current working directory to stdout
	 * Cut (1-2), get the first two characters from the result of cat
	 */
	@Test
	public void testPwdAndCatAndCut(){
		PWDTool pwdTool = new PWDTool(new String[]{"pwd"});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		CutTool cutTool = new CutTool(new String[]{"cut","-c","1-2","-"});
		ITool[] toolCollections = {pwdTool,catTool,cutTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = pwdTool.execute(workingDirectory,null).substring(0,2);
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test Combination of Uniq, Sort, and Cut in this order
	 * Uniq, get unique line from the content of a dummy file
	 * Sort, sort the result of the uniq
	 * Cut (1-11), get the first eleven characters from the result of sort
	 */
	@Test
	public void testUniqAndSortAndCut(){
		UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
		SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
		CutTool cutTool = new CutTool(new String[]{"cut","-c","1-11","-"});
		ITool[] toolCollections = {uniqTool,sortTool,cutTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		String result = pipingTool.execute(workingDirectory,null);
		String expectedResult = "A separator";
		assertEquals(expectedResult,result);
		assertEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test Combination of Comm, Sort, and Uniq in this order
	 * Comm, compare two dummy files (dummy file 1 and dummy file 2)
	 * Sort, sort the result of comparing two files
	 * Uniq, get unique line from the result of the sort
	 */
	@Test
	public void testCommAndSortAndUniq(){
		try {
			//temp file 1
			String tempFileName1 = "dummyfile1";
			File tempFile1 = new File(tempFileName1);
			String fileContent1 = "The first line of first file\nThe second line of first file\n";
			DataOutputStream out1 = new DataOutputStream(new FileOutputStream(tempFile1));
			out1.writeBytes(fileContent1);
			out1.close();
			//temp file 2
			String tempFileName2 = "dummyfile2";
			File tempFile2 = new File(tempFileName2);
			String fileContent2 = "The first line of second file\nThe second line of second file\n";
			DataOutputStream out2 = new DataOutputStream(new FileOutputStream(tempFile2));
			out2.writeBytes(fileContent2);
			out2.close();
			CommTool commTool = new CommTool(new String[]{"comm",tempFileName1,tempFileName2});
			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
			UniqTool uniqTool = new UniqTool(new String[]{"uniq","-"});
			ITool[] toolCollections = {commTool,sortTool,uniqTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "\tThe first line of second file\n\tThe second line of second file\nThe first line of first file\nThe second line of first file\n";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test Combination of Comm, Sort, and Cut in this order
	 * Comm, compare two dummy files (dummy file 1 and dummy file 2)
	 * Sort, sort the result of comparing two files
	 * Cut (1-10), get the first ten characters from the result of the sort
	 */
	@Test
	public void testCommAndSortAndCut(){
		try {
			//temp file 1
			String tempFileName1 = "dummyfile1";
			File tempFile1 = new File(tempFileName1);
			String fileContent1 = "The first line of first file\nThe second line of first file\n";
			DataOutputStream out1 = new DataOutputStream(new FileOutputStream(tempFile1));
			out1.writeBytes(fileContent1);
			out1.close();
			//temp file 2
			String tempFileName2 = "dummyfile2";
			File tempFile2 = new File(tempFileName2);
			String fileContent2 = "The first line of second file\nThe second line of second file\n";
			DataOutputStream out2 = new DataOutputStream(new FileOutputStream(tempFile2));
			out2.writeBytes(fileContent2);
			out2.close();
			CommTool commTool = new CommTool(new String[]{"comm",tempFileName1,tempFileName2});
			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
			CutTool cutTool = new CutTool(new String[]{"cut","-c","1-10","-"});
			ITool[] toolCollections = {commTool,sortTool,cutTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "\tThe first";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test Combination of Comm, Sort, and Cut in this order
	 * Comm, compare two dummy files (dummy file 1 and dummy file 2)
	 * Sort, sort the result of comparing two files
	 * Wc, count the number of characters, words, and new lines from the result of sort
	 */
	@Test
	public void testCommAndSortAndWc(){
		try {
			//temp file 1
			String tempFileName1 = "dummyfile1";
			File tempFile1 = new File(tempFileName1);
			String fileContent1 = "The first line of first file\nThe second line of first file\n";
			DataOutputStream out1 = new DataOutputStream(new FileOutputStream(tempFile1));
			out1.writeBytes(fileContent1);
			out1.close();
			//temp file 2
			String tempFileName2 = "dummyfile2";
			File tempFile2 = new File(tempFileName2);
			String fileContent2 = "The first line of second file\nThe second line of second file\n";
			DataOutputStream out2 = new DataOutputStream(new FileOutputStream(tempFile2));
			out2.writeBytes(fileContent2);
			out2.close();
			CommTool commTool = new CommTool(new String[]{"comm",tempFileName1,tempFileName2});
			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
			WcTool wcTool = new WcTool(new String[]{"wc","-"});
			ITool[] toolCollections = {commTool,sortTool,wcTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 122   Word: 24   New Line: 4";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test Combination of Comm, Sort, and Cut in this order
	 * Comm, compare two dummy files (dummy file 1 and dummy file 2)
	 * Cut (1-10), get the first ten characters from the result of the comm
	 * Wc, count the number of characters, words, and new lines from the result of the cut
	 */
	@Test
	public void testCommAndCutAndWc(){
		try {
			//temp file 1
			String tempFileName1 = "dummyfile1";
			File tempFile1 = new File(tempFileName1);
			String fileContent1 = "The first line of first file\nThe second line of first file\n";
			DataOutputStream out1 = new DataOutputStream(new FileOutputStream(tempFile1));
			out1.writeBytes(fileContent1);
			out1.close();
			//temp file 2
			String tempFileName2 = "dummyfile2";
			File tempFile2 = new File(tempFileName2);
			String fileContent2 = "The first line of second file\nThe second line of second file\n";
			DataOutputStream out2 = new DataOutputStream(new FileOutputStream(tempFile2));
			out2.writeBytes(fileContent2);
			out2.close();
			CommTool commTool = new CommTool(new String[]{"comm",tempFileName1,tempFileName2});
			CutTool cutTool = new CutTool(new String[]{"cut","-c","1-10","-"});
			WcTool wcTool = new WcTool(new String[]{"wc","-"});
			ITool[] toolCollections = {commTool,cutTool,wcTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 10   Word: 2   New Line: 0";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test Combination of Paste, Sort, and Cat in this order
	 * Paste, combine the content of two dummy files (dummy file 1 and dummy file 2) line by line separated by tab
	 * Sort, sort the result of comparing two files
	 * Cat, pass the result of the sort to stdout
	 */
	@Test
	public void testPasteAndSortAndCat(){
		try {
			//We need two files for testing paste
			//temp file 1
			String tempFileName1 = "dummyfile1";
			File tempFile1 = new File(tempFileName1);
			String fileContent1 = "This is just a dummy file content\nThe End\n";
			DataOutputStream out1 = new DataOutputStream(new FileOutputStream(tempFile1));
			out1.writeBytes(fileContent1);
			out1.close();
			//temp file 2
			String tempFileName2 = "dummyfile2";
			File tempFile2 = new File(tempFileName2);
			String fileContent2 = "This is just another dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out2 = new DataOutputStream(new FileOutputStream(tempFile2));
			out2.writeBytes(fileContent2);
			out2.close();
			PASTETool pasteTool = new PASTETool(new String[]{"paste",tempFileName1,tempFileName2});
			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
			CatTool catTool = new CatTool(new String[]{"cat","-"});
			ITool[] toolCollections = {pasteTool,sortTool,catTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "\tThe End\nThe End\tThis is just a dummy file content\nThis is just a dummy file content\tThis is just another dummy file content\n";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			fail();
		}	
	}
	
	/**
	 * Test Combination of Paste, Sort, Cat and Wc in this order
	 * Paste, combine the content of two dummy files (dummy file 1 and dummy file 2) line by line separated by tab
	 * Sort, sort the result of comparing two files
	 * Cat, pass the result of the sort to stdout
	 * Wc, count the number of characters, words, and new lines from the result of the cat
	 */
	@Test
	public void testPasteAndSortAndCatAndWc(){
		try {
			//We need two files for testing paste
			//temp file 1
			String tempFileName1 = "dummyfile1";
			File tempFile1 = new File(tempFileName1);
			String fileContent1 = "This is just a dummy file content\nThe End\n";
			DataOutputStream out1 = new DataOutputStream(new FileOutputStream(tempFile1));
			out1.writeBytes(fileContent1);
			out1.close();
			//temp file 2
			String tempFileName2 = "dummyfile2";
			File tempFile2 = new File(tempFileName2);
			String fileContent2 = "This is just another dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out2 = new DataOutputStream(new FileOutputStream(tempFile2));
			out2.writeBytes(fileContent2);
			out2.close();
			PASTETool pasteTool = new PASTETool(new String[]{"paste",tempFileName1,tempFileName2});
			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
			CatTool catTool = new CatTool(new String[]{"cat","-"});
			WcTool wcTool = new WcTool(new String[]{"wc","-"});
			ITool[] toolCollections = {pasteTool,sortTool,catTool,wcTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 125   Word: 25   New Line: 3";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			fail();
		}	
	}

	/**
	 * Test Broken Pipe, Invalid Input in First Tool in this case
	 * Uniq, this tool is given an invalid parameter for the -f option, should terminate here by returning error status code, the pipe breaks here
	 * Sort, this tool will never been executed since the pipe is broken
	 * Cat, this tool will never been executed since the pipe is broken
	 */
	@Test
	public void testBrokenPipeWithInvalidInputInFirstToolUniq(){
		UniqTool uniqTool = new UniqTool(new String[]{"uniq","-f","dummy","-"});
		SORTTool sortTool = new SORTTool(new String[]{"sort",tempFileName});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		ITool[] toolCollections = {uniqTool, sortTool, catTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		pipingTool.execute(workingDirectory,null);
		assertNotEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test Broken Pipe, Invalid Input in Second Tool in this case
	 * Sort, sort the content of the dummy file
	 * Uniq, this tool is given an invalid parameter for the -f option, should terminate here by returning error status code, the pipe breaks here
	 * Cat, this tool will never been executed since the pipe is broken
	 */
	@Test
	public void testBrokenPipeWithInvalidInputInSecondTool(){
		SORTTool sortTool = new SORTTool(new String[]{"sort",tempFileName});
		UniqTool uniqTool = new UniqTool(new String[]{"uniq","-f","dummy","-"});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		ITool[] toolCollections = {sortTool,uniqTool,catTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		pipingTool.execute(workingDirectory,null);
		assertNotEquals(0,pipingTool.getStatusCode());
	}

	/**
	 * Test Broken Pipe, Invalid Input in Third Tool in this case
	 * Sort, sort the content of the dummy file
	 * Cat, pass the result of sort to stdout
	 * Uniq, this tool is given an invalid parameter for the -f option, should terminate here by returning error status code, the pipe breaks here
	 */
	@Test
	public void testBrokenPipeWithInvalidInputInThirdTool(){
		SORTTool sortTool = new SORTTool(new String[]{"sort",tempFileName});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		UniqTool uniqTool = new UniqTool(new String[]{"uniq","-f","dummy","-"});
		ITool[] toolCollections = {sortTool,catTool,uniqTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		pipingTool.execute(workingDirectory,null);
		assertNotEquals(0,pipingTool.getStatusCode());
	}


}
