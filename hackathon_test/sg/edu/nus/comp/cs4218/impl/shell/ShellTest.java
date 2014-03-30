package sg.edu.nus.comp.cs4218.impl.shell;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;

public class ShellTest {
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
		assertNull(tool);
	}
	
	@Test
	public void parse_Pipe2Commands_ReturnPipingTool() {
		// cd | pwd
		tool = shell.parse("cd \"D:"+sep+sep+"dir 1"+sep+"dir 1 2 3\"|pwd");
		assertTrue(tool instanceof PipingTool);
	}
	
	@Test
	public void parse_CdDirectory_ReturnCdTool() {
		tool = shell.parse("cd \"dir2"+sep+"dir  3\" ");
		assertTrue(tool instanceof CdTool);
	}
	
	@Test
	public void parse_DeleteDirectory_ReturnDeleteTool() {	
		// delete directory
		tool = shell.parse("delete\tabc"+sep+"def"+sep);
		assertTrue(tool instanceof DeleteTool);
	}
	
}
