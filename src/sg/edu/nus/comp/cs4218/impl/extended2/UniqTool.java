package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class UniqTool extends ATool implements IUniqTool{

	/**
	 * Constructor taking the arguments
	 * @param arguments (args[0] is the command name)
	 */
	public UniqTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("uniq")) {
			setStatusCode(127);
		}
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

	private String skipFields(String line, int NUM){
		if(line == null || line.equals("")){
			return "";
		}
		else{
			int skipCount = 0;
			//normalize the counting, in case the first character is the null
			if((line.charAt(0) == ' ')||(line.charAt(0) == '\t')){
				skipCount--;
			}
			for(int i = 0; i < line.length()-1; i++){
				if(((line.charAt(i) == ' ')||(line.charAt(i) == '\t'))&&((line.charAt(i+1) != ' ')||(line.charAt(i+1) != '\t'))){
					skipCount++;
				}
				if(skipCount == NUM){
					return line.substring(i+1);
				}
			}
			//return empty string when the NUM > skipCount (number of tokens available in this line)
			return "";
		}
	}

	@Override
	public String execute(File workingDir, String stdin) {
		String output = null;
		if(args.length == 2){
			if(args[1].equals("-help")){
				return getHelp();
			}
			else{
				String filename = args[args.length-1];
				if(filename.equals("-")){
					String input = stdin;
					output = getUnique(true, input);
				}
				else{
					if(checkFileExistence(filename)){
						String input = readFile(filename);
						output = getUnique(true, input);
					}
					else{
						setStatusCode(3);
						return null;
					}
				}
			}
		}
		else if(args.length > 2 && args.length <= 5){
			boolean checkCase = true;
			boolean skipChar = false;
			int offSet = 0;

			for(int i = 1; i < args.length - 1; i++){
				if(args[i].equals("-i")){
					checkCase = false;
				}
				if(args[i].equals("-f")){
					if(i != args.length-1){
						try{
							offSet = Integer.parseInt(args[i+1]);
							skipChar = true;
						}
						catch(NumberFormatException e){
							setStatusCode(2);
							System.err.print("-f needs to be followed with an int value");
							return null;
						}
					}
					else{
						setStatusCode(2);
						System.err.print("-f needs to be followed with skip number");
						return null;
					}
				}
			}

			String input = null;
			String filename = args[args.length-1];

			//Determine the input
			if(filename.equals("-")){
				input = stdin;
			}
			else{
				if(checkFileExistence(filename)){
					input = readFile(filename);
				}
				else{
					setStatusCode(3);
					return null;
				}
			}

			//Determine whether we need to skip some char
			if(skipChar == true){
				output = getUniqueSkipNum(offSet, checkCase, input);
			}
			else{
				output = getUnique(checkCase, input);
			}
		}
		else{
			setStatusCode(2);
			return null;
		}
		return output;
	}

	/**Check the given input, and eliminate any duplicate line
	 * @param	checkCase	false if ignore checking the case
	 * @param	input	the input to be checked
	 * @return	the unique line
	 */
	public String getUnique(boolean checkCase, String input) {
		if(input != null){
			String[] line = null;
			String lineSeparator = ""; //determine the line separator for the input (different OS different line separator)
			if(input.contains("\r\n")){
				lineSeparator = "\r\n";
				line = input.split("\\r\\n");
			}
			else if(input.contains("\n")){
				lineSeparator = "\n";
				line = input.split("\\n");
			}
			else{
				line = input.split("\\n");
			}

			String currentLine = null;
			String currentLineConsideringCase = null;
			String prev = null;
			StringBuilder output = new StringBuilder();
			for (int i = 0; i < line.length; i++){
				currentLine = line[i];
				if(!checkCase){
					currentLineConsideringCase = currentLine.toLowerCase(); //ignore case
				}
				else{
					currentLineConsideringCase = currentLine;
				}

				//Check similarity with previous line
				if(!currentLineConsideringCase.equals(prev)){
					output.append(currentLine);
					//Add line separator if this is not the last line
					if(i != line.length-1){
						output.append(lineSeparator);
					}
				}
				prev = currentLineConsideringCase;
			}
			return output.toString();
		}
		else{
			return "";
		}
	}

	/**Check the given input, and eliminate any duplicate line (considering skipping some tokens)
	 * @param	checkCase	false if ignore checking the case
	 * @param	input	the input to be checked
	 * @return	the unique line
	 */
	public String getUniqueSkipNum(int NUM, boolean checkCase, String input) {
		if(input != null){
			String[] line = null;
			String lineSeparator = ""; //determine the line separator for the input (different OS different line separator)
			if(input.contains("\r\n")){
				lineSeparator = "\r\n";
				line = input.split("\\r\\n");
			}
			else if(input.contains("\n")){
				lineSeparator = "\n";
				line = input.split("\\n");
			}
			else{
				line = input.split("\\n");
			}
			String currentLine = null;
			String currentLineConsideringCase = null;
			String prev = null;
			StringBuilder output = new StringBuilder();
			for (int i = 0; i < line.length; i++){
				currentLine = line[i];
				if(!checkCase){
					currentLineConsideringCase = currentLine.toLowerCase(); //ignore case
				}
				else{
					currentLineConsideringCase = currentLine;
				}
				String offSetCurrentLine = currentLineConsideringCase;
				String offSetPrev = prev;
				//Set the offset
				if(NUM <= 0){
					setStatusCode(1);
					System.out.println("Invalid Skip Number, Skip Number must be greater or equal to 0");
					return null;
				}
				else{
					offSetCurrentLine = skipFields(currentLineConsideringCase, NUM);
					if(prev != null){
						offSetPrev = skipFields(prev,NUM);
					}
				}

				//Check similarity with previous line
				if(!offSetCurrentLine.equals(offSetPrev)){
					output.append(currentLine);
				}

				//Add line separator if this is not the last line
				if(i != line.length-1){
					output.append(lineSeparator);
				}
				prev = currentLineConsideringCase;
			}
			return output.toString();
		}
		else{
			return "";
		}

	}

	@Override
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
