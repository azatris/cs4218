package sg.edu.nus.comp.cs4218.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class Common {
	public static String concatenateDirectory(String curAbsDir, String newRelDir){
		String separator = File.separator;
		if(File.separator.equals("\\")){
			separator =("\\\\");
		}

		Stack<String> buildNewAbsDir = new Stack<String>();
		buildNewAbsDir.addAll(Arrays.asList(curAbsDir.split(separator)));
		
		for(String str: Arrays.asList(newRelDir.split(separator))){
			if (!str.equals("")){
				if (str.equals("..")){ // parent directory
					buildNewAbsDir.pop();
				}else if ((str.equals("."))){ // current directory
				}else{ // child directory
					buildNewAbsDir.push(str);
				}
			}
		}
		StringBuilder newWorkingDir = new StringBuilder();
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") > 0){
			newWorkingDir.append(File.separator);
		}
		for (int i = 0; i<buildNewAbsDir.size(); i++){
			newWorkingDir.append(buildNewAbsDir.get(i));
				newWorkingDir.append(File.separator);
		}
		return newWorkingDir.toString();
	}
	
	public static String writeRandomStringTo(File toWrite) throws IOException{
		// generate random string as file contents
		StringBuilder strBuilder = new StringBuilder();
		Random random = new Random();		
		int size = random.nextInt(512);
		String chars = "abcdefghijklmnopqrstuvwxyz"
				+ "1234567890"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "~!@#$%^&*()-_+=}{] [;:'\"?><,./"
				+ "\\\n\r\t";
		for (int i = 0; i < size; i++) {
			char c = chars.charAt(random.nextInt(chars.length()));
			strBuilder.append(c);
		}
		String str = strBuilder.toString();

		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toWrite), "utf-8"));
		writer.write(str);
		writer.close();

		return str;
	}
}
