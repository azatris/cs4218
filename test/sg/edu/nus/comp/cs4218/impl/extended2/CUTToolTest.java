package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

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

	//Test cutSpecfiedCharacters method with valid range
	@Test
	public void cutSpecfiedCharactersListWithInRangeTest() {
		String list1 = "1,8-9,3-10";
		String input1 = "123456789012345";
		String output11 = "134567890";
		assertEquals(output11,cutTool.cutSpecfiedCharacters(list1,input1));

	}

	@Test
	public void cutSpecfiedCharactersListNoList(){
		String list =prop.getProperty("cutSpecfiedCharactersListNoListLIST");
		String input =prop.getProperty("cutSpecfiedCharactersListNoListINPUT");
		String actual= prop.getProperty("cutSpecfiedCharactersListNoListOUTPUT");
		String result = cutTool.cutSpecfiedCharacters(list, input);
		assertEquals("Not empty string", result, actual);	
		assertEquals("wrong status code", 67, cutTool.getStatusCode());
	}
	
	@Test
	public void cutSpecfiedCharactersListNoNummerList(){
		String list =prop.getProperty("cutSpecfiedCharactersListWrongListLIST");
		String input =prop.getProperty("cutSpecfiedCharactersListWrongLisINPUT");
		String actual= prop.getProperty("cutSpecfiedCharactersListWrongListOUTPUT");
		String result = cutTool.cutSpecfiedCharacters(list, input);
		assertEquals("Not empty string", result, actual);	
		assertEquals("wrong status code", 67, cutTool.getStatusCode());
	}

	//Test cutSpecfiedCharacters methodW out of range
	@Test
	public void cutSpecfiedCharactersListOurOfRangeTest() {
		String list1 = "1,8-9,3-16";
		String input1 = "123456789012345";
		String output11 = "13456789012345";
		assertEquals(output11,cutTool.cutSpecfiedCharacters(list1,input1));
	}


	//Test cutSpecifiedCharactersUseDelimiter method with valid range
	@Test
	public void cutSpecifiedCharactersUseDelimiterListWithInRangeTest(){
		String list1 = "1,8-9,3-15";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 3 4 5 6 7 8 9 10 11 12 13 14 15";		
		assertEquals(output1,cutTool.cutSpecifiedCharactersUseDelimiter(list1," ",input1));
		
	}

	//Test cutSpecifiedCharactersUseDelimiter method out of range
	@Test
	public void cutSpecifiedCharactersUseDelimiterListOutOfRangeTest(){
		String list1 = "1,8-9,3-100";
		String input1 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";
		String output1 = "1 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17";		
		assertEquals(output1,cutTool.cutSpecifiedCharactersUseDelimiter(list1," ",input1));	
	}

	

}
