package sg.edu.nus.comp.cs4218.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class Common {
	/**
	 * Generate absolute path from two inputs
	 * @param curAbsDir absolute working directory
	 * @param newRelDir relative directory
	 * @return Absolute path of the file
	 */
	public static String concatenateDirectory(String curAbsDir, String newRelDir){
		String separator = File.separator;
		if(File.separator.equals("\\")){
			separator =("\\\\");
		}
		if (curAbsDir != null ){
			Stack<String> buildNewAbsDir = new Stack<String>();
			buildNewAbsDir.addAll(Arrays.asList(curAbsDir.split(separator)));
			if (newRelDir != null){
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
		}else{
			return "";
		}
	}
	
	/**
	 * Write random string (char set defined in function) with random length (between 0 to 512) to file
	 * @param toWrite
	 * @return
	 * @throws IOException
	 */
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
	
	/**
	 * Write a string to a file
	 * @param file file to write
	 * @param s string to be written to the file
	 * @throws IOException
	 */
	public static void writeFile(File file, String s) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(s);
		out.close();
	}
	
	/**
	 * Read file char by char because it is sometimes needed to keep original newline char regardless of platforms
	 * @param toRead
	 * @return content of the file
	 * @throws IOException
	 */
	public static String readFileByChar(File toRead) throws IOException{
		FileReader fileReader = new FileReader(toRead);
		String fileContents = "";
		int i ;
		while((i = fileReader.read()) != -1){
			char ch = (char)i;
			fileContents = fileContents + ch; 
		}
		fileReader.close();
		return fileContents;
	}
	
	/**
	 * Read file line by line and insert platform independent newline characters
	 * @param toRead
	 * @return content of the file
	 */
	public static String readFileByLine(File toRead){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(toRead));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder fileContents = new StringBuilder();
		String line;
		try {
			while((line=reader.readLine()) != null){
				fileContents.append(line);
				fileContents.append(System.lineSeparator());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContents.toString();
	}
}
