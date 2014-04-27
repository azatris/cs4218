package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.ICdTool;

public class CdToolTest {
	private ICdTool cdTool;
	private File workingDir = new File(System.getProperty("user.dir"));
	private File file1Abs, folder1, folder2;
	
	@Before
	public void setUp() throws Exception {
		file1Abs = File.createTempFile("cd1", "");
		folder1 = Files.createTempDirectory("catFolder1").toFile();
		folder2 = Files.createTempDirectory(workingDir.toPath(), "cdtmpfolder").toFile();
	}

	@After
	public void tearDown() throws Exception {
		file1Abs.delete();
		folder1.delete();
		folder2.delete();
		cdTool = null;
	}

	/**
	 *  Testing File changeDirectory(String newDirectory)
	 *  newDirectory is empty String
	 */
	@Test
	public void changeToEmptyStringTest(){
		String args[] = {"cd", ""};
		cdTool = new CdTool(args);
		assertNull(cdTool.changeDirectory(""));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	/**
	 * Testing File changeDirectory(String newDirectory)
	 * newDirectory is an absolute path
	 */
	@Test
	public void changeToFileTest(){
		String args[] = {"cd", file1Abs.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		assertTrue(file1Abs.exists());
		assertNull(cdTool.changeDirectory(file1Abs.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	/**
	 * Testing File changeDirectory(String newDirectory)
	 * newDirectory is a non-existing absolute path
	 */
	@Test
	public void changeToNonExistingFileTest(){
		String args[] = {"cd", file1Abs.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		file1Abs.delete();
		assertFalse(file1Abs.exists());
		assertNull(cdTool.changeDirectory(file1Abs.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	/**
	 * Testing File changeDirectory(String newDirectory)
	 * newDirectory is a folder path
	 */
	@Test
	public void changeToDirectoryTest(){
		String args[] = {"cd", folder1.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		assertTrue(folder1.exists());
		assertEquals(folder1, cdTool.changeDirectory(folder1.getAbsolutePath()));
		assertEquals(55, cdTool.getStatusCode());
	}
	
	/**
	 * Testing File changeDirectory(String newDirectory)
	 * newDirectory is a non-existing folder path
	 */
	@Test
	public void changeToNonExistingDirectoryTest() {
		String args[] = {"cd", folder1.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		folder1.delete();
		assertFalse(folder1.exists());
		assertNull(cdTool.changeDirectory(folder1.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	/**
	 * Testing File changeDirectory(String newDirectory)
	 * newDirectory is null
	 */
	@Test
	public void changeToNullDirectoryTest() {
		String args[] = {"cd", null};
		cdTool = new CdTool(args);
		assertNull(cdTool.changeDirectory(null));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	/** 
	 * Test String execute(File workingDir, String stdin) 
	 * Test Home ~
	 */
	@Test
	public void cdHomeTest() {
		String[] args = {"cd", "~"};
		cdTool = new CdTool(args);
		
		assertTrue(System.getProperty("user.dir").equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
	}
	
	/**
	 * Test String execute(File workingDir, String stdin)
	 * Test absolute file path
	 */
	@Test
	public void cdAbsolutePathFileTest() {
		String[] args = {"cd", file1Abs.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		assertTrue(workingDir.getAbsolutePath().equals(cdTool.execute(workingDir, null)));
		assertTrue(cdTool.getStatusCode() != 55);
	}
	
	/**
	 * Test String execute(File workingDir, String stdin)
	 * Test absolute folder path
	 */
	@Test
	public void cdAbsolutePathFolderTest(){ 
		String[] args = {"cd", folder1.getAbsolutePath()};
		cdTool = new CdTool(args);
		
		assertTrue(folder1.getAbsolutePath().equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
	}
	
	/**
	 * Test String execute(File workingDir, String stdin)
	 * Test parent folder
	 */
	@Test
	public void cdParentFolderTest() {
		String parentPath = workingDir.getParent();
		String[] args = {"cd", ".."};
		cdTool = new CdTool(args);
		
		assertTrue(parentPath.equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
	}

	/**
	 * Test String execute(File workingDir, String stdin)
	 * Test child folder
	 */
	@Test
	public void cdChildFolderTest() { 
		String[] args = {"cd", folder2.getName()};
		cdTool = new CdTool(args);
		
		assertTrue(folder2.getAbsolutePath().equals(cdTool.execute(workingDir, null)));
		assertEquals(55, cdTool.getStatusCode());
	}
	
	/**
	 * Test String execute(File workingDir, String stdin)
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
