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
		catTool = new CatTool();
		File fileToRead = File.createTempFile("emptyfile", "cattmp");
		assertTrue(fileToRead.exists());
		assertEquals("",catTool.getStringForFile(fileToRead));
		assertEquals(0, catTool.getStatusCode());
		fileToRead.delete();
	}
	
	@Test
	public void getStringForExistingFileTest() throws IOException {
		catTool = new CatTool();
		File fileToRead = File.createTempFile("random", "cattmp");
		String str = writeRandomStringTo(fileToRead);
		
		assertEquals(str,catTool.getStringForFile(fileToRead));
		assertEquals(0, catTool.getStatusCode());
		fileToRead.delete();
	}
	
	@Test
	public void getStringForNonExistingFileTest() throws IOException {
		catTool = new CatTool();
		File fileNonExists = File.createTempFile("nonExists", "cattmp");
		
		assertTrue(fileNonExists.delete());
		assertFalse(fileNonExists.exists());
		assertNull(catTool.getStringForFile(fileNonExists));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	@Test
	public void getStringForExistingDirectoryTest() throws IOException {
		catTool = new CatTool();
		File dirExists = Files.createTempDirectory("cattmpfolder").toFile();

		assertTrue(dirExists.exists());
		assertNull(catTool.getStringForFile(dirExists));
		assertTrue(catTool.getStatusCode() != 0);
		
		dirExists.delete();
	}
	
	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException {
		catTool = new CatTool();
		File dirNonExists = Files.createTempDirectory("cattmpfolder").toFile();
		dirNonExists.delete();
		
		assertFalse(dirNonExists.exists());
		assertNull(catTool.getStringForFile(dirNonExists));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	@Test
	public void getStringForNullDirectoryTest() {
		catTool = new CatTool();
		assertTrue(catTool.getStringForFile(null) == null);
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	/*Testing String execute(File workingDir, String stdin)*/
	// Test "cat -"
	@Test
	public void catStdinTest() {

	}
	
	@Test
	public void catOneFileInCurDirTest() {

	}
	
	// "./......"
	@Test
	public void catOneFileInCurDirWithDotInPathTest() {

	}
	
	@Test
	public void catMoreThanOneFilesInCurDirTest() {

	}
	
	// "../../"
	@Test
	public void catTwoFilesInParentDirTest() {

	}
	
	// "~/"
	@Test
	public void catTwoFilesInHomeDirTest() {

	}
	
	// "/......"
	@Test
	public void catTwoFilesInChildDirTest() {

	}
	
	@Test
	public void catTwoFilesInAbsolutePathTest() {

	}
	
	@Test
	public void catNoArgumentTest() {

	}

}
