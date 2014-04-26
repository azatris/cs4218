package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;

/**
 * BUG_ID: 24
 * Fix location 1: Line 31, Common.Java 
 * Fix location 2: Line 43, DeleteTool.Java
 */
public class TestBug24DeleteToolTest {
	private IDeleteTool deleteTool;

	@Test
	public void nullWorkingDirExecuteTest() {
		String[] args = {"delete", "file1"};
		deleteTool = new DeleteTool(args);
		deleteTool.execute(null, null);
		assertFalse(deleteTool.getStatusCode() == 0);
	}
	
	@Test
	public void nullRelDirExecuteTest() {
		String[] args = {"delete", null};
		deleteTool = new DeleteTool(args);
		File workingDir = new File(System.getProperty("user.dir"));
		deleteTool.execute(workingDir, "");
		assertFalse(deleteTool.getStatusCode() == 0);
	}
}
