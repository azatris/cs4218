package sg.edu.nus.comp.cs4218.impl;

import java.io.*;
import java.util.concurrent.*;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

/**
 * The Shell is used to interpret and execute user's
 * commands. Following sequence explains how a basic
 * shell can be implemented in Java
 */
public class Shell implements IShell {

	private static final int ZERO=0;
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
         * @retur
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
         * @param hasPattern A string array of lenght 3 with command and maybe option it there is any
         * as first paramenter, The pattern as is second and the filename as the third paramenter.
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
                        newCommand =new GrepTool(arguments);
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
                        newCommand = null;
	                        System.out.println("Wrong parsing");
                default:
                        // wrong input
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
                //TODO Implement
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
                                if(commandLine.equalsIgnoreCase("ctrl-z")){
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
        
 
        public class ExecutingCommandThread extends Thread{
 
                private ITool executionTool;
                private String standardIn;
                public ExecutingCommandThread(final ITool tool, final String stdin){
                		super();
                        executionTool = tool;
                        standardIn = stdin;
                }
                
                //Get Error Message based on Status Code (http://tldp.org/LDP/abs/html/exitcodes.html#EXITCODESREF)
                private String getErrorMessage(final int statusCode){
                	String errorMessage = null;
                	switch(statusCode){
                	case 1:
                		errorMessage = "General Error Detected";
                		break;
                	case 2:
                		errorMessage = "Missing Keyword or Command";
                		break;
                	case 126:
                		errorMessage = "Command invoked cannot execute";
                		break;
                	case 127:
                		errorMessage = "Command not found";
                		break;
                	default:
                		errorMessage = "Error Detected";
                		break;
                	}
                	return errorMessage;              	
                }
        
                @Override
                public void run() {
                        // TODO Auto-generated method stub
                        if(executionTool != null){
                                final String executionResult = executionTool.execute(workingDirectory, standardIn);
                                if(executionTool.getStatusCode() == ZERO){
                                        out.println(executionResult); //print the result to the screen
                                }
                                else{
                                        String errorMessage = getErrorMessage(executionTool.getStatusCode());
                                        out.println(errorMessage);
                                }
                        }
                }
        }
}
