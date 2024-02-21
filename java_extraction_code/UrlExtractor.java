/**
 * UrlExtractor
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UrlExtractor {
    public static void main(String[] args) {
        String filePath = "/home/talentum/shared/Search_Engine/java_extraction_code/wiki_merged.txt"; // File path
        String objectIdToFind; // ID to find

        // Map to store objects by their IDs
        Map<String, String> objectMap = new HashMap<>();

        // Read objects from file and store in map
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line represents an object in JSON format
                String[] parts = line.split("\"id\": \"|\", \"url\": \"");
                if (parts.length >= 3) {
                    String id = parts[1];
                    String url = parts[2].substring(0, parts[2].indexOf("\""));
                    objectMap.put(id, url);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

	Scanner sc = new Scanner(System.in);
	System.out.println("Enter the id to find corresponding url : ");
	objectIdToFind = sc.next();

        // Find object by ID and print its URL
        String url = objectMap.get(objectIdToFind);
        if (url != null) {
            System.out.println("URL for object with ID " + objectIdToFind + ": " + url);
        } else {
            System.out.println("Object with ID " + objectIdToFind + " not found.");
        }
    }
}
