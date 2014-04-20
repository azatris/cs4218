package sg.edu.nus.comp.cs4218.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;
import static org.junit.Assert.*;


public class ConstructingToolFromShellCommandLineTest {
	Shell shell;
	File tempFile;
	String tempFileName;

	@Before
	public void before(){
		shell = new Shell();
		tempFileName = "dummyfile";
		try{
			String fileContent = "a dummy file content\n The End\n";
			tempFile = createDummyFile(tempFileName, fileContent);
		}
		catch(IOException e){
			System.out.println("IO Exception Caught in the test set up");
		}
	}

	@After
	public void after(){
		tempFile.delete();
	}

	/**
	 * Helper function to create dummy file
	 */

	public File createDummyFile(String fileName, String fileContent) throws IOException{
		File temporaryFile = new File(fileName);
		DataOutputStream out = new DataOutputStream(new FileOutputStream(temporaryFile));
		out.writeBytes(fileContent);
		out.close();
		return temporaryFile;
	}
	
	@Test
	public void ConstructingUniqToolFromCommandLineTest(){
		String commandline = "uniq " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof UniqTool);
	}
	
	@Test
	public void ConstructingWcToolFromCommandLineTest(){
		String commandline = "wc " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof WcTool);
	}


	@Test
	public void ConstructingLsToolFromCommandLineTest(){
		String commandline = "ls " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof LsTool);
	}
	
	@Test
	public void ConstructingPwdToolFromCommandLineTest(){
		String commandline = "pwd";
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof PWDTool);
	}

	@Test
	public void ConstructingCdToolFromCommandLineTest(){
		String commandline = "cd";
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof CdTool);
	}
	
	@Test
	public void ConstructingEchoToolFromCommandLineTest(){
		String commandline = "echo " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof EchoTool);
	}
	
	@Test
	public void ConstructingDeleteToolFromCommandLineTest(){
		String commandline = "delete " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof DeleteTool);
	}
	
	@Test
	public void ConstructingCopyToolFromCommandLineTest(){
		String targetFileName = "newdummyfile";
		String commandline = "copy " + tempFileName + targetFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof CopyTool);
	}
	
	@Test
	public void ConstructingMoveToolFromCommandLineTest(){
		String targetFileName = "newdummyfile";
		String commandline = "move " + tempFileName + targetFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof MoveTool);
	}
	@Test
	public void ConstructingCutToolFromCommandLineTest(){
		String commandline = "cut -c 1-2 " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof CutTool);
	}
	
	@Test
	public void ConstructingSortToolFromCommandLineTest(){
		String commandline = "sort " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof SORTTool);
	}

	@Test
	public void ConstructingPasteToolFromCommandLineTest(){
		String commandline = "paste " + tempFileName + " " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof PASTETool);
	}
	
	@Test
	public void ConstructingCommToolFromCommandLineTest(){
		String commandline = "comm " + tempFileName + " " + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof CommTool);
	}
	
	@Test
	public void ConstructingGrepToolFromCommandLineTest(){
		String commandline = "grep a" + tempFileName;
		ITool instantiatedTool = shell.parse(commandline);
		assertTrue(instantiatedTool instanceof GrepTool);
	}
}
