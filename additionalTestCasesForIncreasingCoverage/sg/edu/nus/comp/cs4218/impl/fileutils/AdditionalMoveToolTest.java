package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;

public class AdditionalMoveToolTest {
	private IMoveTool moveTool;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		moveTool = null;
	}

	@Test
	public void passNullToConstructorTest() {
		moveTool = new MoveTool(null);
		assertEquals(127, moveTool.getStatusCode());
	}
	
	@Test
	public void passEmptyArgumentToConstructorTest() {
		moveTool = new MoveTool(new String[]{});
		assertEquals(127, moveTool.getStatusCode());
	}
	
	@Test
	public void passWrongArgumentToConstructorTest() {
		moveTool = new MoveTool(new String[]{"mv"});
		assertEquals(127, moveTool.getStatusCode());
	}
}
