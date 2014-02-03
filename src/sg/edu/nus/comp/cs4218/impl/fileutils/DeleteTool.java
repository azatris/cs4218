package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class DeleteTool extends ATool implements IDeleteTool {

	public DeleteTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean delete(File toDelete) {
		// TODO Auto-generated method stub
		if(toDelete.isFile() && toDelete.delete()){
			return true;
		}else
		{
			setStatusCode(1);
			return false;
			//return "Error: Cannot find working directory";
		}
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
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
			return "";
		}
	}
}
