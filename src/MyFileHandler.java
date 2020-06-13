import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class MyFileHandler {

	public void DirectoryCreator(String DirectoryPath) {

		boolean success = false;
		// Creating new directory in Java, if it doesn't exists
		File directory = new File(DirectoryPath);
		if (directory.exists()) {
			System.out.println("Directory already exists ...");
		} else {
			System.out.println("Directory not exists, creating now");

			success = directory.mkdirs();
			if (success) {
				System.out.printf("Successfully created new directory : %s%n", DirectoryPath);
			} else {
				System.out.printf("Failed to create new directory: %s%n", DirectoryPath);
			}
		}
	}

	public int FileCreator(String FileName) {

		boolean success = false;

		File f = new File(FileName);
		if (f.exists()) {
			//System.out.println("File already exists");
			return 1;
		} else {
			//System.out.println("No such file exists, creating now");

			try {

				success = f.createNewFile();
				if (success) {
					return 2;
					//System.out.printf("Successfully created new file: %s%n", f);
				} else {
					return 3;
					//System.out.printf("Failed to create new file: %s%n", f);
				}
			} catch (Exception e) {
				System.out.println("Error Occurred While Creating File: " + e.getMessage());
				return 3;
			}
		}

	}

	public int WriteToFile(String path, String content,int res) {
		try {
			Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8));
			
			if(res == 1)
				return 202;
			else if(res == 2)
				return 201;
			
			else
				return 402;
			// encoding
			// Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8));

			// extra options
			// Files.write(Paths.get(path), content.getBytes(),
			// StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		} catch (IOException e) {
			System.out.println("Error Occurred While Creating File: " + e.getMessage());
			return 402;
		}
	}
	
	
	public String ReadFromFile(String FilePath) {
		StringBuilder contentBuilder = new StringBuilder();
		File f = new File(FilePath);

		// Check if the specified file
		// Exists or not
		if (f.exists()) {
			try (Stream<String> stream = Files.lines(Paths.get(FilePath), StandardCharsets.UTF_8)) {
				stream.forEach(s -> contentBuilder.append(s).append("\n"));
				
			} catch (IOException e) {
				e.printStackTrace();
				return "Error";
			}
			return contentBuilder.toString();
			
		} else {
			return "File doesn't exist";
		}
		
	}
	
	
	public int DeleteFile(String target) {
		File file = new File(target); 
        
		if(!file.exists())
			return 400;
		else {
			
			if(file.delete()) {
				return 203;
			}else
				return 402;
        } 
	}
}
