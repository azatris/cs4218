package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class DeleteTool extends ATool implements IDeleteTool {

	public DeleteTool() {
		super(null);
	}
	
	public DeleteTool(String[] arguments) {
		super(arguments);
	}
	
	@Override
	public boolean delete(File toDelete) {
		if(toDelete != null && toDelete.delete()){
			setStatusCode(0);
			return true;
		}else{
			setStatusCode(1);
			return false;
			//return "Error: Cannot find working directory";
		}
	}

	@Override
	public String execute(File workingDir, String stdin) {
		if(args.length==0){
			setStatusCode(1);
			return "Error: Cannot find working directory";
		}else{
			for (int i=0; i<args.length; i++){
				if (!delete(new File(workingDir+File.separator+args[i]))){
					setStatusCode(1);
					return "Error: Cannot delete " + args[i];
				}
			}
			return null;
		}
	}
}
