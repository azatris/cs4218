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
	 * @param	arguments	(args[0] is the command name)
	 */
	public UniqTool(final String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("uniq")) {
			setStatusCode(127);
		}
	}

	/**
	 * Helper method to check whether the given filename exists in the system
	 * @param	filename	the given filename
	 * @return	true if the file exists
	 */
	public static boolean checkFileExistence(final String filename){
		File f = new File(filename);
		return f.exists();
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
							String input = null;
							try{
								input = readFile(filename);
							} catch (IOException e) {
								setStatusCode(4);
								return null;
							}
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
					if(args[i].equals("-i")||args[i].equals("-f")){
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
									err.println("-f needs to be followed with an int value");
									return null;
								}
							}
							else{
								setStatusCode(2);
								err.println("-f needs to be followed with skip number");
								return null;
							}
						}
					}
					else{
						//if it is not option -i, -f, we can consider this argument is illegal if it is integer and followed by -f
						try{
							Integer.parseInt(args[i]);
							if(!(args[i-1].equals("-f"))){
								setStatusCode(5);
								return null;
							}
						}
						catch(NumberFormatException e){
							setStatusCode(5);
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
						try {
							input = readFile(filename);
						} catch (IOException e) {
							setStatusCode(4);
							return null;
						}
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

		/**
		 * Check the given input, and eliminate any duplicate line
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
						else{
							//add line separator if the original input ends with new line
							if(input.endsWith(lineSeparator)){
								output.append(lineSeparator);
							}
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

		/**
		 * Check the given input, and eliminate any duplicate line (considering skipping some tokens)
		 * @param	checkCase	false if ignore checking the case
		 * @param	input	the input to be checked
		 * @return	the unique line
		 */
		public String getUniqueSkipNum(final int NUM, final boolean checkCase, final String input) {
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
						err.println("Invalid Skip Number, Skip Number must be greater or equal to 0");
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
						//Add line separator if this is not the last line
						if(i != line.length-1){
							output.append(lineSeparator);
						}
						else{
							//add line separator if the original input ends with new line
							if(input.endsWith(lineSeparator)){
								output.append(lineSeparator);
							}
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
