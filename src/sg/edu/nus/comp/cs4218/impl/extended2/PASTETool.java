package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

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

	@Override
	public String pasteSerial(String[] input) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String word : input) {
			stringBuilder.append(word);
			
			// If not end of file, add tab.
			if (!(word.equals("\n") || word.equals("\r")))
				stringBuilder.append('\t');
		}
		stringBuilder.append('\n');
		
		return stringBuilder.toString();
	}

	@Override
	public String pasteUseDelimiter(String delim, String[] input) {
		ArrayList<String> output = new ArrayList<>();
		
		int filesDone = 0;
		int fileLength = -1;
		int currentDelimIndex = 0;	
		int delimLength = delim.length();
		
		for (int i = 0; i < input.length; i++) {
			String word = input[i];
			
			if ((word.equals("\n") || word.equals("\r"))) {
				if (filesDone == 0) {
					fileLength = i;
				}
				
				filesDone++;
				continue;
			}
		
			if (filesDone == 0) {
				output.add(i, word);
			} else {
				int newIndex = i - filesDone*(fileLength + 1);
				output.add(newIndex, output.get(newIndex) + delim.charAt(currentDelimIndex) + input[i]);
				currentDelimIndex = (currentDelimIndex + 1) % delimLength;
			}

		}
		
		StringBuilder stringBuilder = new StringBuilder();
		for (String row : output) {
			stringBuilder.append(row);
			stringBuilder.append('\n');
		}
		
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
		return prop.getProperty("pasteHelp");
	}

	@Override
	// assumes files are separated by \n or \r
	public String execute(File workingDir, String stdin) {
		// using catTool to call the file reading method
		CatTool catTool = new CatTool(new String[]{"cat"});
		
		String returnable = null;
		
		if (args.length < 2) {
			setStatusCode(127);
		} else if (args.length == 2) {
			String argument = args[1];
			switch (argument) {
			case "-help":
				returnable = getHelp();
				break;
			case "-":
				if (stdin != null) {
					returnable = pasteUseDelimiter("\t", stdin.split("\\r?\\n"));
				} else {
					setStatusCode(127);	
				}
				break;
			case "-C":
				returnable = getMatchingLinesWithOutputContext(num, pattern, input);
				break;
			default:
				setStatusCode(127);
				break;
		}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		if (args.length == 5) {
			String argument = args[1];
			int num = Integer.parseInt(args[2]);
			String pattern = args[3];
			String input;
			if (stdin != null) {
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
		return null;
	}

}
