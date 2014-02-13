package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

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

	/**
	 * Test executing a null ITool, if there is no exception being thrown 
	 */
	@Test
	public void testExecutingANullITool() {
		try {
			final ITool tool = null;
			shell.execute(tool);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test running an ITool
	 */
	@Test
	public void testRunningAnITool(){
		try {
			final ITool tool = new PWDTool(new String[]{"pwd"});
			shell.execute(tool);
			assertTrue(tool.getStatusCode() == 0);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test stopping a null thread
	 */
	@Test
	public void testStoppingANullThread() {
		try {
			final Thread executingThread = null;
			shell.stop(executingThread);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test stopping a running Thread immediately
	 */
	@Test
	public void testStoppingARunningThreadImmediately() {
		try {
			final ITool tool = new PWDTool(new String[]{"pwd"});
			shell.stop(shell.execute(tool));
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test stopping a running Thread after a while
	 */
	@Test
	public void testStoppingARunningThreadAfterAWhile() {
		try {
			final ITool tool = new PWDTool(new String[]{"pwd"});
			Runnable executingThread = shell.execute(tool);
			Thread.sleep(3000);
			shell.stop(executingThread);
		} catch (Exception e) {
			fail();
		}
	}
}
