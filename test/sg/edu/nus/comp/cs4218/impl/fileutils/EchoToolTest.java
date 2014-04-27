package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

public class EchoToolTest {
	private IEchoTool echoTool;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		echoTool = null;
	}

	/**
	 *  Testing String echo(String toEcho)
	 */
	@Test
	public void echoStringTest() {
		String[] args = {"echo", ""};
		echoTool = new EchoTool(args);
		String str= "echo testing echo testing echo testing echo testing \n echo testing echo testing \t\n";
		assertEquals(str, echoTool.echo(str));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	// TODO
	@Test
	public void echoEmptyStringTest() {
		String[] args = {"echo", ""};
		echoTool = new EchoTool(args);
		assertEquals("", echoTool.echo(""));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	// TODO
	@Test
	public void echoNullTest() {
		String[] args = {"echo", ""};
		echoTool = new EchoTool(args);
		assertNull(echoTool.echo(null));
		assertTrue(echoTool.getStatusCode() != 0);
	}
	
	/**
	 * String execute(File workingDir, String stdin)
	 * echo str1
	 */
	@Test
	public void echoOneTest() {
		File workingDir = new File(System.getProperty("user.dir"));
		String[] args = {"echo", "str1"};
		echoTool = new EchoTool(args);
		assertEquals(args[1]+System.lineSeparator(), echoTool.execute(workingDir, null));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	/**
	 *  echo str1 str2
	 */
	@Test
	public void echoTwoTest() {
		File workingDir = new File(System.getProperty("user.dir"));
		String[] args = {"echo", "str1", "str2"};
		echoTool = new EchoTool(args);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i=1; i < args.length; i++){
			stringBuilder.append(args[i]);
			if (i != args.length-1){
				stringBuilder.append(" ");
			}
		}
		stringBuilder.append(System.lineSeparator());
		assertEquals(stringBuilder.toString(), echoTool.execute(workingDir, ""));
		assertEquals(0, echoTool.getStatusCode());
	}
	
	/**
	 *  echo str1 str2 str3
	 */
	@Test
	public void echoThreeTest() {
		File workingDir = new File(System.getProperty("user.dir"));
		String[] args = {"echo", "str1", "str2", "str3"};
		echoTool = new EchoTool(args);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i=1; i < args.length; i++){
			stringBuilder.append(args[i]);
			if (i != args.length-1){
				stringBuilder.append(" ");
			}
		}
		stringBuilder.append(System.lineSeparator());
		assertEquals(stringBuilder.toString(), echoTool.execute(workingDir, null));
		assertEquals(0, echoTool.getStatusCode());
	}
}
