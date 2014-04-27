package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.WrongParsingTool;

public class TestBug42Shell {

	Shell shell;
	String sep = File.separator; // to make testing OS independent
	ITool instantiatedTool;

	@Before
	public void setUp() throws Exception {
		shell = new Shell();
	}

	@After
	public void tearDown() throws Exception {
		shell = null;
		instantiatedTool = null;
	}
	
	/**
	 * Showing bug 42 is now working as intended 
	 * but cannot be shown by test because the code is in main.
	 */
	@Test
	public void parserWhatHappensSpaces() {
		instantiatedTool=shell.parse(" ");
		assertNull(instantiatedTool);
	}
	

}
