package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;

public class CopyToolTest {
	private ICopyTool copyTool;
	
	@Before
	public void setUp() throws Exception {
		copyTool = new CopyTool();
	}

	@After
	public void tearDown() throws Exception {
		copyTool = null;
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

	public String readFile(File toRead) throws IOException{
		 FileReader fileReader = new FileReader(toRead);

		String fileContents = "";
		int i ;
		while((i = fileReader.read()) != -1){
			char ch = (char)i;
			fileContents = fileContents + ch; 
		}
		fileReader.close();

		return fileContents;
	}
	
	//Testing boolean copy(File from, File to)
	@Test
	public void copyFileToExistFileTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		File to = File.createTempFile("tofile","copytmp");
		String fromStr = writeRandomStringTo(from);
		String toStr = writeRandomStringTo(to);

		assertFalse(fromStr.equals(toStr)); // It does have very small possibility that fromStr=toStr
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(copyTool.copy(from, to));
		assertEquals(0, copyTool.getStatusCode());
		assertEquals(fromStr, readFile(to));
		
		from.delete();
		to.delete();
	}
	
	@Test
	public void copyFileToNonExistFileTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		String fromStr = writeRandomStringTo(from);
		File to = File.createTempFile("tofile","copytmp");
		to.delete();
		
		assertFalse(to.exists());
		assertTrue(from.exists());
		assertTrue(copyTool.copy(from, to));
		assertEquals(0, copyTool.getStatusCode());
		assertEquals(fromStr, readFile(to));
		
		from.delete();
		to.delete();
	}
	
	@Test
	public void copyFileToExistDirTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		File to = Files.createTempDirectory("tocopytmp").toFile();
		String fromStr = writeRandomStringTo(from);

		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(copyTool.copy(from, to));
		assertEquals(0, copyTool.getStatusCode());
		to = new File(to.getAbsolutePath() + File.separator +from.getName());
		assertEquals(fromStr, readFile(to));
		
		from.delete();
		to.delete();
	}
	
	@Test
	public void copyFileToNonExistDirTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		String fromStr = writeRandomStringTo(from);
		File to = Files.createTempDirectory("tononexistcopytmp").toFile();
		to.delete();
		
		assertFalse(to.exists());
		assertTrue(from.exists());
		assertTrue(copyTool.copy(from, to));
		assertEquals(0, copyTool.getStatusCode());
		assertEquals(fromStr, readFile(to));
		
		from.delete();
		to.delete();
	}
	
	@Test
	public void copyFileToNullDirTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		writeRandomStringTo(from);

		assertTrue(from.exists());
		assertFalse(copyTool.copy(from, null));
		assertTrue(copyTool.getStatusCode()!= 0);
		
		from.delete();
	}
	
	@Test
	public void copyNonExistFileTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		File to = File.createTempFile("tofile","copytmp");
		writeRandomStringTo(from);
		writeRandomStringTo(to);

		from.delete();
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertFalse(copyTool.copy(from, to));
		assertTrue(copyTool.getStatusCode() != 0);

		to.delete();
	}
	
	@Test
	public void copyExistDirTest() throws IOException {
		File from = Files.createTempDirectory("fromFoldercopytmp").toFile();
		File to = File.createTempFile("tofile","copytmp");
		writeRandomStringTo(to);

		assertTrue(from.exists());
		assertTrue(to.exists());
		assertFalse(copyTool.copy(from, to));
		assertTrue(copyTool.getStatusCode() != 0);

		from.delete();
		to.delete();
	}
	
	@Test
	public void copyNonExistDirTest() throws IOException {
		File from = Files.createTempDirectory("fromFoldercopytmp").toFile();
		File to = File.createTempFile("tofile","copytmp");
		writeRandomStringTo(to);

		from.delete();
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertFalse(copyTool.copy(from, to));
		assertTrue(copyTool.getStatusCode()!=0);

		to.delete();
	}
	
	@Test
	public void copyNullTest() {
		assertFalse(copyTool.copy(null, null));
		assertTrue(copyTool.getStatusCode()!=0);
	}
}
