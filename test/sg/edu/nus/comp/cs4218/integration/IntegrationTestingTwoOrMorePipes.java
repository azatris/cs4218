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
	 * Test Combination of Sort, Uniq, Wc, and Cat in this order
	 */
	@Test
	public void TestSortAndUniqAndWcAndCat(){
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
	 * Test Combination of Uniq, Wc, and Cat in this order
	 */
	@Test
	public void TestUniqAndWcAndCat(){
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
	 * Test Combination of Uniq, Sort, and Cat in this order
	 */
	@Test
	public void TestUniqAndSortAndCat(){
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
	 */
	@Test
	public void TestSortAndUniqAndCat(){
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
	 */
	@Test
	public void TestCatAndWcAndWc(){
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
	 */
	@Test
	public void TestUniqAndSortAndPaste(){
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
	 */
	@Test
	public void TestPwdAndCatAndPaste(){
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
	 */
	@Test
	public void TestPwdAndCatAndCut(){
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
	 */
	@Test
	public void TestUniqAndSortAndCut(){
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
	 */
	@Test
	public void TestCommAndSortAndUniq(){
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
	 */
	@Test
	public void TestCommAndSortAndCut(){
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
	 * Test Combination of Paste, Sort, and Cat in this order
	 */
	@Test
	public void PasteAndSortAndCat(){
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
	 */
	@Test
	public void PasteAndSortAndCatAndWc(){
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
	 */
	@Test
	public void TestBrokenPipeWithInvalidInputInFirstToolUniq(){
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
	 */
	@Test
	public void TestBrokenPipeWithInvalidInputInSecondTool(){
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
	 */
	@Test
	public void TestBrokenPipeWithInvalidInputInThirdTool(){
		SORTTool sortTool = new SORTTool(new String[]{"sort",tempFileName});
		CatTool catTool = new CatTool(new String[]{"cat","-"});
		UniqTool uniqTool = new UniqTool(new String[]{"uniq","-f","dummy","-"});
		ITool[] toolCollections = {sortTool,catTool,uniqTool};
		PipingTool pipingTool = new PipingTool(toolCollections);
		pipingTool.execute(workingDirectory,null);
		assertNotEquals(0,pipingTool.getStatusCode());
	}


}
