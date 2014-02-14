package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;

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

	@Override
	public String sortFile(String input) {
		// using catTool to call the file reading method
		CatTool catTool = new CatTool(new String[]{"cat"});
		String sortableString = catTool.getStringForFile(new File(input));
		
		// putting all of the rows in an array
		ArrayList<String> sortList = new ArrayList<>();
		Scanner scanner = new Scanner(sortableString);
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

	@Override
	public String checkIfSorted(String input) {
		String firstUnsortedLine = null;
		
		// using catTool to call the file reading method
		CatTool catTool = new CatTool(new String[]{"cat"});
		String sortableString = catTool.getStringForFile(new File(input));
		
		
		// finding the first unsorted line, if it exists
		Scanner scanner = new Scanner(sortableString);
		String currentLine;
		String lastLine = "";
		int lineNumber = 1;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			if (lastLine.compareTo(currentLine) <= 0) {
				lastLine = currentLine;
			} else {
				firstUnsortedLine = "sort: " + input + ":" + lineNumber + " disorder: " + currentLine;
				break;
			}
			lineNumber++;
		}
		scanner.close();
		
		return firstUnsortedLine;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		return null;
	}

}
