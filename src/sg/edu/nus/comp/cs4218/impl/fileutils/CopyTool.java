package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CopyTool extends ATool implements ICopyTool {

	public CopyTool(String[] arguments) {
		super(arguments);
	}
	
	@Override
	public boolean copy(File fromFile, File toFile) {
		File from = fromFile;
		File to = toFile;
		if (from!=null && from.isFile()){
			if (to == null){
				setStatusCode(1);
				System.err.println("Error: Destination is null");
				return false;
			}else if (!to.exists()){
				try {
					to.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (to.isDirectory()){
				String toDir = to.getAbsolutePath();
				to = null;
				to = new File(toDir+File.separator+from.getName());
			}
			
			FileWriter fstream = null;
			try {
				fstream = new FileWriter(to, false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			BufferedWriter out = new BufferedWriter(fstream);

			FileReader fileReader = null;
			try {
				fileReader = new FileReader(from);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			int i ;
			try {
				while((i =  fileReader.read()) != -1){
					char ch = (char)i;
					out.write(ch);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				fileReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// close buffer writer
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}else{
			setStatusCode(1);
			System.err.println("Error: Source is null");
			return false;
		}
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		if (args.length == 2 && stdin==""){
			copy(new File(workingDir+File.separator+args[0]), new File(workingDir+File.separator+args[1]));
			return "";
		}else{
			setStatusCode(1);
			return "Error: Should have two directory";
		}
	}

}
