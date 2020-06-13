import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHTTPRequest {

	public String message = "request";
	public String type;
	public String target;
	public String content;

	public JsonHTTPRequest() {

	}

	// PUT Request
	public JsonHTTPRequest(String type, String content, String target) {
		this.type = type;
		if(target.charAt(0) == '/')
			target=target.substring(1);
		
		this.target = target;
		this.content = readFile(content);
	}

	// GET and DELETE
	public JsonHTTPRequest(String type, String target) {

		this.type = type;
		
		if(target.charAt(0) == '/')
			target=target.substring(1);
		
		this.target = target;
	}

	public JsonHTTPRequest(String type) {
		this.type = type;
	}

	public String CreateJsonHTTPRequest(String Command) {

		// Create Object of JsonHTTP
		String []Parameters = null;
		JsonHTTPRequest jsonhttp = null;
		Parameters = Command.split(" ");
		// System.out.println(Parameters.length);

		if (Parameters.length == 1 && Parameters[0].compareTo("disconnect") ==0 )
			jsonhttp = new JsonHTTPRequest(Parameters[0]);
		else if (Parameters.length == 2 && (Parameters[0].compareTo("get") == 0 || Parameters[0].compareToIgnoreCase("delete") == 0) )
			jsonhttp = new JsonHTTPRequest(Parameters[0], Parameters[1]);
		else if (Parameters.length == 3 && Parameters[0].compareTo("put") == 0)
			jsonhttp = new JsonHTTPRequest(Parameters[0], Parameters[1], Parameters[2]);
		else {
			return "Invalid Command";
		}
		
		
		/*For PUT Command Source file not found*/
		
		if(jsonhttp.type.compareToIgnoreCase("PUT") == 0)
			if(jsonhttp.content.compareToIgnoreCase("ERROR_SRC_FILE_NOT_FOUND") == 0)
				return "Invalid Command";
		
		// Convert Java Plain Object into JSON

		Gson gsonBuilder = new GsonBuilder().create();
		String jsonFromPojo = gsonBuilder.toJson(jsonhttp);
		return jsonFromPojo;
	}

	public static String readFile(String filePath) {

		StringBuilder contentBuilder = new StringBuilder();
		File f = new File(filePath);

		// Check if the specified file
		// Exists or not
		if (f.exists()) {
			try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
				stream.forEach(s -> contentBuilder.append(s).append("\n"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return contentBuilder.toString();
		} else {
			System.out.println("Source file not found.");
			return "ERROR_SRC_FILE_NOT_FOUND";
		}
//		return contentBuilder.toString();

	}

}
