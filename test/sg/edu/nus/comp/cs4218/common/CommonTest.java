package sg.edu.nus.comp.cs4218.common;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CommonTest {
	Common common;
	
	@Before
	public void before(){
		common = new Common();
	}

	/**
	 * Test creating an absolute directory given the relative directory
	 */
	@Test
	public void testConcatenateDirectory() {
		String absDir = System.getProperty("user.dir");
		String relDir = "dummyDirectory";
		String result = Common.concatenateDirectory(absDir, relDir);
		String expectedResult = absDir + File.separator + relDir + File.separator;
		assertEquals(expectedResult,result);
	}
	
	/**
	 * Test writing a random string to a file
	 */
	@Test
	public void testWriteRandomStringToFile() {
		try {
			String dummyFileName = "dummyfile";
			File dummyFile = new File(dummyFileName);
			String expectedResult = Common.writeRandomStringTo(dummyFile);
			StringBuilder resultBuilder = new StringBuilder();
			FileInputStream fis = new FileInputStream(dummyFile);
			int reader = fis.read();
			while(reader != -1){
				resultBuilder.append((char)reader);
				reader = fis.read();
			}
			fis.close();
			String result = resultBuilder.toString();
			assertEquals(expectedResult,result);
			dummyFile.delete();
		} catch (IOException e) {
			fail();
		}
	}
	
	/**
	 * Test reading a content of the file
	 */
	@Test
	public void testReadByChar(){
		try{
			//Write a dummy content to a file
			String dummyFileName = "dummyfile";
			File dummyFile = new File(dummyFileName);
			String fileContent = "Just a dummy file content\nSecond sentence of dummy file\n";
			FileOutputStream fos = new FileOutputStream(dummyFile);
			DataOutputStream out = new DataOutputStream(fos);
			out.writeBytes(fileContent);
			fos.close();
			out.close();
			String expectedResult = fileContent;
			String result = Common.readFileByChar(dummyFile);
			assertEquals(expectedResult, result);
			dummyFile.delete();
		} catch (IOException e){
			fail();
		}
	}
	
	/**
	 * Test reading a content of the file, but changing the newline characters in the file into platform specific
	 */
	@Test
	public void testReadByLine(){
		try{
			//Write a dummy content to a file
			String dummyFileName = "dummyfile";
			File dummyFile = new File(dummyFileName);
			String fileContent = "Just a dummy file content\nSecond sentence of dummy file\n";
			FileOutputStream fos = new FileOutputStream(dummyFile);
			DataOutputStream out = new DataOutputStream(fos);
			out.writeBytes(fileContent);
			fos.close();
			out.close();
			String expectedResult = "Just a dummy file content"+System.lineSeparator()+"Second sentence of dummy file"+System.lineSeparator();
			String result = Common.readFileByLine(dummyFile);
			assertEquals(expectedResult, result);
			dummyFile.delete();
		} catch (IOException e){
			fail();
		}
	}
}
