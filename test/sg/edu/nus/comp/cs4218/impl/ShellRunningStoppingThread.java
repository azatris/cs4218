package sg.edu.nus.comp.cs4218.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class ShellRunningStoppingThread {

	private Shell shell;
	
	@Before
	public void before(){
		shell = new Shell();
	}
	
	@After
	public void after(){
		shell = null;
	}
	
	//Test executing a null ITool
	@Test
	public void testExecutingANullITool() {
		final ITool tool = null;
		shell.execute(tool);
	}
	
	//Test running an ITool
	@Test
	public void testRunningAnITool(){
		final ITool tool = new PWDTool();
		shell.execute(tool);
	}
	
	//Test stopping a null thread
	@Test
	public void testStoppingANullThread() {
		final Thread executingThread = null;
		shell.stop(executingThread);
	}
	
	//Test stopping a running Thread
	@Test
	public void testStoppingARunningThread() {
		final ITool tool = new PWDTool();
		shell.stop(shell.execute(tool));
	}
}
