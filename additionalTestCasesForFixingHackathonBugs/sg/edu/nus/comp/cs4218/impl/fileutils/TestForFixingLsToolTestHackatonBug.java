package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestForFixingLsToolTestHackatonBug {
	private transient LsTool lsTool;
	private LsTool lsToolForExecute;
	final private static String PARENTFOLDER = "tempParentFolder";
	final private static String CHILDFOLDER = "tempChildFolder";
	final private static String FILE = "tempFile.txt";
	final private static Path PATH = Paths.get(PARENTFOLDER + File.separator + CHILDFOLDER);
	final private static Path PATHTOFILE = Paths.get(PARENTFOLDER + File.separator + FILE);
	private static final Object DIRECTORY_ERROR_MSG = "Invalid directory should be handled.";
	
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
	 * BUG_ID: 9
	 * Fix location 1: Line 26-30, CommTool.java
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
