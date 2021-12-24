//
// Created by milo on 09/12/2021.
//

import classes.EnvData;
import interfaces.IEnvService;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * The TcpEnviClient class implements the interface interfaces.IEnvService,
 * trys to connect to the C++ EnviServer and to establish
 * a communication with the C++ EnviServer.
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
	 * trys to connect to the server.
	 *
	 * @param _port     contains the port of the server
	 * @param _serverIp contains the IP address of the server
	 */
	public TcpEnviClient(int _port, String _serverIp) throws IOException {
		System.out.println("Trying to connect to host: " + _serverIp + ":" + _port);

		mSocket = new Socket(_serverIp, _port);
		mOut = new PrintWriter(mSocket.getOutputStream(), true);
		mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
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
			String sensor = data[0];
			int[] values = Arrays
					.stream(Arrays.copyOfRange(data, 1, data.length - 1))
					.mapToInt(Integer::parseInt).toArray();

			envData = new EnvData(timestamp, sensor, values);

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
		EnvData[] envData = null;

		try {
			input = mIn.readLine();
			String[] s = input.split("\\|");

			if (Objects.equals(input, "NOT FOUND")) {
				System.err.println("ERROR FETCHING ENV DATA");
				return null;
			}

			String timestamp = s[0];
			String[] data = Arrays.copyOfRange(s, 1, s.length);
			envData = new EnvData[data.length - 1];

			for (int i = 0; i < data.length - 1; ++i) {
				String[] res = data[i].split(";");
				String sensor = res[0];
				int[] values = Arrays
						.stream(Arrays.copyOfRange(res, 1, res.length))
						.mapToInt(Integer::parseInt).toArray();
				envData[i] = new EnvData(timestamp, sensor, values);
			}
		} catch (IOException _e) {
			_e.printStackTrace();
		}

		return envData;
	}

	public void close() {
		try {
			mOut.close();
			mSocket.close();
		} catch (IOException _e) {
			_e.printStackTrace();
		}
	}
}