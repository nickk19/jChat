import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MultiClient {
	String serverIp = "localhost";	//Indirizzo server locale
	int serverPort = 6789; 			//Porta x servizio data e ora
	Socket mySocket;

	BufferedReader inStream; 		//Stream di input
	BufferedWriter outStream; 		//Stream di output
	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

	public Socket connect() {
		System.out.println("Client ready.\n");
		try {
			mySocket = new Socket(serverIp, serverPort);
			inStream = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			outStream = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
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
				outStream.write(strUtente);
				outStream.newLine();
				outStream.flush();
				
				String strRicevuta = inStream.readLine();
				System.out.println("Server says: "+ strRicevuta);
				
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