/**
 * Assumption(s) Made: 
 * The Move command only supports the following functions:
 * 
 * 1. Move file1 to file2
 * 2. Move directory dir1 to dir2
 * 3. Move multiple files into directory
 *
 * These functions are tested in the test cases below.
 * 
 */
package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;

public class MoveToolExtraTest {
 // Variables
 private IMoveTool moveTool;
 private File workingDir;
 private File sourceDir;
 private File destDir;

 @Before
 public void setUp() throws Exception {
  this.moveTool = new MoveTool(null);
  this.workingDir = new File(System.getProperty("user.dir"));
  this.sourceDir = createDir("_source_dir");
  this.destDir = createDir("_dest_dir");
 }

 @After
 public void tearDown() throws Exception {
  this.moveTool = null;

  this.removeFiles(this.sourceDir);
  this.removeFiles(this.destDir);
  this.sourceDir.delete();
  this.destDir.delete();

  this.workingDir = null;
  this.sourceDir = null;
  this.destDir = null;
 }

 // Black Box Positive Testing

 @Test
 public void execute_renameDirectoryTest() throws IOException {

  File from = createDir("_source_dir");
  File to = new File("renamedDir");

  String[] args = { "move", from.toString(), to.toString() };

  this.moveTool = new MoveTool(args);
  String returnMessage = this.moveTool.execute(workingDir, null);

  assertEquals(0, this.moveTool.getStatusCode());
  assertEquals(returnMessage, new String());

 }

 @Test
 public void execute_FilesToDirectoryTest() throws IOException {

  File aFile = null;

  String[] args = new String[5];

  for (int i = 0; i < args.length - 1; i++) {
   aFile = File.createTempFile("newFileToMove_" + i + "_", ".txt",
     this.sourceDir);
   args[i] = aFile.toString();
  }

  args[args.length - 1] = this.destDir.toString();

  this.moveTool = new MoveTool(args);
  String returnMessage = this.moveTool.execute(workingDir, null);

  assertEquals(0,this.moveTool.getStatusCode());
  assertEquals(returnMessage, new String());

 }

  //Black Box Negative Testing

  @Test
 public void execute_FilesToNonDirectoryTest() throws IOException {

  File aFile = null;

  String[] args = new String[5];

  for (int i = 0; i < args.length - 1; i++) {
   aFile = File.createTempFile("newFileToMove_" + i + "_", ".txt",
     this.sourceDir);
   args[i] = aFile.toString();
  }

  args[args.length - 1] = this.destDir.toString() + "/notADir";

  this.moveTool = new MoveTool(args);
  String returnMessage = this.moveTool.execute(workingDir, null);

  assertEquals(-3,this.moveTool.getStatusCode());
  assertEquals(returnMessage, "move: target '" + args[args.length - 1]
    + "' is not a directory");

 }

 // Helper Functions
 private void removeFiles(File directory) throws IOException {

  if (directory.listFiles() != null) {
   for (File f : directory.listFiles()) {
    if (f.isDirectory()) {
     removeFiles(f);
    }

    f.delete();
   }
  }
 }

 private File createDir(String path) {
  File dir = new File(path);
  try {
   Files.createDirectories(dir.toPath());
  } catch (IOException e) {
   e.printStackTrace();
  }
  return dir;
 }
}
