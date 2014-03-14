package sg.edu.nus.comp.cs4218.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.extended1.*;
import sg.edu.nus.comp.cs4218.impl.extended2.*;
import sg.edu.nus.comp.cs4218.impl.fileutils.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileUtilsPipeGrepTest {
	private ITool tool;
	private GrepTool grepTool;
	private PipingTool pipeTool;
	private static String testDataFileName;
	private static String testData;
	private static Properties prop;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Loading the test data from the properties file.
		prop = new Properties();
		try {
			prop.load(new FileInputStream("test.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		testDataFileName = prop.getProperty("testDataFileName");
		testData = prop.getProperty("testData");
		// Creating a file to store the test data.
		File testfile = new File(testDataFileName);
		if (!testfile.isFile()) {
			PrintWriter writer = new PrintWriter(testDataFileName, "UTF-8");
			writer.println(testData);
			writer.close();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Path testFile = Paths.get(testDataFileName);
		if (testFile.toFile().exists()) {
			Files.delete(Paths.get(testDataFileName));	
		}
	}

	@Before
	public void setUp() throws Exception {
		grepTool = new GrepTool(new String[]{"grep", ".*", "-"});
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests cat tool with pipe and grep
	 */
	@Test
	public void testCat() {
		tool = new CatTool(new String[]{"cat", testDataFileName});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result = pipeTool.execute(Paths.get(".").toFile(), null);
		assertEquals("Cat pipe grep result incorrect", testData, result);
	}
	
	/**
	 * Tests cd tool with pipe and grep
	 */
	@Test
	public void testCs() {
		tool = new CdTool(new String[]{"cd", "."});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result = pipeTool.execute(Paths.get(".").toFile(), null);
		assertEquals("Cd pipe grep result incorrect", "", result);
	}
	
	/**
	 * Tests copy tool with pipe and grep
	 * @throws IOException 
	 */
	@Test
	public void testCopy() throws IOException {
		String destFile = "copyToolDestinationFile.txt";
		tool = new CopyTool(new String[]{"copy", testDataFileName, destFile});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result = pipeTool.execute(Paths.get(".").toFile(), null);
		Files.delete(Paths.get(destFile));
		assertEquals("Copy pipe grep result incorrect", "", result);
	}
	
	/**
	 * Tests delete tool with pipe and grep
	 */
	@Test
	public void testDelete() {
		tool = new DeleteTool(new String[]{"delete", testDataFileName});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result = pipeTool.execute(Paths.get(".").toFile(), null);
		assertEquals("Delete pipe grep result incorrect", "", result);
	}
	
	/**
	 * Tests echo tool with pipe and grep
	 */
	@Test
	public void testEcho() {
		tool = new EchoTool(new String[]{"echo", "abracadabra"});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result = pipeTool.execute(Paths.get(".").toFile(), null);
		assertEquals("Echo pipe grep result incorrect", "abracadabra", result);
	}
	
	/**
	 * Tests ls tool with pipe and grep
	 * @throws IOException 
	 */
	@Test
	public void testLs() throws IOException {		
		final String PARENTFOLDER = "tempParentFolder";
		final String CHILDFOLDER = "tempChildFolder";
		final String FILE = "tempFile.txt";
		final Path PATH = Paths.get(PARENTFOLDER + File.separator + CHILDFOLDER);
		final Path PATHTOFILE = Paths.get(PARENTFOLDER + File.separator + FILE);
		
		tool = new LsTool(new String[]{"ls", PARENTFOLDER});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		
        Files.createDirectories(PATH);
        try {
            Files.createFile(PATHTOFILE);
        } catch (FileAlreadyExistsException e) {
            System.err.println("File already exists: " + e.getMessage()); // NOPMD
        }
		
		String result = pipeTool.execute(Paths.get(".").toFile(), null);
		
        Files.delete(PATHTOFILE);
        Files.delete(PATH);
        Files.delete(Paths.get(PARENTFOLDER));
        
		assertTrue("Ls pipe grep result incorrect", 
				result.contains(CHILDFOLDER + File.separator) &&
				result.contains(FILE));
	}
	
	/**
	 * Tests move tool with pipe and grep
	 * @throws IOException 
	 */
	@Test
	public void testMove() throws IOException {
		String destFile = "moveTestFile.txt";
		tool = new MoveTool(new String[]{"move", testDataFileName, destFile});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result = pipeTool.execute(Paths.get(".").toFile(), null);
		Files.delete(Paths.get(destFile));
		assertEquals("Move pipe grep result incorrect", "", result);
	}
	
	/**
	 * Tests pwd tool with pipe and grep
	 */
	@Test
	public void testPwd() {
		tool = new PWDTool(new String[]{"pwd"});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result = pipeTool.execute(new  File (System.getProperty("user.dir")), null);
		assertEquals("Pwd pipe grep result incorrect", System.getProperty("user.dir"), result);
	}	
	
	/**
	 * Tests cat tool with pipe and grep with invalid number of arguments for Cat
	 */
	@Test
	public void testCatInvalidArguments() {
		tool = new CatTool(new String[]{"cat"});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result =pipeTool.execute(Paths.get(".").toFile(), null);
		assertEquals("Output not empty string", "", result);
		assertNotEquals(0, pipeTool.getStatusCode());
	}	

	/**
	 * Tests cat tool with pipe and grep with invalid number of arguments for Cat
	 */
	@Test
	public void testCatInvalidArguments2() {
		tool = new CutTool(new String[]{"NOT A TOOL"," -"});
		pipeTool = new PipingTool(new ITool[]{tool, grepTool});
		String result=pipeTool.execute(Paths.get(".").toFile(), null);
		assertEquals("Output not empty string", "", result);
		assertNotEquals(0, pipeTool.getStatusCode());
	}
	
}
