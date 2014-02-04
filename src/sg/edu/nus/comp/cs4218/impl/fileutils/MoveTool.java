package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class MoveTool extends ATool implements IMoveTool {

	public MoveTool() {
		super(null);
	}

	@Override
	public boolean move(File from, File to) {
		if (from.renameTo(to)){
			setStatusCode(0);
			return true;
		}else{
			setStatusCode(1);
			return false;
		}
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
