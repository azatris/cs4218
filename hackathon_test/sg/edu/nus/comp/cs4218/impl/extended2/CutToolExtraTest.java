package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;

public class CutToolExtraTest {
 // Static Variables
 private static ICutTool cutTool;
 private static File workingDir;
 private static File tempDir;
 private static File inputNoDelimiter;
 private static File inputNoDelimiterTwo;
 private static File inputWithDelimiter;
 private static File inputWithDelimiterTwo;
 private static String inputNoDelimiterStr;
 private static String inputNoDelimiterTwoStr;
 private static String inputWithDelimiterStr;
 private static String inputWithDelimiterTwoStr;

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
  workingDir = new File(System.getProperty("user.dir"));

  /* Creation of directories and files for testing purposes */
  tempDir = new File(Files.createDirectory(
    new File(workingDir.toString() + File.separator + "tempDir")
      .toPath()).toString());
  inputNoDelimiter = new File(tempDir.toString() + File.separator
    + "inputNoDelimiter");
  inputNoDelimiter.createNewFile();

  inputNoDelimiterTwo = new File(tempDir.toString() + File.separator
    + "inputNoDelimiterTwo");
  inputNoDelimiterTwo.createNewFile();

  inputWithDelimiter = new File(tempDir.toString() + File.separator
    + "inputWithDelimiter");
  inputWithDelimiter.createNewFile();

  inputWithDelimiterTwo = new File(tempDir.toString() + File.separator
    + "inputWithDelimiterTwo");
  inputWithDelimiterTwo.createNewFile();

  /* Writing of content into files */
  inputNoDelimiterStr = "012345678901234567890" + System.lineSeparator()
    + "123456789 0123456789";

  inputNoDelimiterTwoStr = "123456789 0123456789"
    + System.lineSeparator() + "012345678901234567890";

  inputWithDelimiterStr = "0123456789;0123456789;0123456789"
    + System.lineSeparator() + "0123456789;0123456789;0123456789"
    + System.lineSeparator() + "0123456789 0123456789;0123456789";

  inputWithDelimiterTwoStr = "9876543210;9876543210;9876543210"
    + System.lineSeparator() + "9876543210;9876543210;9876543210"
    + System.lineSeparator() + "9876543210;9876543210;9876543210";

  Files.write(inputNoDelimiter.toPath(), inputNoDelimiterStr.getBytes(),
    StandardOpenOption.APPEND);
  Files.write(inputNoDelimiterTwo.toPath(),
    inputNoDelimiterTwoStr.getBytes(), StandardOpenOption.APPEND);
  Files.write(inputWithDelimiter.toPath(),
    inputWithDelimiterStr.getBytes(), StandardOpenOption.APPEND);
  Files.write(inputWithDelimiterTwo.toPath(),
    inputWithDelimiterTwoStr.getBytes(), StandardOpenOption.APPEND);

 }

 @Before
 public void setUp() {
  cutTool = new CutTool(null);
 }

 @After
 public void tearDown() {
  cutTool = null;
 }

 @AfterClass
 public static void tearDownAfterClass() throws IOException {
  /* Delete all temporary testing files */
  Files.delete(inputNoDelimiter.toPath());
  Files.delete(inputWithDelimiter.toPath());
  Files.delete(inputNoDelimiterTwo.toPath());
  Files.delete(inputWithDelimiterTwo.toPath());
  Files.delete(tempDir.toPath());

  /* Setting all file objects to null */
  workingDir = null;
  tempDir = null;
  inputNoDelimiter = null;
  inputNoDelimiterTwo = null;
  inputWithDelimiter = null;
  inputWithDelimiterTwo = null;

  /* Setting all file object Strings to null */
  inputNoDelimiterStr = null;
  inputNoDelimiterTwoStr = null;
  inputWithDelimiterStr = null;
  inputWithDelimiterTwoStr = null;
 }

 // Team Rocket's Black Box Positive Test Cases

 @Test
 public void execute_cutSpecifiedCharactersEndRange_MultipleCharactersReturned() {

  final String[] args = { "cut", "-c", "-3,-6,-5", inputNoDelimiter.toString() };
  cutTool = new CutTool(args);

  final String expectedMessage = "012345" + System.lineSeparator()
    + "123456";
  final String returnMessage = cutTool.execute(workingDir, null);

  assertEquals(expectedMessage, returnMessage);
  assertEquals(0, cutTool.getStatusCode());

 }

 @Test
 public void execute_cutSpecifiedCharactersStartRange_MultipleCharactersReturned() {

  final String[] args = { "cut", "-c", "3-,6-,5-", inputNoDelimiter.toString() };
  cutTool = new CutTool(args);

  final String expectedMessage = "2345678901234567890"
    + System.lineSeparator() + "3456789 0123456789";
  final String returnMessage = cutTool.execute(workingDir, null);

  assertEquals(expectedMessage, returnMessage);
  assertEquals(0, cutTool.getStatusCode());

 }



 @Test
 public void execute_cutSpecifiedCharactersWithDelimiterOpF_OriginalInputReturned() {

  final String[] args = { "cut", "-f", "1-3", inputWithDelimiter.toString() };
  cutTool = new CutTool(args);

  final String expectedMessage = "0123456789;0123456789;0123456789"
    + System.lineSeparator() + "0123456789;0123456789;0123456789"
    + System.lineSeparator() + "0123456789 0123456789;0123456789";

  final String returnMessage = cutTool.execute(workingDir, null);

  assertEquals(expectedMessage, returnMessage);
  assertEquals(0, cutTool.getStatusCode());

 }

}
