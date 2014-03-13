/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;

public class WrongParsingToolTest {
	private WrongParsingTool wrongParsingTool;

	@Before
	public void setUp() throws Exception {
		wrongParsingTool = new WrongParsingTool(new String[]{"wrong", "dummyOptions", "dummyFileName"});
	}
	/**
	 * Test method for execute
	 */
	@Test
	public void testExecute() {
		assertEquals(
				"Wrong parsing tool should not return anything meaningful", 
				wrongParsingTool.execute(Paths.get(".").toFile(), null), 
				"");
	}

	/**
	 * Test method for getStatusCode
	 */
	@Test
	public void testGetStatusCode() {
		assertEquals(wrongParsingTool.getStatusCode(), 98);
	}

}
