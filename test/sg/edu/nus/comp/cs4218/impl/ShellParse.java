package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.hamcrest.core.IsEqual;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;

public class ShellParse {

	private static Properties prop;
	private Shell shell;
	public ShellParse(){
		shell = new Shell();
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		prop = new Properties();
		try {
			prop.load(new FileInputStream("parsing.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test
	public void normal() {
		String args[] = new String[]{"cat", "from.txt"};
		ITool actual = new CatTool(args);
		String input = prop.getProperty("normal");
		ITool result = shell.parse(input);
		assertEquals("NOT THE SAME", actual, result);
	}
	
	/**
	 * as equals is define it can't find diffrence in what 
	 * called the value
	 */
	@Test
	public void wrongTypOfRunner() {
		String args[] = new String[]{"cat", "from.txt"};
		ITool actual = new LsTool(args);
		String input = prop.getProperty("normal");
		ITool result = shell.parse(input);
		assertEquals("NOT THE SAME", actual, result);
	}
	/**
	 * wrong input should be diffrent 
	 */
	@Test
	public void Wrongnormal() {
		String args[] = new String[]{"cat", "asd.txt"};
		ITool actual = new CatTool(args);
		String input = prop.getProperty("normal");
		ITool result = shell.parse(input);
		assertNotEquals("NOT THE SAME", actual, result);
	}
	
	// TODO
	@Test
	public void pattern() {
		String[] args = new String[]{"grep", "Sleep", "-"};
		ITool actual = new GrepTool(args);
		String input = prop.getProperty("pattern");
		ITool result = shell.parse(input);
		assertEquals("NOT THE SAME", actual, result);
	}
	
	// TODO
	@Test
	public void manySpaces() {
		String[] args = new String[]{"ls", "","", "Sleep", "-"};	
		ITool actual = new CatTool(args);
		String input = prop.getProperty("manySpaces");
		ITool result = shell.parse(input);
		assertEquals("NOT THE SAME", actual, result);
	}
	
	// TODO
	@Test
	public void onlySpace() {
		ITool actual = null;
		String input = prop.getProperty("onlySpace");
		ITool result = shell.parse(input);
		assertEquals("NOT THE SAME", actual, result);
	}
	
	// TODO
	@Test 
	public void pipe() {
		String[] ettA = new String[]{"ls"};
		String[] ettB = new String[]{"cat","txt.s"};
		ITool[] args = new ITool[]{new LsTool(ettA), new CatTool(ettB)};
		PipingTool actual = new PipingTool(args);
		String input = prop.getProperty("pipe");
		PipingTool result = (PipingTool) shell.parse(input);
		assertEquals("NOT THE SAME", actual, result);
	}
	
	// TODO
	@Test 
	public void pipe2() {
		String[] twoA = new String[]{"ls"};
		String[] twoB = new String[]{"cat","txt.s"};
		String[] twoc = new String[]{"ls"};
		ITool[] args = new ITool[]{new LsTool(twoA), new CatTool(twoB)
			, new LsTool(twoc)};
		PipingTool actual = new PipingTool(args);
		String input = prop.getProperty("pipe2");
		PipingTool result = (PipingTool) shell.parse(input);
		assertEquals("NOT THE SAME", actual, result);
	}
	
	// TODO
	@Test 
	public void pipeNotSame() {
		String[] twoA = new String[]{"ls"};
		String[] twoB = new String[]{"hallo","txt.s"};
		String[] twoc = new String[]{"ls"};
		ITool[] args = new ITool[]{new LsTool(twoA), new CatTool(twoB)
			, new LsTool(twoc)};
		PipingTool actual = new PipingTool(args);
		String input = prop.getProperty("pipe2");
		PipingTool result = (PipingTool) shell.parse(input);
		assertNotEquals("NOT THE SAME", actual, result);
	}
	
	// TODO
	@Test 
	public void pipewithpipeinpattern() {
		String[] twoA = new String[]{"ls"};
		String[] twoc = new String[]{"grep", "|", "file.txt"};
		ITool[] args = new ITool[]{new LsTool(twoA), new GrepTool(twoc)};
		PipingTool actual = new PipingTool(args);
		String input = prop.getProperty("pipewitinpattern");
		PipingTool result = (PipingTool) shell.parse(input);
		assertEquals("NOT THE SAME", actual, result);
	}
	
	// TODO
	@Test
	public void patternSpacePipeSpace() {
		String[] twoA = new String[]{"ls"};
		String[] twoB = new String[]{"cat","txt.s"};
		ITool[] args = new ITool[]{new LsTool(twoA), new CatTool(twoB)};
		PipingTool actual = new PipingTool(args);
		String input = prop.getProperty("patternSpacePipeSpace");
		PipingTool result = (PipingTool) shell.parse(input);
		assertNotEquals("NOT THE SAME", actual, result);
	}
	
}
