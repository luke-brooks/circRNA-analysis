package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class FileUtility {

    public static BufferedWriter createOutFile(String outDir, String outFileName) {
        BufferedWriter result = null;
        try {
            FileWriter initFile = new FileWriter(new File(outDir + outFileName));
                result = new BufferedWriter(initFile);
        } catch (Exception e) {
            LoggingUtility.printError("Error in FileUtility#createOutFile()" + e.getMessage());
        }
        return result;
    }

    public static String readFileToString(String file, boolean skipFirstLine) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirstLine = true;
        try {
            BufferedReader fileToRead = new BufferedReader(new FileReader(file));
            for (String line = fileToRead.readLine(); line != null; line = fileToRead.readLine()) {
                if (!skipFirstLine || !isFirstLine) {
                    stringBuilder.append(line);
                } else {
                    LoggingUtility.printInfo("Skipping line: " + line);
                    isFirstLine = false;
                }
            }
            fileToRead.close();
        } catch (Exception e) {
            LoggingUtility.printError("Error in FileUtility#readFileToString(): file - " + file + " Error msg: " + e.getMessage());
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
                    LoggingUtility.printInfo("Line data: " + line);
                    fileOutput.add(line.toUpperCase());
                } else {
                    LoggingUtility.printInfo("Skipping: " + line);
                    isFirstLine = false;
                }
            }
            fileToRead.close();
        } catch (Exception e) {
            LoggingUtility.printError("Error in FileUtility#readFileLinesToArrayList(): file - " + file + "Error msg: " + e.getMessage());
        }
        return fileOutput;
    }
}
