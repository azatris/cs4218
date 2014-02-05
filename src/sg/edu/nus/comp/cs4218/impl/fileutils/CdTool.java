package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CdTool extends ATool implements ICdTool {

	public CdTool(String[] arguments) {
		super(arguments);
	}
	
	@Override
	public File changeDirectory(String newDirectory) {
		if (newDirectory != null){
			File newDir = new File(newDirectory);
			if (newDir.isDirectory()){
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
		System.out.println(newWorkingDir.toString());
		return newWorkingDir.toString();
	}
	
	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length==0){
			changeDirectory(System.getProperty( "user.home" ));
			return "";
		}else if(args.length == 1){
			StringBuilder stringBuilder = new StringBuilder();
			//List<String> newDir = Arrays.asList(workingDir.getAbsolutePath().split(File.separator));
			for(String dir: args[0].split(File.separator)){
				if(dir == "~"){
					//stringBuilder.delete(0, stringBuilder.length());
					stringBuilder.append(System.getProperty("user.home"));
					break;
				}else if(dir == ".."){
					stringBuilder.append(workingDir.getAbsolutePath());
					stringBuilder.delete(stringBuilder.lastIndexOf(File.separator), stringBuilder.length());
				}else if(dir == "."){
					
				}else{
					stringBuilder.append(File.separator);
					stringBuilder.append(dir);
				}
			}
			changeDirectory(stringBuilder.toString());
			return "";
		}else
		{
			setStatusCode(1);
			return "Error: Should have one directory";
		}
	}
}
