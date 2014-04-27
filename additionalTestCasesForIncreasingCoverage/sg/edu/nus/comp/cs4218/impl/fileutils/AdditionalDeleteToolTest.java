package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;

public class AdditionalDeleteToolTest {
	private IDeleteTool deleteTool;
	private File workingDir = new File(System.getProperty("user.dir"));
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		deleteTool = null;
	}
	
	@Test
	public void executeNullWorkingDirTest() {
		deleteTool = new DeleteTool(new String[]{"delete"});
		deleteTool.execute(null, "");
		assertNotEquals(0, deleteTool.getStatusCode());
	}
	
	@Test
	public void executeWithOneArgumentDirTest() {
		deleteTool = new DeleteTool(new String[]{"delete"});
		deleteTool.execute(workingDir, "");
		assertNotEquals(0, deleteTool.getStatusCode());
	}
	
	@Test
	public void passNullToConstructorTest() {
		deleteTool = new DeleteTool(null);
		assertEquals(127, deleteTool.getStatusCode());
	}
	
	@Test
	public void passEmptyArgumentToConstructorTest() {
		deleteTool = new DeleteTool(new String[]{});
		assertEquals(127, deleteTool.getStatusCode());
	}
	
	@Test
	public void passWrongArgumentToConstructorTest() {
		deleteTool = new DeleteTool(new String[]{"cdgs"});
		assertEquals(127, deleteTool.getStatusCode());
	}

}
