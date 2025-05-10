// TarjetaCreditoImpl.java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class TarjetaCreditoImpl extends UnicastRemoteObject implements TarjetaCreditoInterface {
    private String numeroTarjeta;
    private String titular;
    private double saldo;
    private double limiteCredito;
    private String password;
    
    // Base de datos simple de tarjetas (en un sistema real usarías una DB)
    private static final Map<String, TarjetaCreditoImpl> tarjetas = new HashMap<>();
    
    static {
        try {
            // Tarjetas pre-registradas para demostración
            tarjetas.put("1234", new TarjetaCreditoImpl("1234-5678-9012-3456", "Juan Pérez", 10000.0, "pass123"));
            tarjetas.put("5678", new TarjetaCreditoImpl("5678-1234-9012-3456", "María López", 15000.0, "pass456"));
        } catch (RemoteException e) {
            System.err.println("Error al inicializar tarjetas: " + e.getMessage());
        }
    }

    public TarjetaCreditoImpl(String numeroTarjeta, String titular, double limiteCredito, String password) throws RemoteException {
        super();
        this.numeroTarjeta = numeroTarjeta;
        this.titular = titular;
        this.limiteCredito = limiteCredito;
        this.saldo = 0;
        this.password = password;
    }

    @Override
    public double consultarSaldo() throws RemoteException {
        return saldo;
    }

    @Override
    public boolean realizarCompra(double monto) throws RemoteException, IllegalArgumentException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        
        if ((saldo + monto) > limiteCredito) {
            throw new IllegalArgumentException("Excede el límite de crédito disponible");
        }
        
        saldo += monto;
        return true;
    }

    @Override
    public boolean pagarTarjeta(double monto) throws RemoteException, IllegalArgumentException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        
        if (monto > saldo) {
            throw new IllegalArgumentException("No puede pagar más de lo que debe");
        }
        
        saldo -= monto;
        return true;
    }

    @Override
    public String getNumeroTarjeta() throws RemoteException {
        return numeroTarjeta;
    }

    @Override
    public String getTitular() throws RemoteException {
        return titular;
    }

    @Override
    public boolean autenticar(String numeroTarjeta, String password) throws RemoteException {
        // Extraer los primeros 4 dígitos para buscar en la "base de datos"
        String key = numeroTarjeta.substring(0, 4);
        TarjetaCreditoImpl tarjeta = tarjetas.get(key);
        
        if (tarjeta != null && 
            tarjeta.getNumeroTarjeta().equals(numeroTarjeta) && 
            tarjeta.password.equals(password)) {
            // Copiar datos a esta instancia para mantener la sesión
            this.numeroTarjeta = tarjeta.numeroTarjeta;
            this.titular = tarjeta.titular;
            this.limiteCredito = tarjeta.limiteCredito;
            this.saldo = tarjeta.saldo;
            this.password = tarjeta.password;
            return true;
        }
        return false;
    }
}