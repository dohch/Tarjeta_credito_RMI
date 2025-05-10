import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorTarjetaCredito {
    public static void main(String[] args) {
        try {
            TarjetaCreditoImpl servicio = new TarjetaCreditoImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("TarjetaCreditoService", servicio);
            System.out.println("Servidor de Tarjeta de Crédito listo...");
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}