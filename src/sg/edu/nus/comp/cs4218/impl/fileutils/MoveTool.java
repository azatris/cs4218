package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Stack;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class MoveTool extends ATool implements IMoveTool {

	public MoveTool(String[] arguments) {

	super(arguments);
	if (args.length == 0 || !args[0].equals("move")) {
=======
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("move")) {
>>>>>>> 8367ab659ebcbe72e2b87029bdbb98bf2fa77896
			setStatusCode(127);
		}
	}
	
	@Override
	public boolean move(File fromFile, File toFile) {
		File from = fromFile;
		File to = toFile;
		if (from == null || !from.exists() || from.isDirectory() || to == null){
			setStatusCode(1);
			return false;
		}
		
		if (!to.exists()){
			try {
				to.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(to.isDirectory()){
			to = new File(to.getAbsolutePath() + File.separator + from.getName());
		}
		try {
			Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setStatusCode(0);
		return true;
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
		if (args.length==3) {
			String fromStr = null;
			String toStr = null;
			if (Paths.get(args[1]).isAbsolute()){
				fromStr = args[1];
			}else{
				fromStr = concatenateDirectory(workingDir.getAbsolutePath(), args[1]);
			}
			
			if (Paths.get(args[2]).isAbsolute()){
				toStr = args[2];
			}else{
				toStr = concatenateDirectory(workingDir.getAbsolutePath(), args[2]);
			}
			
			if ( !move(new File(fromStr), new File(toStr)) ) {
				return "Move unsuccessfully\n";
			}
			return null;
		}else{
			return "Error: Should have two arguments.";
		}
	}
}
