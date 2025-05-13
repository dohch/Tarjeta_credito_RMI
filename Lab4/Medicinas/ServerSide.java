/*package Medicinas;
import java.rmi.*;
public class ServerSide {
public static void main(String [] args) throws Exception {
Stock pharmacy = new Stock();
pharmacy.addMedicine("Paracetamol", 3.2f, 10);
pharmacy.addMedicine("Mejoral", 2.0f, 20);
pharmacy.addMedicine("Amoxilina", 1.0f, 30);
pharmacy.addMedicine("Aspirina", 5.0f, 40);
Naming.rebind("PHARMACY", pharmacy);
System.out.println("Server ready");
}
}*/
package Medicinas;
import java.rmi.*;
import java.rmi.registry.*;

public class ServerSide {
    public static void main(String [] args) throws Exception {
        // Crear e iniciar el registro RMI en el puerto 1099
        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("Registry started on port 1099");
        } catch (Exception e) {
            System.out.println("Registry already running, continuing...");
        }
        
        Stock pharmacy = new Stock();
        pharmacy.addMedicine("Paracetamol", 3.2f, 10);
        pharmacy.addMedicine("Mejoral", 2.0f, 20);
        pharmacy.addMedicine("Amoxilina", 1.0f, 30);
        pharmacy.addMedicine("Aspirina", 5.0f, 40);
        
        Naming.rebind("PHARMACY", pharmacy);
        System.out.println("Server ready");
    }
}