package builders;

import java.io.BufferedWriter;
import java.util.ArrayList;

import static config.Constants.CSV_EXTENSION;
import models.BedFile;
import models.BsjDataRow;
import utilities.FileUtility;
import utilities.LoggingUtility;
import utilities.StringUtility;

public class BsjSpliceDaFrequencyBuilder {

    public static void buildBsjSpliceDaFrequencyOutputFile(ArrayList<BedFile> processedBedFiles,
            String bsjSpliceDaFrequencyOutputPath, ArrayList<String> _spliceOptions) {
        try {
            BufferedWriter bsjSpliceDaFrequencyOutfile = FileUtility.createOutFile(bsjSpliceDaFrequencyOutputPath,
                    "SpliceDASummary" + CSV_EXTENSION);

            ArrayList<String> headersList = new ArrayList<String>();
            headersList.add("Splice D-A");
            headersList.addAll(_spliceOptions);
            headersList.add("Summary");

            String header = StringUtility.buildCsvLine(headersList, ",");
            bsjSpliceDaFrequencyOutfile.write(header);

            for (BedFile processedFile : processedBedFiles) {

                ArrayList<String> spliceCounts = buildSpliceCountList(_spliceOptions.size());

                for (BsjDataRow row : processedFile.getFileBsjData()) {

                    String sequenceToMatch = row.getSpliceDa();
                    int sequenceNum = row.getBsjCount();

                    // get index of matched 4 character splice DA sequence
                    int targetIndex = _spliceOptions.indexOf(sequenceToMatch.toUpperCase());

                    // get & update count for matched splice DA sequence
                    String targetItem = spliceCounts.get(targetIndex);
                    int updatedCount = Integer.parseInt(targetItem) + sequenceNum;

                    spliceCounts.set(targetIndex, String.valueOf(updatedCount));
                }

                String spliceTotal = getSpliceTotal(spliceCounts);
                ArrayList<String> spliceData = new ArrayList<String>();
                spliceData.add(processedFile.getFileName());
                spliceData.addAll(spliceCounts);
                spliceData.add(spliceTotal);

                String dataLine = StringUtility.buildCsvLine(spliceData, ",");
                bsjSpliceDaFrequencyOutfile.write(dataLine);
            }

            bsjSpliceDaFrequencyOutfile.close();
        } catch (Exception e) {
            LoggingUtility.printError(
                    "Error in BsjSpliceDaFrequencyBuilder#buildBsjSpliceDaFrequencyOutputFile(): " + e.getMessage());
        }
    }

    private static String getSpliceTotal(ArrayList<String> list) {
        int total = 0;
        for (String value : list) {
            total += Integer.parseInt(value);
        }
        return String.valueOf(total);
    }

    private static ArrayList<String> buildSpliceCountList(int listLength) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < listLength; i++) {
            result.add("0");
        }
        return result;
    }
}
