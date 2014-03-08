package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;

public class MoveToolTest {
	private IMoveTool moveTool;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		moveTool = null;
	}
	
	/*Testing boolean move(File from, File to)*/
	@Test
	public void renameFileToExistFileTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = Files.createTempFile(
				new File(from.getParent()).toPath(), 
				"to", 
				"movetmp"
				).toFile();
		String[] args = {"move", from.getAbsolutePath(), to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		String fromStr = Common.writeRandomStringTo(from);
		String toStr = Common.writeRandomStringTo(to);

		assertFalse(fromStr.equals(toStr)); // It does have very small possibility that fromStr=toStr
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertEquals(fromStr, Common.readFile(to));

		to.delete();
	}
	
	public void renameFileToNonExistFileTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = File.createTempFile("to","movetmp", new File(from.getParent()));
		String fromStr = Common.writeRandomStringTo(from);
		String[] args = {"move", from.getAbsolutePath(), to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		to.delete();
		assertTrue(from.exists());
		assertFalse(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertEquals(fromStr, Common.readFile(to));

		to.delete();
	}
	
	@Test
	public void renameFileToItselfTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		Common.writeRandomStringTo(from);
		String[] args = {"move", from.getAbsolutePath(), from.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		assertTrue(from.exists());
		assertTrue(moveTool.move(from, new File(from.getAbsolutePath())));
		assertEquals(0, moveTool.getStatusCode());
		assertTrue(from.exists());
		
		from.delete();
	}
	
	@Test
	public void moveFileToFileInDifferentDirTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = Files.createTempFile(
				new File(from.getParent()).toPath(), 
				"to", 
				"movetmp"
				).toFile();
		String fromStr = Common.writeRandomStringTo(from);
		String toStr = Common.writeRandomStringTo(to);
		String[] args = {"move", from.getAbsolutePath(), to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		assertFalse(fromStr.equals(toStr)); // It does have very small possibility that fromStr=toStr
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertEquals(fromStr, Common.readFile(to));

		to.delete();	
	}
	
	@Test
	public void moveFileToDifferentDirTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		String fromStr = Common.writeRandomStringTo(from);
		String[] args = {"move", from.getAbsolutePath(), to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());

		File movedFile = new File(to.getAbsolutePath()+File.separator+from.getName());
		assertEquals(
				fromStr, 
				Common.readFile(movedFile)
				);
		movedFile.delete();
		to.delete();
	}
	
	@Test
	public void moveFileToNonExistDirTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		String fromStr = Common.writeRandomStringTo(from);
		String[] args = {"move", from.getAbsolutePath(), to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		to.delete();
		assertTrue(from.exists());
		assertFalse(to.exists());
		assertTrue(moveTool.move(from, to));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertEquals(fromStr, Common.readFile(to));
		
		File movedFile = new File(to.getAbsolutePath()+File.separator+from.getName());
		movedFile.delete();
		to.delete();
	}
	
	@Test
	public void moveFileToNullTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		Common.writeRandomStringTo(from);
		String[] args = {"move", from.getAbsolutePath(), null};
		moveTool = new MoveTool(args);
		
		assertTrue(from.exists());
		assertFalse(moveTool.move(from, null));
		assertTrue(moveTool.getStatusCode()!=0);
		assertTrue(from.exists());

		from.delete();
	}
	
	@Test
	public void moveNonExistFileTest() throws IOException {
		File from = File.createTempFile("from","movetmp");
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		Common.writeRandomStringTo(from);
		String[] args = {"move", from.getAbsolutePath(), to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		from.delete();
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertFalse(moveTool.move(from, to));
		assertTrue(moveTool.getStatusCode()!=0);

		to.delete();
	}
	
	@Test
	public void moveDirTest() throws IOException {
		File from = Files.createTempDirectory("fromdirmovetmp").toFile();
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		String[] args = {"move", from.getAbsolutePath(), to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		assertTrue(from.exists());
		assertTrue(to.exists());
		assertFalse(moveTool.move(from, to));
		assertTrue(moveTool.getStatusCode()!=0);
		assertTrue(from.exists());
		assertTrue(to.exists());

		from.delete();
		to.delete();
	}
	
	@Test
	public void moveNonExistDirTest() throws IOException {
		File from = Files.createTempDirectory("fromdirmovetmp").toFile();
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		String[] args = {"move", from.getAbsolutePath(), to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		from.delete();
		assertFalse(from.exists());
		assertTrue(to.exists());
		assertFalse(moveTool.move(from, to));
		assertTrue(moveTool.getStatusCode()!=0);

		to.delete();
	}
	
	@Test
	public void moveNullTest() throws IOException {
		File to = Files.createTempDirectory("todirmovetmp").toFile();
		String[] args = {"move", null, to.getAbsolutePath()};
		moveTool = new MoveTool(args);
		
		assertTrue(to.exists());
		assertFalse(moveTool.move(null, to));
		assertTrue(moveTool.getStatusCode()!=0);

		to.delete();
	}
	
	/*String execute(File workingDir, String stdin)*/
	// move file1 file2 - in current directory
	@Test
	public void moveFileInCurDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","movetmp", workingDir);
		Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","movetmp", workingDir);
		Common.writeRandomStringTo(to);
		String[] args = {"move", from.getName(), to.getName()};
		moveTool = new MoveTool(args);

		assertNull(moveTool.execute(workingDir, null));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		
		to.delete();
	}

	// move ../file1 ../file2
	@Test
	public void moveFileInParentDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","movetmp", workingDir.getParentFile());
		Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","movetmp", workingDir.getParentFile());
		Common.writeRandomStringTo(to);
		String[] args = {
				"move",
				".." + File.separator + from.getName(), 
				".." + File.separator + to.getName()
		};
		moveTool = new MoveTool(args);

		assertNull(moveTool.execute(workingDir, null));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		
		to.delete();
	}

	// move dir1/file1 dir2/file2
	@Test
	public void moveFileInChildDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File fromChild = Files.createTempDirectory(workingDir.toPath(), "fromChild").toFile();
		File toChild = Files.createTempDirectory(workingDir.toPath(), "toChild").toFile();
		File from = File.createTempFile("from","movetmp", fromChild);
		Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","movetmp", toChild);
		Common.writeRandomStringTo(to);

		String[] args = {
				"move", 
				fromChild.getName() + File.separator + from.getName(), 
				toChild.getName() + File.separator + to.getName()
		};
		moveTool = new MoveTool(args);

		assertNull(moveTool.execute(workingDir, null));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());

		to.delete();
		fromChild.delete();
		toChild.delete();
	}

	// move abs_path_file1 abs_path_file2
	@Test
	public void moveAbsPathTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","movetmp");
		Common.writeRandomStringTo(from);
		File to = File.createTempFile("tofile","movetmp");
		Common.writeRandomStringTo(to);

		String[] args = {
				"move", 
				from.getAbsolutePath(), 
				to.getAbsolutePath()
		};
		moveTool = new MoveTool(args);

		assertNull(moveTool.execute(workingDir, null));
		assertEquals(0, moveTool.getStatusCode());
		assertFalse(from.exists());
		
		to.delete();
	}

	// move file1
	public void moveNoToDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File from = File.createTempFile("from","movetmp");
		Common.writeRandomStringTo(from);

		String[] args = {
				"move", 
				from.getAbsolutePath(),
		};
		moveTool = new MoveTool(args);

		assertFalse(moveTool.execute(workingDir, null).equals(null));
		assertTrue(moveTool.getStatusCode() != 0);

		from.delete();
	}
}
