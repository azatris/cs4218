package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;

/**
 * Contains methods to call the grep command with
 * different arguments.
 */
public class GrepTool extends ATool implements IGrepTool {

	public GrepTool(String[] arguments) {
		super(arguments);
		if (args.length == 0 || !args[0].equals("grep")) {
			setStatusCode(127);
			
		}
	}

	/**
	 * Is called using "-c" flag.
	 * @param pattern regular expression
	 * @param input multiline string being matched 
	 * @return count of matched lines
	 */
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

	/**
	 * Trims the trailing newline of a stringBuilder.
	 * @param stringBuilder
	 * @return output
	 */
	private String trimNewLine(StringBuilder stringBuilder) {
		String output = stringBuilder.toString();
		int len = output.length();
		if (len > 0) {
			output = output.substring(0, len - 1);
		}
		return output;
	}
	
	/**
	 * Flag-free grep.
	 * @param pattern regular expression
	 * @param input multiline string being matched
	 * @return matched lines
	 */
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

		return trimNewLine(stringBuilder);
	}

	/**
	 * Is called using "-A NUM" flag.
	 * @param optionA number of trailing lines
	 * @param pattern regular expression
	 * @param input multiline string being matched 
	 * @return matched lines with trailing lines
	 */
	@Override
	public String getMatchingLinesWithTrailingContext(int optionA,
			String pattern, String input) {
		Pattern regexp = Pattern.compile(pattern, Pattern.MULTILINE);
		
		int linesShown = optionA;
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
			} else if (linesShown < optionA) {
				stringBuilder.append(currentLine);
			    stringBuilder.append('\n');
			    linesShown++;
			}
		}
		scanner.close();
		
		return trimNewLine(stringBuilder);
	}

	/**
	 * Is called using "-B NUM" flag.
	 * @param optionB number of leading lines
	 * @param pattern regular expression
	 * @param input multiline string being matched
	 * @return matched lines with leading lines
	 */
	@Override
	public String getMatchingLinesWithLeadingContext(int optionB,
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
				if (leadingContext.size() == optionB) {
					leadingContext.remove();
					leadingContext.add(currentLine);
				} else {
					leadingContext.add(currentLine);
				}
			}
		}
		scanner.close();
		
		return trimNewLine(stringBuilder);
	}

	/**
	 * Is called using "-C NUM" flag.
	 * @param optionC number of trailing lines
	 * @param pattern regular expression
	 * @param input multiline string being matched
	 * @return matched lines with leading and trailing lines
	 */
	@Override
	public String getMatchingLinesWithOutputContext(int optionC,
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
				trailingContext = optionC;
			} else {
				if (trailingContext > 0) {
					stringBuilder.append(currentLine);
					stringBuilder.append('\n');
					trailingContext--;
				} else if (leadingContext.size() == optionC) {
					leadingContext.remove();
					leadingContext.add(currentLine);
				} else {
					leadingContext.add(currentLine);
				}
			}
		}
		scanner.close();
		
		return trimNewLine(stringBuilder);
	}

	/**
	 * Is called using "-o" flag.
	 * @param pattern regular expression
	 * @param input multiline string being matched 
	 * @return matching part from the matched lines
	 */
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
		
		return trimNewLine(stringBuilder);
	}

	/**
	 * Is called using "-v" flag.
	 * @param pattern regular expression
	 * @param input multiline string being matched
	 * @return lines that did not match
	 */
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
		
		return trimNewLine(stringBuilder);
	}

	/**
	 * Is called using "-help" flag.
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
		return prop.getProperty("grepHelp");
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// using catTool to call the file reading method
		CatTool catTool = new CatTool(new String[]{"cat"});
		
		
		String returnable = null;
		if (args.length == 5) {
			String argument = args[1];
			int num = Integer.parseInt(args[2]);
			String pattern = args[3];
			String input;
			String fileName = args[4];
			if (fileName.equals("-")) {
				input = stdin;
			} else {
				input = catTool.getStringForFile(new File(args[4]));	
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
				input = catTool.getStringForFile(new File(args[3]));	
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
				input = catTool.getStringForFile(new File(args[2]));	
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
