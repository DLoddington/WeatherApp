package utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class APIUtils {
	
	private static String apiKey = "5b93d80dd24b92df365f6ae510e868a2";
	
	public static String getAPIKey() {
		return apiKey;
	}
	
	public static String apiReturn(InputStream input) {
		InputStreamReader reader = new InputStreamReader(input);
		StringBuilder builder = new StringBuilder();
		Scanner scanner = new Scanner(reader);
		
		while(scanner.hasNext()) {
			builder.append(scanner.nextLine());
		}
		scanner.close();
		
		String toReturn = builder.toString();
		return toReturn;	
	}

}
