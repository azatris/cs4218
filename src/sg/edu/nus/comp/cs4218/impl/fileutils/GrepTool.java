package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class GrepTool extends ATool implements IGrepTool {

	public GrepTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public int getCountOfMatchingLines(String pattern, String input) {
		Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
		
		int count = 0;
		Scanner scanner = new Scanner(input);
		while (scanner.hasNextLine()) {
			Matcher regexMatcher = regexp.matcher(scanner.nextLine());
			if (regexMatcher.find()) {
			    count++;
			} 
		}
		scanner.close();
		
		return count;
	}

	@Override
	public String getOnlyMatchingLines(String pattern, String input) {
		Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
		
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(input);
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			Matcher regexMatcher = regexp.matcher(currentLine);
			if (regexMatcher.find()) {
			    stringBuilder.append(currentLine);
			    stringBuilder.append('\n');
			} 
		}
		scanner.close();
		
		return stringBuilder.toString();
	}

	@Override
	public String getMatchingLinesWithTrailingContext(int option_A,
			String pattern, String input) {
		Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
		
		int linesShown = option_A;
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(input);
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			Matcher regexMatcher = regexp.matcher(currentLine);
			if (regexMatcher.find()) {
			    stringBuilder.append(currentLine);
			    stringBuilder.append('\n');
			    linesShown = 0;
			} else if (linesShown < option_A) {
				stringBuilder.append(currentLine);
			    stringBuilder.append('\n');
			    linesShown++;
			}
		}
		scanner.close();
		
		return stringBuilder.toString();
	}

	@Override
	public String getMatchingLinesWithLeadingContext(int option_B,
			String pattern, String input) {
		Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
		
		Queue<String> leadingContext = new LinkedList<String>();
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(input);
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			Matcher regexMatcher = regexp.matcher(currentLine);
			if (regexMatcher.find()) {
				String leadingContextLine = leadingContext.poll();
				while (leadingContextLine != null) {
					stringBuilder.append(leadingContextLine);
					stringBuilder.append('\n');
					leadingContextLine = leadingContext.poll();
				}
				stringBuilder.append(currentLine);
				stringBuilder.append('\n');
			} else {
				if (leadingContext.size() == option_B) {
					leadingContext.remove();
					leadingContext.add(currentLine);
				} else {
					leadingContext.add(currentLine);
				}
			}
		}
		scanner.close();
		
		return stringBuilder.toString();
	}

	@Override
	public String getMatchingLinesWithOutputContext(int option_C,
			String pattern, String input) {
		Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
		
		int trailingContext = 0;
		Queue<String> leadingContext = new LinkedList<String>();
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(input);
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			Matcher regexMatcher = regexp.matcher(currentLine);
			if (regexMatcher.find()) {
				String leadingContextLine = leadingContext.poll();
				while (leadingContextLine != null) {
					stringBuilder.append(leadingContextLine);
					stringBuilder.append('\n');
					leadingContextLine = leadingContext.poll();
				}
				stringBuilder.append(currentLine);
				stringBuilder.append('\n');
				trailingContext = option_C;
			} else {
				if (trailingContext > 0) {
					stringBuilder.append(currentLine);
					stringBuilder.append('\n');
					trailingContext--;
				} else if (leadingContext.size() == option_C) {
					leadingContext.remove();
					leadingContext.add(currentLine);
				} else {
					leadingContext.add(currentLine);
				}
			}
		}
		scanner.close();
		
		return stringBuilder.toString();
	}

	@Override
	public String getMatchingLinesOnlyMatchingPart(String pattern, String input) {
		Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
		
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(input);
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			Matcher regexMatcher = regexp.matcher(currentLine);
			while (regexMatcher.find()) {
			    stringBuilder.append(regexMatcher.group());
			    stringBuilder.append('\n');
			} 
		}
		scanner.close();
		
		return stringBuilder.toString();
	}

	@Override
	public String getNonMatchingLines(String pattern, String input) {
		Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
				
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(input);
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			Matcher regexMatcher = regexp.matcher(currentLine);
			if (!regexMatcher.find()) {
			    stringBuilder.append(currentLine);
			    stringBuilder.append('\n');
			} 
		}
		scanner.close();
		
		return stringBuilder.toString();
	}

	@Override
	public String getHelp() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return prop.getProperty("grepHelp");
	}

	@Override
	public String execute(File workingDir, String stdin) {
		String returnable = null;
		if (args.length == 5) {
			String argument = args[1];
			int num = Integer.parseInt(args[2]);
			String pattern = args[3];
			String input;
			if (stdin != null) {
				input = stdin;
			} else {
				input = args[4];	
			}
			switch (argument) {
			case "-A":
				returnable = getMatchingLinesWithTrailingContext(num, pattern, input);
				break;
			case "-B":
				returnable = getMatchingLinesWithLeadingContext(num, pattern, input);
				break;
			case "-C":
				returnable = getMatchingLinesWithOutputContext(num, pattern, input);
				break;
			default:
				setStatusCode(127);
				break;
			}
		} else if (args.length == 4) {
			String argument = args[1];
			String pattern = args[2];
			String input;
			if (stdin != null) {
				input = stdin;
			} else {
				input = args[3];	
			}
			switch (argument) {
			case "-c":
				returnable = Integer.toString(getCountOfMatchingLines(pattern, input));
				break;
			case "-o":
				returnable = getMatchingLinesOnlyMatchingPart(pattern, input);
				break;
			case "-v":
				returnable = getNonMatchingLines(pattern, input);
				break;
			default:
				setStatusCode(127);
				break;
			}
		} else if (args.length == 3) {
			String pattern = args[1];
			String input;
			if (stdin != null) {
				input = stdin;
			} else {
				input = args[2];	
			}
			returnable = getOnlyMatchingLines(pattern, input);
		} else if (args.length == 2) {
			if (args[1].equals("help")) {
				returnable = getHelp();
			}
		} else {
			setStatusCode(127);                                                                                                                                                                               
		}
		return returnable;
	}
}
