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
	
	@Before
	public void setUp() throws Exception {
		cdTool = new CdTool();
	}

	@After
	public void tearDown() throws Exception {
		cdTool = null;
	}

	// Testing File changeDirectory(String newDirectory)
	
	@Test
	public void changeToEmptyStringTest(){
		assertNull(cdTool.changeDirectory(""));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	@Test
	public void changeToFileTest() throws IOException {
		File fileToCd = File.createTempFile("exists", "cdtmp");
		
		assertTrue(fileToCd.exists());
		assertNull(cdTool.changeDirectory(fileToCd.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
		
		fileToCd.delete();
	}
	
	@Test
	public void changeToNonExistingFileTest() throws IOException {
		File fileToCd = File.createTempFile("nonExists", "cdtmp");
		
		fileToCd.delete();
		assertFalse(fileToCd.exists());
		assertNull(cdTool.changeDirectory(fileToCd.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	@Test
	public void changeToDirectoryTest() throws IOException {
		File dirToCd = Files.createTempDirectory("cdtmpfolder").toFile();
		
		assertTrue(dirToCd.exists());
		assertEquals(dirToCd, cdTool.changeDirectory(dirToCd.getAbsolutePath()));
		assertEquals(0, cdTool.getStatusCode());
		
		dirToCd.delete();
	}
	
	@Test
	public void changeToNonExistingDirectoryTest() throws IOException {
		File dirToCd = Files.createTempDirectory("cdtmpfolder").toFile();
		dirToCd.delete();
		assertFalse(dirToCd.exists());
		assertNull(cdTool.changeDirectory(dirToCd.getAbsolutePath()));
		assertTrue(cdTool.getStatusCode() != 0);
	}
	
	@Test
	public void changeToNullDirectoryTest() {
		assertNull(cdTool.changeDirectory(null));
		assertTrue(cdTool.getStatusCode() != 0);
	}

}
