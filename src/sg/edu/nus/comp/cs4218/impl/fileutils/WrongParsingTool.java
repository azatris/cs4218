package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class WrongParsingTool extends ATool implements ITool{
		private int statusCode = 98;
		
		public WrongParsingTool(String[] arguments) {
			super(arguments);
		}

		// TODO
		@Override
		public String execute(File workingDir, String stdin) {
			return "";
		}
		
		// TODO
		public int getStatusCode(){
			return statusCode;
		}
}
