import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ClienteTarjetaCredito {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            TarjetaCreditoInterface tarjetaService = (TarjetaCreditoInterface) registry.lookup("TarjetaCreditoService");
            
            while (true) {
                mostrarMenuPrincipal();
                int opcion = obtenerOpcion();
                
                switch (opcion) {
                    case 1:
                        accederSistema(tarjetaService);
                        break;
                    case 2:
                        registrarNuevaTarjeta(tarjetaService);
                        break;
                    case 3:
                        System.out.println("Saliendo del sistema...");
                        return;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.toString());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE TARJETAS DE CRÉDITO ===");
        System.out.println("1. Acceder al sistema");
        System.out.println("2. Obtener una nueva tarjeta");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    private static int obtenerOpcion() {
        while (true) {
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();
                return opcion;
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número válido.");
                scanner.nextLine();
                System.out.print("Seleccione una opción: ");
            }
        }
    }
    
    private static void accederSistema(TarjetaCreditoInterface tarjetaService) throws Exception {
        System.out.println("\n=== ACCESO AL SISTEMA ===");
        System.out.print("Número de tarjeta (ej. 1234-5678-9012-3456): ");
        String numeroTarjeta = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        if (tarjetaService.autenticar(numeroTarjeta, password)) {
            System.out.println("\nBienvenido, " + tarjetaService.getTitular() + "!");
            System.out.printf("Límite de crédito: $%.2f\n", tarjetaService.getLimiteCredito());
            menuTarjeta(tarjetaService);
        } else {
            System.out.println("Error: Número de tarjeta o contraseña incorrectos.");
        }
    }
    
    private static void registrarNuevaTarjeta(TarjetaCreditoInterface tarjetaService) throws Exception {
        System.out.println("\n=== REGISTRO DE NUEVA TARJETA ===");
        
        System.out.print("Nombre del titular: ");
        String titular = scanner.nextLine();
        
        if (titular.isEmpty()) {
            System.out.println("Error: El nombre del titular no puede estar vacío.");
            return;
        }
        
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        if (password.isEmpty()) {
            System.out.println("Error: La contraseña no puede estar vacía.");
            return;
        }
        
        try {
            String numeroTarjeta = tarjetaService.registrarNuevaTarjeta(titular, password);
            
            // Autenticar para obtener los datos completos
            if (tarjetaService.autenticar(numeroTarjeta, password)) {
                double limite = tarjetaService.getLimiteCredito();
                
                System.out.println("\n¡Registro exitoso!");
                System.out.println("Sus datos de tarjeta son:");
                System.out.println("Titular: " + titular);
                System.out.println("Número de tarjeta: " + numeroTarjeta);
                System.out.printf("Límite de crédito: $%.2f\n", limite);
                System.out.println("\nGuarde esta información en un lugar seguro.");
            } else {
                System.out.println("Error: No se pudo autenticar la nueva tarjeta.");
            }
        } catch (Exception e) {
            System.out.println("Error al registrar la tarjeta: " + e.getMessage());
        }
    }
    
    private static void menuTarjeta(TarjetaCreditoInterface tarjeta) throws Exception {
        int opcion;
        do {
            System.out.println("\n=== MENÚ TARJETA DE CRÉDITO ===");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Realizar compra");
            System.out.println("3. Pagar tarjeta");
            System.out.println("4. Ver información de la tarjeta");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                
                switch (opcion) {
                    case 1:
                        consultarSaldo(tarjeta);
                        break;
                    case 2:
                        realizarCompra(tarjeta);
                        break;
                    case 3:
                        pagarTarjeta(tarjeta);
                        break;
                    case 4:
                        mostrarInfoTarjeta(tarjeta);
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número válido.");
                scanner.nextLine();
                opcion = -1;
            }
        } while (opcion != 0);
    }
    
    private static void consultarSaldo(TarjetaCreditoInterface tarjeta) {
        try {
            System.out.printf("\nSaldo actual: $%.2f\n", tarjeta.consultarSaldo());
            System.out.printf("Límite disponible: $%.2f\n", 
                tarjeta.getLimiteCredito() - tarjeta.consultarSaldo());
        } catch (Exception e) {
            System.out.println("Error al consultar saldo: " + e.getMessage());
        }
    }
    
    private static void realizarCompra(TarjetaCreditoInterface tarjeta) {
        try {
            System.out.printf("\nLímite de crédito: $%.2f\n", tarjeta.getLimiteCredito());
            System.out.printf("Saldo actual: $%.2f\n", tarjeta.consultarSaldo());
            System.out.print("Ingrese el monto a comprar: ");
            double monto = scanner.nextDouble();
            scanner.nextLine();
            
            if (tarjeta.realizarCompra(monto)) {
                System.out.println("Compra realizada con éxito.");
                System.out.printf("Nuevo saldo: $%.2f\n", tarjeta.consultarSaldo());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un monto numérico válido.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Error al realizar la compra: " + e.getMessage());
        }
    }
    
    private static void pagarTarjeta(TarjetaCreditoInterface tarjeta) {
        try {
            System.out.printf("\nSaldo actual: $%.2f\n", tarjeta.consultarSaldo());
            System.out.print("Ingrese el monto a pagar: ");
            double monto = scanner.nextDouble();
            scanner.nextLine();
            
            if (tarjeta.pagarTarjeta(monto)) {
                System.out.println("Pago realizado con éxito.");
                System.out.printf("Nuevo saldo: $%.2f\n", tarjeta.consultarSaldo());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un monto numérico válido.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Error al realizar el pago: " + e.getMessage());
        }
    }
    
    private static void mostrarInfoTarjeta(TarjetaCreditoInterface tarjeta) {
        try {
            System.out.println("\n=== Información de la Tarjeta ===");
            System.out.println("Titular: " + tarjeta.getTitular());
            System.out.println("Número de tarjeta: " + tarjeta.getNumeroTarjeta());
            System.out.printf("Límite de crédito: $%.2f\n", tarjeta.getLimiteCredito());
            System.out.printf("Saldo actual: $%.2f\n", tarjeta.consultarSaldo());
            System.out.printf("Crédito disponible: $%.2f\n", 
                tarjeta.getLimiteCredito() - tarjeta.consultarSaldo());
        } catch (Exception e) {
            System.out.println("Error al obtener información: " + e.getMessage());
        }
    }
}