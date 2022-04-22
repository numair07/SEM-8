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

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public double encrypt(int p, int e, int n) {
        return BigInteger.valueOf(p).pow(e).mod(BigInteger.valueOf(n)).intValue();
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public static void main(String... args) throws IOException {
        Server server = new Server(6969);
        int e = server.getDataInputStream().readInt();
        int n = server.getDataInputStream().readInt();
        System.out.println("PUBLIC KEY OF CLIENT {" + e + ", " + n + "}");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Plain Text (Numeric) : ");
        int p = scanner.nextInt();
        System.out.println("Encrypting...");
        double cipher = server.encrypt(p, e, n);
        System.out.println("Encrypted Data : " + cipher);
        server.getDataOutputStream().writeDouble(cipher);
        server.closeConnection();
    }
}
