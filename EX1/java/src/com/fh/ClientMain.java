//
// Created by milo on 09/12/2021.
//

package com.fh;

import static java.lang.Integer.parseInt;

import java.io.*;
import java.util.Scanner;

import com.fh.clients.echo.EchoClient;
import com.fh.clients.envi.EnvData;
import com.fh.clients.envi.IEnvService;
import com.fh.clients.envi.TcpEnviClient;


/**
 * The ClientMain simply contains a main method
 * where the Client side will be executed.
 */
@SuppressWarnings("all")
public class ClientMain {
	/**
	 * @param _args contains the server IP address and server port
	 */
	public static void main(String[] _args) {
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

		IEnvService service = new TcpEnviClient(port, serverIp);

		while (true) {
			String[] sensors = service.requestEnvironmentDataTypes();
			for (String sensor : sensors) {
				EnvData dataO = service.requestEnvironmentData(sensor);
				System.out.print(dataO);
				System.out.println();
				System.out.println("*****************************");
			} // for sensor
			System.out.println();
			System.out.println();
			EnvData[] dataOs = service.requestAll();
			for (EnvData dataO : dataOs) {
				System.out.println(dataO);
			} // for data
			try {
				Thread.sleep(1000);
			} catch (Exception _e) {
				_e.printStackTrace();
			}
		}


//		Scanner in = new Scanner(System.in);
//
//		int inputMenu;
//		do {
//			System.out.println();
//			System.out.println("Choose server type:");
//			System.out.println("------------------------:");
//			System.out.println("1. Echo Server:");
//			System.out.println("2. Envi Client:");
//			System.out.println("------------------------:");
//
//			inputMenu = in.nextInt();
//		} while (inputMenu < 1 || inputMenu > 2);
//
//		switch (inputMenu) {
//			case 1 -> {
//				EchoClient echoClient = new EchoClient();
//				echoClient.setupConnection(port, serverIp);
//				echoClient.startRequest();
//			}
//			case 2 -> {
//				TcpEnviClient client = new TcpEnviClient(port, serverIp);
//				client.startRequest();
//			}
//			default -> System.out.println("Closing");
//		}
//
//		in.close();
	}
}
