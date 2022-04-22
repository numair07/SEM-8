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

    public Client() {
        scanner = new Scanner(System.in);
    }

    public void establishConnection(int port) throws IOException {
        socket = new Socket("127.0.0.1", port);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public int getPrivateKey() {
        System.out.print("ENTER PRIVATE KEY : ");
        return scanner.nextInt();
    }

    public int generateSharedKey(int p, int q, int b) {
        return BigInteger.valueOf(q).pow(b).mod(BigInteger.valueOf(p)).intValue();
    }

    public int generateSecretKey(int receivedSharedKey, int p, int b) {
        return BigInteger.valueOf(receivedSharedKey).pow(b).mod(BigInteger.valueOf(p)).intValue();
    }

    public void sendSharedKey(int sharedKey) throws IOException {
        dataOutputStream.writeInt(sharedKey);
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public static void main(String... args) throws IOException {
        Client client = new Client();
        client.establishConnection(6969);
        int p = client.getDataInputStream().readInt();
        int q = client.getDataInputStream().readInt();
        int b = client.getPrivateKey();
        int sharedKey = client.generateSharedKey(p, q, b);
        int receivedSharedKey = client.getDataInputStream().readInt();
        client.sendSharedKey(sharedKey);
        System.out.println("SHARED KEY : " + sharedKey);
        System.out.println("RECEIVED SHARED KEY : " + receivedSharedKey);
        int secretKey = client.generateSecretKey(receivedSharedKey, p, b);
        System.out.println("SECRET KEY GENERATED : " + secretKey);
        client.closeConnection();
    }
}
