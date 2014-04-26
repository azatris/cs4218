package sg.edu.nus.comp.cs4218.common;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Test;

public class AdditionalCommonTest {

	@Test
	public void concatenateNull() {
		assertEquals("", Common.concatenateDirectory(null, ""));
	}
	
	@Test
	public void concatenatePathWithDotDot() {
		assertEquals(Paths.get(System.getProperty("user.dir")).getParent().toString() + File.separator, Common.concatenateDirectory(System.getProperty("user.dir"), ".."));
	}
}
