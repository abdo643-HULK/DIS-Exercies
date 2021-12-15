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
 * The TcpEnviClient class implements the interface IEnvService, trys to connect to the C++ EnviServer
 * and to establish a communication with the C++ EnviServer.
 */
public class TcpEnviClient implements IEnvService {
	/**
	 * mSocket contains the Enviclient socket.
	 */
	Socket mSocket = null;
	/**
	 * mOut contains the outputstream that sends data to the server
	 */
	PrintWriter mOut = null;
	/**
	 * mIn contains the inputstream that receives data from the server
	 */
	BufferedReader mIn = null;

    /**
     * setupConnection() trys to connect to the server.
     *
     * @param _port contains the port of the server
     * @param _serverIp contains the IP address of the server
     */
	public void setupConnection(int _port, String _serverIp) {

		System.out.println("Trying to connect to host: " + _serverIp + ":" + _port);

		try {
			mSocket = new Socket(_serverIp, _port);
			mOut = new PrintWriter(mSocket.getOutputStream(), true);
			mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + _serverIp);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + _serverIp);
			System.exit(1);
		}
	}

    /**
     * startRequest() sends messages to the server.
     *
     * @throws IOException
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

		int i = 1;
		for (EnvData str : s) {
			String timestamp = "Timestamp: " + str.mTimeStamp;
			String sensor = "Sensor: " + str.mValues[0];
			String value = "Value: " + str.mValues[i];
			System.out.println(timestamp + ", " + sensor + ", " + value);
			++i;
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

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (input == null) return null;
		return input.split(";");
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
			String data = s[1];

			envData = new EnvData(timestamp, data.split(";"));

		} catch (IOException e) {
			e.printStackTrace();
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

			for (int i = 0; i < data.length; i++) {
				envData[i] = new EnvData(timestamp, data[i].split(";"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return envData;

	}
}