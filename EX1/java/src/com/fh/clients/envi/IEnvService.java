package com.fh.clients.envi;

/**
 * IEnvService provides an Environment Data API on the client side.
 */
public interface IEnvService {
	/**
	 * @return all sensor types available on the server
	 */
	String[] requestEnvironmentDataTypes();

	EnvData requestEnvironmentData(String _type);

	EnvData[] requestAll();
}
