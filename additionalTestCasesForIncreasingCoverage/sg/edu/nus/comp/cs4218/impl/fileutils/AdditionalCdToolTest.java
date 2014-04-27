package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.fileutils.ICdTool;

public class AdditionalCdToolTest {
	private ICdTool cdTool;
	private File workingDir = new File(System.getProperty("user.dir"));
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		cdTool = null;
	}

	@Test
	public void cdWithThreeArguments() {
		cdTool = new CdTool(new String[]{"cd", ".", ".."});
		assertEquals(System.getProperty("user.dir"), cdTool.execute(workingDir, ""));
		assertEquals(98, cdTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass null as arguments
	 */
	@Test
	public void passNullToConstructorTest() {
		cdTool = new CdTool(null);
		assertEquals(127, cdTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass empty array as arguments
	 */
	@Test
	public void passEmptyArgumentToConstructorTest() {
		cdTool = new CdTool(new String[]{});
		assertEquals(127, cdTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass wrong command as arguments
	 */
	@Test
	public void passWrongArgumentToConstructorTest() {
		cdTool = new CdTool(new String[]{"cdgs"});
		assertEquals(127, cdTool.getStatusCode());
	}
}
