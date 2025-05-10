// ClienteTarjetaCredito.java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ClienteTarjetaCredito {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            // Obtener el registro RMI del servidor
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            
            // Buscar el objeto remoto
            TarjetaCreditoInterface tarjetaService = (TarjetaCreditoInterface) registry.lookup("TarjetaCreditoService");
            
            // Autenticación
            if (!autenticarUsuario(tarjetaService)) {
                System.out.println("Autenticación fallida. Saliendo del sistema...");
                return;
            }
            
            int opcion;
            do {
                mostrarMenu();
                
                try {
                    opcion = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer
                    
                    switch (opcion) {
                        case 1:
                            consultarSaldo(tarjetaService);
                            break;
                        case 2:
                            realizarCompra(tarjetaService);
                            break;
                        case 3:
                            pagarTarjeta(tarjetaService);
                            break;
                        case 4:
                            mostrarInfoTarjeta(tarjetaService);
                            break;
                        case 0:
                            System.out.println("Saliendo del sistema...");
                            break;
                        default:
                            System.out.println("Opción no válida. Intente nuevamente.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Debe ingresar un número válido.");
                    scanner.nextLine(); // Limpiar entrada incorrecta
                    opcion = -1;
                }
                
            } while (opcion != 0);
            
        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.toString());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    private static boolean autenticarUsuario(TarjetaCreditoInterface tarjetaService) throws Exception {
        System.out.println("\n=== AUTENTICACIÓN ===");
        System.out.print("Número de tarjeta (ej. 1234-5678-9012-3456): ");
        String numeroTarjeta = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        if (tarjetaService.autenticar(numeroTarjeta, password)) {
            System.out.println("\nBienvenido, " + tarjetaService.getTitular() + "!");
            return true;
        }
        return false;
    }
    
    private static void mostrarMenu() {
        System.out.println("\n=== Sistema de Tarjeta de Crédito ===");
        System.out.println("1. Consultar saldo");
        System.out.println("2. Realizar compra");
        System.out.println("3. Pagar tarjeta");
        System.out.println("4. Ver información de la tarjeta");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    private static void consultarSaldo(TarjetaCreditoInterface tarjeta) throws Exception {
        System.out.printf("\nSaldo actual: $%.2f\n", tarjeta.consultarSaldo());
    }
    
    private static void realizarCompra(TarjetaCreditoInterface tarjeta) {
        try {
            System.out.print("\nIngrese el monto a comprar: ");
            double monto = scanner.nextDouble();
            
            if (tarjeta.realizarCompra(monto)) {
                System.out.println("Compra realizada con éxito.");
                System.out.printf("Nuevo saldo: $%.2f\n", tarjeta.consultarSaldo());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un monto numérico válido.");
            scanner.nextLine(); // Limpiar entrada incorrecta
        } catch (Exception e) {
            System.out.println("Error al realizar la compra: " + e.getMessage());
        }
    }
    
    private static void pagarTarjeta(TarjetaCreditoInterface tarjeta) {
        try {
            System.out.print("\nIngrese el monto a pagar: ");
            double monto = scanner.nextDouble();
            
            if (tarjeta.pagarTarjeta(monto)) {
                System.out.println("Pago realizado con éxito.");
                System.out.printf("Nuevo saldo: $%.2f\n", tarjeta.consultarSaldo());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un monto numérico válido.");
            scanner.nextLine(); // Limpiar entrada incorrecta
        } catch (Exception e) {
            System.out.println("Error al realizar el pago: " + e.getMessage());
        }
    }
    
    private static void mostrarInfoTarjeta(TarjetaCreditoInterface tarjeta) throws Exception {
        System.out.println("\n=== Información de la Tarjeta ===");
        System.out.println("Titular: " + tarjeta.getTitular());
        System.out.println("Número de tarjeta: " + tarjeta.getNumeroTarjeta());
    }
}