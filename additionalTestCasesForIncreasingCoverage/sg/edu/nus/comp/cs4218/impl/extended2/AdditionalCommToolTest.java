package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.extended2.ICommTool;

public class AdditionalCommToolTest {

	private static ICommTool commTool; 
	private static File file1, file2, file3, file4, file5, file6, file7;
	private static File workingDir = new File(System.getProperty("user.dir"));

	@BeforeClass 
	public static void executeThisBeforeClass() throws IOException{
		//testFile 1 will be the file in sorted order
		file1 = File.createTempFile("commfile", "sorted", workingDir);
		Common.writeFile(
				file1, 
				System.lineSeparator()+"aaa"+System.lineSeparator()+
				"bbb"+System.lineSeparator()+"ccc"
				);
		
		//testFile 2 will be the file in sorted order
		file2 = File.createTempFile("commfile", "sorted", workingDir);
		Common.writeFile(
				file2, 
				System.lineSeparator()+"aaf"+System.lineSeparator()+
				"abd"+System.lineSeparator()+"ccc"
				);

		//testFile 3 will be the file in unsorted order 
		file3 = File.createTempFile("commfile", "unsorted", workingDir);
		Common.writeFile(
				file3, 
				"ace"+System.lineSeparator()+"ggg"+System.lineSeparator()+
				"aaa"+System.lineSeparator()+"bbb");
		
		//testFile 4 will be the file in unsorted order 
		file4 = File.createTempFile("commfile", "unsorted", workingDir);
		Common.writeFile(
				file4, 
				"aaa"+System.lineSeparator()+"ecc"+System.lineSeparator()+
				"eaa"+System.lineSeparator()+"zbb");
		
		file5 = File.createTempFile("commfile", "empty", workingDir);
		Common.writeFile(file5, "");
		
		file6 = File.createTempFile("commfile", "", workingDir);
		Common.writeFile(file6, "cjwz");
		
		file7 = File.createTempFile("commfile", "", workingDir);
		Common.writeFile(file7, "cjxa");
	}

	@AfterClass 
	public static void executeThisAfterClass(){
		file1.delete();
		file2.delete();
		file3.delete();
		file4.delete();
		file5.delete();
		file6.delete();
		file7.delete();
	}

	@Before
	public void before() throws IOException{
	}

	@After
	public void after(){
		commTool = null;
	}
	
	/**
	 * For comparing files
	 * Both files start with an empty line
	 */
	@Test
	public void compareFilesWithFirstLineEmptyTest() {
		commTool = new CommTool(new String[]{"comm", file6.getName(), file7.getName()});
		String result = 
				"cjwz" +System.lineSeparator()+"\tcjxa"+System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * For comparing files with checking sorted
	 * file 1 is sorted
	 * file 2 is unsorted
	 */
	@Test
	public void executeFilesWithFirstLineEmptyTest() {
		commTool = new CommTool(new String[]{"comm", "-c", file1.getName(), file3.getName()});
		String result = 
				System.lineSeparator()+"aaa"+System.lineSeparator() +
				"\tace"+System.lineSeparator()+
				"comm: File 2 is not in sorted order "+ System.lineSeparator() +
				"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * For comparing files with checking sorted
	 * file 1 is sorted
	 * file 2 is empty
	 */
	@Test
	public void checkingSortedWithSecondFileEmptyTest() {
		commTool = new CommTool(new String[]{"comm", "-c", file2.getName(), file5.getName()});
		String result = System.lineSeparator() +
				"aaf"+System.lineSeparator()+
				"abd"+System.lineSeparator()+
				"ccc"+System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * For comparing files with checking sorted
	 * file 1 is empty
	 * file 2 is sorted
	 */
	@Test
	public void checkingSortedWithFirstFileEmptyTest() {
		commTool = new CommTool(new String[]{"comm", "-c", file5.getName(), file2.getName()});
		String result = "\t"+System.lineSeparator() +
				"\taaf"+System.lineSeparator()+
				"\tabd"+System.lineSeparator()+
				"\tccc"+System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * For comparing files with checking sorted
	 * Both files contain only one line 
	 */
	@Test
	public void checkingSortedBothFilesOneLineTest() {
		commTool = new CommTool(new String[]{"comm", "-c", file6.getName(), file7.getName()});
		String result = 
				"cjwz" +System.lineSeparator()+"\tcjxa"+System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * For comparing files with not checking sorted
	 * Both files contain only one line 
	 */
	@Test
	public void notCheckingSortedBothFilesOneLineTest() {
		commTool = new CommTool(new String[]{"comm", "-d", file7.getName(), file6.getName()});
		String result = 
				"\tcjwz"+System.lineSeparator()+"cjxa" +System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * For comparing files with not checking sorted
	 * file 2 is non-existing 
	 */
	@Test
	public void notCheckingSortedWithNonExistingFileTest() {
		commTool = new CommTool(new String[]{"comm", "-d", file2.getName(), "nonexisting"});
		assertEquals("", commTool.execute(workingDir, ""));
		assertNotEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * For comparing files with not checking sorted
	 * file 2 is empty
	 */
	@Test
	public void notCheckingSortedWithSecondFileEmptyTest() {
		commTool = new CommTool(new String[]{"comm", "-d", file2.getName(), file5.getName()});
		String result = System.lineSeparator() +
				"aaf"+System.lineSeparator()+
				"abd"+System.lineSeparator()+
				"ccc"+System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass null as arguments
	 */
	@Test
	public void passNullToConstructorTest() {
		commTool = new CommTool(null);
		assertEquals(127, commTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass empty array as arguments
	 */
	@Test
	public void passEmptyArgumentToConstructorTest() {
		commTool = new CommTool(new String[]{});
		assertEquals(127, commTool.getStatusCode());
	}
	
	/**
	 * For constructor
	 * pass wrong command as arguments
	 */
	@Test
	public void passWrongArgumentToConstructorTest() {
		commTool = new CommTool(new String[]{"cp"});
		assertEquals(127, commTool.getStatusCode());
	}
	
}
