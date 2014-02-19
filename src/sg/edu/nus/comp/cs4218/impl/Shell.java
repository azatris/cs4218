package sg.edu.nus.comp.cs4218.impl;

import java.io.*;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.WrongParsingTool;

/**
 * The Shell is used to interpret and execute user's
 * commands. Following sequence explains how a basic
 * shell can be implemented in Java
 */
public class Shell implements IShell {

	private static final int ONE=1;
	private static final int TWO=2;
	private static final int THREE=3;
	File workingDirectory = new File(System.getProperty("user.dir"));
	private static PrintStream out = System.out;

	@Override
	public ITool parse(final String commandline) {
		ITool tool;
		final String[] pipe=commandline.split(" \\| ");
		if(pipe.length>ONE){
			ITool[] allTools = parseWithPipe(pipe);
			tool = new PipingTool(allTools);
		}
		else if(pipe.length==ONE){
			String[] commandArray;
			String argument = pipe[0];
			argument = argument.trim();
			commandArray = getCommandArray(argument);
			tool = getWhatTool(commandArray);
		} else {
			tool = null;
		}
		return tool;
	}      

	/**
	 * Method of creating the PipingTool
	 * @param pipe String
	 * @return A PipingTool of the type ITool.
	 */
	public ITool[] parseWithPipe(String[] pipe){
		ITool[] work = new ITool[pipe.length];
		String[] commandArray;
		for(int i=0; i<pipe.length; i++){
			commandArray = getCommandArray(pipe[i]);
			work[i]= getWhatTool(commandArray);
		}
		return work;
	}

	/**
	 *  
	 * @param argument
	 * @return
	 */
	public String[] getCommandArray(final String argument){
		// Something could go wrong
		final String[] hasPattern = argument.split(" \"|\" ");// . ". or ." .
		String[] argumentArray;
		if(hasPattern.length == THREE && argument.startsWith("grep")){
			argumentArray = patternForGrep(hasPattern);
		}
		else if(hasPattern.length == ONE){
			argumentArray = argument.split(" ");
		}
		else if(hasPattern.length != ONE || hasPattern.length != THREE){
			argumentArray = new String[ONE];
			argumentArray[0] = "Parsing failed"; //TODO

		}
		else{
			argumentArray = new String[ONE];
			argumentArray[0] = "Parsing failed"; //TODO

		}
		return argumentArray;
	}

	/**
	 * The parser if a pattern and grep is the next running iTool
	 * @param hasPattern A string array of length 3 with command and maybe option it there is any
	 * as first parameter, The pattern as is second and the filename as the third paramenter.
	 * @return A argument array ready to process by grep.
	 */
	public String[] patternForGrep(final String[] hasPattern){
		final String commandAndOption = hasPattern[0];
		final String pattern=hasPattern[ONE];
		final String file = hasPattern[TWO];
		final String[] firstPart = commandAndOption.split(" ");
		final String[] files = file.split(" ");
		String[] argumentArray = new String[firstPart.length + ONE + files.length];
		int loopVarible;
		for(loopVarible = 0; loopVarible<firstPart.length; loopVarible++){
			argumentArray[loopVarible]=firstPart[loopVarible];
		}
		argumentArray[loopVarible] = pattern;
		loopVarible++;
		for(int i=0; loopVarible<argumentArray.length ; loopVarible++, i++){
			argumentArray[loopVarible] = files[i];
		}

		return argumentArray;
	}



	/**
	 *
	 * @param arguments
	 * @param tool
	 * @return The new Command that should be ececuted
	 */
	private ITool getWhatTool(final String[] arguments){
		ITool newCommand;
		String tool = arguments[0];
		switch(tool){
		case "grep":
			newCommand = new GrepTool(arguments);
			break;
		case  "cat":
			newCommand = new CatTool(arguments);
			break;
		case "cd":
			newCommand = new CdTool(arguments);
			break;
		case "copy":
			newCommand =  new CopyTool(arguments);
			break;
		case "delete":
			newCommand = new DeleteTool(arguments);
			break;
		case "echo":
			newCommand = new EchoTool(arguments);
			break;
		case "ls":
			newCommand = new LsTool(arguments);
			break;
		case "move":
			newCommand = new MoveTool(arguments);
			break;
		case "pwd":
			newCommand = new PWDTool(arguments);
			break;
		case "Parsing failed":
			newCommand = new WrongParsingTool(arguments);
			System.out.println("Wrong parsing");
		default:
			newCommand = null;
		}
		return newCommand;
	}

