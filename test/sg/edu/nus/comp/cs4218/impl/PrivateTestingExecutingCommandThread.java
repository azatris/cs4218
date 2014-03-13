package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;

public class PrivateTestingExecutingCommandThread {

	Shell shell;
	ITool tool;
	String stdin;
	Shell.ExecutingCommandThread executingCommandThread;

	@Before
	public void before(){
		shell = new Shell();
		tool = new PWDTool(new String[]{"args"}); //we can use any other tool, but we use PWD for simplicity
		stdin = null;
		executingCommandThread = shell.new ExecutingCommandThread(tool, stdin);
	}
	
	/**
	 * Check whether this thread get a proper message given status code == 0
	 */
	@Test
	public void testGetMessageWithSuccesfulStatusCode() {
		int statusCode = 0;
		String executionResult = tool.execute(shell.getWorkingDirectory(), stdin);
		String message = executingCommandThread.getMessage(statusCode, executionResult);
		assertTrue(message.equals(executionResult));
	}
	
	/**
	 * Check whether this thread fetch a proper result given status code != 0
	 */
	@Test
	public void testGetMessageWithErrorStatusCode() {
		int statusCode = 1;
		String executionResult = tool.execute(shell.getWorkingDirectory(), stdin);
		String message = executingCommandThread.getMessage(statusCode, executionResult);
		assertFalse(message.equals(executionResult)); //assert that it doesn't print the execution result, i.e. it prints error message
	}

	/**
	 * Check whether the current working directory is being changed when status code == 55
	 */
	@Test
	public void testChangeDirectoryWhenStatusCodeIs55() {
		int statusCode = 55;
		String oldWorkingDir = shell.getWorkingDirectory().getAbsolutePath(); //get old working directory
		File dummyDirectory = new File("dummyDirectory"); //create a dummy directory
		dummyDirectory.mkdir();
		tool = new CdTool(new String[]{"args", dummyDirectory.getAbsolutePath()}); //get the cd tool
		String executionResult = tool.execute(shell.getWorkingDirectory(), stdin); //execute the cd tool
		executingCommandThread.getMessage(statusCode, executionResult); //change the shell directory
		String newDir = shell.getWorkingDirectory().getAbsolutePath(); //get new working directory
		assertFalse(newDir.equals(oldWorkingDir)); //assert that the current working directory has been changed
		dummyDirectory.delete();
	}
}
