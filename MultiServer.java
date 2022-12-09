import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiServer {
	public static ArrayList<Socket> connections = new ArrayList<>();
	
	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(6789)) {
			System.out.println("Server is waiting...\n");

			while (true) {
				Socket socket = serverSocket.accept();
				connections.add(socket);
				System.out.println(socket.getInetAddress().getCanonicalHostName() + " connected");
				System.out.println("Connections number: " + connections.size() + "\n");

				ServerRunnable serverRunnable = new ServerRunnable(socket);
				Thread serverThread = new Thread(serverRunnable);
				serverThread.start();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Error while starting server.\n");
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		MultiServer tcpServer = new MultiServer();
		tcpServer.start();
	}
}