package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;

public class CdToolTest {
	private ICdTool cdTool;
	private File workingDir = null;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		cdTool = null;
	}

	/**
	 *  Testing File changeDirectory(String newDirectory)
	 */
	@Test
	public void changeToEmptyStringTest(){
		String args[] = {"cd", ""};
		cdTool = new CdTool(args);
		assertNull(cdTool.changeDirectory(""));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	// TODO
	@Test
	public void changeToFileTest() throws IOException {
		File fileToCd = File.createTempFile("exists", "cdtmp");
		String args[] = {"cd", fileToCd.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		assertTrue(fileToCd.exists());
		assertNull(cdTool.changeDirectory(fileToCd.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
		
		fileToCd.delete();
	}
	
	// TODO
	@Test
	public void changeToNonExistingFileTest() throws IOException {
		File fileToCd = File.createTempFile("nonExists", "cdtmp");
		String args[] = {"cd", fileToCd.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		fileToCd.delete();
		assertFalse(fileToCd.exists());
		assertNull(cdTool.changeDirectory(fileToCd.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	// TODO
	@Test
	public void changeToDirectoryTest() throws IOException {
		File dirToCd = Files.createTempDirectory("cdtmpfolder").toFile();
		String args[] = {"cd", dirToCd.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		assertTrue(dirToCd.exists());
		assertEquals(dirToCd, cdTool.changeDirectory(dirToCd.getAbsolutePath()));
		assertEquals(55, cdTool.getStatusCode());
		
		dirToCd.delete();
	}
	
	// TODO
	@Test
	public void changeToNonExistingDirectoryTest() throws IOException {
		File dirToCd = Files.createTempDirectory("cdtmpfolder").toFile();
		String args[] = {"cd", dirToCd.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		dirToCd.delete();
		assertFalse(dirToCd.exists());
		assertNull(cdTool.changeDirectory(dirToCd.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	// TODO
	@Test
	public void changeToNullDirectoryTest() {
		String args[] = {"cd", null};
		cdTool = new CdTool(args);
		assertNull(cdTool.changeDirectory(null));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	/** Test String execute(File workingDir, String stdin) 
	 * 	Test Home ~
	 */
	@Test
	public void cdHomeTest() {
		workingDir = new File(System.getProperty("user.dir"));
		String[] args = {"cd", "~"};
		cdTool = new CdTool(args);
		
		assertTrue(System.getProperty("user.dir").equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
	}
	
	/**
	 * Test absolute file path
	 * @throws IOException
	 */
	@Test
	public void cdAbsolutePathFileTest() throws IOException {
		workingDir = new File(System.getProperty("user.dir"));
		File fileToCd = File.createTempFile("file", "cdtmp"); 
		String[] args = {"cd", fileToCd.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		assertTrue(workingDir.getAbsolutePath().equals(cdTool.execute(workingDir, null)));
		assertTrue(cdTool.getStatusCode() != 55);
		fileToCd.delete();
	}
	
	/**
	 * Test absolute folder path
	 * @throws IOException
	 */
	@Test
	public void cdAbsolutePathFolderTest() throws IOException {
		workingDir = new File(System.getProperty("user.dir"));
		File folderToCd = Files.createTempDirectory("cdtmpfolder").toFile(); 
		String[] args = {"cd", folderToCd.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		assertTrue(folderToCd.getAbsolutePath().equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
		folderToCd.delete();
	}
	
	/**
	 *  Test parent folder
	 */
	@Test
	public void cdParentFolderTest() {
		workingDir = new File(System.getProperty("user.dir"));
		String parentPath = workingDir.getParent();
		String[] args = {"cd", ".."};
		cdTool = new CdTool(args);
		
		assertTrue(parentPath.equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
	}

	// TODO
	@Test
	public void cdChildFolderTest() throws IOException {
		workingDir = new File(System.getProperty("user.dir"));
		File folderToCd = Files.createTempDirectory(workingDir.toPath(), "cdtmpfolder").toFile(); 
		String[] args = {"cd", folderToCd.getName()};
		cdTool = new CdTool(args);
		
		assertTrue(folderToCd.getAbsolutePath().equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
		folderToCd.delete();
	}
	
	/**
	 * Test "cd" go back to user.dir
	 */
	@Test
	public void cdNullTest() {
		workingDir = new File(System.getProperty("user.dir"));
		String[] args = {"cd"};
		cdTool = new CdTool(args);
		
		assertTrue(System.getProperty("user.dir").equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
	}
}
