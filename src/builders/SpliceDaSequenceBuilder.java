package builders;
import java.io.BufferedWriter;

import static config.Constants.TXT_EXTENSION;
import static config.Constants.HUMAN_PREFIX;
import static config.Constants.OUT_EXTENSION;
import static config.Constants.VIRAL_PREFIX;
import models.BedFile;
import models.BsjDataRow;
import utilities.FileUtility;
import utilities.LoggingUtility;

public class SpliceDaSequenceBuilder {

    public static void buildSpliceDaSequenceOutputFile(BedFile processedBedFile, String spliceDaSequenceOutputPath) {
        try {
            BufferedWriter humanBsjNtMatrixOutfile = FileUtility.createOutFile(spliceDaSequenceOutputPath, HUMAN_PREFIX + processedBedFile.getFileName() + OUT_EXTENSION + TXT_EXTENSION);
            BufferedWriter viralBsjNtMatrixOutfile = FileUtility.createOutFile(spliceDaSequenceOutputPath, VIRAL_PREFIX + processedBedFile.getFileName() + OUT_EXTENSION + TXT_EXTENSION);

            for (BsjDataRow processedRow : processedBedFile.getFileBsjData()) {
                BufferedWriter targetOutputFile = processedRow.getIsHuman() ? humanBsjNtMatrixOutfile : viralBsjNtMatrixOutfile;
                StringBuilder builder = new StringBuilder();
                builder.append(processedRow.getName());

                builder.append("\t" + processedRow.getSpliceDa());

                // write X lines of SpliceDa to output where X = processedRow.getBsjCount()
                for (int i = 0; i < processedRow.getBsjCount(); i++) {
                    targetOutputFile.write(builder.toString());
                    targetOutputFile.write("\n");
                }
            }

            humanBsjNtMatrixOutfile.close();
            viralBsjNtMatrixOutfile.close();
        } catch (Exception e) {
            LoggingUtility.printError("Error in SpliceDaSequenceBuilder#buildSpliceDaSequenceOutputFile(): " + e.getMessage());
        }
    }
}
