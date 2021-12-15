//
// Created by milo on 09/12/2021.
//

package com.fh.clients.envi;


import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * The TcpEnviClient class implements the interface IEnvService,
 * trys to connect to the C++ EnviServer and to establish
 * a communication with the C++ EnviServer.
 */
public class TcpEnviClient implements IEnvService {
	Socket mSocket = null;
	PrintWriter mOut = null;
	BufferedReader mIn = null;

	/**
	 * trys to connect to the server.
	 *
	 * @param _port     contains the port of the server
	 * @param _serverIp contains the IP address of the server
	 */
	public TcpEnviClient(int _port, String _serverIp) {
		System.out.println("Trying to connect to host: " + _serverIp + ":" + _port);

		try {
			mSocket = new Socket(_serverIp, _port);
			mOut = new PrintWriter(mSocket.getOutputStream(), true);
			mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		} catch (UnknownHostException _e) {
			System.err.println("Don't know about host: " + _serverIp);
			System.exit(1);
		} catch (IOException _e) {
			System.err.println("Couldn't get I/O for the connection to: " + _serverIp);
			System.exit(1);
		}

	}

	/**
	 * sends messages to the server.
	 *
	 * @throws IOException when reading input fails
	 */
	public void startRequest() throws IOException {
		Scanner in = new Scanner(System.in);
		int inputMenu;
		boolean exit = false;

		while (!exit) {
			do {
				System.out.println();
				System.out.println("Envi Client Menu:");
				System.out.println("------------------------:");
				System.out.println("1. Get sensor types:");
				System.out.println("2. Get sensor data:");
				System.out.println("3. Get all Sensor:");
				System.out.println("4. EXIT");
				System.out.println("------------------------:");

				inputMenu = in.nextInt();

			} while (inputMenu < 1 || inputMenu > 4);

			switch (inputMenu) {
				case 1 -> getSensorType();
				case 2 -> {
					System.out.print("Please provide the sensor type: ");
					String input = in.next();
					getSensor(input);
				}
				case 3 -> getAllSensors();
				default -> exit = true;
			}

		}

		mOut.close();
		in.close();
		mSocket.close();
	}

	/**
	 * getSensorType() sends a request for all available sensor types and prints the received data.
	 */
	public void getSensorType() {
		String[] s = requestEnvironmentDataTypes();

		for (String str : s) {
			System.out.println(str);
		}
	}

	/**
	 * getAllSensor() sends a request for all available sensor values and prints the received data.
	 */
	public void getAllSensors() {
		EnvData[] s = requestAll();

		for (EnvData str : s) {
			String timestamp = "Timestamp: " + str.mTimeStamp;
			if (Objects.equals(str.mValues[0], "#")) return;
			String sensor = "Sensor: " + str.mValues[0];

			StringBuilder value = new StringBuilder("Values: ");
			for (int i = 1; i < str.mValues.length; ++i) {
				value.append(str.mValues[i]).append(", ");
			}

			System.out.println(timestamp + ", " + sensor + " - " + value);
		}
	}

	/**
	 * getSensor() sends a request for a concret sensor values and prints the received data.
	 *
	 * @param _type concrete type of sensor.
	 */
	public void getSensor(String _type) {
		EnvData s = requestEnvironmentData(_type);

		for (String str : s.mValues) {
			System.out.println("Timestamp: " + s.mTimeStamp + ", Value: " + str);
		}
	}


	@Override
	public String[] requestEnvironmentDataTypes() {
		String endpoint = "getSensortypes()#";
		String input = null;

		try {
			mOut.printf(endpoint);
			input = mIn.readLine();

			if (Objects.equals(input, "NOT FOUND")) {
				System.err.println("ERROR FETCHING ENV DATA");
				return null;
			}

		} catch (IOException _e) {
			_e.printStackTrace();
		}

		if (input == null) return null;
		final String[] list = input.split(";");
		return Arrays.copyOf(list, list.length - 1);
	}

	@Override
	public EnvData requestEnvironmentData(String _type) {
		mOut.println("getSensor(" + _type + ")#");
		EnvData envData = null;

		try {
			String input = mIn.readLine();

			if (Objects.equals(input, "NOT FOUND")) {
				System.err.println("ERROR FETCHING ENV DATA");
				return null;
			}

			String[] s = input.split("\\|");
			String timestamp = s[0];
			String[] data = s[1].split(";");

			envData = new EnvData(timestamp, Arrays.copyOf(data, data.length - 1));

		} catch (IOException _e) {
			_e.printStackTrace();
		}

		return envData;
	}

	@Override
	public EnvData[] requestAll() {
		String endpoint = "getAllSensors()#";
		mOut.println(endpoint);

		String input;
		EnvData[] envData = new EnvData[0];

		try {
			input = mIn.readLine();
			String[] s = input.split("\\|");

			if (Objects.equals(input, "NOT FOUND")) {
				System.err.println("ERROR FETCHING ENV DATA");
				return null;
			}

			String timestamp = s[0];
			String[] data = Arrays.copyOfRange(s, 1, s.length);
			envData = new EnvData[data.length];

			for (int i = 0; i < data.length - 1; i++) {
				String[] res = data[i].split(";");
				envData[i] = new EnvData(timestamp, res);
			}

			String[] res = data[data.length - 1].split(";");
			envData[data.length - 1] = new EnvData(timestamp, Arrays.copyOf(res, res.length - 1));
		} catch (IOException _e) {
			_e.printStackTrace();
		}

		return envData;

	}
}