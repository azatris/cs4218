package sg.edu.nus.comp.cs4218.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;

import sg.edu.nus.comp.cs4218.impl.Shell;

public class ShellParseWithPipe {

	private static Properties prop;
	private Shell shell;
	public ShellParseWithPipe() {
		shell = new Shell();
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		prop = new Properties();
		try {
			prop.load(new FileInputStream("parsing.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
}
