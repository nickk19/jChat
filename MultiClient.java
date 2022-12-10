import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MultiClient {
	String serverIp = "localhost";
	int serverPort = 6789;
	Socket mySocket;

	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	BufferedReader inStream; 		//Input stream from server
	DataOutputStream outStream; 	//Output stream to server
	

	public Socket connect() {
		System.out.println("Client ready.\n");
		
		try {
			mySocket = new Socket(serverIp, serverPort);
			outStream = new DataOutputStream(mySocket.getOutputStream());
			inStream = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Unknown host.");
		} catch (Exception e) {
			System.out.println("Error while connecting.");
			System.exit(1);
		}
		return mySocket;
	}

	public void send() {
		while (mySocket.isConnected()) {
			try {
				String strUtente = bufferedReader.readLine();
				outStream.writeBytes(strUtente + "\n");
				outStream.flush();
				
				if (strUtente.equals("DISCONNECT")) {
					System.out.println("\nClient is going down.");
					yeet();
				}

			} catch (Exception e) {
				System.out.println("Error while communicating with the server.");
			}
		}
	}

	public void listen() {
		System.out.print(mySocket.getInetAddress().getCanonicalHostName() + ": ");
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(mySocket.isConnected()) {
					String strRicevuta;
					try {
						strRicevuta = inStream.readLine();
						System.out.println(strRicevuta);
						System.out.print(mySocket.getInetAddress().getCanonicalHostName() + ": ");
					} catch (IOException e) {}
				}
			}
		}).start();
	}

	public void yeet() {
		try {
			mySocket.close();
			bufferedReader.close();
			inStream.close();
			outStream.close();
			System.exit(0);
		} catch (IOException e) {}
	}

	public static void main(String args[]) {
		MultiClient client = new MultiClient();
		client.connect();
		client.listen();
		client.send();
	}
}