import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class TarjetaCreditoImpl extends UnicastRemoteObject implements TarjetaCreditoInterface {
    private String numeroTarjeta;
    private String titular;
    private double saldo;
    private double limiteCredito;
    private String password;
    private static final String JSON_FILE = "tarjetas.json";
    private static final Random random = new Random();
    private static final double[] LIMITES_CREDITO = {5000.0, 10000.0, 15000.0};

    public TarjetaCreditoImpl() throws RemoteException {
        super();
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
    public double getLimiteCredito() throws RemoteException {
        return limiteCredito;
    }

    @Override
    public boolean realizarCompra(double monto) throws RemoteException, IllegalArgumentException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        
        if ((saldo + monto) > limiteCredito) {
            throw new IllegalArgumentException("Excede el límite de crédito disponible. Límite: $" + limiteCredito);
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
            throw new IllegalArgumentException("No puede pagar más de lo que debe. Saldo actual: $" + saldo);
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
        try {
            JSONArray tarjetas = leerTarjetasDeJSON();
            for (Object obj : tarjetas) {
                JSONObject tarjetaJson = (JSONObject) obj;
                if (numeroTarjeta.equals(tarjetaJson.get("numeroTarjeta")) && 
                    password.equals(tarjetaJson.get("password"))) {
                    this.numeroTarjeta = (String) tarjetaJson.get("numeroTarjeta");
                    this.titular = (String) tarjetaJson.get("titular");
                    this.limiteCredito = ((Number) tarjetaJson.get("limiteCredito")).doubleValue();
                    this.saldo = ((Number) tarjetaJson.get("saldo")).doubleValue();
                    this.password = (String) tarjetaJson.get("password");
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RemoteException("Error de autenticación: " + e.getMessage());
        }
    }

    @Override
    public String registrarNuevaTarjeta(String titular, String password) throws RemoteException {
        try {
            String nuevoNumero;
            do {
                nuevoNumero = generarNumeroTarjeta();
            } while (existeTarjeta(nuevoNumero));
            
            double limite = LIMITES_CREDITO[random.nextInt(LIMITES_CREDITO.length)];
            TarjetaCreditoImpl nuevaTarjeta = new TarjetaCreditoImpl(nuevoNumero, titular, limite, password);
            guardarTarjetaEnJSON(nuevaTarjeta);
            return nuevoNumero;
        } catch (Exception e) {
            throw new RemoteException("Error al registrar nueva tarjeta: " + e.getMessage());
        }
    }

    @Override
    public boolean existeTarjeta(String numeroTarjeta) throws RemoteException {
        try {
            JSONArray tarjetas = leerTarjetasDeJSON();
            for (Object obj : tarjetas) {
                JSONObject tarjeta = (JSONObject) obj;
                if (numeroTarjeta.equals(tarjeta.get("numeroTarjeta"))) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RemoteException("Error al verificar tarjeta: " + e.getMessage());
        }
    }

    private String generarNumeroTarjeta() {
        return String.format("%04d-%04d-%04d-%04d",
            random.nextInt(10000),
            random.nextInt(10000),
            random.nextInt(10000),
            random.nextInt(10000));
    }

    @SuppressWarnings("unchecked")
    private void guardarTarjetaEnJSON(TarjetaCreditoImpl tarjeta) throws Exception {
        JSONArray tarjetas = leerTarjetasDeJSON();
        
        JSONObject nuevaTarjeta = new JSONObject();
        nuevaTarjeta.put("numeroTarjeta", tarjeta.getNumeroTarjeta());
        nuevaTarjeta.put("titular", tarjeta.getTitular());
        nuevaTarjeta.put("limiteCredito", tarjeta.limiteCredito);
        nuevaTarjeta.put("saldo", tarjeta.saldo);
        nuevaTarjeta.put("password", tarjeta.password);
        
        tarjetas.add(nuevaTarjeta);
        
        try (FileWriter file = new FileWriter(JSON_FILE)) {
            file.write(tarjetas.toJSONString());
        }
    }

    private JSONArray leerTarjetasDeJSON() throws Exception {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return new JSONArray();
        }
        
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(file)) {
            Object obj = parser.parse(reader);
            return (JSONArray) obj;
        } catch (FileNotFoundException e) {
            return new JSONArray();
        }
    }
}