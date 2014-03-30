/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;

public class CdToolExtraTest {

	private final String HOME_DIRECTORY = "user.home";
	private final String USER_DIRECTORY = "user.dir";
	private ICdTool cdTool;
	private String defaultWorkingDirectory;
	private File workingDirectory;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		defaultWorkingDirectory = System.getProperty(USER_DIRECTORY);
		
		cdTool= new CdTool(null);
		workingDirectory = new File(defaultWorkingDirectory);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		cdTool = null;
		workingDirectory = null;
		System.setProperty(USER_DIRECTORY, defaultWorkingDirectory);
	}

	@Test
	public void changeDirectory_validAbsoluteDirectory_CorrectFileDirectoryReturned() {
		File directory = new File(defaultWorkingDirectory);
		File[] currentDirectoryFiles = directory.listFiles();
		File resturnStatement = cdTool.changeDirectory(currentDirectoryFiles[0]
				.getAbsolutePath());

		assertEquals(currentDirectoryFiles[0].getAbsolutePath(),
				resturnStatement.getAbsolutePath());
	}

	@Test
	public void changeDirectory_emptyString_HomeDirectoryReturn(){
		final File correctDirectory = new File ("");
		System.out.println(correctDirectory.getAbsolutePath());
		final File resturnStatement = cdTool.changeDirectory("");

		assertEquals(correctDirectory.getAbsolutePath(),
				resturnStatement.getAbsolutePath());
	}

	@Test
	public void changeDirectory_validNavigateFromHomeDirectory_CorrectRelativeDirectoryReturned(){
		//Valid for all major O.S distribution
		final String newDirectory = "~" + File.separator + "Downloads";
		cdTool.changeDirectory(newDirectory);

		assertEquals(System.getProperty(HOME_DIRECTORY) + File.separator
				+ "Downloads", System.getProperty(USER_DIRECTORY));
	}

	@Test
	public void changeDirectory_doubleDot_ParentDirectoryReturned(){
		final String newDirectory = "..";
		final File resturnStatement = cdTool.changeDirectory(newDirectory);

		assertEquals(workingDirectory.getParent(),
				resturnStatement.getAbsolutePath());
	}

	@Test
	public void execute_validAbsoluteDirectory_StatusCodeZero() {
		File directory = new File(defaultWorkingDirectory);
		File[] currentDirectoryFiles = directory.listFiles();

		final String[] argument = { currentDirectoryFiles[0].getAbsolutePath() }; 

		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(currentDirectoryFiles[0].getAbsolutePath(),
				System.getProperty(USER_DIRECTORY));
	}

	@Test
	public void execute_sameDirectory_StatusCodeZero(){
		final String newDirectory = defaultWorkingDirectory;
		final String[] argument = {newDirectory}; 
		cdTool = new CdTool(argument);

		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(defaultWorkingDirectory, System.getProperty(USER_DIRECTORY)); 
	}

	@Test
	public void execute_emptyString_StatusCodeZero(){
		final String[] argument = {""};
		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(System.getProperty(HOME_DIRECTORY), System.getProperty(USER_DIRECTORY));
	}

	@Test
	public void execute_validNavigateFromHomeDirectory_StatusCodeZero(){
		//Works for major O.S distribution
		final String newDirectory = "~" + File.separator + "Downloads";
		final String[] argument = {"cd" , newDirectory}; 

		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		//assertEquals(0, cdTool.getStatusCode());
		assertEquals(System.getProperty(HOME_DIRECTORY) + File.separator
				+ "Downloads", System.getProperty(USER_DIRECTORY));
	}

	@Test 
	public void execute_ValidNavigateBack_StatusCodeZero(){
		String[] argument = {"src"};
		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);
		
		argument[0] = "-";
		cdTool = new CdTool(argument);
		String result = cdTool.execute(workingDirectory, null);
		
		assertEquals(0, cdTool.getStatusCode());
		assertEquals("", result);
	}
	
//	@Test
//	public void execute_InvalidNavigateBack_StatusCodeNonZero(){
//		String[] argument = {"-"};
//		
//		cdTool = new CdTool(argument, true);
//		cdTool.execute(workingDirectory, null);
//		assertNotEquals(0, cdTool.getStatusCode());
//	}
	
	@Test
	public void execute_NullArgument_StatusCodeNonZero(){
		cdTool = new CdTool(null);
		cdTool.execute(workingDirectory, null);
		assertNotEquals(0, cdTool.getStatusCode());
	}
}