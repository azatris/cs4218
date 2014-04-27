package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;

public class CatToolTest {
	private ICatTool catTool;
	private File workingDir = new File(System.getProperty("user.dir"));
	private File file1, file2ForRelPath, file3ForRelPath, file4InParDir, empFile, folder1, folder2, subFile1;
	private String content1, content2, content3, content4, contentSub1;
	@Before
	public void setUp() throws Exception {
		empFile = File.createTempFile("empty", "cat");
		
		file1 = File.createTempFile("cat", "");
		content1 = Common.writeRandomStringTo(file1);
		
		file2ForRelPath = File.createTempFile("cat", "", workingDir);
		content2 = Common.writeRandomStringTo(file2ForRelPath);
		
		file3ForRelPath = File.createTempFile("cat", "", workingDir);
		content3 = Common.writeRandomStringTo(file3ForRelPath);
		
		file4InParDir = new File(workingDir.getParentFile() + File.separator + "file4InParDir.txt");
		content4 = Common.writeRandomStringTo(file4InParDir);
		
		folder1 = Files.createTempDirectory("catFolder1").toFile();
		
		folder2 = new File("folder2");
		folder2.mkdir();
		subFile1 = File.createTempFile("catTool", "", folder2);
		contentSub1 = Common.writeRandomStringTo(subFile1);
	}

	@After
	public void tearDown() throws Exception {
		empFile.delete();
		file1.delete();
		file2ForRelPath.delete();
		file3ForRelPath.delete();
		file4InParDir.delete();
		subFile1.delete();
		folder1.delete();
		folder2.delete();
		catTool = null;
	}
	
	/**
	 * Testing String getStringForFile(File toRead)
	 * toRead is an empty file
	 */
	@Test
	public void getStringForEmptyFileTest(){
		String[] args = {"cat", empFile.getName()};
		catTool = new CatTool(args);
		assertTrue(empFile.exists());
		assertEquals("",catTool.getStringForFile(empFile));
		assertEquals(0, catTool.getStatusCode());
	}
	/**
	 * Testing String getStringForFile(File toRead)
	 * toRead is a file and is written a random string
	 */
	@Test
	public void getStringForExistingFileTest() {
		String[] args = {"cat", file1.getName()};
		catTool = new CatTool(args);
		
		assertEquals(content1,catTool.getStringForFile(file1));
		assertEquals(0, catTool.getStatusCode());
	}
	
	/**
	 * Testing String getStringForFile(File toRead)
	 * toRead is a file that does not exist
	 */
	@Test
	public void getStringForNonExistingFileTest(){
		String[] args = {"cat", empFile.getName()};
		catTool = new CatTool(args);
		
		assertTrue(empFile.delete());
		assertFalse(empFile.exists());
		assertNull(catTool.getStringForFile(empFile));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	/**
	 * Testing String getStringForFile(File toRead)
	 * toRead is a folder
	 */
	@Test
	public void getStringForExistingDirectoryTest(){
		String[] args = {"cat", folder1.getName()};
		catTool = new CatTool(args);

		assertTrue(folder1.exists());
		assertNull(catTool.getStringForFile(folder1));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	/**
	 * Testing String getStringForFile(File toRead)
	 * toRead is a folder that does not exist
	 */
	@Test
	public void getStringForNonExistingDirectoryTest() {
		String[] args = {"cat", folder1.getName()};
		catTool = new CatTool(args);
		folder1.delete();
		
		assertFalse(folder1.exists());
		assertNull(catTool.getStringForFile(folder1));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	/**
	 * Testing String getStringForFile(File toRead)
	 * toRead is null
	 */
	@Test
	public void getStringForNullDirectoryTest() {
		String[] args = {"cat", null};
		catTool = new CatTool(args);
		assertTrue(catTool.getStringForFile(null) == null);
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	/**
	 * Testing String execute(File workingDir, String stdin)
	 * Test "cat -"
	 */
	@Test
	public void catStdinTest() {
		String[] args = {"cat", "-"};
		catTool = new CatTool(args);
		String stdin = "just a dummy stdin";
		File workingDirectory = new File(System.getProperty("user.dir"));
		String result = catTool.execute(workingDirectory, stdin);
		assertTrue(result.equals(stdin));
	}
	
	/**
	 * Testing String execute(File workingDir, String stdin)
	 * Test cat one file in current directory "cat file1"
	 */
	@Test
	public void catOneFileInCurDirTest() {
		String[] args = {"cat", file2ForRelPath.getName()};
		catTool = new CatTool(args);
		String result = catTool.execute(workingDir, "");
		assertEquals(content2, result);
		assertEquals(0, catTool.getStatusCode());
	}
	
	/**
	 * Testing String execute(File workingDir, String stdin)
	 * Test cat one file in current directory but with dot "cat ./file1"
	 */
	@Test
	public void catOneFileInCurDirWithDotInPathTest() {
		String[] args = {"cat", "." + File.separator + file2ForRelPath.getName()};
		catTool = new CatTool(args);

		String result = catTool.execute(workingDir, "");
		assertEquals(content2, result);
	}
	
	/**
	 * Test cat more than one (maybe three) file in current directory "cat file1 file2 file3"
	 */
	@Test
	public void catMoreThanOneFilesInCurDirTest() {
		String[] args = {"cat", file1.getAbsolutePath(), file2ForRelPath.getName(), file3ForRelPath.getName()};
		catTool = new CatTool(args);
		String concantenatedStr = content1 + content2 + content3;
		String result = catTool.execute(workingDir, null);
		assertEquals(concantenatedStr, result);

	}
	
	/**
	 * Test cat file in parent directory "cat ../file1"
	 */
	@Test
	public void catFileInParentDirTest() {
		String[] args = {"cat", ".." + File.separator + file4InParDir.getName()};
		catTool = new CatTool(args);
		String result = catTool.execute(workingDir, null);
		assertEquals(content4, result);
	}
	
	/**
	 *  Test cat files in Home Dir: Should fail, since we only consider relative directory
	 */
	@Test
	public void catFileInHomeDirTest() {
		String dummyFileName = "file1.txt";
		String[] args = {"cat", "~" + File.separator + dummyFileName};
		catTool = new CatTool(args);
		catTool.execute(workingDir, null);
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	/**
	 *  Test cat file in child directory "cat folder/file1"
	 */
	@Test
	public void catFileInChildDirTest() {
		String[] args = {"cat", folder2.getName() + File.separator + subFile1.getName()};
		catTool = new CatTool(args);

		String result = catTool.execute(workingDir, null);
		assertEquals(contentSub1, result);
		}
	
	/**
	 *  Test cat no arguments "cat"
	 */
	@Test
	public void catNoArgumentTest() {
		String[] args = {"cat"};
		catTool = new CatTool(args);
		catTool.execute(workingDir, null);
		assertTrue(catTool.getStatusCode() != 0);
	}

}
