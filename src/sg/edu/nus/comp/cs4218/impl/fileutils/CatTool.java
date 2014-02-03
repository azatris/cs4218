package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedReader;
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

	@SuppressWarnings("resource")
	@Override
	public String getStringForFile(File toRead) {
		// TODO Auto-generated method stub
		// Error Handling
		if (toRead == null || !toRead.exists()
				|| !toRead.isFile()) {
			setStatusCode(1);
			return "Error: Cannot find working directory";
		}
		
		// Processing the command
		BufferedReader reader = null;
		try {
			reader = new BufferedReader( new FileReader (toRead));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String line = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");

	    try {
			while( ( line = reader.readLine() ) != null ) {
			    stringBuilder.append( line );
			    stringBuilder.append( ls );
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return stringBuilder.toString();
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
