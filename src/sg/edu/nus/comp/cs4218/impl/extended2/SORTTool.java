package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;

public class SORTTool extends ATool implements ISortTool {
	public SORTTool(String[] arguments) {
		super(arguments);
		if (args.length == 0 || !args[0].equals("sort")) {
			setStatusCode(127);
		}
	}

	/**
	 * Sorts files alphabetically
	 * @param input concatenated string of files to be sorted
	 * @return sorted concatenated string
	 */
	@Override
	public String sortFile(String input) {
		// putting all of the rows in an array
		ArrayList<String> sortList = new ArrayList<>();
		Scanner scanner = new Scanner(input);
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			sortList.add(currentLine);
		}
		scanner.close();
		
		Collections.sort(sortList);
		
		// putting the sorted list back to printable form
		StringBuilder stringBuilder = new StringBuilder();
		for(String row: sortList){
			stringBuilder.append(row);
		    stringBuilder.append('\n');
		}
		
		return stringBuilder.toString();
	}

	/**
	 * Checks whether a particular file is sorted,
	 * if not points out the first occurrence of disorder.
	 * @param input name of file to be sorted
	 * @return empty if sorted, else the disorder occurrence
	 */
	@Override
	public String checkIfSorted(String input) {
		String firstUnsortedLine = "";
		
		CatTool catTool = new CatTool(new String[]{"cat"});
		String fileData = catTool.getStringForFile(new File(input));

		// finding the first unsorted line, if it exists
		Scanner scanner = new Scanner(fileData);
		String currentLine;
		String lastLine = "";
		int lineNumber = 1;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			if (lastLine.compareTo(currentLine) <= 0) {
				lastLine = currentLine;
			} else {
				firstUnsortedLine = "sort: " + input + ":" + lineNumber + " disorder: " + currentLine + "\n";
				break;
			}
			lineNumber++;
		}
		scanner.close();
		
		return firstUnsortedLine;
	}

	/**
	 * Prints general information about the usage of the tool.
	 * @return help text
	 */
	@Override
	public String getHelp() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return prop.getProperty("sortHelp");
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
		// using catTool to call the file reading method
		CatTool catTool = new CatTool(new String[]{"cat"});
		
		String returnable = null;
		
		if (args.length < 2) {
			setStatusCode(127);
		} else if (args.length == 2 && args[1].equals("-help")) {
			returnable = getHelp();
		} else if (args[1].equals("-c")) {
			if (args.length == 3) {
				if (args[2].equals("-")) {
					setStatusCode(127);
				} else {
					returnable = checkIfSorted(args[2]);
				}
			} else {
				setStatusCode(127);
			}
		} else {
			// put all arguments into one string
			StringBuilder builder = new StringBuilder();
			for (String s : args) {
				builder.append(s);
				builder.append(' ');
			}
			String arguments = builder.toString().trim();
			
			// extract the filenames from the arguments
			Pattern filesPattern = Pattern.compile("(?<=(^sort( -c)?)) ((?<!-)\\S)+( ((?<!-)\\S)+)*( -)?");
			Matcher matcher = filesPattern.matcher(arguments);
			matcher.find();
			
			// storing filenames in an array
			String[] fileNames = matcher.group(0).trim().split(" ");
			
			StringBuilder input = new StringBuilder();
			for (String fileName : fileNames) {
				if (fileName.equals("-")) {
					input.append(stdin);
				} else {
					String fileContent = catTool.getStringForFile(new File(fileName));
					fileContent = fileContent.trim(); // for removing trailing newline
					input.append(fileContent);
				}
				input.append('\n');
			}
			input.deleteCharAt(input.length() - 1);
			
			String concatenatedInput = input.toString();

			returnable = sortFile(concatenatedInput);
		}

		return returnable;
	}

}
