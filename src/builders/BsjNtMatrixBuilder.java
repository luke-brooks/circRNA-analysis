package builders;

import java.io.BufferedWriter;

import static config.Constants.CSV_EXTENSION;
import static config.Constants.OUT_EXTENSION;
import models.BedFile;
import models.BsjDataRow;
import utilities.FileUtility;

public class BsjNtMatrixBuilder {

    public static void buildBsjNtMatrixOutputFile(BedFile processedBedFile, String bsjNtMatrixOutputPath) {
        try {
            BufferedWriter bsjNtMatrixOutfile = FileUtility.createOutFile(bsjNtMatrixOutputPath, processedBedFile.getFileName() + OUT_EXTENSION + CSV_EXTENSION);

            for (BsjDataRow processedRow : processedBedFile.getFileBsjData()) {
                // c1 is processedRow.getName() - no other logic needed
                StringBuilder builder = new StringBuilder();
                builder.append(processedRow.getName());

                // convert each character of BsjFlankingSequence to numeral
                for (int i = 0; i < processedRow.getBsjFlankingSequence().length(); i++) {
                    builder.append("\t" + convertNT(processedRow.getBsjFlankingSequence().charAt(i)));
                }

                // write X lines of numeral representation of BsjFlankingSequnce to output where X = processedRow.getBsjCount()
                for (int i = 0; i < processedRow.getBsjCount(); i++) {
                    bsjNtMatrixOutfile.write(builder.toString());
                    bsjNtMatrixOutfile.write("\n");
                }
            }

            bsjNtMatrixOutfile.close();
        } catch (Exception e) {
            System.out.println("Error in BsjNtMatrixBuilder#buildBsjNtMatrixOutputFile(): " + e.getMessage());
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
