package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;


public class PipingTool implements  IPipingTool {
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int FIFTYFIVE =55;
	ITool[] arguments;
	File workingDir;
	int statuscode=ZERO;
	public PipingTool(ITool[] arguments){
		this.arguments = arguments;
	}
	
/**
 * Execute a pipeingtool
 * @param Currentworiking directory 
 * @param Stdin    
 * @return A String 
 * 
 */
	@Override
	public String execute(File workingDirectory, String stdin) {
		if(arguments.length<TWO){
			setStatusCode(143);
			return "";
		}
		setWorkingdir(workingDirectory);
		String result =stdin;
		for(int loopValue=ZERO; loopValue<arguments.length && statuscode==ZERO; loopValue++){
			result = pipe(result, arguments[loopValue]);
		}
		return result;
	}

	/**
	 * @return The statuscode
	 */
	@Override
	public int getStatusCode() {
		return statuscode;

	}
	/**
	 * Running the first Tool with null as stdin
	 * Then run the second Tool with the first tools output 
	 * @param ATool
	 * @param ATool
	 * @return Output from the second Tool 
	 */
	// TODO
	@Override
	public String pipe(ITool from, ITool to) {
		if(from == null || to == null){
			setStatusCode(210);
			return "";
		}
		String result = from.execute(workingDir, null);
		int statusCodeFrom = from.getStatusCode();
		int statusCodeTo=ZERO;
		if (statusCodeFrom==FIFTYFIVE){
			statusCodeFrom=ZERO;
			result = "";
		}
		if(statusCodeFrom!=ZERO){
			result ="";
			setStatusCode(statusCodeFrom);
			statusCodeTo=statusCodeFrom;
		} else if(statusCodeFrom==ZERO) {
			setStatusCode(ZERO);
			result = to.execute(workingDir, result);
			statusCodeTo = to.getStatusCode();
		} 
		if (statusCodeTo==FIFTYFIVE){
			statusCodeTo=ZERO;
			result = "";
		}
		if(statusCodeTo!=ZERO){
			result="";
			setStatusCode(statusCodeTo);
		}
		return result;
	}

	/**
	 * @param String Stdout, A string from the last tool that was run
	 * @param The next Tool to run
	 * @retrun The result of the Tool
	 *
	 */
	@Override
	public String pipe(String stdout, ITool to) {
		if(to == null){
			setStatusCode(210);
			return "";
		}
		String result =to.execute(workingDir, stdout);
		int statusCodeTo = to.getStatusCode();
		if (statusCodeTo==FIFTYFIVE){
			statusCodeTo=ZERO;
			result="";
		}
		if(statusCodeTo!=ZERO){
			result="";
			setStatusCode(statusCodeTo);
		}
		return result;
	}
	
	/**
	 * Set statuscode
	 * 55 should still be the same statuscode as before
	 * @param value
	 */
	private void setStatusCode(int value){
		if(value != 55){
			statuscode =value;
		}
	}

	/**
	 * Sets the working directory
	 * @return
	 */
	private void setWorkingdir(File input){
		workingDir = input;
	}

	/**
	 * Implements equals
	 */
	@Override
	public boolean equals(Object object){
		PipingTool pipe =(PipingTool) object;
		if(arguments.length!=pipe.arguments.length){
			return false;
		}
		for (int i = 0; i < arguments.length; i++) {
			if(!arguments[i].equals(pipe.arguments[i])){
				return false;
			}
		}
		return true;
		
	}

}
