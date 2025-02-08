package utilities;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUtils {
    private FileUtils(){}

    public static String convertFileToString(String filePathAndName){
        String fileContent = "";
        try {
            fileContent = Files.readString(Paths.get(filePathAndName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(0);
        }
        return fileContent;
    }

    public static void writeToFile(String content, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFileIfExists(String filePath) {
        Path path = Paths.get(filePath);
        try {
            if (Files.exists(path)) {
                Files.delete(path); // Or Files.deleteIfExists(path) to avoid exception if file doesn't exist
                System.out.println("File deleted successfully: " + filePath);
            } else {
                System.out.println("File does not exist: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + filePath + " - " + e.getMessage());
        }
    }

    public static Properties readProperty(String filePathAndName){
        String propertiesFilePath = filePathAndName;
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}
