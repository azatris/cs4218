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
	
	@Before
	public void before(){
		
	}

    @After
	public void after(){
		pwdtool = null;
	}
	
    // TODO
	@Test
	public void getStringForDirectoryTest() throws IOException {
		String[] test = new String[] {"pwd"};
		pwdtool = new PWDTool(test);
		File tempFile = File.createTempFile("existspwd", "tmp");
		String existsDirString = tempFile.getParent();
		File existsDir = new File(existsDirString);
		String dirString = pwdtool.getStringForDirectory(existsDir);
		assertTrue(dirString.equals(existsDirString));
		assertEquals(pwdtool.getStatusCode(), 0);
		tempFile.delete();
    }

    // TODO
	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException { 
		String[] test = new String[] {"pwd"};
		pwdtool = new PWDTool(test);
		File notExistsDir = new File("notexists");
        pwdtool.getStringForDirectory(notExistsDir);
		assertNotEquals(pwdtool.getStatusCode(), 0);
    }
		
    // TODO
	@Test
	public void getStringForNullDirectoryTest() throws IOException { 
		String[] test = new String[] {"pwd"};
		pwdtool = new PWDTool(test);
		pwdtool.getStringForDirectory(null);
		assertNotEquals(pwdtool.getStatusCode(), 0);
	}
	
    // TODO
	@Test
	public void getStringPWDNullinConstructor() throws IOException { 
		pwdtool = new PWDTool(null);
		File tempFile = File.createTempFile("existspwd", "tmp");
		String existsDirString = tempFile.getParent();
		
		File existsDir = new File(existsDirString);
		String dirString = pwdtool.getStringForDirectory(existsDir);
		assertTrue(dirString.equals(existsDirString));
		assertEquals(pwdtool.getStatusCode(), 127);
		tempFile.delete();
	}
	
    // TODO
	@Test 
	public void executeWrongInput() throws IOException{
		String[] test = new String[] {"asd"};
		pwdtool = new PWDTool(test);
		File tempFile = File.createTempFile("existspwd", "tmp");
		String existsDirString = tempFile.getParent();
		File existsDir = new File(existsDirString);
		String dirString = pwdtool.execute(existsDir, null);
		assertTrue(dirString.equals(existsDirString));
		assertEquals(pwdtool.getStatusCode(), 127);
		tempFile.delete();
	}
	
    // TODO
	@Test 
	public void executeToManyArgs() throws IOException{
		String[] test = new String[] {"pwd" , "Hallo"};
		pwdtool = new PWDTool(test);
		File tempFile = File.createTempFile("existspwd", "tmp");
		String existsDirString = tempFile.getParent();
		
		File existsDir = new File(existsDirString);
		String dirString = pwdtool.execute(existsDir, null);
		assertTrue("Not right dir", dirString.equals("Error: PWD command wrong"));
		assertEquals("Not right statuscode ", pwdtool.getStatusCode(), 1);
		tempFile.delete();
	}
}
