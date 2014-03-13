package sg.edu.nus.comp.cs4218.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class ChangeingStateTest {
	private static String testDataFileName;
	private static String testData;
	private static Properties prop;
    private static File testdir;
    private static File testdir2;
    private static String mapName= "." + File.separator+"ost";
    private static String mapname2="."+ File.separator+"ost" + File.separator + "sleep"; 
    private static String testfile ="."+ File.separator+"ost" + File.separator + "asd";
    private static String umapName = File.separator+"ost";
    private ITool cd;
    private ITool copy;
    private ITool another;
    private ITool ls;
    private Shell shell ;
    private static File testfil;
	File workingDirectory = new File(System.getProperty("user.dir"));
   
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Loading the test data from the properties file.
		prop = new Properties();
		try {
			prop.load(new FileInputStream("test.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		testData = prop.getProperty("testData");
		// Creating a file to store the test data.
		testdir = new File(mapName);
		testdir.mkdir();
		testdir2= new File(mapname2);
		testdir2.mkdir();
	}
    
    @Before
    public void setUp() throws Exception{
    	File testfil = new File(testfile);
		if (!testfil.isFile()) {
			PrintWriter writer = new PrintWriter(testfil, "UTF-8");
			writer.println("asd");
			writer.close();
		}
		System.out.println("***[HERE STARTS UNAVODIBLE OUTPUTSECTION]***\n"); //NOPMD
    }

    @After
    public void tearDown() throws Exception{
    	System.out.println("***[HERE END UNAVODIBLE OUTPUTSECTION]***\n"); //NOPMD
  
    }
    @AfterClass
    public static void tearDownClass() throws Exception {
	deleteDirectory(testdir);
	}
   
    
    /**
    * First Test for changing state.
    * cd->cd->cd->ls 
    * @throws InterruptedException
    */
    @Test
    public void changeStatePoss1() throws InterruptedException {
		shell = new Shell();
		cd = new CdTool(new String[] {"cd", mapName});
		Thread asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		another = new CdTool(new String []{ "cd", ".."});
		asd = (Thread) shell.execute(another);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		cd = new CdTool(new String[] {"cd", mapname2});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		ITool ls = new LsTool(new String[] {"ls"});
		String output =System.getProperty("user.dir") + File.separator+ mapname2;
		assertEquals("error1", "",(ls.execute(new File(output), null)));
		ITool pwd = new PWDTool(new String[]{"pwd"});
		assertEquals("error2", output, (pwd.execute(testdir2, "")));
	} 
 
    /**
     * Second Test for changing state.
     * cd->copy->cd->cd->ls 
     * @throws InterruptedException
     */
	@Test
	public void changeStatePoss2() throws InterruptedException {
		shell = new Shell();
		cd = new CdTool(new String[] {"cd", mapName});
		Thread asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		copy = new CopyTool(new String []{"copy","asd", "hallo"});
		asd = (Thread) shell.execute(copy);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new CdTool(new String[] {"cd", ".."});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new CdTool(new String[] {"cd", mapName});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new LsTool(new String[] {"ls"});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		ITool ls = new LsTool(new String[] {"ls"});
		String output =System.getProperty("user.dir")+ umapName;
		String answer = ls.execute(new File(output), null);
		System.out.println(answer);
		assertEquals("error", "asd	hallo	sleep\\	",answer);
		ITool pwd = new PWDTool(new String[]{"pwd"});
		assertEquals("error", output, (pwd.execute(new File(output), "")));
	}
	/**
     * Third Test for changing state.
     * cd->copy->cd->cd->delete->ls 
     * @throws InterruptedException
     */
	
	@Test
	public void changeStatePoss3() throws InterruptedException {
		shell = new Shell();
		cd = new CdTool(new String[] {"cd", mapName});
		Thread asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		copy = new CopyTool(new String []{"copy","asd", "hallo"});
		asd = (Thread) shell.execute(copy);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new CdTool(new String[] {"cd", ".."});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		cd = new CdTool(new String[] {"cd", mapName});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		ITool delete = new DeleteTool(new String []{"delete","asd"});
		asd = (Thread) shell.execute(delete);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new LsTool(new String[] {"ls"});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		ITool ls = new LsTool(new String[] {"ls"});
		String output =System.getProperty("user.dir")+ umapName;
		String answer = ls.execute(new File(output), null);
		assertEquals("error", "hallo	sleep\\	",answer);
		ITool pwd = new PWDTool(new String[]{"pwd"});
		assertEquals("error", output, (pwd.execute(new File(output), "")));		
	}
	/**
     * Fouth Test for changing state.
     * cd->copy->cd->cd->delete->copy->ls 
     * @throws InterruptedException
     */
	@Test
	public void changeStatePoss4() throws InterruptedException {
		System.out.println("test4");
		shell = new Shell();
		cd = new CdTool(new String[] {"cd", mapName});
		Thread asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		copy = new CopyTool(new String []{"copy","asd", "hallo"});
		asd = (Thread) shell.execute(copy);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new CdTool(new String[] {"cd", ".."});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new CdTool(new String[] {"cd", mapName});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		ITool delete = new DeleteTool(new String []{"delete","asd"});
		asd = (Thread) shell.execute(delete);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		copy = new CopyTool(new String []{"copy","hallo","newfile"});
		asd = (Thread) shell.execute(copy);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new LsTool(new String[] {"ls"});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		ls = new LsTool(new String[] {"ls"});
		String output =System.getProperty("user.dir")+ umapName;
		String answer = ls.execute(new File(output), null);
		assertEquals("error", "hallo	newfile	sleep\\	",answer);
		ITool pwd = new PWDTool(new String[]{"pwd"});
		assertEquals("error", output, (pwd.execute(new File(output), "")));		
	}

	/**
     * Test changing state to a directory that doesn't exist
     * cd->cd(Not existing)->copy->cd->cd(non existing) 
     * @throws InterruptedException
     */
	@Test
	public void changeStateDirNotExist() throws InterruptedException{
	
		shell = new Shell();
		cd = new CdTool(new String[] {"cd", mapName});
		Thread asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new CdTool(new String[] {"cd", "SomewhereELSE"});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		copy = new CopyTool(new String []{"copy","asd", "hallo"});
		asd = (Thread) shell.execute(copy);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new CdTool(new String[] {"cd", ".."});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		cd = new CdTool(new String[] {"cd", "SomewhereELSE"});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		// Shouldn't go to this folder don't exists
		
		ITool ls = new LsTool(new String[] {"ls"});
		String output =System.getProperty("user.dir")+ umapName;
		String answer = ls.execute(new File(output), null);
		System.out.println(answer);
		assertEquals("error", "asd	hallo	sleep\\	",answer);
		ITool pwd = new PWDTool(new String[]{"pwd"});
		assertEquals("error", output, (pwd.execute(new File(output), "")));
		
	}
	
	/**
     * Test changing state by copying a file that doesn't exist.
     * cd->copy->cd->copy(non existing file) 
     * @throws InterruptedException
     */
	@Test
	public void changeStatFileCopyFileNotExisting() throws InterruptedException{
		shell = new Shell();
		
		cd = new CdTool(new String[] {"cd", mapName});
		Thread asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		copy = new CopyTool(new String []{"copy","asd", "hallo"});
		asd = (Thread) shell.execute(copy);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		cd = new CdTool(new String[] {"cd", ".."});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		cd = new CdTool(new String[] {"cd", mapName});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		copy = new CopyTool(new String[] {"copy", "fileNotExist", "dontmatter"});
		asd = (Thread) shell.execute(cd);
		while(asd.isAlive()){
			Thread.sleep(1000);
		}
		
		// Shouldn't go to this folder don't exists
		
		ITool ls = new LsTool(new String[] {"ls"});
		String output =System.getProperty("user.dir")+ umapName;
		String answer = ls.execute(new File(output), null);
		System.out.println(answer);
		assertEquals("error", "asd	hallo	sleep\\	",answer);
		ITool pwd = new PWDTool(new String[]{"pwd"});
		assertEquals("error", output, (pwd.execute(new File(output), "")));
		
	}
	
    /**
     * recusive delete from Stackoverflow
     * @param directory
     * @return
     */
	public static boolean deleteDirectory(File directory) {
		if(directory.exists()){
			File[] files = directory.listFiles();
			if(null!=files){
				for(int i=0; i<files.length; i++) {
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					}
					else {
						files[i].delete();
					}
				}
			}
		}
		return(directory.delete());
	}
}