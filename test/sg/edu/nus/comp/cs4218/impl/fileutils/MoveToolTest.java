package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

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

	//Testing boolean move(File from, File to)
	@Test
	public void renameFileTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void renameFileToItselfTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void moveFileToFileInDifferentDirTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void moveFileToDifferentDirTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void moveFileToNonExistDirTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void moveFileToNullTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void moveNonExistFileTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void moveDirTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void moveNonExistDirTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void moveNullTest() {
		fail("Not yet implemented");
	}
}
