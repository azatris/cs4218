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
	 * @param arguments (args[0] is the command name)
	 */
	public WcTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("wc")) {
			setStatusCode(127);
		}
	}

	/**
	 * Helper method to open a stream to a file and read its content
	 * @param the name of the file
	 * @return the content of the file
	 */
	public String readFile(String filename){
		try {
			FileInputStream inputStream = new FileInputStream(filename);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder builder = new StringBuilder();
			String line = br.readLine();
			while(line != null){
				builder.append(line);
				line = br.readLine();
				if(line != null){
					builder.append(System.getProperty("line.separator"));
				}
			}
			br.close();

			inputStream.close();
			return builder.toString();
		} catch (IOException e) {
			System.err.print("IO Exception caught");
		}
		return null;
	}

	/**
	 * Helper method to check whether the given filename exists in the system
	 * @param the given filename
	 * @return true if the file exists
	 */
	private boolean checkFileExistence(String filename){
		if(new File(filename).exists()){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * 
	 */
	

	@Override
	public String execute(File workingDir, String stdin) {
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
						String input = readFile(filename);
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
					String input = readFile(filename);
					for(int i = 1; i < args.length - 1; i++){
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
		if(characterCount != null){
			output.append("   " + "Word: "+ wordCount);
		}
		if(characterCount != null){
			output.append("   " + "New Line: "+ newLineCount);
		}
		return output.toString();

	}

	@Override
	public String getCharacterCount(String input) {
		int count = 0;
		if(input!=null){
			count = input.length();
		}
		else{
			setStatusCode(1);
		}
		return Integer.toString(count);	
	}

	@Override
	public String getWordCount(String input) {
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

	@Override
	public String getNewLineCount(String input) {
		int count = 0;
		if(input!=null){
			String[] token = input.split("(\\n)|(\\r)|(\\n\\r)");
			count = token.length;
		}
		else{
			setStatusCode(1);
		}
		return Integer.toString(count);
	}

	@Override
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
