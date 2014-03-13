package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;

public class CopyToolTest {
	private ICopyTool copyTool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		copyTool = null;
	}

	/**
	 * Testing boolean copy(File from, File to)
	 * @throws IOException
	 */
	@Test
	public void copyFileToExistFileTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		File to = File.createTempFile("tofile","copytmp");
		String[] args = {"copy", from.getAbsolutePath(), to.getAbsolutePath()};
		copyTool = new CopyTool(args);
		String fromStr = Common.writeRandomStringTo(from);
		String toStr = Common.writeRandomStringTo(to);

		assertFalse(fromStr.equals(toStr)); // It does have very small possibility that fromStr=toStr
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(copyTool.copy(from, to));
		assertEquals(0, copyTool.getStatusCode());
		assertEquals(fromStr, Common.readFileByChar(to));

		from.delete();
		to.delete();
	}

	// TODO
	@Test
	public void copyFileToNonExistFileTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		String fromStr = Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","copytmp");
		String[] args = {"copy", from.getAbsolutePath(), to.getAbsolutePath()};
		copyTool = new CopyTool(args);

		to.delete();
		assertFalse(to.exists());
		assertTrue(from.exists());
		assertTrue(copyTool.copy(from, to));
		assertEquals(0, copyTool.getStatusCode());
		assertEquals(fromStr, Common.readFileByChar(to));

		from.delete();
		to.delete();
	}

	// TODO
	@Test
	public void copyFileToExistDirTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		File to = Files.createTempDirectory("tocopytmp").toFile();
		String fromStr = Common.writeRandomStringTo(from);
		String[] args = {"copy", from.getAbsolutePath(), to.getAbsolutePath()};
		copyTool = new CopyTool(args);

		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(copyTool.copy(from, to));
		assertEquals(0, copyTool.getStatusCode());
		File toFile = new File(to.getAbsolutePath() + File.separator +from.getName());
		assertEquals(fromStr, Common.readFileByChar(toFile));

		from.delete();
		toFile.delete();
		to.delete();
	}

	// TODO
	@Test
	public void copyFileToNonExistDirTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		String fromStr = Common.writeRandomStringTo(from);
		File to = Files.createTempDirectory("tononexistcopytmp").toFile();
		String[] args = {"copy", from.getAbsolutePath(), to.getAbsolutePath()};
		copyTool = new CopyTool(args);
		to.delete();

		assertFalse(to.exists());
		assertTrue(from.exists());
		assertTrue(copyTool.copy(from, to));
		assertEquals(0, copyTool.getStatusCode());
		assertEquals(fromStr, Common.readFileByChar(to));
		(new File(args[2] + File.separator + from.getName())).delete();
		from.delete();
		to.delete();
	}

	// TODO
	@Test
	public void copyFileToNullDirTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		Common.writeRandomStringTo(from);
		String[] args = {"copy", from.getAbsolutePath()};
		copyTool = new CopyTool(args);

		assertTrue(from.exists());
		assertFalse(copyTool.copy(from, null));
		assertTrue(copyTool.getStatusCode()!= 0);

		from.delete();
	}

	// TODO
	@Test
	public void copyNonExistFileTest() throws IOException {
		File from = File.createTempFile("from","copytmp");
		File to = File.createTempFile("tofile","copytmp");
		Common.writeRandomStringTo(from);
		Common.writeRandomStringTo(to);
		String[] args = {"copy", from.getAbsolutePath(), to.getAbsolutePath()};
		copyTool = new CopyTool(args);

		from.delete();
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertFalse(copyTool.copy(from, to));
		assertTrue(copyTool.getStatusCode() != 0);

		to.delete();
	}

	// TODO
	@Test
	public void copyExistDirTest() throws IOException {
		File from = Files.createTempDirectory("fromFoldercopytmp").toFile();
		File to = File.createTempFile("tofile","copytmp");
		Common.writeRandomStringTo(to);
		String[] args = {"copy", from.getAbsolutePath(), to.getAbsolutePath()};
		copyTool = new CopyTool(args);

		assertTrue(from.exists());
		assertTrue(to.exists());
		assertFalse(copyTool.copy(from, to));
		assertTrue(copyTool.getStatusCode() != 0);

		from.delete();
		to.delete();
	}

	// TODO
	@Test
	public void copyNonExistDirTest() throws IOException {
		File from = Files.createTempDirectory("fromFoldercopytmp").toFile();
		File to = File.createTempFile("tofile","copytmp");
		Common.writeRandomStringTo(to);
		String[] args = {"copy", from.getAbsolutePath(), to.getAbsolutePath()};
		copyTool = new CopyTool(args);

		from.delete();
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertFalse(copyTool.copy(from, to));
		assertTrue(copyTool.getStatusCode()!=0);

		to.delete();
	}

	// TODO
	@Test
	public void copyExecuteNullTest() {
		String[] args = {"copy"};
		copyTool = new CopyTool(args);
		assertFalse(copyTool.copy(null, null));
		assertTrue(copyTool.getStatusCode()!=0);
	}

	/**
	 * String execute(File workingDir, String stdin)
	 * copy file1 file2 - in current directory
	 */
	@Test
	public void copyFileInCurDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","copytmp", workingDir);
		Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","copytmp", workingDir);
		Common.writeRandomStringTo(to);
		String[] args = {"copy", from.getName(), to.getName()};
		copyTool = new CopyTool(args);

		assertEquals("", copyTool.execute(workingDir, ""));
		assertEquals(0, copyTool.getStatusCode());

		from.delete();
		to.delete();
	}

	/**
	 * copy ../file1 ../file2
	 * @throws IOException
	 */
	@Test
	public void copyFileInParentDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","copytmp", workingDir.getParentFile());
		Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","copytmp", workingDir.getParentFile());
		Common.writeRandomStringTo(to);
		String[] args = {
				"copy",
				".." + File.separator + from.getName(), 
				".." + File.separator + to.getName()
		};
		copyTool = new CopyTool(args);

		assertEquals("", copyTool.execute(workingDir, ""));
		assertEquals(0, copyTool.getStatusCode());

		from.delete();
		to.delete();
	}

	/**
	 * copy dir1/file1 dir2/file2
	 * @throws IOException
	 */
	@Test
	public void copyFileInChildDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File fromChild = Files.createTempDirectory(workingDir.toPath(), "fromChild").toFile();
		File toChild = Files.createTempDirectory(workingDir.toPath(), "toChild").toFile();
		File from = File.createTempFile("from","copytmp", fromChild);
		Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","copytmp", toChild);
		Common.writeRandomStringTo(to);

		String[] args = {
				"copy", 
				toChild.getName() + File.separator + to.getName(), 
				toChild.getName() + File.separator + to.getName()
		};
		copyTool = new CopyTool(args);

		assertEquals("", copyTool.execute(workingDir, ""));
		assertEquals(0, copyTool.getStatusCode());

		from.delete();
		to.delete();
		fromChild.delete();
		toChild.delete();
	}

	/**
	 * copy abs_path_file1 abs_path_file2
	 * @throws IOException
	 */
	@Test
	public void copyAbsPathTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","copytmp");
		Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","copytmp");
		Common.writeRandomStringTo(to);

		String[] args = {
				"copy", 
				from.getAbsolutePath(), 
				to.getAbsolutePath()
		};
		copyTool = new CopyTool(args);

		assertEquals("", copyTool.execute(workingDir, ""));
		assertEquals(0, copyTool.getStatusCode());

		from.delete();
		to.delete();
	}

	/**
	 * copy file1
	 * @throws IOException
	 */
	public void copyNoToDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","copytmp");
		Common.writeRandomStringTo(from);

		String[] args = {
				"copy", 
				from.getAbsolutePath(),
		};
		copyTool = new CopyTool(args);

		assertFalse(copyTool.execute(workingDir, null).equals(null));
		assertTrue(copyTool.getStatusCode() != 0);

		from.delete();
	}

	/**
	 * copy file1 file2 file3
	 * @throws IOException
	 */
	public void copyToTwoDestiantionTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","copytmp");
		Common.writeRandomStringTo(from);
		File to1 = File.createTempFile("tofile","copytmp");
		File to2 = File.createTempFile("tofile","copytmp");

		String[] args = {
				"copy", 
				from.getAbsolutePath(), 
				to1.getAbsolutePath(),
				to2.getAbsolutePath()
		};
		copyTool = new CopyTool(args);

		assertFalse(copyTool.execute(workingDir, null).equals(null));
		assertTrue(copyTool.getStatusCode() != 0);

		from.delete();
		to1.delete();
		to2.delete();
	}
}
