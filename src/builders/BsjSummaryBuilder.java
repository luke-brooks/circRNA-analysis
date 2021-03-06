package builders;

import java.io.BufferedWriter;

import models.BedFile;
import models.BsjDataRow;

import static config.Constants.BSJ_SUMMARY_FILE_SUFFIX;
import static config.Constants.CSV_EXTENSION;
import utilities.FileUtility;
import utilities.LoggingUtility;

public class BsjSummaryBuilder {

    public static void buildBsjSummaryOutputFile(BedFile processedBedFile, String bsjSummaryOutputPath) {
        try {
            BufferedWriter bsjSummaryOutfile = FileUtility.createOutFile(bsjSummaryOutputPath, processedBedFile.getFileName() + BSJ_SUMMARY_FILE_SUFFIX + CSV_EXTENSION);

            bsjSummaryOutfile.write(buildBsjSummaryHeaderLine());
            bsjSummaryOutfile.write("\n");

            for (BsjDataRow processedRow : processedBedFile.getFileBsjData()) {
                String outputLine = buildOutputLineBsjSummary(processedRow);
                bsjSummaryOutfile.write(outputLine);
                bsjSummaryOutfile.write("\n");
            }

            bsjSummaryOutfile.close();
        } catch (Exception e) {
            LoggingUtility.printError("Error in BsjSummaryBuilder#buildBsjSummaryOutput(): " + e.getMessage());
        }
    }

    private static String buildBsjSummaryHeaderLine() {
        StringBuilder builder = new StringBuilder();

        builder.append("Chromosome");
        builder.append(",");
        builder.append("JunctionEnd");
        builder.append(",");
        builder.append("JunctionStart");
        builder.append(",");
        builder.append("Name");
        builder.append(",");
        builder.append("Count");
        builder.append(",");
        builder.append("Strand");
        builder.append(",");
        builder.append("BSJFlankingSeq");
        builder.append(",");
        builder.append("Splice D-A");

        return builder.toString();
    }

    private static String buildOutputLineBsjSummary(BsjDataRow row) {
        StringBuilder builder = new StringBuilder();

        builder.append(row.getChromosome());
        builder.append(",");
        builder.append(row.getJunctionEnd());
        builder.append(",");
        builder.append(row.getJunctionStart());
        builder.append(",");
        builder.append(row.getName());
        builder.append(",");
        builder.append(row.getBsjCount());
        builder.append(",");
        builder.append(row.getStrand());
        builder.append(",");
        builder.append(row.getBsjFlankingSequence());
        builder.append(",");
        builder.append(row.getSpliceDa());

        return builder.toString();
    }
}
