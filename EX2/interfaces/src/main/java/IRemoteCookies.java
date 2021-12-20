import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteCookies extends Remote {
	public String saySomething() throws RemoteException;
}
