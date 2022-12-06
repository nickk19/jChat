import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class ServerRunnable implements Runnable {
	Socket client = null;
	BufferedReader inStream;
	BufferedWriter outStream;
	
    public ServerRunnable(Socket socket) {
		this.client = socket;
		try {
			this.inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
			this.outStream = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			System.out.println("IO Error");
		}
	}

	public void run() {
		try {
			while (true) {
				String strRicevuta = inStream.readLine();
				if (strRicevuta == null || strRicevuta.equals("DISCONNECT")) {
					outStream.write("Disconnecting.");
					outStream.newLine();
					outStream.flush();
					break;
				} else {
					System.out.println(client.getInetAddress().getCanonicalHostName() + " says: " + strRicevuta);
					broadcast(strRicevuta);
				}
			}

			outStream.close();
			inStream.close();
			System.out.println(client.getInetAddress().getCanonicalHostName() + " disconnected\n");
			client.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public void broadcast(String message) {
		for (Socket sk: MultiServer.connections) {
			try {
				outStream.write(message);
				outStream.newLine();
				outStream.flush();
			} catch (IOException e) {
				System.out.println("Error while sending.");
			}
		}
	}
}