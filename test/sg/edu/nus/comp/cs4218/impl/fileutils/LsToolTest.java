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

	@Test
	public void testGetFiles() throws IOException {
        List<File> files = lsTool.getFiles(new File(PARENTFOLDER));
        assertEquals("The number of files received is incorrect",
        		files.size(), 2);
	}

	@Test
	public void testGetStringForFiles() {
		List<File> files = lsTool.getFiles(new File(PARENTFOLDER));
		String stringForFiles = lsTool.getStringForFiles(files);
		assertTrue("The correct files/folders were not printed",
				stringForFiles.contains(CHILDFOLDER + File.separator) &&
				stringForFiles.contains(FILE));
	}
	
	@Test
	public void testExecuteWithStdIn() {
		lsToolForExecute = new LsTool(new String[]{"ls", "-"});
		lsToolForExecute.execute(Paths.get(".").toFile(), "dummy Data");
		assertNotEquals(lsToolForExecute.getStatusCode(), 0);
	}
	
	@Test
	public void testExecuteArguments0ReturnsNull() {
		
	}
	
	@Test
	public void testExecuteArguments0StatusCode() {
		
	}
	
	@Test
	public void testExecuteArguments1() {
		
	}
	
	@Test
	public void testExecuteArguments2() {
		
	}
	
	@Test
	public void testExecuteArgumentsMoreThan2() {
		
	}
	
	@Test
	public void testExecuteWithIncorrectTool() {

	}
	
	@Test
	public void testExecuteWithInvalidTool() {

	}
	
}
