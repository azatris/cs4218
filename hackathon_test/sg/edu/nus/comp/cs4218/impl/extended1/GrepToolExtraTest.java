package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;

public class GrepToolExtraTest {

	public static final String TEST_FILE_NAME = "_test-file";
	
	@Before
	public void setUp() throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(TEST_FILE_NAME), "utf-8"));
	    writer.write("This is first line" + System.lineSeparator());
	    writer.write("This is second line" + System.lineSeparator());
	    writer.close();
	}
	
	@After
	public void tearDown() {
		File testFile = new File(TEST_FILE_NAME);
		if (testFile.exists()) {
			testFile.delete();
		}
	}
	
	@Test
	public void GrepExecuteNonExistingFile() {
		String[] args = {"grep", "query", "_non-existing-file"};
		ITool grepTool = new GrepTool(args);
		String result = grepTool.execute(new File(""), "");
		
		// The tool should handle non-existing file gracefully
		// instead of throwing NullPointerException.
				
		assertEquals("", result);
	}
	
	@Test
	public void GrepExecuteCountMatchingLinesFromFile() {
		String[] args = {"grep", "-c", "line", TEST_FILE_NAME};
		ITool grepTool = new GrepTool(args);
		
		int actual = Integer.valueOf(grepTool.execute(new File(""), ""));
		int expected = 2;
		
		// There are 2 lines containing the term 'line'
		// in the file called '_test-file'
		
		assertEquals(expected, actual);
	}
	
}
