package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class DeleteTool extends ATool implements IDeleteTool {

	public DeleteTool(String[] arguments) {
		super(arguments);
		if (args.length == 0 || !args[0].equals("rm")) {
			setStatusCode(127);
			
		}
	}
	
	@Override
	public boolean delete(File toDelete) {
		if(toDelete != null && toDelete.delete()){
			setStatusCode(0);
			return true;
		}else{
			setStatusCode(1);
			return false;
		}
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
		if(args.length==1){
			setStatusCode(1);
			return "Error: Cannot find working directory";
		}else{
			StringBuilder returnMsg = new StringBuilder();
			for (int i=1; i<args.length; i++){
				if (!delete(new File(concatenateDirectory(workingDir.getAbsolutePath(), args[i])))){
					returnMsg.append("Error: Cannot delete " + args[i]);
				}
			}
			if (returnMsg.toString().length() > 0){
				return returnMsg.toString();
			}
			return null;
		}
	}
}
