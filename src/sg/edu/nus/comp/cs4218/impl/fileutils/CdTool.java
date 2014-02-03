package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CdTool extends ATool implements ICdTool {

	public CdTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public File changeDirectory(String newDirectory) {
		// TODO Auto-generated method stub
		File newDir = new File(newDirectory);
		if (newDir.isDirectory()){
			return newDir;
		}
		else
		{
			setStatusCode(1);
			System.err.println("Error: No such file or directory");
			return null;
		}
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
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
