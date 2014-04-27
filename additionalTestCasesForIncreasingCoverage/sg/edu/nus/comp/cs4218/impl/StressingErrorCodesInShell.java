package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;

public class StressingErrorCodesInShell {
	Shell shell;
	File tempFile;
	String tempFileName;
	File workingDirectory = new File(System.getProperty("user.dir"));
	
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
	public void statuscode67() {
		shell = new Shell();
		ITool cutTool = new CutTool(new String[] {"cut","-c", "1-2-3", "-"}); 
		Thread thread = (Thread) shell.execute(cutTool);
	}
	
	@Test
	public void statuscode210() {
		shell = new Shell();
		ITool tool = shell.parse("ls | cut -c 1-2-3 -");
		Thread thread = (Thread) shell.execute(tool);
	}
	
	@Test
	public void statuscode98() {
		shell = new Shell();
		ITool ost =shell.parse("hallo kaggee");
		Thread thread = (Thread) shell.execute(ost);
	}
	
}
