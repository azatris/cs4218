package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CopyTool extends ATool implements ICopyTool {

	public CopyTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean copy(File from, File to) {
		// TODO Auto-generated method stub
		if (from.isFile()){
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(from);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
	 
			FileWriter fstream = null;
			try {
				fstream = new FileWriter(to);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedWriter out = new BufferedWriter(fstream);
	 
			String aLine = null;
			try {
				while ((aLine = in.readLine()) != null) {
					//Process each line and add output to Dest.txt file
					out.write(aLine);
					out.newLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
			// do not forget to close the buffer reader
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
			// close buffer writer
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}else{
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
