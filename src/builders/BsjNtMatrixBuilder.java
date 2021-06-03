package builders;

import java.io.BufferedWriter;

import static config.Constants.CSV_EXTENSION;
import static config.Constants.HUMAN_PREFIX;
import static config.Constants.OUT_EXTENSION;
import static config.Constants.VIRAL_PREFIX;
import models.BedFile;
import models.BsjDataRow;
import utilities.FileUtility;
import utilities.LoggingUtility;

public class BsjNtMatrixBuilder {

    public static void buildBsjNtMatrixOutputFile(BedFile processedBedFile, String bsjNtMatrixOutputPath) {
        try {
            BufferedWriter humanBsjNtMatrixOutfile = FileUtility.createOutFile(bsjNtMatrixOutputPath, HUMAN_PREFIX + processedBedFile.getFileName() + OUT_EXTENSION + CSV_EXTENSION);
            BufferedWriter viralBsjNtMatrixOutfile = FileUtility.createOutFile(bsjNtMatrixOutputPath, VIRAL_PREFIX + processedBedFile.getFileName() + OUT_EXTENSION + CSV_EXTENSION);

            for (BsjDataRow processedRow : processedBedFile.getFileBsjData()) {
                BufferedWriter targetOutputFile = processedRow.getIsHuman() ? humanBsjNtMatrixOutfile : viralBsjNtMatrixOutfile;
                StringBuilder builder = new StringBuilder();
                builder.append(processedRow.getName());

                // convert each character of BsjFlankingSequence to numeral
                for (int i = 0; i < processedRow.getBsjFlankingSequence().length(); i++) {
                    builder.append("," + convertNT(processedRow.getBsjFlankingSequence().charAt(i)));
                }

                // write X lines of numeral representation of BsjFlankingSequnce to output where X = processedRow.getBsjCount()
                for (int i = 0; i < processedRow.getBsjCount(); i++) {
                    targetOutputFile.write(builder.toString());
                    targetOutputFile.write("\n");
                }
            }

            humanBsjNtMatrixOutfile.close();
            viralBsjNtMatrixOutfile.close();
        } catch (Exception e) {
            LoggingUtility.printError("Error in BsjNtMatrixBuilder#buildBsjNtMatrixOutputFile(): " + e.getMessage());
        }
    }

    private static int convertNT(char nucleotide) {
        int result = -1;
        switch (nucleotide) {
            case 'a':
            case 'A':
                result = 1;
                break;
            case 't':
            case 'T':
                result = 2;
                break;
            case 'g':
            case 'G':
                result = 3;
                break;
            case 'c':
            case 'C':
                result = 4;
                break;
        }
        return result;
    }
}
