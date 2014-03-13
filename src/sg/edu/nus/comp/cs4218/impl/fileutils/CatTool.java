package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CatTool extends ATool implements ICatTool {	
	public CatTool(String[] arguments) {
		super(arguments);
		if (args == null ||args.length == 0 || !args[0].equals("cat")) {
			setStatusCode(127);
			
		}
	}

	// TODO
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

	// TODO
	@Override
	public String execute(File workingDir, String stdin) {
		StringBuilder stringBuilder = new StringBuilder();
		if (args.length < 2){
			setStatusCode(1);
			return "Error: Not enough arguments\n";
		}else if (args[1].equals("-")) // as long as args[1]="-", not considering the rest
		{
			setStatusCode(0);
			return stdin;
		}else{
			for (int i=1; i<args.length; i++){
				String strForFile = null;
				if (Paths.get(args[i]).isAbsolute()){
					strForFile = getStringForFile(new File(args[i]));
				}else{
					strForFile = getStringForFile(new File(Common.concatenateDirectory(workingDir.getAbsolutePath(), args[i])));
				}
				if (strForFile == null){ 
					strForFile = "cat: " + args[i] +": No such file or directory\n";
				}
				stringBuilder.append(strForFile);
			}
		}
		return stringBuilder.toString();
	}
}