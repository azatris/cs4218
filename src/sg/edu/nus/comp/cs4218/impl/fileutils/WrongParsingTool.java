package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class WrongParsingTool extends ATool implements ITool{
		private int statusCode = 98;
		
		public WrongParsingTool(String[] arguments) {
			super(arguments);
		}

		/**
		 * The general go-to method for using the tool that calls
		 * the suitable submethods.
		 * @param workingDir current working directory
		 * @param stdin optional standard input from e.g. pipe tool
		 * @return output
		 */
		@Override
		public String execute(File workingDir, String stdin) {
			return "";
		}
		
		/**
		 * Returns status code of the tool
		 * @return <code>statusCode</code>
		 */
		public int getStatusCode(){
			return statusCode;
		}
}
