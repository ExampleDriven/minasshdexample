package com.foobar.sshtest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.future.ConnectFuture;

public class HostConnector {

	public HostConnector(SshClient client, EventLogger eventLogger) {
		super();
		this.client = client;
		this.eventLogger = eventLogger;
	}

	private final SshClient client;
	private final EventLogger eventLogger;

	public void connect(final String host, final String user, final String password) throws Exception {
		ConnectFuture connect = client.connect(host, 22);
		connect.await();
		ClientSession session = connect.getSession();
		session.waitFor(ClientSession.WAIT_AUTH, 0);
		session.authPassword(user, password);
		ChannelExec execChannel = session.createExecChannel("vmstat 1");
		execChannel.setIn(new NullInputStream(0));
		execChannel.setErr(new NullOutputStream());
		execChannel.setOut(new OutputStream() {
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			@Override
			public void write(int b) throws IOException {
				if(b == 10) {
					eventLogger.line(host, new String(outputStream.toByteArray()));
					outputStream.reset();
				} else {
					outputStream.write(b);
				}
			}
		});
		execChannel.open();
	}

	public void stop() {
		client.stop();
	}
	
}
