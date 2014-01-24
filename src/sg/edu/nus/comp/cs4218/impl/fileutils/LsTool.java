package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class LsTool extends ATool implements ILsTool {

	public LsTool() {
		super(null);
	}

	@Override
	public List<File> getFiles(File directory) {
		File[] files = directory.listFiles();
		return Arrays.asList(files);
	}

	@Override
	public String getStringForFiles(List<File> files) {
		StringBuilder stringBuilder = new StringBuilder();
		for (File file : files) {
			stringBuilder.append(file.getName());
			if (file.isDirectory()) {
				stringBuilder.append(File.separator);
			}
			stringBuilder.append('\t');
		}
		return stringBuilder.toString();
	}

	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length == 0 && stdin == null) {
			List<File> files = getFiles(workingDir);
			return getStringForFiles(files);
		} else {
			setStatusCode(127);
		}
		return null;
	}

}

