package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

public class AdditionalEchoToolTest {
	private IEchoTool echoTool;
	File workingDir = new File(System.getProperties("user.dir"));
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		echoTool = null;
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
