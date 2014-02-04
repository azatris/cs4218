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
		catTool = new CatTool();
	}

	@After
	public void tearDown() throws Exception {
		catTool = null;
	}
	
	//Testing String getStringForFile(File toRead)
	@Test
	public void getStringForEmptyFileTest() throws IOException {
		File fileToRead = File.createTempFile("emptyfile", "cattmp");
		assertTrue(fileToRead.exists());
		assertEquals("",catTool.getStringForFile(fileToRead));
		assertEquals(0, catTool.getStatusCode());
		fileToRead.delete();
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
	
	@Test
	public void getStringForExistingFileTest() throws IOException {
		File fileToRead = File.createTempFile("random", "cattmp");
		String str = writeRandomStringTo(fileToRead);
		
		assertEquals(str,catTool.getStringForFile(fileToRead));
		assertEquals(0, catTool.getStatusCode());
		fileToRead.delete();
	}
	
	@Test
	public void getStringForNonExistingFileTest() throws IOException {
		File fileNonExists = File.createTempFile("nonExists", "cattmp");
		
		assertTrue(fileNonExists.delete());
		assertFalse(fileNonExists.exists());
		assertNull(catTool.getStringForFile(fileNonExists));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	@Test
	public void getStringForExistingDirectoryTest() throws IOException {
		File dirExists = Files.createTempDirectory("cattmpfolder").toFile();

		assertTrue(dirExists.exists());
		assertNull(catTool.getStringForFile(dirExists));
		assertTrue(catTool.getStatusCode() != 0);
		
		dirExists.delete();
	}
	
	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException {
		File dirNonExists = Files.createTempDirectory("cattmpfolder").toFile();
		dirNonExists.delete();
		
		assertFalse(dirNonExists.exists());
		assertNull(catTool.getStringForFile(dirNonExists));
		assertTrue(catTool.getStatusCode() != 0);
	}
	
	@Test
	public void getStringForNullDirectoryTest() {
		assertTrue(catTool.getStringForFile(null) == null);
		assertTrue(catTool.getStatusCode() != 0);
	}

}
