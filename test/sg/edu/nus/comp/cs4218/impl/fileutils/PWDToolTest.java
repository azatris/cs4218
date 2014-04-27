package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;

public class PWDToolTest {
	private IPwdTool pwdtool; 
	private File fileAbs, fileAbsParDir;
	
	@Before
	public void before() throws IOException{
		fileAbs = File.createTempFile("pwd", "");
		fileAbsParDir = new File(fileAbs.getParent());
	}

    @After
	public void after(){
    	fileAbs.delete();
    	fileAbsParDir.delete();
		pwdtool = null;
	}
	
    /**
     * Testing String getStringForDirectory(File directory)
     * directory is a folder
     */
	@Test
	public void getStringForDirectoryTest() {
		String[] test = new String[] {"pwd"};
		pwdtool = new PWDTool(test);
		String dirString = pwdtool.getStringForDirectory(fileAbsParDir);
		assertTrue(dirString.equals(fileAbsParDir.getAbsolutePath()));
		assertEquals(pwdtool.getStatusCode(), 0);
    }

	/**
     * Testing String getStringForDirectory(File directory)
     * directory is a non-existing folder
     */
	@Test
	public void getStringForNonExistingDirectoryTest() { 
		String[] test = new String[] {"pwd"};
		pwdtool = new PWDTool(test);
		File notExistsDir = new File("notexists");
        pwdtool.getStringForDirectory(notExistsDir);
		assertNotEquals(pwdtool.getStatusCode(), 0);
    }
		
	/**
     * Testing String getStringForDirectory(File directory)
     * directory is null
     */
	@Test
	public void getStringForNullDirectoryTest(){ 
		String[] test = new String[] {"pwd"};
		pwdtool = new PWDTool(test);
		pwdtool.getStringForDirectory(null);
		assertNotEquals(pwdtool.getStatusCode(), 0);
	}
	
	/**
     * Testing constructor
     * pass null to constructor
     */
	@Test
	public void getStringPWDNullInConstructor() { 
		pwdtool = new PWDTool(null);
		String dirString = pwdtool.getStringForDirectory(fileAbsParDir);
		assertTrue(dirString.equals(fileAbsParDir.getAbsolutePath()));
		assertEquals(pwdtool.getStatusCode(), 127);
	}
	
    /**
     * Testing execute
     * pass wrong input to constructor
     */
	@Test 
	public void executeWrongInput() {
		String[] test = new String[] {"asd"};
		pwdtool = new PWDTool(test);
		
		String dirString = pwdtool.execute(fileAbsParDir, null);
		assertTrue(dirString.equals(fileAbsParDir.getAbsolutePath()));
		assertEquals(pwdtool.getStatusCode(), 127);
	}
	
	/**
     * Testing execute
     * pass wrong arguments to constructor
     */
	@Test 
	public void executeToManyArgs() throws IOException{
		String[] test = new String[] {"pwd" , "Hallo"};
		pwdtool = new PWDTool(test);

		String dirString = pwdtool.execute(fileAbsParDir, null);
		assertTrue("Not right dir", dirString.equals("Error: PWD command wrong"));
		assertEquals("Not right statuscode ", pwdtool.getStatusCode(), 1);
	}
}
