package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class EchoTool extends ATool implements IEchoTool {

	public EchoTool() {
		super(null);
	}

	@Override
	public String echo(String toEcho) {
		if (toEcho==null){
			setStatusCode(1);
		}else{
			setStatusCode(0);
		}
		return toEcho;
	}

	@Override
	public String execute(File workingDir, String stdin) {
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
