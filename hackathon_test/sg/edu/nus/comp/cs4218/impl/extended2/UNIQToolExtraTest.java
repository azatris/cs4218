package sg.edu.nus.comp.cs4218.impl.extended2;

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

public class UNIQToolExtraTest {

	public static final String TEST_FILE_NAME = "_test-file";
	
	@Before
	public void setUp() throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(TEST_FILE_NAME), "utf-8"));
	    writer.write("w1 w2 w3" + System.lineSeparator());
	    writer.write("w1  w2 w3" + System.lineSeparator());
	    writer.close();
	}
	
	@After
	public void tearDown() {
		File testFile = new File(TEST_FILE_NAME);
		if (testFile.exists()) {
			testFile.delete();
		}
	}
	
	/**
	 * This test case is wrong, it violates the project specification
	 */
//	@Test
//	public void uniqExecuteGetUniqueSkipNumMultipleWhitespacesTest() {
//		String[] args = {"uniq", "-f", "2", TEST_FILE_NAME};
//		ITool uniqTool = new UniqTool(args);
//		String expected = "w1 w2 w3";
//		String actual = uniqTool.execute(new File(""), "");
//		assertEquals(expected, actual);
//	}
}
