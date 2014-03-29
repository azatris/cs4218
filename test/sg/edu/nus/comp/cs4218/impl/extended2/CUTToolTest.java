package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;

public class CUTToolTest {
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
	 * Test cutSpecfiedCharacters method with valid range
	 */
	@Test
	public void cutSpecfiedCharactersListWithInRangeTest() {
		String list1 = "1,8-9,3-10";
		String input1 = "123456789012345";
		String output11 = "134567890";
		assertEquals(output11,cutTool.cutSpecfiedCharacters(list1,input1));

	}
	
	@Test
	public void cutWrongTool(){
		cutTool = new CutTool(new String[]{"fail"});
		assertEquals("They was strange", "", cutTool.execute(new File("."),"asd"));
		assertEquals("Equals", 127, cutTool.getStatusCode());
	}

	/**
	 * Test shows that statuscode 67 is returned if no list is provided to CutSpecfiedcharacter
	 */
	@Test
	public void cutSpecfiedCharactersListNoList(){
		String list =prop.getProperty("cutSpecfiedCharactersListNoListLIST");
		String input =prop.getProperty("cutSpecfiedCharactersListNoListINPUT");
		String actual= prop.getProperty("cutSpecfiedCharactersListNoListOUTPUT");
		String result = cutTool.cutSpecfiedCharacters(list, input);
		assertEquals("Not empty string", result, actual);	
		assertEquals("wrong status code", 67, cutTool.getStatusCode());
	}
	/**
	 * Test shows that statuscode 67 is returned if the list isn't on this format
	 * ([d]*[d1-d2],)+ where d,d1,d2 is a digit. 
	 */
	@Test
	public void cutSpecfiedCharactersListNoNummerList(){
		String list =prop.getProperty("cutSpecfiedCharactersListWrongListLIST");
		String input =prop.getProperty("cutSpecfiedCharactersListWrongLisINPUT");
		String actual= prop.getProperty("cutSpecfiedCharactersListWrongListOUTPUT");
		String result = cutTool.cutSpecfiedCharacters(list, input);
		assertEquals("Not empty string", result, actual);	
		assertEquals("wrong status code", 67, cutTool.getStatusCode());
	}

	/**
	 * Test cutSpecfiedCharacters methodW out of range
	 */
	@Test
	public void cutSpecfiedCharactersListOurOfRangeTest() {
		String list1 = "1,8-9,3-16";
		String input1 = "123456789012345";
		String output11 = "13456789012345";
		assertEquals(output11,cutTool.cutSpecfiedCharacters(list1,input1));
	}


	/**
	 * Test cutSpecifiedCharactersUseDelimiter method with valid range
	 */
	@Test
	public void cutSpecifiedCharactersUseDelimiterListWithInRangeTest(){
		String list1 = "1,8-9,3-15";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 3 4 5 6 7 8 9 10 11 12 13 14 15";		
		assertEquals(output1,cutTool.cutSpecifiedCharactersUseDelimiter(list1," ",input1));
		
	}

	/**
	 * Test cutSpecifiedCharactersUseDelimiter method out of range
	 */
	@Test
	public void cutSpecifiedCharactersUseDelimiterListOutOfRangeTest(){
		String list1 = "1,8-9,3-100";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";		
		assertEquals(output1,cutTool.cutSpecifiedCharactersUseDelimiter(list1," ",input1));	
	}

	/**
	 * Testing execute with working input.
	 */
	@Test
	public void cutExecuteInput(){
		String [] args = new String[4];
		args[0] = "cut";
		args[1] = prop.getProperty("cutEarg1");
		args[2] = prop.getProperty("cutEarg2");
		args[3] = "-";
		cutTool = new CutTool(args);
		String output=cutTool.execute(new File("."), prop.getProperty("input"));
		assertEquals("wrong output", prop.getProperty("output"), output);
	}
	
	/**
	 * Testing execute with inaccurate input.
	 */	
	@Test
	public void cutExecuteBadInput(){
		String [] args = new String[4];
		args[0] = "cut";
		args[1] = prop.getProperty("cutEBarg1");
		args[2] = prop.getProperty("cutEBarg2");
		args[3] = "-";
		cutTool = new CutTool(args);
		String output=cutTool.execute(new File("."), prop.getProperty("Binput"));
		assertEquals("wrong output", prop.getProperty("Boutput"), output);
		assertEquals("wrong statuscode", 127, cutTool.getStatusCode());
	}
	/**
	 * Showes that regex specilla carecters is handel
	 */
	@Test
	public void cutExecuteRegex(){
		String [] args = new String[6];
		args[0] = "cut";
		args[1] = prop.getProperty("cutEReg1");
		args[2] = prop.getProperty("cutEReg2");
		args[3] = prop.getProperty("cutEReg3");
		args[4] = prop.getProperty("cutEReg4");
		args[5] = "-";
		cutTool = new CutTool(args);
		String output=cutTool.execute(new File("."), prop.getProperty("cutERegIn"));
		assertEquals("wrong output", prop.getProperty("cutERegOut"), output);
		assertEquals("Wrong statuscode", 0, cutTool.getStatusCode());
	}
	@Test
	public void cutBadDelim(){
		String [] args = new String[6];
		args[0] = "cut";
		args[1] = prop.getProperty("cutEBD1");
		args[2] = prop.getProperty("cutEBD2");
		args[3] = prop.getProperty("cutEBD3");
		args[4] = prop.getProperty("cutEBd4");
		args[5] = "-";
		cutTool = new CutTool(args);
		String output=cutTool.execute(new File("."), prop.getProperty("cutEBDIn"));
		assertEquals("wrong output", prop.getProperty("cutEBDOut"), output);
		assertNotEquals("Wrong statuscode", 0, cutTool.getStatusCode());
	}
	/**
	 * Test what happens with empty string 
	 */
	@Test
	public void cutDelimEmptyString(){
		String [] args = new String[6];
		args[0] = "cut";
		args[1] = prop.getProperty("cutEES1");
		args[2] = prop.getProperty("cutEES2");
		args[3] = prop.getProperty("cutEES3");
		args[4] = "";
		args[5] = "-";
		cutTool = new CutTool(args);
		String output=cutTool.execute(new File("."), prop.getProperty("cutEESIn"));
		assertEquals("wrong output", prop.getProperty("cutEESOut"), output);
		assertEquals("Wrong statuscode", 0, cutTool.getStatusCode());
	}
	/**
	 * Testing execute with inaccurate input.
	 */
	@Test
	public void cutExecuteBad2Input(){
		String [] args = new String[4];
		args[0] = "cut";
		args[1] = prop.getProperty("cutEB1arg1");
		args[2] = prop.getProperty("cutEB1arg2");
		args[3] = "-";
		cutTool = new CutTool(args);
		String output=cutTool.execute(new File("."), prop.getProperty("B1input"));
		assertEquals("wrong output", prop.getProperty("B1output"), output);
		assertEquals("wrong statuscode", 67, cutTool.getStatusCode());
	}
	
	/**
	 * Testing getHelp.
	 */
	@Test
	public void cutGetHelp(){
		String oracel = prop.getProperty("cutHelp");
		String output =cutTool.getHelp();
		assertEquals("HELP MESSAGE WRONG",oracel, output);
	}
}
