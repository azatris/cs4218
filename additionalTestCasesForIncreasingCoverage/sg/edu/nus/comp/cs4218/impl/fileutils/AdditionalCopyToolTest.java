package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;

public class AdditionalCopyToolTest {
	private ICopyTool copyTool;
	private File from;
	File workingDir;
	
	@Before
	public void setUp() throws Exception {
		workingDir = new File(System.getProperty("user.dir"));
		from = File.createTempFile("from","copytmp");
		Common.writeRandomStringTo(from);
	}

	@After
	public void tearDown() throws Exception {
		copyTool = null;
		from.delete();
	}
	
	@Test
	public void copyFileToSamePlace() {
		String[] args = {"copy", from.getAbsolutePath(), from.getAbsolutePath()};
		copyTool = new CopyTool(args);
		copyTool.execute(workingDir, null);
		assertEquals(211, copyTool.getStatusCode());	
	}

	@Test
	public void passNullToConstructorTest() {
		copyTool = new CopyTool(null);
		assertEquals(127, copyTool.getStatusCode());
	}
	
	@Test
	public void passEmptyArgumentToConstructorTest() {
		copyTool = new CopyTool(new String[]{});
		assertEquals(127, copyTool.getStatusCode());
	}
	
	@Test
	public void passWrongArgumentToConstructorTest() {
		copyTool = new CopyTool(new String[]{"cp"});
		assertEquals(127, copyTool.getStatusCode());
	}

}
