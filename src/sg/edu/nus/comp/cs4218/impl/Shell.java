package sg.edu.nus.comp.cs4218.impl;

import java.io.*;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
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

	/**
	 * Get the corresponding tool depending on the given command line
	 * @param	commandline input typed by the user
	 * @return	Tool that should be executed
	 */
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
	 * @param	pipe String
	 * @return	A PipingTool of the type ITool.
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
	 * Handle arguments differently for grep
	 * @param	arguments Arguments given by the parser
	 * @return	a string of arguments for the execution of the Tool
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
			argumentArray[0] = "Parsing failed"; 

		}
		else{
			argumentArray = new String[ONE];
			argumentArray[0] = "Parsing failed"; 
		}
		return argumentArray;
	}

	/**
	 * The parser if a pattern and grep is the next running iTool
	 * @param	hasPattern	A string array of length 3 with command and maybe option it there is any
	 * as first parameter, The pattern as is second and the filename as the third paramenter.
	 * @return	An argument array ready to process by grep.(args[0] == grep)
	 */
	public String[] patternForGrep(final String[] hasPattern){
		final String commandAndOption = hasPattern[0];
		final String pattern = hasPattern[ONE];
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
	 * Get corresponding tool depending on the arguments given by the parser
	 * @param	arguments Arguments that are given to the Tool, args[0] is "tool name" in this case
	 * @return	Tool that should be executed
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
		case "wc":
			newCommand = new WcTool(arguments);
			break;
		case "uniq":
			newCommand = new UniqTool(arguments);
			break;
		case "cut":
			newCommand = new CutTool(arguments);
			break;
		case "paste":
			newCommand = new PASTETool(arguments);
			break;
		case "sort":
			newCommand = new SORTTool(arguments);
			break;
		case "comm":
			newCommand = new CommTool(arguments);
			break;
		case "Parsing failed":
			newCommand = new WrongParsingTool(arguments);
		default:
			newCommand = null;
		}
		return newCommand;
	}

	/**
	 * Create a thread to handle execution of a tool
	 * @param	tool	Tool that you want to run
	 * @return	thread which handles the execution of the given tool
	 */
	@Override
	public Runnable execute(final ITool tool) {
		String stdin = "";
		final Thread runningThread = new ExecutingCommandThread(tool, stdin);
		runningThread.start();
		return runningThread;
	}

	/**
	 * Stop a running thread (which handle the execution of a tool) from executing tool
	 * @param	toolExecution	a thread currently executing the tool
	 */
	@Override
	public void stop(final Runnable toolExecution) {
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
	 * We limit the number of tool that can be executed to 1 for each time, since ctrl-z can only stop the execution of the latest tool
	 */
	public static void main(final String[] args){
		final Shell shell = new Shell(); //constructor
		out.print("Welcome to CS4218 Shell\n");

		String commandLine;
		ITool parseResult;
		final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		Thread executingThread = null; //Thread responsible for executing the tool
		do{
			try {
				commandLine = input.readLine(); //Get the command line
				if(commandLine.equalsIgnoreCase("ctrl-z")||
						commandLine.equalsIgnoreCase("ctrl z")||
						commandLine.equalsIgnoreCase("ctrlz")){
					out.println("Stopping the running thread");
					shell.stop(executingThread); //stop the execution when it reads ctrl-z
				}
				else if(commandLine.equalsIgnoreCase("exit")){
					System.exit(0); //exit the shell
				}
				else if(commandLine.equals("")){
					continue; //continue to parse the next command line
				}
				else{
					parseResult = shell.parse(commandLine.trim()); //return the result of parsing
					executingThread = (Thread)shell.execute(parseResult); //run the thread
				}
			} catch (IOException e) {
				out.print("IO Exception Caught\n");
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
         * @param	tool	Tool that we want to run and standard in
         * @param	stdin	Stdin given to the thread
         */
		public ExecutingCommandThread(final ITool tool, final String stdin){
			super();
			executionTool = tool;
			standardIn = stdin;
		}
		
        /**
         * Method for getting a particular Message based on the status code (http://tldp.org/LDP/abs/html/exitcodes.html#EXITCODESREF)
         * User defined status code = {55: change working directory, 98: error in parsing}
         * @param	statusCode	status code of a tool
         * @param	executionResult	the result of running a tool
         * @return	a message to be printed on the shell
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
				message = "Incorrect Number of Keyword or Command";
				break;
			case 3:
				message = "No Such File or Directory";
				break;
			case 4:
				message = "IO Exception Caught";
				break;
			case 5:
				message = "Invalid option is detected for this tool";
				break;
			case 55:
				//55 is our team special defined code to notify the shell to change workingDirectory
				workingDirectory = new File(executionResult);
				message = executionResult;
				break;
			case 67:
				message = "List not right formated";
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
			case 211:
				message = "Cannot copy to the same place";
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
			//and assure that there is no error when construction the tool
			if(executionTool != null && executionTool.getStatusCode() != 127){
				final String executionResult = executionTool.execute(workingDirectory, standardIn);
				final int statusCode = executionTool.getStatusCode();
				final String message = getMessage(statusCode, executionResult);
				if(message != null){
					out.println(message);     
					if(statusCode == 0 || statusCode == 55){
						out.println("Program Execution Finishes, Exit Succesfully");
					}
				}
			}
			else{
				out.println("There is no tool associated with the given command!");
			}
		}
	}
	
    /**
     * Getter to return this shell working directory
     * @return	path of this shell working Directory (absolute path)
     */
	public File getWorkingDirectory(){
		return workingDirectory;
	}

}
