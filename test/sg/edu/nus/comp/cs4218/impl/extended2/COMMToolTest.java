package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;

public class COMMToolTest {

	private static ICommTool commTool; 
	private static File myFile1, myFile2, myFile3, myFile4;
	public static void writeFile(File file, String s) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(s);
		out.close();
	} 

	@BeforeClass 
	public static void executeThisBeforeClass() throws IOException{
		myFile1 = File.createTempFile("commfile", "sorted");
		writeFile(
				myFile1, 
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"ddd");

		myFile2 = File.createTempFile("commfile", "sorted");
		writeFile(
				myFile2, 
				"aaf"+System.lineSeparator()+"abb"+
				System.lineSeparator()+"ccc"+System.lineSeparator()+"fff");

		//testFile 3 will be the file in unsorted order 
		myFile3 = File.createTempFile("commfile", "unsorted");
		writeFile(
				myFile3, 
				"zzz"+System.lineSeparator()+"ccc"+System.lineSeparator()+
				"aaa"+System.lineSeparator()+"bbb");
		
		//testFile 4 will be the file in unsorted order 
		myFile4 = File.createTempFile("commfile", "unsorted");
		writeFile(
				myFile4, 
				"aaa"+System.lineSeparator()+"ecc"+System.lineSeparator()+
				"eaa"+System.lineSeparator()+"zbb");
	}

	@AfterClass 
	public static void executeThisAfterClass(){
		myFile1.delete();
		myFile2.delete();
		myFile3.delete();
		myFile4.delete();
			
	}

	@Before
	public void before() throws IOException{
		commTool = new CommTool(new String[]{"comm"});
	}

	@After
	public void after(){
		commTool = null;
	}

	//test compareFiles method, with sorted file
	@Test
	public void compareFilesSortedFile() {
		String result = commTool.compareFiles(
				myFile1.getAbsolutePath(), 
				myFile2.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+"\taaf"+System.lineSeparator()+
				"bbb"+System.lineSeparator()+"\tabb"+System.lineSeparator()+
				"\t\tccc"+System.lineSeparator()+"ddd"+System.lineSeparator()+
				"\tfff"+System.lineSeparator(),
				result);
	}

	//test compareFiles method, without sorted file
	@Test
	public void compareFilesUnSortedFile1() {
		String result = commTool.compareFiles(
				myFile3.getAbsolutePath(), 
				myFile1.getAbsolutePath());
		assertEquals( 
				"zzz"+System.lineSeparator()+"\taaa"+System.lineSeparator()+
				"comm: File 1 is not in sorted order "+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"\tbbb"+System.lineSeparator()+
				"aaa"+System.lineSeparator()+"\tccc"+System.lineSeparator()+
				"bbb"+System.lineSeparator()+"\tddd"+System.lineSeparator(),
				result);
	}
	
	//test compareFiles method, without sorted file
		@Test
		public void compareFilesUnSortedFile2() {
			String result = commTool.compareFiles(
					myFile1.getAbsolutePath(), 
					myFile3.getAbsolutePath());
			assertEquals(
					"aaa"+System.lineSeparator()+"\tzzz"+System.lineSeparator()+
					"comm: File 2 is not in sorted order "+System.lineSeparator()+
					"bbb"+System.lineSeparator()+"\tccc"+System.lineSeparator()+
					"ccc"+System.lineSeparator()+"\taaa"+System.lineSeparator()+
					"ddd"+System.lineSeparator()+"\tbbb"+System.lineSeparator(),
					result);
		}
	
	//test compareFiles method, with non-existing file
	@Test
	public void compareNonExistingFiles() throws IOException {
		File nonExist = File.createTempFile("commNonExist", "");

		nonExist.delete();
		assertEquals(false, nonExist.exists());
		String result = commTool.compareFiles(
				myFile1.getAbsolutePath(), 
				nonExist.getAbsolutePath());
		assertEquals("", result);
		assertEquals(3, commTool.getStatusCode());
	}
		
	//test compareFilesCheckSortStatus method, with sorted
	@Test
	public void compareFilesCheckSortStatusSortedFile() throws IOException { 
		String result = commTool.compareFilesCheckSortStatus(
				myFile1.getAbsolutePath(), 
				myFile2.getAbsolutePath());
		System.out.println(result);
		assertEquals(
				"aaa"+System.lineSeparator()+"\taaf"+
				System.lineSeparator()+"bbb"+System.lineSeparator()+"\tabb"+
				System.lineSeparator()+"\t\tccc"+System.lineSeparator()+"ddd"+
				System.lineSeparator()+"\tfff"+System.lineSeparator(),
				result);
	}


	//test compareFilesCheckSortStatus method, one not sorted
	@Test
	public void compareFilesCheckSortStatusOneNotSorted() throws IOException { 
		String result = commTool.compareFilesCheckSortStatus(
				myFile1.getAbsolutePath(), 
				myFile3.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+"\tzzz"+System.lineSeparator()+
				"comm: File 2 is not in sorted order "+System.lineSeparator()+
				"bbb"+System.lineSeparator()+"ccc"+System.lineSeparator()+
				"ddd"+System.lineSeparator(),
				result);
	}
	
	//test compareFilesCheckSortStatus method, neither sorted
	@Test
	public void compareFilesCheckSortStatusBothNotSorted() throws IOException { 
		String result = commTool.compareFilesCheckSortStatus(
				myFile3.getAbsolutePath(), 
				myFile4.getAbsolutePath());
		assertEquals(
				"zzz"+System.lineSeparator()+"\taaa"+System.lineSeparator()+
				"comm: File 1 is not in sorted order "+
				System.lineSeparator()+"\tecc"+System.lineSeparator()+
				"comm: File 2 is not in sorted order "+System.lineSeparator(),
				result);
	}
	
	//test compareFilesCheckSortStatus method, with non-existing file
	@Test
	public void compareFilesCheckSortStatus() throws IOException {
		File nonExist = File.createTempFile("commNonExist", "");
		
		nonExist.delete();
		assertEquals(false, nonExist.exists());
		String result = commTool.compareFiles(
				nonExist.getAbsolutePath(),
				myFile1.getAbsolutePath() 
				);
		assertEquals("", result);
		assertEquals(3, commTool.getStatusCode());
	}
	
	//test compareFilesDoNotCheckSortStatus method, with sorted
	@Test
	public void compareFilesDoNotCheckSortStatusSortedFile() throws IOException { 

		String result = commTool.compareFilesDoNotCheckSortStatus(
				myFile1.getAbsolutePath(), 
				myFile2.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+"\taaf"+System.lineSeparator()+
				"bbb"+System.lineSeparator()+"\tabb"+System.lineSeparator()+
				"\t\tccc"+System.lineSeparator()+"ddd"+System.lineSeparator()+
				"\tfff"+System.lineSeparator(),
				result);
	}
	
	//test compareFilesDoNotCheckSortStatus method, without sorted
	@Test
	public void compareFilesDoNotCheckSortStatusNotSortedFile() throws IOException { 
		String result = commTool.compareFilesDoNotCheckSortStatus(
				myFile1.getAbsolutePath(), 
				myFile3.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+"\tzzz"+System.lineSeparator()+
				"bbb"+System.lineSeparator()+"\tccc"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"\taaa"+System.lineSeparator()+
				"ddd"+System.lineSeparator()+"\tbbb"+System.lineSeparator(),
				result
				);

	}
	
	//test compareFilesDoNotCheckSortStatus method, neither sorted
	@Test
	public void compareFilesDoNotCheckSortStatusNeitherSortedFile() throws IOException { 
		String result = commTool.compareFilesDoNotCheckSortStatus(
				myFile3.getAbsolutePath(), 
				myFile4.getAbsolutePath());
		assertEquals(
				"zzz"+System.lineSeparator()+"\taaa"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"\tecc"+System.lineSeparator()+
				"aaa"+System.lineSeparator()+"\teaa"+System.lineSeparator()+
				"bbb"+System.lineSeparator()+"\tzbb"+System.lineSeparator(),
				result);
	}

	// test getHelp
	@Test
	public void testGetHelp(){
		String commHelp="comm : Compares two sorted files line by line. "
				+ "With no options, produce three-column output. "
				+ "Column one contains lines unique to FILE1, "
				+ "column two contains lines unique to FILE2, "
				+ "and column three contains lines common to both files.\n"
				+ "Command Format - comm [OPTIONS] FILE1 FILE2 \n"
				+ "FILE1 - Name of the file 1\n"
				+ "FILE2 - Name of the file 2\n"
				+ "-c : check that the input is correctly sorted, even if all input lines are pairable \n"
				+ "-d : do not check that the input is correctly sorted\n"
				+ "-help : Brief information about supported options\n";
		assertEquals(commHelp, commTool.getHelp());
		
	}

}