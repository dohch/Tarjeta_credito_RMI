import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CalculatorServer {
    public CalculatorServer() {
        try {
            // Crear el registro RMI en el puerto 1099
            LocateRegistry.createRegistry(1099);
            
            Calculator c = new CalculatorImpl();
            Naming.rebind("rmi://localhost:1099/CalculatorService", c);
            
            System.out.println("CalculatorService bound in registry");
        } catch (Exception e) {
            System.err.println("CalculatorServer exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new CalculatorServer();
        System.out.println("CalculatorServer running...");
    }
}