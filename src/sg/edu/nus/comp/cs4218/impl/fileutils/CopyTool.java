package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CopyTool extends ATool implements ICopyTool {
	
	private static PrintStream err = System.err;
	public CopyTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("copy")) {
			setStatusCode(127);
		}
	}
	
	/**
	 * Copy the content from the source to destination
	 * @param	fromFile	source File to be copied
	 * @param	toFile	destination File
	 * @return true if copy successfully
	 */
	@Override
	
	
	
	
	
	
	
	public boolean copy(File fromFile, File toFile) {
		File from = fromFile;
		File to = toFile;
		if (from!=null && from.isFile()){
			if (to == null){
				setStatusCode(1);
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
			fileReader.close();
			out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}else{
			setStatusCode(1);
			err.println("Error: Source is null");
			return false;
		}
	}
	
	/**
	 * The general go-to method for using the tool that calls
	 * the suitable submethods.
	 * @param workingDir current working directory
	 * @param stdin optional standard input from e.g. pipe tool
	 * @return output
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length == 3){
			if (args[1] == args[2])
			{
				setStatusCode(211);
				return "";
			}
			
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
			
			if ( !copy(new File(fromStr), new File(toStr)) ) {
			}
		}else{
			setStatusCode(1);
		}
		return "";
	}
}