	@Override
	public Runnable execute(ITool tool) {
		// TODO Implement
		String stdin = null;
		final Thread runningThread = new ExecutingCommandThread(tool, stdin);
		runningThread.start();
		return runningThread;
	}

	@Override
	public void stop(Runnable toolExecution) {
		final Thread runningThread = (Thread)toolExecution;
		if (runningThread != null){
			if(runningThread.isAlive()){
				runningThread.interrupt();
			}
		}
		else{
			out.println("No Running Thread!");
		}
	}

	/**
	 * Do Forever
	 * 1. Wait for a user input
	 * 2. Parse the user input. Separate the command and its arguments
	 * 3. Create a new thread to execute the command
	 * 4. Execute the command and its arguments on the newly created thread. Exit with the status code of the executed command
	 * 5. In the shell, wait for the thread to complete execution
	 * 6. Report the exit status of the command to the user
	 */
	public static void main(final String[] args){
		//TODO Implement
		final Shell shell = new Shell(); //constructor
		out.print("Welcome to CS4218 Shell\n");

		String commandLine;
		ITool parseResult;
		final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		Thread executingThread = null; //Thread responsible for executing the command

		do{
			try {
				commandLine = input.readLine(); //Get the command line
				if(commandLine.equalsIgnoreCase("ctrl-z")||
						commandLine.equalsIgnoreCase("ctrl z")||
						commandLine.equalsIgnoreCase("ctrlz")){
					out.println("Stopping the running thread");
					shell.stop(executingThread); //stop the execution when it reads ctrl-z
				}
				else{
					parseResult = shell.parse(commandLine); //return the result of parsing
					executingThread = (Thread)shell.execute(parseResult); //run the thread
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(true);

	}

	/**
	 * Thread for handling and executing ITool
	 */
	public class ExecutingCommandThread extends Thread{

		private ITool executionTool = null;
		private String standardIn = null;
		
		/**
         * Constructor, instantiate tool and standard in
         * @param tool that we want to run and standard in
         */
		public ExecutingCommandThread(final ITool tool, final String stdin){
			super();
			executionTool = tool;
			standardIn = stdin;
		}
		
        /**
         * Method for getting a particular Message based on the status code (http://tldp.org/LDP/abs/html/exitcodes.html#EXITCODESREF)
         * User defined status code = {55: change working directory, 98: error in parsing}
         * @return a message to be printed on the shell
         */
		public String getMessage(final int statusCode, final String executionResult){
			String message = null;
			switch(statusCode){
			case 0:
				message = executionResult;
				break;
			case 1:
				message = "General Error Detected";
				break;
			case 2:
				message = "Missing Keyword or Command";
				break;
			case 55:
				//55 is our team special defined code to notify the shell to change workingDirectory
				workingDirectory = new File(executionResult);
				message = executionResult;
				break;
			case 98:
				message = "Parsing Failed";
				break;
			case 126:
				message = "Command invoked cannot execute";
				break;
			case 127:
				message = "Command not found";
				break;
			case 210:
				message = "A command was null";
			default:
				message = "Error Detected";
				break;
			}
			return message;              	
		}

		
        /**
         * Run the execution tool and print the specific result to the shell
         */
		@Override
		public void run() {
			//check whether there is any tool to be executed here
			if(executionTool != null){
				final String executionResult = executionTool.execute(workingDirectory, standardIn);
				if(executionResult != null){
					String message = getMessage(executionTool.getStatusCode(), executionResult);
					out.println(message);            
				}
			}
			else{
				out.println("There is no tool associated with the given command!");
			}
		}
	}
	
    /**
     * Getter to return this shell working directory
     */
	public File getWorkingDirectory(){
		return workingDirectory;
	}

}
