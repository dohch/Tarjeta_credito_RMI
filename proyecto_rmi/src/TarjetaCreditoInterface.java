import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TarjetaCreditoInterface extends Remote {
    double consultarSaldo() throws RemoteException;
    boolean realizarCompra(double monto) throws RemoteException, IllegalArgumentException;
    boolean pagarTarjeta(double monto) throws RemoteException, IllegalArgumentException;
    String getNumeroTarjeta() throws RemoteException;
    String getTitular() throws RemoteException;
    double getLimiteCredito() throws RemoteException;
    boolean autenticar(String numeroTarjeta, String password) throws RemoteException;
    String registrarNuevaTarjeta(String titular, String password) throws RemoteException;
    boolean existeTarjeta(String numeroTarjeta) throws RemoteException;
}