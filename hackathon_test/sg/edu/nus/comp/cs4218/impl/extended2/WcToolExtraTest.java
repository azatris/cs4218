package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;

public class WcToolExtraTest {
	private IWcTool wcTool;
	private File file1, file2, dir;
	private String helpContent;

	
	@Before
	public void setUp() throws Exception {
		BufferedWriter bw;
		String content;

		// create files with content
		file1 = new File("text1");
		file1.createNewFile();
		content = "this file contains 2 newlines, 17 words and 99 characters"+System.lineSeparator();
		content += "this is text1" + System.lineSeparator();
		content += "created only for wctooltest";
		bw = new BufferedWriter(new FileWriter(file1));
		bw.write(content);
		bw.close();

		file2 = new File("text2");
		file2.createNewFile();
		content = "this file contains 4 newlines, 16 words and 88 characters"+System.lineSeparator();
		content += "testing testing 1 2 3"+System.lineSeparator();
		content += " file!"+System.lineSeparator()+System.lineSeparator();
		bw = new BufferedWriter(new FileWriter(file2));
		bw.write(content);
		bw.close();

		// create directory
		dir = new File("dir");
		dir.mkdir();

		// initialize actual content in help file for wc -help option
		// helpContent = new String(Files.readAllBytes(new File("help_files/wc_help").toPath())).trim();

	}

	@Before
	public void before() {
		wcTool = new WcTool(null);
	}

	@After
	public void after() {
		wcTool = null;
	}

	@After
	public void tearDown() throws Exception {
		file1.delete();
		file2.delete();
		dir.delete();
		helpContent = null;
	}
	
	@Test
	public void getWordCount_EmptyString_Return0() {
		String input = "";
		assertEquals("0", wcTool.getWordCount(input));
	}
	
	@Test
	public void getNewLineCount_Null_Return0() {
		String input = null;
		assertEquals("0", wcTool.getNewLineCount(input));
	}
	
	@Test
	public void execute_MultipleFiles_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"wc","text1", "text2"});
		wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
	}
	
	@Test
	public void execute_HelpOptionFile_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"wc","-m", "-help", "text2"});
		wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
	}
	
	@Test
	public void execute_MOptionFile_GetStatusCode0() {
		wcTool = new WcTool(new String[]{"wc","-m", "text1"});
		wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(0, wcTool.getStatusCode());
	}
	
	@Test
	public void execute_NullArguments_GetStatusCode2() {
		wcTool = new WcTool(null);
		wcTool.execute(new File(System.getProperty("user.dir")), null);
		assertTrue(wcTool.getStatusCode() != 0);
	}
}
