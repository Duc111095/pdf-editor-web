package com.ducnh.excellentPdf.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CheckProgramInstall {
	
	private static final List<String> PYTHON_COMMANDS = Arrays.asList("python3", "python");
	private static boolean pythonAvailableChecked = false;
	private static String availablePythonCommand = null;
	
	public static String getAvailablePythonCommand() {
		if (!pythonAvailableChecked) {
			availablePythonCommand = PYTHON_COMMANDS.stream()
					.filter(CheckProgramInstall::checkPythonVersion)
					.findFirst()
					.orElse(null);
			pythonAvailableChecked = true;
		}
		return availablePythonCommand;
	}
	
	private static boolean checkPythonVersion(String pythonCommand) {
		try {
			ProcessExecutor.getInstance(ProcessExecutor.Processes.PYTHON_OPENCV)
			.runCommandWithOutputHandling(
					Arrays.asList(pythonCommand, "--version"));
			return true; // Command succeeded, Python is available;
		} catch (IOException | InterruptedException e) {
			return false; // Command failed, Python is not available;
		}
	}
	
	public static boolean isPythonAvailable( ) {
		return getAvailablePythonCommand() != null;
	}
}
