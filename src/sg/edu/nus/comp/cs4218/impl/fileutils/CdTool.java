package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CdTool extends ATool implements ICdTool {

	public CdTool(String[] arguments) {
		super(arguments);
		if (args.length == 0 || !args[0].equals("cd")) {
			setStatusCode(127);
			
		}
	}
	
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
		System.err.println("Error: No such file or directory");
		return null;
	}
	
	public String concatenateDirectory(String curAbsDir, String newRelDir){
		String separator = File.separator;
		if(File.separator.equals("\\")){
			separator =("\\\\");
		}

		Stack<String> buildNewAbsDir = new Stack<String>();
		buildNewAbsDir.addAll(Arrays.asList(curAbsDir.split(separator)));
		
		for(String str: Arrays.asList(newRelDir.split(separator))){
			if (!str.equals("")){
				if (str.equals("..")){ // parent directory
					buildNewAbsDir.pop();
				}else if ((str.equals("."))){ // current directory
				}else{ // child directory
					buildNewAbsDir.push(str);
				}
			}
		}
		StringBuilder newWorkingDir = new StringBuilder();
		newWorkingDir.append(File.separator);
		for (int i = 0; i<buildNewAbsDir.size(); i++){
			newWorkingDir.append(buildNewAbsDir.get(i));
			if ( i != 0 ){
				newWorkingDir.append(File.separator);
			}		
		}
		return newWorkingDir.toString();
	}
	
	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length==1){
			File newDir = changeDirectory(System.getProperty( "user.home" ));
			if (newDir != null){
				System.out.println(newDir.getAbsolutePath());
				return newDir.getAbsolutePath();
			}
			return null;
		}else if(args.length >= 2){
			File newDir = changeDirectory(concatenateDirectory(workingDir.getAbsolutePath(), args[1]));
			if (newDir != null){
				return newDir.getAbsolutePath();
			}
			return null;
		}else
		{
			setStatusCode(1);
			return "cd: No command";
		}
	}
}
