package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class UniqTool extends ATool implements IUniqTool{

	private static PrintStream err = System.err;
	/**
	 * Constructor taking the arguments
	 * Multiple files are not supported by this UniqTool
	 * @param	arguments	(args[0] is the command name)
	 */
	public UniqTool(final String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("uniq")) {
			setStatusCode(127);
		}
	}


	/**
	 * Helper method to open a stream to a file and read its content
	 * @param the name of the file
	 * @return the content of the file
	 */
	public static String readFile(final String filename) throws IOException{
		final FileInputStream inputStream = new FileInputStream(filename);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder builder = new StringBuilder();
		int currentChar = reader.read();
		while(currentChar != -1){
			builder.append((char)currentChar);
			currentChar = reader.read();
		}
		reader.close();
		inputStream.close();
		return builder.toString();

	}

	/**
	 * Helper method to skip NUM tokens from the line
	 * @param	line	line whose tokens need to be skipped
	 * @param	NUM	number of token need to be skipped
	 * @return	line with skipped tokens
	 */
	private String skipFields(final String line, final int NUM){
		if(line == null || line.equals("")){
			return line;
		}
		else{
			int skipCount = 0;
			//normalize the counting, in case the first character is the null
			String currentLine = line.trim();
			for(int i = 0; i < currentLine.length()-1; i++){
				if(((currentLine.charAt(i) == ' ')||(currentLine.charAt(i) == '\t'))&&
						!((currentLine.charAt(i+1) == ' ')||(currentLine.charAt(i+1) == '\t'))){
					skipCount++;
				}
				if(skipCount == NUM){
					return currentLine.substring(i+1);
				}
			}

			//return empty string when the NUM > skipCount (number of tokens available in this line)
			return "";
		}
	}

	/**
	 * The general go-to method for using the tool that calls
	 * the suitable submethods.
	 * @param workingDir current working directory
	 * @param stdin optional standard input from e.g. pipe tool
	 * @return output
	 */
	@Override
	public String execute(final File workingDir, final String stdin) {
		String output = null;
		//case number of arguments is 2, then we only consider -help option
		if(args.length == 2 && args[1].equals("-help")){
			output = getHelp();
		}
		else if(args.length >= 2 && args.length <= 5){
			boolean checkCase = true; //default value
			boolean skipChar = false; //default value
			int offSet = 0;

			//check the option and set the flag
			for(int i = 1; i < args.length - 1; i++){
				if(checkValidArguments(args[i], i, args)){
					if(args[i].equals("-i")){
						checkCase = false;
					}
					if(args[i].equals("-f")){
						try{
							offSet = getOffSetNumber(i, args.length-2, args);
							skipChar = true;
						}
						catch(Exception e){
							setStatusCode(2);
							err.println("-f needs to be followed with an int value");
							return null;
						}
					}
				}
				else{
					setStatusCode(2);
					err.println("Invalid Arguments detected");
					return null;
				}
			}

			//Determine the input
			String filename = args[args.length-1];
			String input = checkStdinOrFile(stdin, filename);

			//Determine whether we need to skip some char
			output = makeTheInputUnique(checkCase, skipChar, offSet, input);
		}
		else{
			setStatusCode(2); //normal # of args is 2-5, returns error if it doesn't obey this limit
			output = null;
		}
		return output;
	}

	private String makeTheInputUnique(boolean checkCase, boolean skipChar, int offSet, String input) {
		String output = null;
		if(skipChar == true){
			output = getUniqueSkipNum(offSet, checkCase, input);
		}
		else{
			output = getUnique(checkCase, input);
		}
		return output;
	}

	private boolean checkValidArguments(String arg, int argIndex, String[] arguments) {
		boolean valid = false;
		if ("-i".equals(arg) || "-f".equals(arg)){
			valid = true; //if it is -f or -i, we consider it as valid arguments
		}
		//other case for valid argument is that it is integer that follows after -f
		else{
			try{
				Integer.parseInt(arguments[argIndex]);
				if("-f".equals(arguments[argIndex-1])){
					valid = true;
				}
			}
			catch(NumberFormatException e){
				setStatusCode(5);
				valid = false;
			}
		}
		return valid;
	}

	private String checkStdinOrFile(final String stdin, final String filename) {
		String input = null;
		if(filename.equals("-")){
			input = stdin;
		}
		else{
			try{
				input = readFile(filename);
			} catch (IOException e) {
				setStatusCode(4);
				return null;
			}
		}
		return input;
	}

	private int getOffSetNumber(int argIndex, int lengthOfArgsForOptions, String[] arguments) throws Exception{
		int offSet = 0;
		//check if there is still another argument after -f
		if(argIndex != lengthOfArgsForOptions){
			offSet = Integer.parseInt(arguments[argIndex+1]);
			return offSet;
		}
		else{
			throw new Exception();
		}
	}

	/**
	 * Check the given input, and eliminate any duplicate line
	 * @param	checkCase	false if ignore checking the case
	 * @param	input	the input to be checked
	 * @return	the unique line
	 */
	public String getUnique(boolean checkCase, String input) {
		if(input != null){
			String lineSeparator = getLineSeparator(input);; //determine the line separator for the input (different OS different line separator)
			String[] line = input.split(lineSeparator);

			String currentLine = null;
			String prevLine = null;
			StringBuilder output = new StringBuilder();
			for (int i = 0; i < line.length; i++){
				currentLine = line[i];
				currentLine = considerCase(checkCase, currentLine);
				prevLine = considerCase(checkCase, prevLine);

				//Check similarity with previous line
				if(!currentLine.equals(prevLine)){
					output.append(line[i]);
					boolean isLastLine = (i == line.length-1);
					boolean inputEndsWithLineSeparator = input.endsWith(lineSeparator);
					appendLineSeparatorAsNeeded(output, lineSeparator, isLastLine, inputEndsWithLineSeparator);
				}
				prevLine = line[i];
			}
			return output.toString();
		}
		else{
			return ""; //input is null
		}
	}

	/**
	 * Helper function to convert a sentence to lower case when the case is unimportant
	 * @param	checkCase	false if ignore checking the case
	 * @param	line	line that needed to be converted
	 * @return	the line in lower case (if it needs to be converted) or the original line
	 */
	private String considerCase(boolean checkCase, String line){
		if(line != null){
			if(!checkCase){
				return line.toLowerCase();
			}
			else{
				return line;
			}
		}
		else{
			return null;
		}
	}

	/**
	 * Check the line separator of the given input (depending on the os)
	 * @param	input	the input to be checked
	 * @return	the line separator
	 */
	private String getLineSeparator(String input) {
		String lineSeparator;
		if(input.contains("\r\n")){
			lineSeparator = "\r\n";
		}
		else if(input.contains("\n")){
			lineSeparator = "\n";
		}
		else{
			lineSeparator = "\n"; //by default
		}
		return lineSeparator;
	}

	/**
	 * Check the given input, and eliminate any duplicate line (considering skipping some tokens)
	 * @param	checkCase	false if ignore checking the case
	 * @param	input	the input to be checked
	 * @return	the unique line
	 */
	public String getUniqueSkipNum(final int NUM, final boolean checkCase, final String input) {
		if(input != null){
			String lineSeparator = getLineSeparator(input);; //determine the line separator for the input (different OS different line separator)
			String[] line = input.split(lineSeparator);

			String currentLine = null;
			String prevLine = null;
			StringBuilder output = new StringBuilder();
			for (int i = 0; i < line.length; i++){
				currentLine = line[i];
				currentLine = considerCase(checkCase, currentLine);
				prevLine = considerCase(checkCase, prevLine);

				//Set the offset
				if(NUM <= 0){
					setStatusCode(1);
					err.println("Invalid Skip Number, Skip Number must be greater or equal to 0");
					return null;
				}
				else{
					currentLine = skipFields(currentLine, NUM);
					prevLine = skipFields(prevLine,NUM);
				}

				//Check similarity with previous line
				if(!currentLine.equals(prevLine)){
					output.append(line[i]);
					boolean isLastLine = (i == line.length-1);
					boolean inputEndsWithLineSeparator = input.endsWith(lineSeparator);
					appendLineSeparatorAsNeeded(output, lineSeparator, isLastLine, inputEndsWithLineSeparator);
				}

				prevLine = line[i];
			}
			return output.toString();
		}
		else{
			return ""; //input is null
		}

	}

	/**
	 * Helper function to append line separator if it is needed
	 * @param	output	builder for output
	 * @param	lineSeparator	line separator used in the input
	 * @param	isLastLine	boolean operator to tell if the currentLine is the last line of the input
	 * @param	inputEndsWithLineSeparator	boolean operator to tell that the original input ends with line separator
	 */

	private void appendLineSeparatorAsNeeded(StringBuilder output, String lineSeparator, boolean isLastLine, boolean inputEndsWithLineSeparator) {
		//Add line separator if this is not the last line
		if(!isLastLine){
			output.append(lineSeparator);
		}
		else{
			//add line separator if the original input ends with new line
			if(inputEndsWithLineSeparator){
				output.append(lineSeparator);
			}
		}
	}

	/**
	 * Get Help Explanation
	 * @return	the help explanation
	 */
	public String getHelp() {
		String help = "Command Format - uniq [OPTIONS] [FILE]\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") use standard input\n"
				+ "OPTIONS\n"
				+ "f NUM : Skips NUM fields on each line before checking for uniqueness. Use a null\n"
				+ "string for comparison if a line has fewer than n fields. Fields are sequences of\n"
				+ "non-space non-tab characters that are separated from each other by at least one\n"
				+ "space or tab.\n"
				+ "-i : Ignore differences in case when comparing lines.\n"
				+ "-help : Brief information about supported options";
		return help;
	}

}
