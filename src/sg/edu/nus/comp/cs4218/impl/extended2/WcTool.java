package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
	 * Helper method to check whether the given filename exists in the system
	 * @param	filename	the given filename
	 * @return	true if the file exists
	 */
	public static boolean checkFileExistence(String filename){
		File f = new File(filename);
		return f.exists();
	}

	@Override
	public String execute(final File workingDir, final String stdin) {
		String characterCount = null;
		String wordCount = null;
		String newLineCount = null;
		if(args.length == 2){
			if(args[1].equals("-help")){
				return getHelp();
			}
			else{
				String filename = args[args.length-1];
				if(filename.equals("-")){
					String input = stdin;
					characterCount = getCharacterCount(input);
					wordCount = getWordCount(input);
					newLineCount = getNewLineCount(input);
				}
				else{
					if(checkFileExistence(filename)){
						String input = null;
						try {
							input = readFile(filename);
						} catch (IOException e) {		
							System.err.print("IO Exception caught");
							setStatusCode(1);
							return null;
						}
						characterCount = getCharacterCount(input);
						wordCount = getWordCount(input);
						newLineCount = getNewLineCount(input);
					}
					else{
						setStatusCode(3);
						return null;
					}
				}
			}
		}
		else if(args.length == 3 || args.length == 4 || args.length == 5){
			String filename = args[args.length-1];
			if(filename.equals("-")){
				String input = stdin;
				characterCount = getCharacterCount(input);
				wordCount = getWordCount(input);
				newLineCount = getNewLineCount(input);
			}
			else{
				if(checkFileExistence(filename)){
					String input = null;
					try {
						input = readFile(filename);
					} catch (IOException e) {
						System.err.print("IO Exception caught");
						setStatusCode(1);
						return null;
					}
					for(int i = 1; i < args.length - 1; i++){
						if(args[i].equals("-m")||args[i].equals("-w")||args[i].equals("-l")){
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
							System.err.println("Illegal Option");
							setStatusCode(2);
							return null;
						}
					}
				}
				else{
					setStatusCode(3);
					return null;
				}
			}
		}
		else{
			setStatusCode(2);
			return null;
		}

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
			String[] token = (input.trim()).split("\\s+");
			count = token.length;
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
		for(int i=0; i< input.length(); i++){
			if(input.charAt(i) == '\n'){
				count++;
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
