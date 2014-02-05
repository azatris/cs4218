package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class MoveTool extends ATool implements IMoveTool {

	public MoveTool(String[] arguments) {
		super(arguments);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(to.isDirectory()){
			to = new File(to.getAbsolutePath() + File.separator + from.getName());
		}
		try {
			Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setStatusCode(0);
		return true;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		if (args.length==2) {
			if (move(new File(workingDir+File.separator+args[0]), new File(workingDir+File.separator+args[1]))){
				return null;
			}else{
				return "Error: Cannot move file.";
			}
		}else{
			return "Error: Should have two arguments.";
		}
	}
}
