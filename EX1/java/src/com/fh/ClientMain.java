//
// Created by milo on 09/12/2021.
//

package com.fh;

import static java.lang.Integer.parseInt;

import java.io.*;
import java.util.Scanner;
import com.fh.clients.echo.EchoClient;
import com.fh.clients.envi.TcpEnviClient;


public class ClientMain {
	public static void main(String[] _args) throws IOException {

		if (_args.length < 2) {
			System.out.println("Please provide a port and an IP-Address");
			return;
		}


		int port = parseInt(_args[0]);
		String serverIp = _args[1];

		if (port < 1024 || port > 65353) {
			System.out.println("Please provide a port in the range of 1024-65353");
			return;
		}


		Scanner in = new Scanner(System.in);

		int inputMenu;
		do {

			System.out.println("Choose server type:");
			System.out.println("------------------------:");
			System.out.println("1. Echo Server:");
			System.out.println("2. Envi Client:");
			System.out.println("------------------------:");

			inputMenu = in.nextInt();
		} while (inputMenu < 1 || inputMenu > 2);

		switch (inputMenu) {
			case 1 -> {
				EchoClient echoClient = new EchoClient();
				echoClient.setupConnection(port, serverIp);
				echoClient.startRequest();
			}
			case 2 -> {
				TcpEnviClient client = new TcpEnviClient();
				client.setupConnection(port, serverIp);
				client.startRequest();
			}
			default -> System.out.println("Closing");
		}

		in.close();
	}
}
