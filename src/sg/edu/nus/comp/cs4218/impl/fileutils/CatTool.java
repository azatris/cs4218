package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CatTool extends ATool implements ICatTool {

	public CatTool() {
		super(null);
	}
	
	public CatTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String getStringForFile(File toRead) {
		// Error Handling
		if (toRead == null || !toRead.exists()
				|| !toRead.isFile()) {
			setStatusCode(1);
			return null;
		}
		
		// Processing the command
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(toRead);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    
		String fileContents = "";
		int i ;
		try {
			while((i =  fileReader.read()) != -1){
				char ch = (char)i;
				fileContents = fileContents + ch; 
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			fileReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return fileContents;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		StringBuilder stringBuilder = new StringBuilder();
		if (args.length < 2){
			return "Error: Not enough arguments\n";
		}else if (args[1]=="-") // as long as args[1]="-", not considering the rest
		{
			return stdin;
		}else{
			for (int i=1; i<args.length; i++){
				String separator = File.separator;
				if(File.separator.equals("\\")){
					separator =("\\\\");
				}
				
				Stack<String> buildNewAbsDir = new Stack<String>();
				buildNewAbsDir.addAll(Arrays.asList(workingDir.getAbsolutePath().split(separator)));
				// The for loop works correctly if it loops from dir1 to dirN in the case "args[i]=dir1/dir2/.../dirN"
				for(String str: Arrays.asList(args[i].split(separator))){
					if (str != ""){
						if (str == ".."){ // parent directory
							buildNewAbsDir.pop();
						}else if ((str == ".")){ // current directory
						}else{ // child directory
							buildNewAbsDir.push(str);
						}
					}
				}
				StringBuilder newWorkingDir = new StringBuilder();
				for (int j=buildNewAbsDir.size()-1; j>=0; j--){
					newWorkingDir.append(buildNewAbsDir.get(j));
					newWorkingDir.append(File.separator);	
				}
				String strForFile = getStringForFile(new File(newWorkingDir.toString()));
				if (strForFile == null){
					strForFile = "cat: " + args[i] +": No such file or directory\n";
				}
				stringBuilder.append(strForFile);
			}
		}
		return stringBuilder.toString();
	}

}
