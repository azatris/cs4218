package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;

public class DeleteToolTest {
	private IDeleteTool deleteTool;

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		deleteTool = null;
	}
	
	// Testing boolean delete(File toDelete)
	@Test
	public void deletFileTest() throws IOException {
		deleteTool = new DeleteTool(null);
		String filePathStr = File.createTempFile("exists", "deltmp").getAbsolutePath();
		File fileToDelete = new File(filePathStr);
		
		assertTrue(fileToDelete.isFile());
		assertTrue(deleteTool.delete(fileToDelete));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(fileToDelete.isFile());
	}
	
	@Test
	public void deleteDirectoryWithFilesTest() throws IOException {
		deleteTool = new DeleteTool(null);
		String dirPath = File.createTempFile("exists", "deltmp").getParent();
		File dirToDelete = new File(dirPath);
		
		assertTrue(dirToDelete.isDirectory());
		assertFalse(deleteTool.delete(dirToDelete));
		assertTrue(dirToDelete.isDirectory());
	}
	
	@Test
	public void deleteDirectoryWithoutFilesTest() throws IOException {
		deleteTool = new DeleteTool(null);
		File emptyDir = Files.createTempDirectory("deltmpfolder").toFile();
		
		assertTrue(emptyDir.isDirectory());
		assertTrue(deleteTool.delete(emptyDir));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(emptyDir.isDirectory());
	}
	
	@Test
	public void deleteNonExistingFileTest() throws IOException {
		deleteTool = new DeleteTool(null);
		String filePathStr = File.createTempFile("nonExists", "deltmp").getAbsolutePath();
		File fileToDelete = new File(filePathStr);
		
		assertTrue(fileToDelete.delete());
		assertFalse(fileToDelete.exists());
		assertFalse(deleteTool.delete(fileToDelete));
		assertFalse(deleteTool.getStatusCode() == 0);
	}
	
	@Test
	public void deleteNonExistingDirectoryTest() throws IOException {
		deleteTool = new DeleteTool(null);
		File nonExistingDir = Files.createTempDirectory("deltmpfolder").toFile();
		nonExistingDir.delete();
		assertFalse(nonExistingDir.exists());
		assertFalse(deleteTool.delete(nonExistingDir));
		assertFalse(deleteTool.getStatusCode() == 0);
	}
	
	@Test
	public void deleteNullDirectoryTest() {
		deleteTool = new DeleteTool(null);
		assertFalse(deleteTool.delete(null));
		assertFalse(deleteTool.getStatusCode() == 0);
	}
}
