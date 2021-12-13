//
// Created by milo on 09/12/2021.
//

package com.fh.clients.echo;

import java.io.*;
import java.net.*;

public class EchoClient {
	Socket mEchoSocket = null;
	PrintWriter mOut = null;
	BufferedReader mIn = null;

	public void setupConnection(int _port, String _serverIp) {

		System.out.println("Trying to connect to host: " + _serverIp + ":" + _port);

		try {
			mEchoSocket = new Socket(_serverIp, _port);
			mOut = new PrintWriter(mEchoSocket.getOutputStream(), true);
			mIn = new BufferedReader(new InputStreamReader(mEchoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + _serverIp);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + "the connection to: " + _serverIp);
			System.exit(1);
		}
	}

	public void startRequest() throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		String userInput;
		System.out.print("input: ");
		while ((userInput = stdIn.readLine()) != null) {
			mOut.println(userInput);

			if (userInput.equals("shutdown")) {
				System.out.print("Server shutting down -> Closing client connection: ");
				break;
			}

			System.out.println(mIn.readLine());
			System.out.print("input: ");
		}

		mOut.close();
		mIn.close();
		stdIn.close();
		mEchoSocket.close();
	}
}
