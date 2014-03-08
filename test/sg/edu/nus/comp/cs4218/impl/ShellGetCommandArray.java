package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;

public class ShellGetCommandArray {

	private static Properties prop;
	private Shell shell;
	public ShellGetCommandArray(){
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

	
	/**
	 * Run a normal input
	 */
	@Test
	public void normal() {
		String[] actual = new String[]{"cat", "from.txt"};
		String input = prop.getProperty("normal");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
	/**
	 * Run with pattern as "pattern"
	 */
	
	@Test
	public void pattern() {
		String[] actual = new String[]{"grep", "Sleep", "-"};
		String input = prop.getProperty("pattern");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
/**
 * Many spaces don't work as intended
 */
	@Test
	public void manySpaces() {
		String[] actual = new String[]{"ls", "","", "Sleep", "-"};
		String input = prop.getProperty("manySpaces");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
	/**
	 * still parses if only a space is provided 
	 */
	@Test
	public void onlySpace() {
		String[] actual = new String[]{""};
		String input = prop.getProperty("onlySpace");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}

	/**
	 * show that Many files no problem 
	 */
	@Test
	public void manyFiles() {
		String[] actual = new String[]{"cat", "file.txt", "file.c"};
		String input = prop.getProperty("manyFiles");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
	/**
	 * PAttern in not grep 
	 */
	@Test
	public void quetionMarkNotInGrep() {
		String[] actual = new String[]{"Parsing failed"};
		String input = prop.getProperty("quetionMark");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
	/**
	 * Space in filename is allowed 
	 */
	@Test
	public void spaceInFilename() {
		String[] actual = new String[]{"cat", "hello", "world.txt"};
		String input = prop.getProperty("spaceInFilename");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
	/**
	 * Space in grep quetions allowed in grep; 
	 */
	@Test
	public void grepWithQuetion() {
		String[] actual = new String[]{"grep", "-c", "3","\"Hallo\"", "file.txt"};
		String input = prop.getProperty("grepWithQuetion");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
	/**
	 * ( ") can't be in the parser  
	 */
	@Test
	public void spaceQuoteInGrep() {
		String[] actual = new String[]{"Parsing failed"};
		String input = prop.getProperty("spaceQuoteInGrep");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
	/**
	 * (" ) can't be in the parser  
	 */
	@Test
	public void quoteSpaceInGrep() {
		String[] actual = new String[]{"Parsing failed"};
		String input = prop.getProperty("quoteSpaceInGrep");
		String[] result = shell.getCommandArray(input);
		assertArrayEquals("parsing went wrong", result, actual);
	}
	
	
}
