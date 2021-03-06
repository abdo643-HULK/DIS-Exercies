package com.fh.clients.envi;

/**
 * IEnvService provides an Environment Data API on the client side.
 */
public interface IEnvService {
	/**
	 * @return all sensor types available on the server
	 */
	String[] requestEnvironmentDataTypes();

	/**
	 * requests the sensor data from a specific sensor type
	 * @param _type type of sensor
	 * @return data from requested sensor type
	 */
    EnvData requestEnvironmentData(String _type);

	/**
	 * requests all sensor and their data
	 * @return all sensor and their data
	 */
    EnvData[] requestAll();
}
