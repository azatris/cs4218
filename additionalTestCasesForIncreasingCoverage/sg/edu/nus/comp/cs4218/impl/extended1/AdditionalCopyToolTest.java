package sg.edu.nus.comp.cs4218.impl.extended1;

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
		assertTrue(copyTool.getStatusCode() == 211);
		
	}

}
