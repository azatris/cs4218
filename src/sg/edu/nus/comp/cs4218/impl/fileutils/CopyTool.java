package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CopyTool extends ATool implements ICopyTool {

	public CopyTool(String[] arguments) {
		super(arguments);
		if (args.length == 0 || !args[0].equals("cp")) {
			setStatusCode(127);
			
		}
	}
	
	@Override
	public boolean copy(File fromFile, File toFile) {
		File from = fromFile;
		File to = toFile;
		if (from!=null && from.isFile()){
			if (to == null){
				setStatusCode(1);
				System.err.println("Error: Destination is null");
				return false;
			}else if (!to.exists()){
				try {
					to.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (to.isDirectory()){
				String toDir = to.getAbsolutePath();
				to = null;
				to = new File(toDir+File.separator+from.getName());
			}
			
			FileWriter fstream = null;
			try {
				fstream = new FileWriter(to, false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			BufferedWriter out = new BufferedWriter(fstream);

			FileReader fileReader = null;
			try {
				fileReader = new FileReader(from);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			int i ;
			try {
				while((i =  fileReader.read()) != -1){
					char ch = (char)i;
					out.write(ch);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				fileReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// close buffer writer
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}else{
			setStatusCode(1);
			System.err.println("Error: Source is null");
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
		if (args.length == 3){
			copy(
					new File(concatenateDirectory(workingDir.getAbsolutePath(), args[1])), 
					new File(concatenateDirectory(workingDir.getAbsolutePath(), args[2])));
			return "";
		}else{
			setStatusCode(1);
			return "Error: Should have two directory";
		}
	}

}
