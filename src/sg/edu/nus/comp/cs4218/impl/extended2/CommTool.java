package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CommTool extends ATool implements ICommTool {
	public CommTool(String[] args) {
		super(args);
		if (args == null || args.length == 0 || !args[0].equals("comm")) {
			setStatusCode(127);
		}
	}

	/**
	 * Compare two files 
	 */
	@Override
	public String compareFiles(String input1, String input2) {
		File file1 = new File(input1);
		File file2 = new File(input2);
		if ((!file1.exists()) || (!file2.exists())){
			setStatusCode(3);
			return "";
		}
		String[] lines1 = Common.readFileByLine(file1).split(System.lineSeparator());
		String[] lines2 = Common.readFileByLine(file2).split(System.lineSeparator());
		
		StringBuilder output = new StringBuilder();
		boolean sorted1=true, sorted2=true;
		if (lines1.length == 1 && lines1[0].equals("")){ 
			//if file 1 is empty
			for (int i=0;i<lines2.length; i++){
				output.append("\t"+lines2[i] + System.lineSeparator());
			}
		}else if (lines2.length == 1&& lines2[0].equals("")){
			// if file 2 is empty
			for (int i=0;i<lines1.length; i++){
				output.append(lines1[i] + System.lineSeparator());
			}
		}else{
			int minSize;
			minSize = Math.min(lines1.length, lines2.length);
			for (int i=0; i<minSize; i++){
				if (i>0){
					if (lines1[i-1].compareTo(lines1[i]) > 0 && sorted1){
						output.append("comm: File 1 is not in sorted order ");
						output.append(System.lineSeparator());
						sorted1 = false;
					}
					if (lines2[i-1].compareTo(lines2[i]) > 0 && sorted2){
							output.append("comm: File 2 is not in sorted order ");
							output.append(System.lineSeparator());
							sorted2 = false;
					}
				}
				if (lines1[i].compareTo(lines2[i]) == 0) {
					output.append("\t\t" + lines1[i] + System.lineSeparator());
				}else{
					output.append(lines1[i] + System.lineSeparator());
					output.append("\t" + lines2[i] + System.lineSeparator());
				}
			}
			for (int i = minSize; i < lines1.length; i++){
				output.append(lines1[i] + System.lineSeparator());
			}
			for (int i = minSize; i < lines2.length; i++){
				output.append(lines2[i] + System.lineSeparator());
			}
		}
		setStatusCode(0);
		return output.toString();
	}

	// TODO
	@Override
	public String compareFilesCheckSortStatus(String input1, String input2) {
		File file1 = new File(input1);
		File file2 = new File(input2);
		if ((!file1.exists()) || (!file2.exists())){
			setStatusCode(3);
			return "";
		}
		String[] lines1 = Common.readFileByLine(file1).split(System.lineSeparator());
		String[] lines2 = Common.readFileByLine(file2).split(System.lineSeparator());

		StringBuilder output = new StringBuilder();
		int minLength = Math.min(lines1.length, lines2.length);
		int breakIdx = 0, unsortedIdx = -1;
		for (int i=0; i < minLength; i++){
			if (i>0){
				if (lines1[i-1].compareTo(lines1[i]) > 0){
						output.append("comm: File 1 is not in sorted order ");
						output.append(System.lineSeparator());
						breakIdx = i;
						unsortedIdx = 0;
						break;
				}
				if (lines2[i-1].compareTo(lines2[i]) > 0){
						output.append("comm: File 2 is not in sorted order ");
						output.append(System.lineSeparator());
						breakIdx = i;
						unsortedIdx = 1;
						break;
				}
			}
			if (lines1[i].compareTo(lines2[i]) == 0) {
				output.append("\t\t" + lines1[i] + System.lineSeparator());
			}
			else{
				output.append(lines1[i] + System.lineSeparator());
				output.append("\t" + lines2[i] + System.lineSeparator());
			}
		}	
		int startIdx = 0;
		if (breakIdx == 0){
			startIdx = minLength;
		}else{
			startIdx = breakIdx;
		}
		if (unsortedIdx == 0){
			for (int i = startIdx; i < lines2.length; i++){
				if (i>0 && lines2[i-1].compareTo(lines2[i]) > 0){
					output.append("comm: File 2 is not in sorted order ");
					output.append(System.lineSeparator());
					break;				
				}
				output.append("\t" + lines2[i] + System.lineSeparator());
			}
		}else if (unsortedIdx == 1){
			for (int i = startIdx; i < lines1.length; i++){
				if (i>0 && lines1[i-1].compareTo(lines1[i]) > 0){
					output.append("comm: File 1 is not in sorted order ");
					output.append(System.lineSeparator());
					break;
				}
				output.append(lines1[i] + System.lineSeparator());
			}
		}
	
		setStatusCode(0);
		return output.toString();
	}

	// TODO
	@Override
	public String compareFilesDoNotCheckSortStatus(String input1, String input2) {
		File file1 = new File(input1);
		File file2 = new File(input2);
		if ((!file1.exists()) || (!file2.exists())){
			setStatusCode(3);
			return "";
		}
		String[] lines1 = Common.readFileByLine(file1).split(System.lineSeparator());
		String[] lines2 = Common.readFileByLine(file2).split(System.lineSeparator());
		
		StringBuilder output = new StringBuilder();
		int minSize = Math.min(lines1.length, lines2.length);
		for (int i=0; i<minSize; i++){
			if (lines1[i].compareTo(lines2[i]) == 0) {
				output.append("\t\t" + lines1[i] + System.lineSeparator());
			}else{
				output.append(lines1[i] +System.lineSeparator());
				output.append("\t" + lines2[i] + System.lineSeparator());
			}
		}
		for (int i = minSize; i < lines1.length; i++){
			output.append(lines1[i] + System.lineSeparator());
		}
		for (int i = minSize; i < lines2.length; i++){
			output.append(lines2[i] + System.lineSeparator());
		}
		return output.toString();
	}

	// TODO
	@Override
	public String getHelp() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		setStatusCode(0);
		return prop.getProperty("commHelp");
	}

	// TODO
	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length == 0 || !args[0].equals("comm")) {
			setStatusCode(127);
			return "";
		}else if (args.length > 1 && args[0].equals("comm")){
			if (args.length ==2){
				 if ((args[1].equals("-help"))){
					return getHelp();
				}else{
					setStatusCode(98);
					return "";
				}
			}else if (args.length == 3){
				return compareFiles(
						Common.concatenateDirectory(workingDir.getAbsolutePath(), args[1]), 
						Common.concatenateDirectory(workingDir.getAbsolutePath(), args[2])
						);
			}else if (args.length == 4){
				if (args[1].equals("-c")){
					return compareFilesCheckSortStatus (
							Common.concatenateDirectory(workingDir.getAbsolutePath(), args[2]), 
							Common.concatenateDirectory(workingDir.getAbsolutePath(), args[3])
							);
				}else if (args[1].equals("-d")){
					return compareFilesDoNotCheckSortStatus(
							Common.concatenateDirectory(workingDir.getAbsolutePath(), args[2]), 
							Common.concatenateDirectory(workingDir.getAbsolutePath(), args[3])
							);
				}else{
					setStatusCode(98);
					return "";
				}
			}else{
				setStatusCode(2);
				return "";
			}
		}else{
			setStatusCode(2);
			return "";
		}
	}
}
