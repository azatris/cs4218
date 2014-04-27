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
	private static File file1, file2, file3, file4, file5, file6;
	private static File workingDir = new File(System.getProperty("user.dir"));

	@BeforeClass 
	public static void executeThisBeforeClass() throws IOException{
		//testFile 1 will be the file in sorted order
		file1 = File.createTempFile("commfile", "sorted", workingDir);
		Common.writeFile(
				file1, 
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"ddd");
		
		//testFile 2 will be the file in sorted order
		file2 = File.createTempFile("commfile", "sorted", workingDir);
		Common.writeFile(
				file2, 
				"aaf"+System.lineSeparator()+"abb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"fff");

		//testFile 3 will be the file in unsorted order 
		file3 = File.createTempFile("commfile", "unsorted", workingDir);
		Common.writeFile(
				file3, 
				"zzz"+System.lineSeparator()+"ccc"+System.lineSeparator()+
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
	}

	@AfterClass 
	public static void executeThisAfterClass(){
		file1.delete();
		file2.delete();
		file3.delete();
		file4.delete();
		file5.delete();
		file6.delete();
	}

	@Before
	public void before() throws IOException{
		commTool = new CommTool(new String[]{"comm"});
	}

	@After
	public void after(){
		commTool = null;
	}
	

	
}
