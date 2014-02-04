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
	public ITool parse(String commandline) {
		if(commandline.startsWith("pwd")){
			return new PWDTool();
		} else {
			//TODO Implement all other tools
			System.err.println("Cannot parse " + commandline);
			return null;
		}
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
