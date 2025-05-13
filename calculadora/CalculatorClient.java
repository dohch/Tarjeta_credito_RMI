import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class CalculatorClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java CalculatorClient <num1> <num2>");
            System.exit(1);
        }

        int num1 = Integer.parseInt(args[0]);
        int num2 = Integer.parseInt(args[1]);

        try {
            Calculator c = (Calculator) Naming.lookup("rmi://localhost/CalculatorService");
            
            System.out.println("The addition of " + num1 + " and " + num2 + " is: " + c.add(num1, num2));
            System.out.println("The substraction of " + num1 + " and " + num2 + " is: " + c.sub(num1, num2));
            System.out.println("The multiplication of " + num1 + " and " + num2 + " is: " + c.mul(num1, num2));
            System.out.println("The division of " + num1 + " and " + num2 + " is: " + c.div(num1, num2));
            
        } catch (MalformedURLException murle) {
            System.err.println("MalformedURLException: " + murle.getMessage());
        } catch (RemoteException re) {
            System.err.println("RemoteException: " + re.getMessage());
        } catch (NotBoundException nbe) {
            System.err.println("NotBoundException: " + nbe.getMessage());
        } catch (java.lang.ArithmeticException ae) {
            System.err.println("ArithmeticException: " + ae.getMessage());
        }
    }
}   