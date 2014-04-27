package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.WrongParsingTool;

public class AddotionalPipeingToTest {


	private static Properties prop;
	private File testfile;
	static String pipeTestFile ="pipetestFile";
	String[] empty = new String[] {"cat", pipeTestFile};
	ITool cat = new CatTool(empty);
	ITool[] set = new ITool[2];
	PipingTool pipe;

	/**
	 * Test wrongparsingtool|echo 
	 */
	@Test
	public void pipeWrongParsingToolEcho(){
		ITool wrong = new WrongParsingTool(new String[]{"Parsing failed"});
		ITool[] set = new ITool[2];
		String[] a = new String[] {"echo", "pipe.properties"};
		ITool echo = new EchoTool(a);
		set[0]=wrong;
		set[1]=echo;
		pipe = new PipingTool(set);
		String actual = pipe.pipe(set[0], set[1]);
		assertEquals("Pipeing failed", "", actual);
		assertEquals("Statuscode", 98, pipe.getStatusCode());
	}
	
	/**
	 * Test wrongparsingtool|echo 
	 */
	@Test
	public void pipeEchoWrongParsingTool(){
		ITool wrong = new WrongParsingTool(new String[]{"Parsing failed"});
		ITool[] set = new ITool[2];
		String[] a = new String[] {"echo", "pipe.properties"};
		ITool echo = new EchoTool(a);
		set[1]=wrong;
		set[0]=echo;
		pipe = new PipingTool(set);
		String actual = pipe.pipe(set[0], set[1]);
		assertEquals("Pipeing failed", "", actual);
		assertEquals("Statuscode", 98, pipe.getStatusCode());
	}
	

	
	/**
	 * Test null|cat 
	 */
	@Test
	public void canPipeTwoITools(){
		ITool[] set = new ITool[2];
		String[] a = new String[] {"echo", "pipe.properties"};
		ITool echo = new EchoTool(a);
		set[1]=null;
		set[0]=echo;
		pipe = new PipingTool(set);
		String actual = pipe.pipe(set[0], set[1]);
		assertEquals("Pipeing failed", "", actual);
		assertEquals("Statuscode", 210, pipe.getStatusCode());
	}


}
