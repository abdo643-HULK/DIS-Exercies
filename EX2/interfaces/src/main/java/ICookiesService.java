import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICookiesService extends Remote {
	public void saySomething() throws RemoteException;
}
