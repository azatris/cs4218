package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

public class IntegrationTestingTwoOrMorePipes {

	File workingDirectory = new File(System.getProperty("user.dir"));

	@Test
	public void TestUniqAndWcAndEcho(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
			WcTool wcTool = new WcTool(new String[]{"wc","-"});
			CatTool catTool = new CatTool(new String[]{"echo","-"});
			ITool[] toolCollections = {uniqTool,wcTool,catTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 42   Word: 9   New Line: 2";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void TestUniqAndSortAndEcho(){
		try {
			String tempFileName = "dummy file";
			File tempFile = new File(tempFileName);
			String fileContent = "This is just a dummy file content\nThis is just a dummy file content\nThe End\n";
			DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
			out.writeBytes(fileContent);
			out.close();
			UniqTool uniqTool = new UniqTool(new String[]{"uniq",tempFileName});
			WcTool wcTool = new WcTool(new String[]{"wc","-"});
			CatTool catTool = new CatTool(new String[]{"echo","-"});
			ITool[] toolCollections = {uniqTool,wcTool,catTool};
			PipingTool pipingTool = new PipingTool(toolCollections);
			String result = pipingTool.execute(workingDirectory,null);
			String expectedResult = "   Character: 42   Word: 9   New Line: 2";
			assertEquals(expectedResult,result);
			assertEquals(0,pipingTool.getStatusCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
