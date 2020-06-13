import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHTTPResponse {

	public String message = "response";
	public int statuscode;
	public String content;

	public JsonHTTPResponse() {

	}

	public JsonHTTPResponse(int statuscode, String content) {
		this.statuscode = statuscode;

		switch (statuscode) {
		case 200:
			this.content = content;
			break;
		case 201:
			this.content = "Ok";
			break;
		case 202:
			this.content = "Modified";
			break;
		case 203:
			this.content = "Ok";
			break;
		case 400:
			this.content = "Not Found";
			break;
		case 401:
			this.content = "Bad Request";
			break;
		default:
			this.content = "Unknown Error";

		}
	}

	public String CreateJsonHTTPResponse() {
		// Convert Java Plain Object into JSON

		Gson gsonBuilder = new GsonBuilder().create();
		String jsonFromPojo = gsonBuilder.toJson(this);

		return jsonFromPojo;

	}

}
