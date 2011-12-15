package org.system;

import java.util.Scanner;

import org.logger.MyLogger;

public class OsRun {

	String stdout;
	String stderr;
	String[] command;
	int returnCode;
	Process process;

	public OsRun(String[] pcommand) {
		command = pcommand;
		RunStack.addToStack(this);
	}

	/*public OsRun(String pcommand) {
		pcommand = pcommand.replaceAll("\"", "");
		command = pcommand.split(" ");
		RunStack.addToStack(this);
	}*/

	public int getReturnCode() {
		return returnCode;
	}

	public String[] getCommand() {
		return command;
	}
	
	public Process getProcess() {
		return process;
	}

	public String getStdOut() {
		return stdout;
	}

	public RunOutputs getOutputs() {
		return new RunOutputs(stdout, stderr);
	}
	
	public String getStdErr() {
		return stderr;
	}


	
	public void run() throws Exception {
		String longcommand = "";
		try {
		for (int i=0;i<command.length;i++)
			longcommand = longcommand + command[i]+" ";
		} catch (Exception e) {}
		MyLogger.getLogger().debug("Running "+longcommand.trim());
		stdout="";stderr="";
		process = new ProcessBuilder(command).start();
        Scanner sc_stdout = new Scanner(process.getInputStream());    		
		while (sc_stdout.hasNext()) {
			String line = sc_stdout.nextLine();
			if (line.length()>0) {
				stdout=stdout+line+"\n";
				MyLogger.getLogger().debug("stdout :" + line);
			}
		}
        Scanner sc_stderr = new Scanner(process.getErrorStream());
		while (sc_stderr.hasNext()) {
			String line = sc_stderr.nextLine();
			if (line.length()>0) {
				stderr=stderr+line+"\n";
				MyLogger.getLogger().debug("stderr :" + line);
			}
		}
        RunStack.removeFromStack(this);
        process.waitFor();
        returnCode=process.exitValue();
	}

	public void destroy() {
		if (process!=null) {
			process.destroy();
		}
	}

}