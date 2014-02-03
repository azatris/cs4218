package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class EchoTool extends ATool implements IEchoTool {

	public EchoTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String echo(String toEcho) {
		// TODO Auto-generated method stub
		return toEcho;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		if (args.length == 0){
			return echo(System.getProperty("line.separator"));
		}else{
			StringBuilder stringBuilder = new StringBuilder();
			for (int i=0; i<args.length; i++){
				stringBuilder.append(echo(args[i]));
			}
			return stringBuilder.toString();
		}
	}

}
