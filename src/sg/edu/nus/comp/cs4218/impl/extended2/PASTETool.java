package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;

public class PASTETool extends ATool implements IPasteTool {

	public PASTETool(String[] arguments) {
		super(arguments);
		if (args.length == 0 || !args[0].equals("paste")) {
			setStatusCode(127);
		}
	}

	/**
	 * Is called using -s flag.
	 * @param input all files' data separated by newlines
	 * @return final output
	 */
	@Override
	public String pasteSerial(String[] input) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String line : input) {
			stringBuilder.append(line);
			// If not end of file, add tab.
			if (!(line.equals("\n") || line.equals("\r"))) {
				stringBuilder.append('\t');
			} else {
				stringBuilder.deleteCharAt(stringBuilder.length() - 2);
			}
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.append('\n');
		
		return stringBuilder.toString();
	}

	/**
	 * Is called using -d flag, no flag defaults to "-d \t".
	 * @param delim one or more delimiters for separating files' data in output into columns
	 * @param input all files' data separated by newlines
	 * @return final output
	 */
	@Override
	public String pasteUseDelimiter(String delim, String[] input) {
		ArrayList<String> output = new ArrayList<>();
		
		int filesDone = 0;
		int delimLength = delim.length();
		int fileLength = -1;
		int currentDelimIndex = 0;	
		
		for (int i = 0; i < input.length; i++) {
			String line = input[i];
			if ((line.equals("\n") || line.equals("\r"))) {
				if (fileLength == -1)
					fileLength = i;
				filesDone++;
				currentDelimIndex = 0;
			} else if (filesDone == 0) {
				output.add(line);
			} else {
				if (delimLength == 0) {
					setStatusCode(127);
				} else {
					int newIndex = i - filesDone*(fileLength + 1);
					output.set(newIndex, output.get(newIndex) + delim.charAt(currentDelimIndex) + line);
					currentDelimIndex = (currentDelimIndex + 1) % delimLength;
				}	
			}

		}
		
		StringBuilder stringBuilder = new StringBuilder();
		for (String row : output) {
			stringBuilder.append(row);
			stringBuilder.append('\n');
		}
		
		return stringBuilder.toString();
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
		return prop.getProperty("pasteHelp");
	}

	/**
	 * The general go-to method for using the tool that calls
	 * the suitable submethods.
	 * @param workingDir current working directory
	 * @param stdin optional standard input from e.g. pipe tool
	 * @return output
	 */
	@Override
	// assumes files are separated by \n or \r
	public String execute(File workingDir, String stdin) {
		// using catTool to call the file reading method
		CatTool catTool = new CatTool(new String[]{"cat"});
		
		String returnable = null;
		
		if (args.length < 2) {
			setStatusCode(127);
		} else if (args.length == 2 && args[1].equals("-help")) {
			returnable = getHelp();
		} else {
			// put all arguments into one string
			String flag = args[1];
			StringBuilder builder = new StringBuilder();
			for(String s : args) {
			    builder.append(s);
			    builder.append(' ');
			}
			String arguments = builder.toString().trim();
			
			// extract the filenames from the arguments
			String mode = "default";
			Pattern filesPattern = Pattern.compile("(?<=(^paste(( -d \\S{0,10})|( -s))?)) ((?<!-)\\S)+( ((?<!-)\\S)+)*( -)?");
			Matcher matcher = filesPattern.matcher(arguments);
			if (flag.equals("-s")) {
				mode = "s";
				matcher.find();
			} else if (flag.equals("-d")) {
				mode = "d";
				matcher.find();
			}
			matcher.find();
			
			// storing filenames in an array
			String[] fileNames = matcher.group(0).trim().split(" ");
			
			ArrayList<String> input = new ArrayList<>();
			for (String fileName : fileNames) {
				if (fileName.equals("-")) {
					input.addAll(new ArrayList<String>(Arrays.asList(stdin.split("\n"))));
				} else {
					String fileContent = catTool.getStringForFile(new File(fileName));
					fileContent = fileContent.trim(); // for removing trailing newline
					input.addAll(new ArrayList<String>(Arrays.asList(fileContent.split("\n"))));
				}
				input.add("\n");
			}
			input.remove(input.size() - 1);
			
			String[] inputAsArray = input.toArray(new String[input.size()]);
			
			switch (mode) {
				case "default":
					returnable = pasteUseDelimiter("\t", inputAsArray);
					break;
				case "s":
					returnable = pasteSerial(inputAsArray);
					break;
				case "d":
					returnable = pasteUseDelimiter(args[2], inputAsArray);
					break;
				default:
					setStatusCode(127);
					break;
			}
			
		}

		return returnable;
	}
}
