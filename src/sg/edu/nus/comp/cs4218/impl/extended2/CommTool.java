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
	private boolean checkSorted(String[] lines, int curIdx, boolean firstTimeCheck, int fileNum, StringBuilder output){
		if (curIdx + 1 <lines.length && 
				lines[curIdx].compareTo(lines[curIdx+1]) > 0 &&
				firstTimeCheck){
			output.append( "comm: File " + fileNum + " is not in sorted order ");
			output.append(System.lineSeparator());
			firstTimeCheck = false;
		}
		return firstTimeCheck;
	}
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
		int i = 0, j = 0;
		if (lines1.length == 1 && lines1[0].equals("")){
			i = 1;
		}
		if (lines2.length == 1 && lines2[0].equals("")){
			j = 1;
		}
		boolean fstTimeSortedCheck1=true, fstTimeSortedCheck2=true;
		for (; i<lines1.length && j<lines2.length;){
			fstTimeSortedCheck1 = checkSorted(lines1, i, fstTimeSortedCheck1, 1, output);
			fstTimeSortedCheck2 = checkSorted(lines2, j, fstTimeSortedCheck2, 2, output);
			if (lines1[i].compareTo(lines2[j]) == 0) {
				output.append("\t\t" + lines1[i] + System.lineSeparator());
				i++; j++;
			}else if (lines1[i].compareTo(lines2[j]) < 0){
				output.append(lines1[i] + System.lineSeparator());
				i++;
			}else{
				output.append("\t" + lines2[j] + System.lineSeparator());
				j++;
			}
		}
		for (int k = i; k < lines1.length; k++){
			fstTimeSortedCheck1 = checkSorted(lines1, k, fstTimeSortedCheck1, 1, output);
			output.append(lines1[k] + System.lineSeparator());
		}
		for (int k = j; k < lines2.length; k++){
			fstTimeSortedCheck2 = checkSorted(lines2, k, fstTimeSortedCheck2, 2, output);
			output.append( "\t" + lines2[k] + System.lineSeparator());
		}
		setStatusCode(0);
		return output.toString();
	}

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
		
		int i = 0, j = 0;
		if (lines1.length == 1 && lines1[0].equals("")){
			i = 1;
		}
		if (lines2.length == 1 && lines2[0].equals("")){
			j = 1;
		}
		boolean fstTimeSortedCheck1=true, fstTimeSortedCheck2=true;
		for (; i<lines1.length && j<lines2.length;){
			fstTimeSortedCheck1 = checkSorted(lines1, i, fstTimeSortedCheck1, 1, output);
			fstTimeSortedCheck2 = checkSorted(lines2, j, fstTimeSortedCheck2, 2, output);
			if (!fstTimeSortedCheck1 || !fstTimeSortedCheck2){
				break;
			}
			if (lines1[i].compareTo(lines2[j]) == 0) {
				output.append("\t\t" + lines1[i] + System.lineSeparator());
				i++; j++;
			}else if (lines1[i].compareTo(lines2[j]) < 0){
				output.append(lines1[i] + System.lineSeparator());
				i++;
			}else{
				output.append("\t" + lines2[j] + System.lineSeparator());
				j++;
			}
		}
		
		for (int k = j; k < lines2.length && fstTimeSortedCheck2; k++){
			fstTimeSortedCheck2 = checkSorted(lines2, k, fstTimeSortedCheck2, 2, output);
			if (fstTimeSortedCheck2 == false)
				break;
			output.append( "\t" + lines2[k] + System.lineSeparator());
		}
		for (int k = i; fstTimeSortedCheck1 && (k < lines1.length); k++){
			fstTimeSortedCheck1 = checkSorted(lines1, k, fstTimeSortedCheck1, 1, output);
			if (fstTimeSortedCheck1 == false)
				break;
			output.append(lines1[k] + System.lineSeparator());
		}
		setStatusCode(0);
		return output.toString();
	}
	
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
		
		int i = 0, j = 0;
		if (lines1.length == 1 && lines1[0].equals("")){
			i = 1;
		}
		if (lines2.length == 1 && lines2[0].equals("")){
			j = 1;
		}
		for (; i<lines1.length && j<lines2.length;){
			if (lines1[i].compareTo(lines2[j]) == 0) {
				output.append("\t\t" + lines1[i] + System.lineSeparator());
				i++; j++;
			}else if (lines1[i].compareTo(lines2[j]) < 0){
				output.append(lines1[i] + System.lineSeparator());
				i++;
			}else{
				output.append("\t" + lines2[j] + System.lineSeparator());
				j++;
			}
		}
		for (int k = i; k < lines1.length; k++){
			output.append(lines1[k] + System.lineSeparator());
		}
		for (int k = j; k < lines2.length; k++){
			output.append( "\t" + lines2[k] + System.lineSeparator());
		}
		
		setStatusCode(0);
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
		setStatusCode(0);
		return prop.getProperty("commHelp");
	}

	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length > 1 && args.length < 5 && args[0].equals("comm")){
			if (args.length ==2){
				 if ((args[1].equals("-help"))){
					return getHelp();
				}else{
					setStatusCode(98);
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
				}
			}
		}else{
			setStatusCode(2);
		}
		return "";
	}
}
