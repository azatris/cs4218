package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LsToolTest {
	private transient LsTool lsTool;
	private LsTool lsToolForExecute;
	final private static String PARENTFOLDER = "tempParentFolder";
	final private static String CHILDFOLDER = "tempChildFolder";
	final private static String FILE = "tempFile.txt";
	final private static Path PATH = Paths.get(PARENTFOLDER + File.separator + CHILDFOLDER);
	final private static Path PATHTOFILE = Paths.get(PARENTFOLDER + File.separator + FILE);
	
	// Creates the mock folders and file.
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
        Files.createDirectories(PATH);
        try {
            Files.createFile(PATHTOFILE);
        } catch (FileAlreadyExistsException e) {
            System.err.println("File already exists: " + e.getMessage()); // NOPMD
        }
	}
	
	@Before
	public void setUp() {
		lsTool = new LsTool(new String[]{"ls", "."});
	}
	
	// Removes the created mock folders and file.
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
        Files.delete(PATHTOFILE);
        Files.delete(PATH);
        Files.delete(Paths.get(PARENTFOLDER));
	}

	/**
	 * Tests the helper method for getting the file list
	 * @throws IOException
	 */
	@Test
	public void testGetFiles() throws IOException {
        List<File> files = lsTool.getFiles(new File(PARENTFOLDER));
        assertEquals("The number of files received is incorrect",
        		files.size(), 2);
	}

	/**
	 * Tests the human-readable form of the file list received
	 */
	@Test
	public void testGetStringForFiles() {
		List<File> files = lsTool.getFiles(new File(PARENTFOLDER));
		String stringForFiles = lsTool.getStringForFiles(files);
		assertTrue("The correct files/folders were not printed",
				stringForFiles.contains(CHILDFOLDER + File.separator) &&
				stringForFiles.contains(FILE));
	}
	
	/**
	 * Tests the execute method with stdin (from pipe)
	 */
	@Test
	public void testExecuteWithStdIn() {
		lsToolForExecute = new LsTool(new String[]{"ls", "-"});
		lsToolForExecute.execute(Paths.get(".").toFile(), "dummy Data");
		assertNotEquals(lsToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 * Tests ls tool execute method with 0 arguments
	 */
	@Test
	public void testExecuteArguments0ReturnsNull() {
		lsToolForExecute = new LsTool(new String[]{});
		String stringForFiles = lsToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNull(stringForFiles);
	}
	
	/**
	 * Tests the status code of ls tool execute method with 0 arguments
	 */
	@Test
	public void testExecuteArguments0StatusCode() {
		lsToolForExecute = new LsTool(new String[]{});
		lsToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals(lsToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 * Tests ls tool execute method with 1 argument
	 */
	@Test
	public void testExecuteArguments1() {
		lsToolForExecute = new LsTool(new String[]{"ls"});
		String stringForFiles = lsToolForExecute.execute(Paths.get(PARENTFOLDER).toFile(), null);
		assertTrue("The correct files/folders were not printed",
				stringForFiles.contains(CHILDFOLDER + File.separator) &&
				stringForFiles.contains(FILE));
	}
	
	/**
	 * Tests ls tool execute method with 2 arguments
	 */
	@Test
	public void testExecuteArguments2() {
		lsToolForExecute = new LsTool(new String[]{"ls", PARENTFOLDER});
		String stringForFiles = lsToolForExecute.execute(Paths.get(".").toFile(), null);
		assertTrue("The correct files/folders were not printed",
				stringForFiles.contains(CHILDFOLDER + File.separator) &&
				stringForFiles.contains(FILE));
	}

	/**
	 * Tests ls tool execute method status code with more than 2 arguments
	 */
	@Test
	public void testExecuteArgumentsMoreThan2StatusCode() {
		lsToolForExecute = new LsTool(new String[]{"ls", PARENTFOLDER, "dummyArgument"});
		lsToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals(lsToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 * Tests ls tool execute method with an incorrect tool
	 */
	@Test
	public void testExecuteWithIncorrectTool() {
		lsToolForExecute = new LsTool(new String[]{"cat", PARENTFOLDER});
		lsToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals(lsToolForExecute.getStatusCode(), 0);
	}
	
	/**
	 * Tests ls tool execute method with an invalid tool
	 */
	@Test
	public void testExecuteWithInvalidTool() {
		lsToolForExecute = new LsTool(new String[]{"dog", PARENTFOLDER});
		lsToolForExecute.execute(Paths.get(".").toFile(), null);
		assertNotEquals(lsToolForExecute.getStatusCode(), 0);
	}
	
}
