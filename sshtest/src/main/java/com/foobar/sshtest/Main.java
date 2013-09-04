package com.foobar.sshtest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.sshd.SshClient;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		SshClient client = SshClient.setUpDefaultClient();
		client.start();
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		HostConnector hostConnector = new HostConnector(client, new NullEventLogger());
		while(true) {
			String line = reader.readLine();
			if(line == null) {
				break;
			}
			String[] split = line.split(" ");
			if(split.length == 3) {
				System.out.println(">"+line);
				hostConnector.connect(split[0], split[1], split[2]);
			} else {
				System.out.println("-"+line);
			}
		}
		System.out.println("waiting");
		Object wait = new Object();
		synchronized (wait) {
			wait.wait();
		}
	}

}
