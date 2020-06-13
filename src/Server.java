import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 0;
		/* Add Argument of port if available */
		if (args.length == 2 && args[0].compareTo("-p") == 0) {
			try {
				port = Integer.parseInt(args[1]);

				if (port > 65535 && port < 0) {
					System.out.println("Invalid Port Number");
				}
			} catch (Exception e) {
				System.out.println("Invalid Port Number");
			}
		} else if (args.length == 0) {
			port = 5521; // default port
		} else {
			System.out.println("Invalid arguments");
		System.exit(0);	
		}

		/* End of Port Configuration */

		/* Socket Programming */

		Socket s = null;
		ServerSocket ss2 = null;
		System.out.println("Server Listening......");
		try {
			ss2 = new ServerSocket(port); // can also use static final PORT_NUM , when defined

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server error");

		}

		while (true) {
			try {
				s = ss2.accept();
				System.out.println("Connection Established");
				ServerThread st = new ServerThread(s);
				st.start();

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection Error");

			}
		}

		/* End of Socket Programming */
	}

}

/* Server Thread Class for MUltiple Clients connection */

class ServerThread extends Thread {

	String line = null;
	BufferedReader is = null;
	PrintWriter os = null;
	Socket s = null;

	public ServerThread(Socket s) {
		this.s = s;
	}

	/* JSON String Request To JSON Object */

	public static JsonObject StringToJSON(String jsonString) {

		JsonParser jsonParser = new JsonParser();
		JsonObject objectFromString = jsonParser.parse(jsonString).getAsJsonObject();

		return objectFromString;
	}

	public void run() {
		try {
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream());

		} catch (IOException e) {
			System.out.println("IO error in server thread");
		}

		try {
			line = is.readLine();

			/* String to JSON Convert */
			
			//System.out.println(line);
			JsonObject jsonObj = StringToJSON(line);

			while (jsonObj.get("type").getAsString().toUpperCase() != "DISCONNECT") { // disconnect socket connection

				/* Command Handler call */

				 CommandHandler command = new CommandHandler(); 
				 String responseJSON = command.ExecuteRequest(jsonObj); 
				/* send JSON response os.println */
				 //System.out.println(responseJSON);
				 os.println(responseJSON);
				 os.flush();

				//System.out.println("Response to Client  :  " + line);
				line = is.readLine();
				jsonObj = StringToJSON(line);
			}
		} catch (IOException e) {

			line = this.getName(); // reused String line for getting thread name
			System.out.println("IO Error/ Client " + line + " terminated abruptly");
		} catch (NullPointerException e) {
			line = this.getName(); // reused String line for getting thread name
			System.out.println("Client " + line + " Closed");
		}

		finally {
			try {
				System.out.println("Connection Closing..");
				
				
				if (is != null) {
					is.close();
					//System.out.println(" Socket Input Stream Closed");
				}

				if (os != null) {
					os.close();
					//System.out.println("Socket Out Closed");
				}
				if (s != null) {
					s.close();
					System.out.println("Successfully disconnected");
				}

			} catch (IOException ie) {
				System.out.println("Socket Close Error: " + ie.getMessage());
			}
		} // end finally
	}
}
