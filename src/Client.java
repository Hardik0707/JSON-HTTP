import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Client {

	public static JsonObject StringToJSON(String jsonString) {

		JsonParser jsonParser = new JsonParser();
		JsonObject objectFromString = jsonParser.parse(jsonString).getAsJsonObject();

		return objectFromString;
	}

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);

		while (true) {
			String ConnectionString = sc.nextLine();

			String[] Connection = ConnectionString.split(" ");

			if (Connection[0].equals("connect") && Connection.length == 3) {

				String address = Connection[1];
				int port = Integer.parseInt((Connection[2]));
				Socket s1 = null;
				String Command = null;

				BufferedReader br = null;
				BufferedReader is = null;
				PrintWriter os = null;
				String jsonRequest = null;

				try {
					s1 = new Socket(address, port); // You can use static final constant PORT_NUM
					System.out.println("Successfully connected");
					br = new BufferedReader(new InputStreamReader(System.in));
					is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
					os = new PrintWriter(s1.getOutputStream());

					String response = null;
					try {
						Command = br.readLine();

						JsonHTTPRequest jsonhttp = new JsonHTTPRequest();
						jsonRequest = jsonhttp.CreateJsonHTTPRequest(Command);

						while (jsonRequest.compareToIgnoreCase("disconnect") != 0) {

							if (jsonRequest.compareToIgnoreCase("Invalid Command") != 0) {
								os.println(jsonRequest);
								os.flush();
//								System.out.println("msg sent");
								response = is.readLine();

								/* String to JSON Convert */

								JsonObject jsonObj = StringToJSON(response);
//								System.out.println("msg received");
								System.out.println(jsonObj.get("content").getAsString());
							}else
								System.out.println(jsonRequest);
							Command = br.readLine();
//							System.out.println("msg read");
							jsonRequest = jsonhttp.	CreateJsonHTTPRequest(Command);
						}

					} catch (IOException e) {
						System.out.println("");
					}

				} catch (IOException e) {
					System.err.println("No Server");

				} finally {
					try {
						is.close();
						os.close();
						br.close();
						s1.close();
						System.out.println("Successfully disconnected");
						sc.close();
						System.exit(0);
					} catch (Exception e) {
					}
				}

			} else {
			}
		}
	}
}
