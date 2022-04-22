import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Scanner scanner;

    private int primeNumberOne;
    private int primeNumberTwo;

    private int e;
    private int eulersToientFuncion;
    private int d;
    private int n;

    public Client() {
        primeNumberOne = 0;
        primeNumberTwo = 0;

        e = 0;
        eulersToientFuncion = 0;
        d = 0;
        n = 0;

        scanner = new Scanner(System.in);
    }

    public void establishConnection(int port) throws IOException {
        socket = new Socket("127.0.0.1", port);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void setPrimeNumbers() {
        System.out.println("Enter two prime numbers : ");
        primeNumberOne = scanner.nextInt();
        primeNumberTwo = scanner.nextInt();
    }

    public int GCD(int num1, int num2) {
        int resultGCD = 1;
        for (int i=1; i<=num1 && i<=num2; i++) {
            if (num1%i==0 && num2%i==0) {
                resultGCD = i;
            }
        }
        return resultGCD;
    }

    public void generatePublicKey() {
        n = primeNumberOne * primeNumberTwo;
        System.out.println("N = " + n);
        eulersToientFuncion = (primeNumberOne-1)*(primeNumberTwo-1);
        for (int i=2; i<eulersToientFuncion; i++) {
            if(GCD(i, eulersToientFuncion) == 1) {
                e = i;
                break;
            }
        }
        System.out.println("PUBLIC KEY : {" + e + ", " + n + "}");
    }

    public void generatePrivateKey() {
        int i = 1;
        while (true) {
            if(((eulersToientFuncion*i) + 1)%e == 0) {
                d = ((eulersToientFuncion*i) + 1)/e;
                break;
            }
            i++;
        }
        System.out.println("PRIVATE KEY : {" + d + ", " + n + "}");
    }

    public void sendPublicKey() throws IOException {
        dataOutputStream.writeInt(e);
        dataOutputStream.writeInt(n);
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public double decrypt(double cipher) {
        return BigInteger.valueOf((long) cipher).pow(d).mod(BigInteger.valueOf(n)).intValue();
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public static void main(String... args) throws IOException {
        Client client = new Client();
        client.setPrimeNumbers();
        client.generatePublicKey();
        client.generatePrivateKey();
        client.establishConnection(6969);
        client.sendPublicKey();
        double response = client.getDataInputStream().readDouble();
        System.out.println("Encrypted Text : " + response);
        System.out.println("Decrypting...");
        System.out.println("DECRYPTED PLAIN TEXT : " +  client.decrypt(response));
        client.closeConnection();
    }

}
