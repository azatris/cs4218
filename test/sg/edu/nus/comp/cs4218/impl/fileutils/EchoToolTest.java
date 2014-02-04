package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

public class EchoToolTest {
	private IEchoTool echoTool;
	@Before
	public void setUp() throws Exception {
		echoTool = new EchoTool();
	}

	@After
	public void tearDown() throws Exception {
		echoTool = null;
	}

	// Testing String echo(String toEcho)
	@Test
	public void echoStringTest() {
		String str= "echo testing echo testing echo testing echo testing \n echo testing echo testing \t\n";
		assertEquals(str, echoTool.echo(str));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	@Test
	public void echoEmptyStringTest() {
		assertEquals("", echoTool.echo(""));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	@Test
	public void echoNullTest() {
		assertNull(echoTool.echo(null));
		assertTrue(echoTool.getStatusCode() != 0);
	}

}
