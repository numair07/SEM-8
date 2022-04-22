import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Waiting for connection on port : " + serverSocket.getLocalPort());
        socket = serverSocket.accept();
        System.out.println("Accepted connection from : " + socket.getRemoteSocketAddress());
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void sendPAndQValues(int p, int q) throws IOException {
        dataOutputStream.writeInt(p);
        dataOutputStream.writeInt(q);
    }

    public void sendSharedKey(int sharedKey) throws IOException {
        dataOutputStream.writeInt(sharedKey);
    }

    public int generateSharedKey(int p, int q, int a) {
        return BigInteger.valueOf(q).pow(a).mod(BigInteger.valueOf(p)).intValue();
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public int generateSecretKey(int receivedSharedKey, int p, int a) {
        return BigInteger.valueOf(receivedSharedKey).pow(a).mod(BigInteger.valueOf(p)).intValue();
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public static void main(String... args) throws IOException {
        System.out.print("Enter the values of P and Q : ");
        Scanner scanner = new Scanner(System.in);
        int p = scanner.nextInt();
        int q = scanner.nextInt();
        Server server = new Server(6969);
        server.sendPAndQValues(p, q);
        System.out.print("Enter Private Key : ");
        int a = scanner.nextInt();
        int sharedKey = server.generateSharedKey(p, q, a);
        System.out.println("SHARED KEY : " + sharedKey);
        server.sendSharedKey(sharedKey);
        int receivedSharedKey = server.getDataInputStream().readInt();
        System.out.println("RECEIVED SHARED KEY : " + receivedSharedKey);
        int secretKey = server.generateSecretKey(receivedSharedKey, p, a);
        System.out.println("SECRET KEY : " + secretKey);
        server.closeConnection();
    }
}
