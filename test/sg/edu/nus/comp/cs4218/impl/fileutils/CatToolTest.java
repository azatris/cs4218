package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	
	public String writeRandomStringTo(File toWrite) throws IOException{
		// generate random string as file contents
		StringBuilder strBuilder = new StringBuilder();
		Random random = new Random();		
		int size = random.nextInt(512);
		char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()-_+=}{][;:'\"?><,./\\\n".toCharArray();
		for (int i = 0; i < size; i++) {
			char c = chars[random.nextInt(chars.length)];
			strBuilder.append(c);
		}
		String str = strBuilder.toString();

		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toWrite), "utf-8"));
		writer.write(str);
		writer.close();
		
		return str;
	}
	
	//Testing String getStringForFile(File toRead)
	@Test
	public void getStringForEmptyFileTest() throws IOException {
		catTool = new CatTool(null);
		File fileToRead = File.createTempFile("emptyfile", "cattmp");
		assertTrue(fileToRead.exists());
		assertEquals("",catTool.getStringForFile(fileToRead));
		assertEquals(0, catTool.getStatusCode());
		fileToRead.delete();
	}
	
	@Test
	public void getStringForExistingFileTest() throws IOException {
		catTool = new CatTool(null);
		File fileToRead = File.createTempFile("random", "cattmp");
		String str = writeRandomStringTo(fileToRead);
		
		assertEquals(str,catTool.getStringForFile(fileToRead));
		assertEquals(0, catTool.getStatusCode());
		fileToRead.delete();
	}
	
	@Test
	public void getStringForNonExistingFileTest() throws IOException {
		catTool = new CatTool(null);
		File fileNonExists = File.createTempFile("nonExists", "cattmp");
		
		assertTrue(fileNonExists.delete());
		assertFalse(fileNonExists.exists());
		assertNull(catTool.getStringForFile(fileNonExists));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	@Test
	public void getStringForExistingDirectoryTest() throws IOException {
		catTool = new CatTool(null);
		File dirExists = Files.createTempDirectory("cattmpfolder").toFile();

		assertTrue(dirExists.exists());
		assertNull(catTool.getStringForFile(dirExists));
		assertTrue(catTool.getStatusCode() != 0);
		
		dirExists.delete();
	}
	
	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException {
		catTool = new CatTool(null);
		File dirNonExists = Files.createTempDirectory("cattmpfolder").toFile();
		dirNonExists.delete();
		
		assertFalse(dirNonExists.exists());
		assertNull(catTool.getStringForFile(dirNonExists));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	@Test
	public void getStringForNullDirectoryTest() {
		catTool = new CatTool(null);
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
			String str = writeRandomStringTo(fileToRead);
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
		String[] args = {"cat", "./" + dummyFileName};
		catTool = new CatTool(args);
		File fileToRead = new File(dummyFileName);
		try {
			String str = writeRandomStringTo(fileToRead);
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
			String str1 = writeRandomStringTo(fileToRead1);
			String str2 = writeRandomStringTo(fileToRead2);
			String str3 = writeRandomStringTo(fileToRead3);
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
	
	// Test cat Two file in parent directory "cat ../file1"
	@Test
	public void catTwoFilesInParentDirTest() {
		String dummyFileName = "file1.txt";
		String[] args = {"cat", "../" + dummyFileName};
		catTool = new CatTool(args);
		File fileToRead = new File(workingDirectory.getParentFile() + "/" + dummyFileName);
		try {
			String str = writeRandomStringTo(fileToRead);
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
	public void catTwoFilesInHomeDirTest() {
		
	}
	
	// Test cat Two file in child directory "cat folder/file1"
	@Test
	public void catTwoFilesInChildDirTest() {
		
	}
	
	// Test cat Two file in absolute path "cat /folder/file1": Should fail, since we only consider relative directory
	@Test
	public void catTwoFilesInAbsolutePathTest() {

	}
	
	// Test cat no arguments "cat"
	@Test
	public void catNoArgumentTest() {

	}

}
