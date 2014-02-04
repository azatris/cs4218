package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CatTool extends ATool implements ICatTool {

	public CatTool() {
		super(null);
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
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder();
		if (args.length==0 && args[0]=="-")
		{
			return stdin;
		}else{
			for (int i=0; i<args.length; i++){
			stringBuilder.append(getStringForFile(new File(workingDir+File.separator+args[i])));
			}
		}
		return stringBuilder.toString();
	}

}
