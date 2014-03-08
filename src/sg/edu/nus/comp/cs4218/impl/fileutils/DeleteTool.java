package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Stack;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class DeleteTool extends ATool implements IDeleteTool {

	public DeleteTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("delete")) {
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
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") > 0){
			newWorkingDir.append(File.separator);
		}
		for (int i = 0; i<buildNewAbsDir.size(); i++){
			newWorkingDir.append(buildNewAbsDir.get(i));
				newWorkingDir.append(File.separator);
		}
		return newWorkingDir.toString();
	}
	
	@Override
	public String execute(File workingDir, String stdin) {
		if(args.length==1){
			setStatusCode(1);
			return "Error: Cannot find working directory";
		}else{
			StringBuilder returnMsg = new StringBuilder();
			int status = 0;
			for (int i=1; i<args.length; i++){
				String filePath = null;
				if (Paths.get(args[i]).isAbsolute()){
					filePath = args[i];
				}else{
					filePath = concatenateDirectory(workingDir.getAbsolutePath(), args[i]);
				}
				if (!delete(new File(filePath))){
					status = 1;
					returnMsg.append("Error: Cannot delete " + args[i]);
				}
			}
			if (status == 1) {
				setStatusCode(1);
			}
			if (returnMsg.toString().length() > 0){
				return returnMsg.toString();
			}
			return null;
		}
	}
}
