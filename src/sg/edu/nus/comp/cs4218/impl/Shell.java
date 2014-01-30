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

	File workingDirectory = new File(System.getProperty("user.dir"));
	
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
		Thread runningThread = new ExecutingCommandThread(tool, stdin);
		runningThread.start();
		return runningThread;
	}

	@Override
	public void stop(Runnable toolExecution) {
		//TODO Implement
		((Thread)toolExecution).interrupt();
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
	public static void main(String[] args){
		//TODO Implement
		Shell shell = new Shell(); //constructor
		System.out.print("Welcome to CS4218 Shell\n");
		
		String commandLine;
		ITool parseResult;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		Thread executingCommandThread = null; //Thread responsible for executing the command

		do{

			try {
				commandLine = input.readLine(); //Get the command line
				if(commandLine.toLowerCase().equals("ctrl-z") && executingCommandThread != null){
					shell.stop(executingCommandThread); //stop the execution when it reads ctrl-z
				}
				else{
					parseResult = shell.parse(commandLine); //return the result of parsing
					executingCommandThread = (Thread)shell.execute(parseResult); //run the thread
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		while(true);

	}

	public class ExecutingCommandThread extends Thread{

		ITool executionTool;
		String standardIn;
		public ExecutingCommandThread(ITool tool, String stdin){
			executionTool = tool;
			standardIn = stdin;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(executionTool != null){
				String executionResult = executionTool.execute(workingDirectory, standardIn);
				if(executionTool.getStatusCode() == 0){
					System.out.print(executionResult); //print the result to the screen
				}
				else{
					System.out.println("Error Detected");
				}
			}
		}
	}
}
