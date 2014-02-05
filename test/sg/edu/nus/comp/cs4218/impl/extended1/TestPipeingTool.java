package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;

public class TestPipeingTool {

	private static Properties prop;
	private File testfile;
	static String pipeTestFile ="pipetestFile";
	String[] empty = new String[] {"cat", pipeTestFile};
	ITool cat = new CatTool(empty);
	ITool[] set = new ITool[2];
	PipingTool pipe;
	public TestPipeingTool(){
		
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		prop = new Properties();
		try {
			prop.load(new FileInputStream("parsing.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		File testfile = new File(pipeTestFile);
		if (!testfile.isFile()) {
		PrintWriter writer = new PrintWriter(pipeTestFile, "UTF-8");
		writer.println(prop.getProperty("cat"));
		writer.close();
		}
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Files.delete(Paths.get(pipeTestFile));
	}
	
	@Before
	public void setup(){
	}
	@Test
	public void cattest(){
		String[] empty = new String[] {"cat", "-"};
		ITool cat = new CatTool(empty);
		ITool[] set = new ITool[2];
		String[] a = new String[] {"echo", "pipe.properties"};
		ITool echo = new EchoTool(a);
		set[1]=cat;
		String[] tmp = new String[] {"grep","hallo","-"};
		ITool grep = new GrepTool(tmp);
		set[0]=echo;
		pipe = new PipingTool(set);
		System.out.println("echo\n" + echo.execute(new File(System.getProperty("user.dir")),null));
		System.out.println("cat\n" + cat.execute(new File(System.getProperty("user.dir")),"pipe.properties"));
		String LSD = pipe.pipe(set[0], set[1]);
		System.out.println("result\n" + LSD);
		assertEquals("hej", "", "asd");	
	}
	
	
	
}
