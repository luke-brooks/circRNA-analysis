package utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtility {

    public static String removeFileNameExtension(String fileName) {
        List<String> fileNameParts = Arrays.asList(fileName.split("\\."));

        int indexOfLastElement = fileNameParts.size() - 1;
        fileNameParts.remove(indexOfLastElement + 1); // this doesnt work cuz java cant remove the last item from a list...

        String result = convertListToString(fileNameParts);

        return result;
    }

    public static String buildCsvLine(ArrayList<String> valuesToWrite, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : valuesToWrite) {
            stringBuilder.append(value);
            stringBuilder.append(delimiter);
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public static String convertListToString(List<String> valuesToWrite) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : valuesToWrite) {
            stringBuilder.append(value);
        }
        return stringBuilder.toString();
    }
}
