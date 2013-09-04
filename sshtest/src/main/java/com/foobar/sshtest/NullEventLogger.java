package com.foobar.sshtest;

import java.util.regex.Pattern;

public class NullEventLogger implements EventLogger {

	final static Pattern pattern = Pattern.compile("\\s+");

	public void line(String host, String line) {
		if (!line.startsWith("procs") && !line.trim().startsWith("r")) {
			String[] split = pattern.split(line);
			int load = Integer.valueOf(split[14]);
			System.out.println(host + ": " + load);
		}
	}

}
