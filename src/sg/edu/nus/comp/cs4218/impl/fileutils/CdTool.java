package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CdTool extends ATool implements ICdTool {
	public CdTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("cd")) {
			setStatusCode(127);
		}
	}
	
	// TODO
	@Override
	public File changeDirectory(String newDirectory) {
		if (newDirectory != null){
			File newDir = new File(newDirectory);
			if (newDir.isDirectory()){
				// set status code to 55 meaning change directory
				setStatusCode(55);
				return newDir;
			}
		}
		setStatusCode(1);
		return null;
	}
	
	// TODO
	@Override
	public String execute(File workingDir, String stdin) {
		File newDir = null;
		if (args.length==1){
			newDir = changeDirectory(System.getProperty( "user.dir" ));
		}else if(args.length == 2){	
			if (args[1].equals("~")){
				newDir = changeDirectory(System.getProperty( "user.dir" ));
			}else if (Paths.get(args[1]).isAbsolute()){
				newDir = changeDirectory(args[1]);
			}else{
				newDir = changeDirectory(Common.concatenateDirectory(workingDir.getAbsolutePath(), args[1]));
			}
		}else{
			setStatusCode(98);
		}
		
		if (newDir != null){
			workingDir = newDir;
			return newDir.getAbsolutePath();
		}else{
			return workingDir.toString();
		}
	}
}
