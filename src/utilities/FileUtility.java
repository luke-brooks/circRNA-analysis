package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FileUtility {

    public static String readFileToString(String file, boolean skipFirstLine) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirstLine = true;
        try {
            BufferedReader fileToRead = new BufferedReader(new FileReader(file));
            for (String line = fileToRead.readLine(); line != null; line = fileToRead.readLine()) {
                if (!skipFirstLine || !isFirstLine) {
                    stringBuilder.append(line);
                } else {
                    System.out.println("LTB: Skipping line: " + line);
                    isFirstLine = false;
                }
            }
            fileToRead.close();
        } catch (Exception e) {
            System.out.println(
                    "Error in FileUtility#readFileToString(): file - " + file + " Error msg: " + e.getMessage());
        }
        return stringBuilder.toString();
    }

    public static ArrayList<String> readFileLinesToArrayList(String file, boolean skipFirstLine) {
        ArrayList<String> fileOutput = new ArrayList<String>();
        boolean isFirstLine = true;
        try {
            BufferedReader fileToRead = new BufferedReader(new FileReader(file));
            for (String line = fileToRead.readLine(); line != null; line = fileToRead.readLine()) {
                if (!skipFirstLine || !isFirstLine) {
                    System.out.println("Line data: " + line);
                    fileOutput.add(line.toUpperCase());
                } else {
                    System.out.println("Skipping: " + line);
                    isFirstLine = false;
                }
            }
            fileToRead.close();
        } catch (Exception e) {
            System.out.println("Error in FileUtility#readFileLinesToArrayList(): file - " + file + "Error msg: " + e.getMessage());
        }
        return fileOutput;
    }
}
