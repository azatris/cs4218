package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class MoveTool extends ATool implements IMoveTool {
	public MoveTool(String[] arguments) {
		super(arguments);
		if (args.length == 0 || !args[0].equals("move")) {
			if (args == null || args.length == 0 || !args[0].equals("move")) {
				setStatusCode(127);
			}
		}
	}
	
	// TODO
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
	
	// TODO
	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length==3) {
			String fromStr = "";
			String toStr = "";
			if (Paths.get(args[1]).isAbsolute()){
				fromStr = args[1];
			}else{
				fromStr = Common.concatenateDirectory(workingDir.getAbsolutePath(), args[1]);
			}
			
			if (Paths.get(args[2]).isAbsolute()){
				toStr = args[2];
			}else{
				toStr = Common.concatenateDirectory(workingDir.getAbsolutePath(), args[2]);
			}
			
			if ( !move(new File(fromStr), new File(toStr)) ) {
				return "Move unsuccessfully"+System.lineSeparator();
			}
			return "";
		}else{
			setStatusCode(2);
			return "";
		}
	}
}
