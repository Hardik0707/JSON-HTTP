import java.io.File;

import com.google.gson.JsonObject;

public class CommandHandler {

	public String ExecuteRequest(JsonObject jsonObj) {

		String type = jsonObj.get("type").getAsString().toUpperCase();
		String content = null;
		if(type.equalsIgnoreCase("PUT"))
			content = jsonObj.get("content").getAsString();
		
		String target = jsonObj.get("target").getAsString();

		int statusCode = 0;
		String responseContent = "";
		JsonHTTPResponse jsonResponse = null;
		
		if(type.compareToIgnoreCase("PUT") == 0) {
			statusCode = PUTCommand(target, content);
		}
		else if(type.compareToIgnoreCase("GET") == 0)
			responseContent = GETCommand(target);
		else if(type.compareToIgnoreCase("DELETE") == 0)
			statusCode = DELETECommand(target);
		else {
			System.out.println("Command Not Found");
			statusCode = 402;
		}
		/*switch (type) {
		case "PUT":
			statusCode = PUTCommand(target, content);
			break;
		case "GET":
			responseContent = GETCommand(target);
			break;
		case "DELETE":
			statusCode = DELETECommand(target);
			break;

		default:
			System.out.println("Command Not Found");
			statusCode = 402;
			//Return from here
		}*/
		
		/*Create JSON response*/
		
		
		if(responseContent.compareToIgnoreCase("File doesn't exist") == 0)
			statusCode = 400;
		else if(responseContent.compareToIgnoreCase("Error") == 0 )
			statusCode = 402;
		else if(responseContent.compareToIgnoreCase("") != 0)
			statusCode = 200;
		
		jsonResponse = new JsonHTTPResponse(statusCode,responseContent);
		return jsonResponse.CreateJsonHTTPResponse();
		
	}

	public static int PUTCommand(String target, String content) {

		MyFileHandler handler = new MyFileHandler();
		File file = new File(target);

		String parentDirName = file.getParent(); // to get the parent dir name
		if(parentDirName != null)
			handler.DirectoryCreator(parentDirName);
		int res = handler.FileCreator(target);

		return (handler.WriteToFile(target, content, res));
	}

	public static String GETCommand(String target) {

		MyFileHandler handler = new MyFileHandler();
		return handler.ReadFromFile(target);
	}

	public static int DELETECommand(String target) {

		MyFileHandler handler = new MyFileHandler();
		return handler.DeleteFile(target);
	}

}
