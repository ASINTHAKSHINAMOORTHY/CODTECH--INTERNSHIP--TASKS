import java.io.*;

public class FileHandlingUtility {

    public static void main(String[] args) throws IOException {
        String fileName = "sample.txt";

        // Write to file
        FileWriter writer = new FileWriter(fileName);
        writer.write("Hello Internship\nWelcome to Java File Handling");
        writer.close();

        // Read file
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        System.out.println("File Content:");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();

        // Append to file
        FileWriter appendWriter = new FileWriter(fileName, true);
        appendWriter.write("\nFile modified successfully.");
        appendWriter.close();

        System.out.println("File operation completed.");
    }
}
