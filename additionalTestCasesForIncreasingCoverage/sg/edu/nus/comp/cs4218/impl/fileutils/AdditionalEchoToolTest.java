package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

public class AdditionalEchoToolTest {
	private IEchoTool echoTool;
	File workingDir = new File(System.getProperty("user.dir"));
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		echoTool = null;
	}

	@Test
	public void echoNothingTest() {
		echoTool = new EchoTool(new String[]{"echo"});
		assertEquals(System.lineSeparator(), echoTool.execute(workingDir, ""));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass null as arguments
	 */
	@Test
	public void passNullToConstructorTest() {
		echoTool = new EchoTool(null);
		assertEquals(127, echoTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass empty array as arguments
	 */
	@Test
	public void passEmptyArgumentToConstructorTest() {
		echoTool = new EchoTool(new String[]{});
		assertEquals(127, echoTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass wrong command as arguments
	 */
	@Test
	public void passWrongArgumentToConstructorTest() {
		echoTool = new EchoTool(new String[]{"ecdsgjl"});
		assertEquals(127, echoTool.getStatusCode());
	}
}
