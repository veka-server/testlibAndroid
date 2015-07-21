package vekaUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public class Shell {

	private BufferedReader buf;
	private String command;
	
	public Shell(String command) {
		this.command = command;		
	}
	
	public void execute() throws IOException, InterruptedException {
		execute(command);
	}

	public void execute(String command) throws IOException, InterruptedException {
	    Runtime run = Runtime.getRuntime();
	    Process pr;
		pr = run.exec(command);
		pr.waitFor();
		buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));		 
	}

	public void showResultInLog() throws IOException {
		String line = "";
	    do {
	    	line=buf.readLine();
	    	if(line != null)
	    		Log.e("debug",line);
		} while (line != null);
	}
		
}
