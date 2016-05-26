import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class SimpleChatServer {
	ArrayList<PrintWriter> clientWriters = new ArrayList<>();
	public void start() {
		try {
			ServerSocket serv = new ServerSocket(60038);

		while(true) {
			Socket sock = serv.accept();
			// = new PrintWriter(sock.getOutputStream());
			//pw1.println("Are you receiving this?");
			Thread t = new Thread(/*new ClientHandler(sock, pw1));*/
					() -> {
				try {
					clientWriters.add(new PrintWriter(sock.getOutputStream()));

					InputStreamReader isr = new InputStreamReader(sock.getInputStream());
					BufferedReader br = new BufferedReader(isr);

					String msg;
					while ((msg = br.readLine()) != null) {
						for (PrintWriter pw : clientWriters){
							pw.println(msg);
							pw.flush();
							System.out.println("Received and processed: " + msg);
						}
					}
				} catch (Exception e) {e.printStackTrace();}
			});
			t.start();
		}
		} catch (Exception ex) {}
		//String[] msg = {"Have a nice day", "Have a great day", "Have an okay day", "Have a day"};

	/*public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		PrintWriter pw;

		public ClientHandler(Socket clientSocket, PrintWriter pw) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
				this.pw = pw;

			} catch(Exception ex) {ex.printStackTrace();}
		} // close constructor

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					pw.println(message);
					pw.flush();
					//tellEveryone(message);

				} // close while
			} catch(Exception ex) {ex.printStackTrace();}
		} // close run
	}*/
	}

	public static void main(String[] args) {
		SimpleChatServer simp = new SimpleChatServer();
		simp.start();
	}
}