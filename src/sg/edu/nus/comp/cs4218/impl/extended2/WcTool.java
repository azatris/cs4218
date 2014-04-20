package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class WcTool extends ATool implements IWcTool{
	/**
	 * Constructor taking the arguments
	 * @param	arguments	(args[0] is the command name)
	 */

	public WcTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("wc")) {
			setStatusCode(127);
		}
	}

	/**
	 * Helper method to open a stream to a file and read its content
	 * @param	filename	the name of the file
	 * @return	the content of the file
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
	 * The general go-to method for using the tool that calls
	 * the suitable submethods.
	 * @param workingDir current working directory
	 * @param stdin optional standard input from e.g. pipe tool
	 * @return output
	 */
	@Override
	public String execute(final File workingDir, final String stdin) {
		String characterCount = null;
		String wordCount = null;
		String newLineCount = null;
		String output = null;
		if(args.length == 2 &&args[1].equals("-help")){
			output = getHelp();
		}
		else if(args.length >= 2 && args.length <= 5){
			String filename = args[args.length-1];
			String input;
			try {
				input = checkStdinOrFile(stdin, filename);
			} catch (IOException e) {
				setStatusCode(4); //IO Exception caught
				return null;
			}
			//If arguments length is 2, then there is no option given, count the char, word, and new line
			if(args.length == 2){
				characterCount = getCharacterCount(input);
				wordCount = getWordCount(input);
				newLineCount = getNewLineCount(input);
			}
			//If there is argument(s), check which char/word/newline that need to be counted
			else{
				for(int i = 1; i < args.length - 1; i++){
					if(checkValidOption(i, args)){
						if(args[i].equals("-m")){
							characterCount = getCharacterCount(input);
						}
						if(args[i].equals("-w")){
							wordCount = getWordCount(input);
						}
						if(args[i].equals("-l")){
							newLineCount = getNewLineCount(input);
						}
					}
					else{
						//Invalid Option is detected
						setStatusCode(5);
						return null;
					}
				}
			}
			output = createOutput(characterCount, wordCount, newLineCount);
		}
		else{
			setStatusCode(2); //normal # of args is 2-5, returns error if it doesn't obey this limit
			output = null;
		}
		return output;
	}

	/**
	 * Helper function to return the corresponding input, whether it is from stdin or from a file
	 * @param 	stdin	standard in to the tool
	 * @param 	filename	filename that need to be parsed ("-" denotes standard input)
	 * @return	the corresponding input that needs to be fed to wc tool
	 */
	private String checkStdinOrFile(final String stdin, final String filename) throws IOException{
		String input = null;
		if(filename.equals("-")){
			input = stdin;
		}
		else{
			input = readFile(filename);
		}
		return input;
	}

	/**
	 * Check valid options for wc tool
	 * @param 	argIndex	index of the argument that need to be checked
	 * @param 	arguments	array of arguments
	 * @return	true or false depending the validity of the options
	 */
	private boolean checkValidOption(int argIndex, String[] arguments) {
		String arg = arguments[argIndex];
		return "-m".equals(arg)||"-w".equals(arg)||"-l".equals(arg);
	}

	/**
	 * Concatenate all of the character count, word count, and new line count
	 * @param characterCount
	 * @param wordCount
	 * @param newLineCount
	 * @return	the concatenated output
	 */
	private String createOutput(String characterCount, String wordCount, String newLineCount) {
		StringBuilder output = new StringBuilder();
		//Print the output
		if(characterCount != null){
			output.append("   " + "Character: "+ characterCount);
		}
		if(wordCount != null){
			output.append("   " + "Word: "+ wordCount);
		}
		if(newLineCount != null){
			output.append("   " + "New Line: "+ newLineCount);
		}
		return output.toString();
	}

	/**
	 * Count the number of characters in the given input
	 * @param	input	the input string (/file content) whose characters need to be counted
	 * @return	number of characters in the input
	 */
	@Override
	public String getCharacterCount(final String input) {
		int count = 0;
		if(input!=null){
			count = input.length();
		}
		else{
			setStatusCode(1);
		}
		return Integer.toString(count);	
	}

	/**
	 * Count the number of words in the given input
	 * @param	input	the input string (/file content) whose words need to be counted
	 * @return	number of words in the input
	 */
	@Override
	public String getWordCount(final String input) {
		int count = 0;
		if(input!=null){
			//Special case for empty string
			if(input == ""){
				count = 0;
			}
			else{
				String[] token = (input.trim()).split("\\s+");
				count = token.length;
			}
		}
		else{
			setStatusCode(1);
		}
		return Integer.toString(count);
	}

	/**
	 * Count the number of newlines in the given input
	 * @param	input	the input string (/file content) whose newlines need to be counted
	 * @return	number of newlines in the input
	 */
	@Override
	public String getNewLineCount(final String input) {
		int count = 0;
		if(input!=null){
			for(int i=0; i< input.length(); i++){
				if(input.charAt(i) == '\n'){
					count++;
				}
			}
		}
		return Integer.toString(count);
	}

	/**
	 * Get Help Explanation
	 * @return	the help explanation
	 */
	public String getHelp() {
		String help = "Command Format - wc [OPTIONS] [FILE]\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") use standard input\n"
				+ "OPTIONS\n"
				+ "-m : Print only the character counts\n"
				+ "-w : Print only the word counts\n"
				+ "-l : Print only the newline counts\n"
				+ "-help : Brief information about supported options";
		return help;
	}

}
