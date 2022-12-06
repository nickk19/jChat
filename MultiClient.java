import java.io.BufferedReader;
import java.io.DataOutputStream;
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

	public void communicate() {
		while (true) {
			try {
				System.out.print(mySocket.getInetAddress().getCanonicalHostName() + ": ");
				String strUtente = bufferedReader.readLine();
				outStream.writeBytes(strUtente + "\n");
				
				String strRicevuta = inStream.readLine();
				System.out.println(strRicevuta + "\n");
				
				if (strUtente.equals("DISCONNECT")) {
					System.out.println("\nClient is going down.");
					mySocket.close();
					break;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Error while communicating with the server.");
				System.exit(1);
			}
		}
	}

	public static void main(String args[]) {
		MultiClient client = new MultiClient();
		client.connect();
		client.communicate();
	}
}