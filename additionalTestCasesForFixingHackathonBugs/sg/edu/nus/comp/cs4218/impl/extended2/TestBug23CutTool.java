package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;

public class TestBug23CutTool {

	private ICutTool cutTool;
	private Properties prop;
	
	@Before
	public void before() {
		cutTool = new CutTool(new String[]{"cut"});
		prop = new Properties();
		try {
			prop.load(new FileInputStream("cut.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}

	@After
	public void after() {
		cutTool = null;
	}
	
	/**
	 * BUG_ID: 23
     * Fix location 1: Line 31, CutTool.Java 
     * Fix location 2: Line 130-140, CutTool.Java
	 */
	@Test
	public void cutExecuteManyLines(){
		String [] args = new String[4];
		args[0] = "cut";
		args[1] = "-c";
		args[2] = "1-3";
		args[3] = "-";
		CutTool cutTool = new CutTool(args);
		String output=cutTool.execute(new File("."), "ostsas\nostsas");
		assertEquals("wrong output", "ost\nost", output);
		assertEquals("wrong statuscode", 0, cutTool.getStatusCode());
	}
}
