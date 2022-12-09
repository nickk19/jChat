import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class ServerRunnable implements Runnable {
	Socket client = null;
	
    public ServerRunnable(Socket socket) {
		this.client = socket;
	}

	public void run() {
		try {
			BufferedReader inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
			DataOutputStream outStream = new DataOutputStream(client.getOutputStream());

			while (true) {
				String strRicevuta = inStream.readLine();
				if (strRicevuta == null || strRicevuta.equals("DISCONNECT")) {
					outStream.writeBytes("Disconnecting.");
					break;
				} else {
					System.out.println(client.getInetAddress().getCanonicalHostName() + " says: " + strRicevuta);
					
					for (Socket sk: MultiServer.connections) {
						System.out.println("Sending " + strRicevuta + " to " + sk.getInetAddress().getCanonicalHostName());
						outStream.writeBytes(sk.getInetAddress().getCanonicalHostName() + " says: " + strRicevuta + "\n");
					}
				}
			}

			outStream.close();
			inStream.close();
			System.out.println(client.getInetAddress().getCanonicalHostName() + " disconnected");
			MultiServer.connections.remove(client);
			client.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}