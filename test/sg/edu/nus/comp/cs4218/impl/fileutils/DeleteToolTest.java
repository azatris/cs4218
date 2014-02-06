package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;

public class DeleteToolTest {
	private IDeleteTool deleteTool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		deleteTool = null;
	}

	// Testing boolean delete(File toDelete)
	@Test
	public void deletFileTest() throws IOException {
		File fileToDelete = File.createTempFile("exists", "deltmp");
		String[] args = {"delete", fileToDelete.getAbsolutePath()};
		deleteTool = new DeleteTool(args);
		
		assertTrue(fileToDelete.isFile());
		assertTrue(deleteTool.delete(fileToDelete));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(fileToDelete.isFile());
	}

	@Test
	public void deleteDirectoryWithFilesTest() throws IOException {
		File fileInFolder = File.createTempFile("exists", "deltmp");
		File dirToDelete = fileInFolder.getParentFile();
		String[] args = {"delete", dirToDelete.getAbsolutePath()};
		deleteTool = new DeleteTool(args);

		assertTrue(dirToDelete.isDirectory());
		assertFalse(deleteTool.delete(dirToDelete));
		assertTrue(dirToDelete.isDirectory());
		fileInFolder.delete();
		dirToDelete.delete();
	}

	@Test
	public void deleteDirectoryWithoutFilesTest() throws IOException {
		File emptyDir = Files.createTempDirectory("deltmpfolder").toFile();
		String[] args = {"delete", emptyDir.getAbsolutePath()};
		deleteTool = new DeleteTool(args);

		assertTrue(emptyDir.isDirectory());
		assertTrue(deleteTool.delete(emptyDir));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(emptyDir.exists());
	}

	@Test
	public void deleteNonExistingFileTest() throws IOException {
		String filePathStr = File.createTempFile("nonExists", "deltmp").getAbsolutePath();
		File fileToDelete = new File(filePathStr);
		String[] args = {"delete", fileToDelete.getAbsolutePath()};
		deleteTool = new DeleteTool(args);
		
		fileToDelete.delete();
		assertFalse(fileToDelete.exists());
		assertFalse(deleteTool.delete(fileToDelete));
		assertFalse(deleteTool.getStatusCode() == 0);
	}

	@Test
	public void deleteNonExistingDirectoryTest() throws IOException {
		File nonExistingDir = Files.createTempDirectory("deltmpfolder").toFile();
		String[] args = {"delete", nonExistingDir.getAbsolutePath()};
		deleteTool = new DeleteTool(args);
		
		nonExistingDir.delete();
		assertFalse(nonExistingDir.exists());
		assertFalse(deleteTool.delete(nonExistingDir));
		assertFalse(deleteTool.getStatusCode() == 0);
		assertFalse(nonExistingDir.exists());
	}

	@Test
	public void deleteNullDirectoryTest() {
		String[] args = {"delete", null};
		deleteTool = new DeleteTool(args);

		assertFalse(deleteTool.delete(null));
		assertFalse(deleteTool.getStatusCode() == 0);
	}

	/*String execute(File workingDir, String stdin)*/
	//delete file1
	@Test
	public void deleteOneInCurDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File fileToDelete = File.createTempFile("exists", "deltmp", workingDir);
		String[] args = {"delete", fileToDelete.getName()};
		deleteTool = new DeleteTool(args);

		assertNull(deleteTool.execute(workingDir, null));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(fileToDelete.exists());
	}

	//delete ../file1
	@Test
	public void deleteOneInParentDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File fileToDelete = File.createTempFile("exists", "deltmp", workingDir.getParentFile());
		String[] args = {"delete", ".." + File.separator + fileToDelete.getName()};
		deleteTool = new DeleteTool(args);

		assertNull(deleteTool.execute(workingDir, null));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(fileToDelete.exists());
	}

	//delete dir1/file1
	@Test
	public void deleteOneInChildDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File folder = Files.createTempDirectory(workingDir.toPath(), "folderdeltmp").toFile();
		File fileToDelete = File.createTempFile("exists", "deltmp", folder);
		String[] args = {"delete", folder.getName() + File.separator + fileToDelete.getName()};
		deleteTool = new DeleteTool(args);

		assertNull(deleteTool.execute(workingDir, null));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(fileToDelete.exists());
		folder.delete();
	}

	//delete abs_path_file1
	@Test
	public void deleteOneInAbsDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File fileToDelete = File.createTempFile("exists", "deltmp");
		String[] args = {"delete", fileToDelete.getAbsolutePath()};
		deleteTool = new DeleteTool(args);

		assertNull(deleteTool.execute(workingDir, null));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(fileToDelete.exists());
	}

	//delete file1 file2 file3
	@Test
	public void deleteThreeInCurDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File fileToDelete1 = File.createTempFile("exists", "deltmp", workingDir);
		File fileToDelete2 = File.createTempFile("exists", "deltmp", workingDir);
		File fileToDelete3 = File.createTempFile("exists", "deltmp", workingDir);
		String[] args = {"delete", fileToDelete1.getName(), fileToDelete2.getName(), fileToDelete3.getName()};
		deleteTool = new DeleteTool(args);

		assertNull(deleteTool.execute(workingDir, null));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(fileToDelete1.exists());
		assertFalse(fileToDelete2.exists());
		assertFalse(fileToDelete3.exists());
	}

	//delete file1 non_exist_file1 file3
	@Test
	public void deleteOneNonExistInCurDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File fileToDelete1 = File.createTempFile("exists", "deltmp", workingDir);
		File fileToDelete2 = File.createTempFile("exists", "deltmp", workingDir);
		File fileToDelete3 = File.createTempFile("exists", "deltmp", workingDir);
		String[] args = {"delete", fileToDelete1.getName(), fileToDelete2.getName(), fileToDelete3.getName()};
		deleteTool = new DeleteTool(args);
		
		fileToDelete2.delete();
		assertFalse(fileToDelete2.exists());
		assertFalse(deleteTool.execute(workingDir, null).equals(null));
		assertTrue(deleteTool.getStatusCode() != 0);
		assertFalse(fileToDelete1.exists());
		assertFalse(fileToDelete2.exists());
		assertFalse(fileToDelete3.exists());
	}

	//delete empty_dir
	@Test
	public void deleteEmptyDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File emptyFolder = Files.createTempDirectory(workingDir.toPath(), "folderdeltmp").toFile();
		String[] args = {"delete", emptyFolder.getName()};
		deleteTool = new DeleteTool(args);

		assertNull(deleteTool.execute(workingDir, null));
		assertEquals(0, deleteTool.getStatusCode());
		assertFalse(emptyFolder.exists());
	}

	//delete non_empty_dir
	@Test
	public void deleteNonEmptyDirTest() throws IOException {
		File workingDir = new File(System.getProperty("user.dir"));
		File folder = Files.createTempDirectory(workingDir.toPath(), "folderdeltmp").toFile();
		File fileInFolder = File.createTempFile("exists", "deltmp", folder);
		String[] args = {"delete", folder.getName()};
		deleteTool = new DeleteTool(args);

		assertFalse(deleteTool.execute(workingDir, null).equals(null));
		assertTrue(deleteTool.getStatusCode() != 0);
		assertTrue(folder.exists());
		fileInFolder.delete();
		folder.delete();
	}
}
