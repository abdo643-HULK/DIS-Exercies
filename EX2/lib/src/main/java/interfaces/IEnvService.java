package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import classes.EnvData;

public interface IEnvService extends Remote {
	/**
	 * Provides the types of the available environmental sensors
	 *
	 * @return A String array with the sensor types
	 * @throws RemoteException An error occurred during the communication
	 * @see String
	 * @see RemoteException
	 */
	public String[] requestEnvironmentDataTypes() throws RemoteException;

	/**
	 * Provides the measurement values of a specific sensor in the form
	 * of an environmental data object (classes.EnvData)
	 *
	 * @param _type the type of the targeted sensor
	 * @return classes.EnvData the current measurement value of the sensor
	 * null, if the sensor doesn’t exist
	 * @throws RemoteException An error occurred during the communication
	 * @see EnvData
	 * @see java.lang.String
	 * @see RemoteException
	 */
	public EnvData requestEnvironmentData(String _type) throws RemoteException;

	/**
	 * Provides the measurement values of all available sensors
	 *
	 * @return classes.EnvData[] all available measurement values
	 * @throws RemoteException An error occurred during the communication
	 * @see EnvData
	 * @see String
	 * @see RemoteException
	 */
	public EnvData[] requestAll() throws RemoteException;
}
