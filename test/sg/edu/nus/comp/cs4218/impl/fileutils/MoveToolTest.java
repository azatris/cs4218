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

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;

public class MoveToolTest {
	private IMoveTool moveTool;
	
	@Before
	public void setUp() throws Exception {
		moveTool = new MoveTool();
	}

	@After
	public void tearDown() throws Exception {
		moveTool = null;
	}
	
	public String writeRandomStringTo(File toWrite) throws IOException{
		// generate random string as file contents
		StringBuilder strBuilder = new StringBuilder();
		Random random = new Random();		
		int size = random.nextInt(512);
		String chars = "abcdefghijklmnopqrstuvwxyz"
				+ "1234567890"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "~!@#$%^&*()-_+=}{][;:'\"?><,./"
				+ "\\\n\r\t";
		for (int i = 0; i < size; i++) {
			char c = chars.charAt(random.nextInt(chars.length()));
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
	
	//Testing boolean move(File from, File to)
	@Test
	public void renameFileToExistFileTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
//		System.out.println(from.getParent());
		File to = Files.createTempFile(
				new File(from.getParent()).toPath(), 
				"to", 
				"movetmp"
				).toFile();
		String fromStr = writeRandomStringTo(from);
		String toStr = writeRandomStringTo(to);

		assertFalse(fromStr.equals(toStr)); // It does have very small possibility that fromStr=toStr
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertEquals(fromStr, readFile(to));

		to.deleteOnExit();
	}
	
	public void renameFileToNonExistFileTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = File.createTempFile("to","movetmp", new File(from.getParent()));
		String fromStr = writeRandomStringTo(from);
		
		to.delete();
		assertTrue(from.exists());
		assertFalse(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertEquals(fromStr, readFile(to));

		to.deleteOnExit();
	}
	
	@Test
	public void renameFileToItselfTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		writeRandomStringTo(from);

		assertTrue(from.exists());
		assertTrue(moveTool.move(from, new File(from.getAbsolutePath())));
		assertEquals(0, moveTool.getStatusCode());
		assertTrue(from.exists());
		
		from.deleteOnExit();
	}
	
	@Test
	public void moveFileToFileInDifferentDirTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
//		System.out.println(from.getParent());
		File to = Files.createTempFile(
				new File(from.getParent()).toPath(), 
				"to", 
				"movetmp"
				).toFile();
		String fromStr = writeRandomStringTo(from);
		String toStr = writeRandomStringTo(to);

		assertFalse(fromStr.equals(toStr)); // It does have very small possibility that fromStr=toStr
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertEquals(fromStr, readFile(to));

		to.deleteOnExit();	
	}
	
	@Test
	public void moveFileToDifferentDirTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		String fromStr = writeRandomStringTo(from);
		
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		///bug here, should pass by reference
		assertEquals(
				fromStr, 
				readFile(new File(to.getAbsolutePath()+File.separator+from.getName()))
				);

		to.deleteOnExit();
	}
	
	@Test
	public void moveFileToNonExistDirTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
//		System.out.println(from.getParent());
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		String fromStr = writeRandomStringTo(from);
		
		to.delete();
		assertTrue(from.exists());
		assertFalse(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertEquals(fromStr, readFile(to));

		to.deleteOnExit();
	}
	
	@Test
	public void moveFileToNullTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		writeRandomStringTo(from);
		
		assertTrue(from.exists());
		assertFalse(moveTool.move(from, null));
		assertTrue(moveTool.getStatusCode()!=0);
		assertTrue(from.exists());

		from.deleteOnExit();
	}
	
	@Test
	public void moveNonExistFileTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		writeRandomStringTo(from);
		
		from.delete();
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertFalse(moveTool.move(from, to));
		assertTrue(moveTool.getStatusCode()!=0);

		to.deleteOnExit();
	}
	
	@Test
	public void moveDirTest() throws IOException {
		File from = Files.createTempDirectory("fromdirmovetmp").toFile();
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertFalse(moveTool.move(from, to));
		assertTrue(moveTool.getStatusCode()!=0);
		assertTrue(from.exists());
		assertTrue(to.exists());

		to.deleteOnExit();
	}
	
	@Test
	public void moveNonExistDirTest() throws IOException {
		File from = Files.createTempDirectory("fromdirmovetmp").toFile();
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		
		from.delete();
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertFalse(moveTool.move(from, to));
		assertTrue(moveTool.getStatusCode()!=0);

		to.deleteOnExit();
	}
	
	@Test
	public void moveNullTest() throws IOException {
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		
		assertTrue(to.exists());
		assertFalse(moveTool.move(null, to));
		assertTrue(moveTool.getStatusCode()!=0);

		to.deleteOnExit();
	}
}
