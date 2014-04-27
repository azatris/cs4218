package sg.edu.nus.comp.cs4218.common;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AdditionalCommonTest {
	private File fileToWrite;
	
	@Before
	public void setUp() throws Exception {
		fileToWrite = File.createTempFile("common", "");
	}

	@After
	public void tearDown() throws Exception {
		fileToWrite.delete();
	}

	@Test
	public void concatenateNull() {
		assertEquals("", Common.concatenateDirectory(null, ""));
	}
	
	@Test
	public void concatenatePathWithDotDot() {
		assertEquals(Paths.get(System.getProperty("user.dir")).getParent().toString() + File.separator, Common.concatenateDirectory(System.getProperty("user.dir"), ".."));
	}
	
	@Test
	public void writeFileTestt() throws IOException {
		Common.writeFile(fileToWrite, "Test writing to file");
		assertEquals("Test writing to file", Common.readFileByChar(fileToWrite));
	}
}
