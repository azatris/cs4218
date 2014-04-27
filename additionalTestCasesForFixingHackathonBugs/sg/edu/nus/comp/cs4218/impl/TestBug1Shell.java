package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * BUG_ID: 1
 * Fix location 1: Line 65, getToolFromArgument Sheel.java
 */

public class TestBug1Shell {

	Shell shell;
	String sep = File.separator; // to make testing OS independent
	ITool tool;

	@Before
	public void setUp() throws Exception {
		shell = new Shell();
	}

	@After
	public void tearDown() throws Exception {
		shell = null;
		tool = null;
	}

	@Test
	public void parse_PipeAtStartOfCommand_ReturnNull() {
		tool = shell.parse("   | copy file1.a file2.a directory | ls ");
		assertNotNull("should not return null", tool);	
	}



}
