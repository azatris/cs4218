package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;

public class CommTool extends ATool implements ICommTool {

	public CommTool(String[] args) {
		super(args);
		if (args.length == 0 || !args[0].equals("comm")) {
			setStatusCode(127);
		}
	}

	@Override
	public String compareFiles(String input1, String input2) {
		StringBuilder output = new StringBuilder();
		String lineSeparator = System.lineSeparator();
		if (input1.contains("\r\n")){
			lineSeparator = "\\r\\n";
		}else if (input1.contains("\n")){
			lineSeparator = "\\n";
		}
		String[] lines1 = input1.split(lineSeparator);
		if (input2.contains("\r\n")){
			lineSeparator = "\\r\\n";
		}else if (input2.contains("\n")){
			lineSeparator = "\\n";
		}
		String[] lines2 = input2.split(lineSeparator);
		
		int minSize = Math.min(lines1.length, lines2.length);
		for (int i=0; i<minSize; i++){
			if (i>0){
				if (lines1[i-1].compareTo(lines1[i]) > 0){
					output.append("comm: File 1 is not in sorted order \n");
				}
				if (lines2[i-1].compareTo(lines2[i]) > 0){
					output.append("comm: File 2 is not in sorted order \n");
				}
			}
			if (lines1[i].compareTo(lines2[i]) == 0) {
				output.append("\t\t" + lines1[i] + "\n");
			}else{
				output.append(lines1[i] + "\n");
				output.append("\t" + lines2[i] + "\n");
			}
		}
		for (int i = minSize; i < lines1.length; i++){
			output.append(lines1[i] + "\n");
		}
		for (int i = minSize; i < lines2.length; i++){
			output.append(lines2[i] + "\n");
		}
		return output.toString();
	}

	@Override
	public String compareFilesCheckSortStatus(String input1, String input2) {
		StringBuilder output = new StringBuilder();
		String[] lines1 = input1.split("\n");
		String[] lines2 = input2.split("\n");
		boolean sorted1, sorted2;
		sorted1 = sorted2 = true;
		
		for (int i=0, j=0; i<lines1.length && j<lines2.length;){
			if (i>0){
				if (lines1[i-1].compareTo(lines1[i]) > 0){
					output.append("comm: File 1 is not in sorted order \n");
					sorted1 = false;
				}
				if (lines2[i-1].compareTo(lines2[i]) > 0){
					output.append("comm: File 2 is not in sorted order \n");
					sorted2 = false;
				}
			}
			if (sorted1==true && sorted2==true){
				if (lines1[i].compareTo(lines2[i]) == 0) {
					output.append("\t\t" + lines1[i] + "\n");
				}
				else{
					output.append(lines1[i] + "\n");
					output.append("\t" + lines2[i] + "\n");
				}
				i++;
				j++;
			} else if (sorted1 == true){
				output.append(lines1[i] + "\n");
				i++;
			} else if (sorted2 == true){
				output.append("\t" + lines2[i] + "\n");
				j++;
			}	
		}
		return output.toString();
	}

	@Override
	public String compareFilesDoNotCheckSortStatus(String input1, String input2) {
		StringBuilder output = new StringBuilder();
		String[] lines1 = input1.split("\n");
		String[] lines2 = input2.split("\n");		
		int minSize = Math.min(lines1.length, lines2.length);
		for (int i=0; i<minSize; i++){
			if (lines1[i].compareTo(lines2[i]) == 0) {
				output.append("\t\t" + lines1[i] + "\n");
			}else{
				output.append(lines1[i] + "\n");
				output.append("\t" + lines2[i] + "\n");
			}
		}
		for (int i = minSize; i < lines1.length; i++){
			output.append(lines1[i] + "\n");
		}
		for (int i = minSize; i < lines2.length; i++){
			output.append(lines2[i] + "\n");
		}
		return output.toString();
	}

	@Override
	public String getHelp() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return prop.getProperty("commHelp");
	}

	@Override
	public String execute(File workingDir, String stdin) {
		CatTool catTool = new CatTool(new String[]{"cat"});
		if (args.length > 1 && args[0].equals("comm")){
			if (args.length ==2){
				 if ((args[1].equals("-help"))){
					setStatusCode(0);
					return getHelp();
				}else{
					setStatusCode(1);
					System.err.println("comm: Invalid option");
					return "";
				}
			}else if (args.length == 3){
				File file1 = new File(args[1]);
				File file2 = new File(args[2]);
				if (!file1.exists()){
					setStatusCode(1);
					System.err.println("comm: File 1 does not exists.");
					return "";
				}else if (!file2.exists()){
					setStatusCode(1);
					System.err.println("comm: File 2 does not exists.");
					return "";
				}else{
					setStatusCode(0);
					return compareFiles(catTool.getStringForFile(file1), catTool.getStringForFile(file2));
				}
			}else if (args.length == 4){
				File file1 = new File(args[2]);
				File file2 = new File(args[3]);
				if (!file1.exists()){
					setStatusCode(1);
					System.err.println("comm: File 1 does not exists.");
					return "";
				}else if (!file2.exists()){
					setStatusCode(1);
					System.err.println("comm: File 2 does not exists.");
					return "";
				}
				if (args[1].equals("-c")){
					setStatusCode(0);
					return compareFilesCheckSortStatus (catTool.getStringForFile(file1), catTool.getStringForFile(file2));
				}else if (args[1].equals("-d")){
					setStatusCode(0);
					return compareFilesDoNotCheckSortStatus(catTool.getStringForFile(file1), catTool.getStringForFile(file2));
				}else{
					setStatusCode(1);
					System.err.println("comm: Invalid option");
					return "";
				}
			}else{
				setStatusCode(1);
				System.err.println("comm: Invalid arguments");
				return "";
			}
		}else{
			setStatusCode(1);
			System.err.println("comm: Need more arguments");
			return "";
		}
	}
}
