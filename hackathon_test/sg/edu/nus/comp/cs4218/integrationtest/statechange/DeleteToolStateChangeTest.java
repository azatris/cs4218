package sg.edu.nus.comp.cs4218.integrationtest.statechange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.Shell;

public class DeleteToolStateChangeTest {

	private File file1, file2, dir;
	private String userDir;
	
	@Before
	public void setUpOnce() throws Exception {
		
		// create files with content
		createFile1();
		createFile2();
		
		// create directory
		dir = new File("dir");
		dir.mkdir();
		
		userDir = System.getProperty("user.dir");
	}
	
	private void createFile1() throws Exception {
		BufferedWriter bw;
		String content;
		
		file1 = new File("file1");
		file1.createNewFile();
	
		content = "this is file1 used for testing"+System.lineSeparator();
		content += "there are 5 lines in this file" + System.lineSeparator();
		content += "this is the 3rd line" + System.lineSeparator();
		content += "this is the 4th line" + System.lineSeparator();
		content += "this is the 5th line";
		
		bw = new BufferedWriter(new FileWriter(file1));
		bw.write(content);
		bw.close();
	}
	
	private void createFile2() throws Exception {
		BufferedWriter bw;
		String content;
		
		file2 = new File("file2");
		file2.createNewFile();
		content = "this is file2 used for testing"+System.lineSeparator();
		content += "testing testing 1 2 3"+System.lineSeparator();
		content += "	???	"+System.lineSeparator()+System.lineSeparator();
		
		bw = new BufferedWriter(new FileWriter(file2));
		bw.write(content);
		bw.close();
	}

	@After
	public void tearDown() throws Exception {
		file1.delete();
		file2.delete();
		dir.delete();
		System.setProperty("user.dir", userDir);
	}
	
	
	
	@Test
	public void stateChange_LsDeleteLs_GetStatusCode0() throws InterruptedException {
		String commandline1, commandline2, commandline3;
		Shell shell = new Shell();
			
		commandline1 = "ls";
		commandline2 = "delete file1 file2";
		commandline3 = "ls";
			
		// 1. check that ls initially contains file1 and file2
		ITool tool = shell.parse(commandline1);
		Thread asd = (Thread) shell.execute(tool);
		while(asd.isAlive()){
			Thread.sleep(100);
		}
		assertEquals(0, tool.getStatusCode());
			
			
		// 2. delete file1 and file2
		// 3. check if ls output contained the files deleted
		tool = shell.parse(commandline2);
		asd = (Thread) shell.execute(tool);
		while(asd.isAlive()){
			Thread.sleep(100);
		}
		assertEquals(0, tool.getStatusCode());
			
		tool = shell.parse(commandline3);
		asd = (Thread) shell.execute(tool);
		while(asd.isAlive()){
			Thread.sleep(100);
		}
		assertEquals(0, tool.getStatusCode());
			
		assertFalse(file1.exists());
		assertFalse(file2.exists());
		// recreate the files that have been deleted
		try {
			createFile1();
			createFile2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
