package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class LsTool extends ATool implements ILsTool {
	public LsTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("ls")) {
			setStatusCode(127);

		}
	}

	/**
	 * @param directory place to scan for files
	 * @return list of files in the <code>directory</code>
	 */
	@Override
	public List<File> getFiles(File directory) {
		if (Files.exists(directory.toPath())) {
			File[] files = directory.listFiles();
			return Arrays.asList(files);
		} else {
			return null;
		}

	}

	/**
	 * @param files list of files
	 * @return <code>files</code> in human-readable form
	 */
	@Override
	public String getStringForFiles(List<File> files) {
		String returnable = null;
		if (files != null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (File file : files) {
				stringBuilder.append(file.getName());
				if (file.isDirectory()) {
					stringBuilder.append(File.separator);
				}
				stringBuilder.append('\t');
			}
			returnable = stringBuilder.toString();
		} else {
			setStatusCode(127);
		}
		return returnable;
	}

	/**
	 * Executes the command according to 
	 * <code>args</code> and <code>stdin</code>. 
	 * @param workingDir current directory
	 * @param stdin passed input from another command
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		List<File> files = null;
		if (args.length == 2) {
			File targetDirectory = new File(args[1]);
			files = getFiles(targetDirectory);
			if (files == null) {
				setStatusCode(127);
				return null;
			}
		}
		if (args.length == 1) {
			files = getFiles(workingDir);
		}
		return getStringForFiles(files);
	}

}

