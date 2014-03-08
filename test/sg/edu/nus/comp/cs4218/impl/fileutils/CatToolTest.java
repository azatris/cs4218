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
	private File workingDirectory = new File(System.getProperty("user.dir"));
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		catTool = null;
	}
	
	//Testing String getStringForFile(File toRead)
	@Test
	public void getStringForEmptyFileTest() throws IOException {
		File fileToRead = File.createTempFile("emptyfile", "cattmp");
		String[] args = {"cat", fileToRead.getName()};
		catTool = new CatTool(args);
		assertTrue(fileToRead.exists());
		assertEquals("",catTool.getStringForFile(fileToRead));
		assertEquals(0, catTool.getStatusCode());
		fileToRead.delete();
	}
	
	@Test
	public void getStringForExistingFileTest() throws IOException {
		File fileToRead = File.createTempFile("random", "cattmp");
		String[] args = {"cat", fileToRead.getName()};
		catTool = new CatTool(args);
		String str = Common.writeRandomStringTo(fileToRead);
		
		assertEquals(str,catTool.getStringForFile(fileToRead));
		assertEquals(0, catTool.getStatusCode());
		fileToRead.delete();
	}
	
	@Test
	public void getStringForNonExistingFileTest() throws IOException {
		File fileNonExists = File.createTempFile("nonExists", "cattmp");
		String[] args = {"cat", fileNonExists.getName()};
		catTool = new CatTool(args);
		
		assertTrue(fileNonExists.delete());
		assertFalse(fileNonExists.exists());
		assertNull(catTool.getStringForFile(fileNonExists));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	@Test
	public void getStringForExistingDirectoryTest() throws IOException {
		File dirExists = Files.createTempDirectory("cattmpfolder").toFile();
		String[] args = {"cat", dirExists.getName()};
		catTool = new CatTool(args);

		assertTrue(dirExists.exists());
		assertNull(catTool.getStringForFile(dirExists));
		assertTrue(catTool.getStatusCode() != 0);
		
		dirExists.delete();
	}
	
	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException {
		File dirNonExists = Files.createTempDirectory("cattmpfolder").toFile();
		String[] args = {"cat", dirNonExists.getName()};
		catTool = new CatTool(args);
		dirNonExists.delete();
		
		assertFalse(dirNonExists.exists());
		assertNull(catTool.getStringForFile(dirNonExists));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	@Test
	public void getStringForNullDirectoryTest() {
		String[] args = {"cat", null};
		catTool = new CatTool(args);
		assertTrue(catTool.getStringForFile(null) == null);
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	/*Testing String execute(File workingDir, String stdin)*/
	// Test "cat -"
	@Test
	public void catStdinTest() {
		String[] args = {"cat", "-"};
		catTool = new CatTool(args);
		String stdin = "just a dummy stdin";
		File workingDirectory = new File(System.getProperty("user.dir"));
		String result = catTool.execute(workingDirectory, stdin);
		assertTrue(result.equals(stdin));
	}
	
	// Test cat one file in current directory "cat file1"
	@Test
	public void catOneFileInCurDirTest() {
		String dummyFileName = "file1.txt";
		String[] args = {"cat", dummyFileName};
		catTool = new CatTool(args);
		File fileToRead = new File(dummyFileName);
		try {
			String str = Common.writeRandomStringTo(fileToRead);
			String result = catTool.execute(workingDirectory, null);
			assertTrue(result.equals(str));
			assertTrue(catTool.getStatusCode() == 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Test cat one file in current directory but with dot "cat ./file1"
	@Test
	public void catOneFileInCurDirWithDotInPathTest() {
		String dummyFileName = "file1.txt";
		String[] args = {"cat", "." + File.separator + dummyFileName};
		catTool = new CatTool(args);
		File fileToRead = new File(dummyFileName);
		try {
			String str = Common.writeRandomStringTo(fileToRead);
			String result = catTool.execute(workingDirectory, null);
			assertTrue(result.equals(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileToRead.delete();
	}
	
	// Test cat more than one (maybe three) file in current directory "cat file1 file2 file3"
	@Test
	public void catMoreThanOneFilesInCurDirTest() {
		String dummyFileName1 = "file1.txt";
		String dummyFileName2 = "file2.txt";
		String dummyFileName3 = "file3.txt";
		String[] args = {"cat", dummyFileName1, dummyFileName2, dummyFileName3};
		catTool = new CatTool(args);
		File fileToRead1 = new File(dummyFileName1);
		File fileToRead2 = new File(dummyFileName2);
		File fileToRead3 = new File(dummyFileName3);
		try {
			String str1 = Common.writeRandomStringTo(fileToRead1);
			String str2 = Common.writeRandomStringTo(fileToRead2);
			String str3 = Common.writeRandomStringTo(fileToRead3);
			String concantenatedStr = str1 + str2 + str3;
			String result = catTool.execute(workingDirectory, null);
			assertTrue(result.equals(concantenatedStr));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileToRead1.delete();
		fileToRead2.delete();
		fileToRead3.delete();
	}
	
	// Test cat file in parent directory "cat ../file1"
	@Test
	public void catFileInParentDirTest() {
		String dummyFileName = "file1.txt";
		String[] args = {"cat", ".." + File.separator + dummyFileName};
		catTool = new CatTool(args);
		File fileToRead = new File(workingDirectory.getParentFile() + File.separator + dummyFileName);
		try {
			String str = Common.writeRandomStringTo(fileToRead);
			String result = catTool.execute(workingDirectory, null);
			assertTrue(result.equals(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileToRead.delete();
	}
	
	// Test cat files in Home Dir: Should fail, since we only consider relative directory
	@Test
	public void catFileInHomeDirTest() {
		String dummyFileName = "file1.txt";
		String[] args = {"cat", "~" + File.separator + dummyFileName};
		catTool = new CatTool(args);
		catTool.execute(workingDirectory, null);
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	// Test cat file in child directory "cat folder/file1"
	@Test
	public void catFileInChildDirTest() {
		String dummyFileName = "file1.txt";
		String dummyFolderName = "folder1";
		String[] args = {"cat", dummyFolderName + File.separator + dummyFileName};
		catTool = new CatTool(args);
		File childDirectory = new File(dummyFolderName);
	    childDirectory.mkdir();
		File fileToRead = new File(workingDirectory + File.separator + dummyFolderName + File.separator + dummyFileName);
		try {
			String str = Common.writeRandomStringTo(fileToRead);
			String result = catTool.execute(workingDirectory, null);
			assertTrue(result.equals(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileToRead.delete();
		childDirectory.delete();

	}
	
	// Test cat no arguments "cat"
	@Test
	public void catNoArgumentTest() {
		String[] args = {"cat"};
		catTool = new CatTool(args);
		catTool.execute(workingDirectory, null);
		assertTrue(catTool.getStatusCode() != 0);
	}

}
