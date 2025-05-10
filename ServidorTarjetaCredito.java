// ServidorTarjetaCredito.java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorTarjetaCredito {
    public static void main(String[] args) {
        try {
            // Crear instancia del servicio de tarjetas
            // Pasamos valores vacíos ya que la autenticación real se manejará con las tarjetas pre-registradas
            TarjetaCreditoImpl servicio = new TarjetaCreditoImpl("", "", 0, "");
            
            // Crear y obtener el registro RMI en el puerto 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Registrar el objeto remoto
            registry.rebind("TarjetaCreditoService", servicio);
            
            System.out.println("Servidor de Tarjeta de Crédito listo...");
            System.out.println("Tarjetas disponibles para prueba:");
            System.out.println("1. Número: 1234-5678-9012-3456, Contraseña: pass123");
            System.out.println("2. Número: 5678-1234-9012-3456, Contraseña: pass456");
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}