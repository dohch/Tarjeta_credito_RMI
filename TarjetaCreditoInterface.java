// TarjetaCreditoInterface.java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TarjetaCreditoInterface extends Remote {
    double consultarSaldo() throws RemoteException;
    boolean realizarCompra(double monto) throws RemoteException, IllegalArgumentException;
    boolean pagarTarjeta(double monto) throws RemoteException, IllegalArgumentException;
    String getNumeroTarjeta() throws RemoteException;
    String getTitular() throws RemoteException;
    boolean autenticar(String numeroTarjeta, String password) throws RemoteException;
}