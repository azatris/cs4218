package sg.edu.nus.comp.cs4218.privatemetod;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.privatemetod.CutToolP;

public class CutToolPTest {
	private CutToolP cutTool;
	private Properties prop;
	@Before
	public void before() {
		cutTool = new CutToolP(new String[]{"cut"});
		prop = new Properties();
		try {
			prop.load(new FileInputStream("cutp.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}		

	@Test
	public void parseString() {
		String input = prop.getProperty("parseString1Input");
	    LinkedList<Integer> oracel = new LinkedList<Integer>();
		oracel.add(23);
		assertEquals("wrong thingexpected", oracel, cutTool.parseString(input));
	}
	
	@Test
	public void parseString2() {
		String input = prop.getProperty("parseString2Input");
	    LinkedList<Integer> oracel = new LinkedList<Integer>();
		oracel.add(23);
		oracel.add(24);
		oracel.add(25);
		assertEquals("wrong thingexpected", oracel, cutTool.parseString(input));
	}

	@Test
	public void parseStringbad() {
		String input = prop.getProperty("parseStringbad");
	    LinkedList<Integer> oracel = new LinkedList<Integer>();
		assertEquals("wrong thingexpected", oracel, cutTool.parseString(input));
		assertEquals("Wrong statuscode",67,cutTool.getStatusCode());
	}

	
	@Test
	public void parseStringbad2() {
		String input = prop.getProperty("parseStringbad");
	    LinkedList<Integer> oracel = new LinkedList<Integer>();
		assertEquals("wrong thingexpected", oracel, cutTool.parseString(input));
		assertEquals("Wrong statuscode",67,cutTool.getStatusCode());
	}
	
	@Test
	public void parseSingelNumber() {
		String input = prop.getProperty("parseSingelNumber1");
		Integer ost = 234;
		assertEquals("wrong thingexpected", ost, cutTool.parseSingelNumber(input));
		assertEquals("Wrong statuscode", 0 ,cutTool.getStatusCode());
	}

	@Test
	public void parseSingelNumberbad() {
		String input = prop.getProperty("parseSingelNumberbad");
		Integer ost = -1;
		assertEquals("wrong thingexpected", ost, cutTool.parseSingelNumber(input));
		assertEquals("Wrong statuscode", 67 ,cutTool.getStatusCode());
	}
}
