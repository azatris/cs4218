package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;

public class TestBug42Shell {

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
	public void test() {
		fail("Not yet implemented");
	}

}
