package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;

public class AdditionalCatToolTest {
	private ICatTool catTool;
	private File workingDir = new File(System.getProperty("user.dir"));
	private File fileForCat;
	@Before
	public void setUp() throws Exception {
		fileForCat = File.createTempFile("catTool", "");
	}

	@After
	public void tearDown() throws Exception {
		fileForCat.delete();
		catTool = null;
	}
	
	/**
	 * Test cat absolute_file_path
	 */
	@Test
	public void executeAbsolutePathTest(){
		catTool = new CatTool(new String[]{"cat", fileForCat.getAbsolutePath()});
		assertEquals("", catTool.execute(workingDir, ""));
		assertEquals(0, catTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass null as arguments
	 */
	@Test
	public void passNullToConstructorTest() {
		catTool = new CatTool(null);
		assertEquals(127, catTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass empty array as arguments
	 */
	@Test
	public void passEmptyArgumentToConstructorTest() {
		catTool = new CatTool(new String[]{});
		assertEquals(127, catTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass wrong command as arguments
	 */
	@Test
	public void passWrongArgumentToConstructorTest() {
		catTool = new CatTool(new String[]{"cp"});
		assertEquals(127, catTool.getStatusCode());
	}

}
