package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;


public class PWDTool extends ATool implements IPwdTool{
	public PWDTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("pwd")) {
			setStatusCode(127);
		}
	}

	// TODO
	@Override
	public String getStringForDirectory(File directory) {
		//Error Handling
		if(directory==null || !directory.exists() || !directory.isDirectory()){
			setStatusCode(1);
			return "Error: Cannot find working directory";
		}
		//Processing the 
		return directory.getAbsolutePath();
	}

	// TODO
	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length == 1){
			return getStringForDirectory(workingDir);
		}else{
			setStatusCode(1);
			return "Error: PWD command wrong";
		}
		
	}


}
