package sg.edu.nus.comp.cs4218.integration;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

public class CommToolIntegrationTest {
	private static final String USER_DIRECTORY = "user.dir";
	private static File defaultWorkingDirectory;

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		defaultWorkingDirectory = new File(System.getProperty(USER_DIRECTORY));
	}

	private void createNewTestFile(String fileName, String content)
			throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(content);
		writer.close();
		writer = null;
		System.gc();
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	private void deleteTestFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}

	@Before
	public void setUp() throws IOException {
		/*
		 * sortfile1 content aaa aab aab bba cca ccc
		 */
		createNewTestFile(
				"sortfile1",
				"aaa" + System.lineSeparator() + "aab" + System.lineSeparator()
						+ "aab" + System.lineSeparator() + "bba"
						+ System.lineSeparator() + "cca"
						+ System.lineSeparator() + "ccc");

		/*
		 * sortfile2 content aaa bbb
		 */
		createNewTestFile("sortfile2", "aaa" + System.lineSeparator() + "bbb");
	}

	@After
	public void tearDown() {
		deleteTestFile("sortfile1");
		deleteTestFile("sortfile2");
	}
		
	@Test
	public void CommThenEcho_Output(){
		String[] commArguments = {"comm", "sortfile1", "sortfile2" };
		ITool commTool = new CommTool(commArguments);
		String intermediateResult = commTool.execute(defaultWorkingDirectory,
				null);

		String[] echoArguments = {"echo", intermediateResult};
		ITool echoTool = new EchoTool(echoArguments);
		
		assertEquals("aaa" + System.lineSeparator(),
				echoTool.execute(defaultWorkingDirectory, null));
	}
	// TODO: To check with other team mate how they do negative testing for cat
	// and echo that can break both tools.
}
