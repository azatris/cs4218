package sg.edu.nus.comp.cs4218.integration;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class IntegrationTestingTwoOrMorePipes {

	File workingDirectory = new File(System.getProperty("user.dir"));

	@Test
	public void TestSortAndUniqAndWcAndCat(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nA separtor for uniq\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			SORTTool sortTool = new SORTTool(new String[]{"sort",tempFileName});
			UniqTool uniqTool = new UniqTool(new String[]{"uniq","-"});
			WcTool wcTool = new WcTool(new String[]{"wc","-"});
			CatTool catTool = new CatTool(new String[]{"cat","-"});
			ITool[] toolCollections = {sortTool,uniqTool,wcTool,catTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 42   Word: 9   New Line: 2";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void TestUniqAndWcAndCat(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
			WcTool wcTool = new WcTool(new String[]{"wc","-"});
			CatTool catTool = new CatTool(new String[]{"cat","-"});
			ITool[] toolCollections = {uniqTool,wcTool,catTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 42   Word: 9   New Line: 2";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestUniqAndSortAndCat(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
			CatTool catTool = new CatTool(new String[]{"cat","-"});
			ITool[] toolCollections = {uniqTool,sortTool,catTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "The End\nThis is just a dummy file content\n";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestSortAndUniqAndCat(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nA separtor for uniq\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			SORTTool sortTool = new SORTTool(new String[]{"sort",tempFileName});
			UniqTool uniqTool = new UniqTool(new String[]{"uniq","-"});
			CatTool catTool = new CatTool(new String[]{"cat","-"});
			ITool[] toolCollections = {sortTool,uniqTool,catTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "The End\nThis is just a dummy file content\n";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestCatAndWcAndWc(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			CatTool catTool = new CatTool(new String[]{"cat",tempFileName});
			WcTool wcTool1 = new WcTool(new String[]{"wc","-"});
			WcTool wcTool2 = new WcTool(new String[]{"wc","-"});
			ITool[] toolCollections = {catTool,wcTool1,wcTool2};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 41   Word: 7   New Line: 0";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void TestUniqAndSortAndPaste(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
			PASTETool pasteTool = new PASTETool(new String[]{"paste","-"});
			ITool[] toolCollections = {uniqTool,sortTool,pasteTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "The End\nThis is just a dummy file content\n";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void TestPwdAndCatAndPaste(){
		try {
			PWDTool pwdTool = new PWDTool(new String[]{"pwd"});
			CatTool catTool = new CatTool(new String[]{"cat","-"});
			PASTETool pasteTool = new PASTETool(new String[]{"paste","-"});
			ITool[] toolCollections = {pwdTool,catTool,pasteTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = pwdTool.execute(workingDirectory,null) + '\n';
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void TestPwdAndCatAndWc(){
		try {
			PWDTool pwdTool = new PWDTool(new String[]{"pwd"});
			CatTool catTool = new CatTool(new String[]{"cat","-"});
			WcTool wcTool = new WcTool(new String[]{"wc","-"});
			ITool[] toolCollections = {pwdTool,catTool,wcTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 46   Word: 1   New Line: 0";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//This doesn't work yet
	@Test
	public void TestUniqAndSortAndCut(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
//			CutTool cutTool = new CutTool(new String[]{"cut","-c","1-2",-});
//			ITool[] toolCollections = {uniqTool,sortTool,cutTool};
//			PipingTool pipingTool = new PipingTool(toolCollections);
//			String result = pipingTool.execute(workingDirectory,null);
//			String expectedResult = "e End\nThis is just a dummy file content\n";
//			assertEquals(expectedResult,result);
//			assertEquals(0,pipingTool.getStatusCode());
			tempFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//This doesn't work yet
	@Test
	public void TestCommAndSortAndUniq(){
		try {
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
//			CommTool commTool = new CommTool(new String[]{"comm",tempFileName1,tempFileName2});
//			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
//			PASTETool uniqTool = new PASTETool(new String[]{"uniq","-"});
//			ITool[] toolCollections = {commTool,sortTool,uniqTool};
//			PipingTool pipingTool = new PipingTool(toolCollections);
//			String result = pipingTool.execute(workingDirectory,null);
//			String expectedResult = "The End\nThis is just a dummy file content\n";
//			assertEquals(expectedResult,result);
//			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//This doesn't work yet
	@Test
	public void TestCommAndCutAndSort(){
		try {
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
//			CommTool commTool = new CommTool(new String[]{"comm",tempFileName1,tempFileName2});
//			CutTool cutTool = new CutTool(new String[]{"cut","-"});
//			SORTTool sortTool = new SORTTool(new String[]{"sort","-"});
//			ITool[] toolCollections = {commTool,cutTool,sortTool};
//			PipingTool pipingTool = new PipingTool(toolCollections);
//			String result = pipingTool.execute(workingDirectory,null);
//			String expectedResult = "The End\nThis is just a dummy file content\n";
//			assertEquals(expectedResult,result);
//			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void PasteAndSortAndCat(){
		try {
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
			System.out.println(result);
			String expectedResult = "The End\nThis is just a dummy file content\n";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
			tempFile1.delete();
			tempFile2.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
}
