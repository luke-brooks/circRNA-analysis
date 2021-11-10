package builders;

import java.io.BufferedWriter;

import models.BedFile;
import models.BsjDataRow;

import static config.Constants.BED_EXTENSION;
import static config.Constants.SASHIMI_FILE_SUFFIX;
import utilities.FileUtility;
import utilities.LoggingUtility;

public class SashimiBuilder {
    private static int block_size = 1;

    public static void buildSashimiOutputFile(BedFile processedBedFile, String sashimiOutputPath, int sashimiThreshold) {
        try {
            BufferedWriter sashimiHsvOutfile = FileUtility.createOutFile(sashimiOutputPath, processedBedFile.getFileName() + "_hsv_" + SASHIMI_FILE_SUFFIX + BED_EXTENSION);
            BufferedWriter sashimiKshvOutfile = FileUtility.createOutFile(sashimiOutputPath, processedBedFile.getFileName() + "_kshv_" + SASHIMI_FILE_SUFFIX + BED_EXTENSION);
            BufferedWriter sashimiHumanOutfile = FileUtility.createOutFile(sashimiOutputPath, processedBedFile.getFileName() + "_human_" + SASHIMI_FILE_SUFFIX + BED_EXTENSION);

            sashimiHsvOutfile.write(buildSashimiHeaderLine());
            sashimiHsvOutfile.write("\n");

            sashimiKshvOutfile.write(buildSashimiHeaderLine());
            sashimiKshvOutfile.write("\n");

            sashimiHumanOutfile.write(buildSashimiHeaderLine());
            sashimiHumanOutfile.write("\n");

            for (BsjDataRow processedRow : processedBedFile.getFileBsjData()) {
                if (processedRow.getBsjCount() >= sashimiThreshold) {
                    String outputLine = buildOutputLineSashimi(processedRow);

                    if (processedRow.getChromosome().equals("KT899744")) {
                        sashimiHsvOutfile.write(outputLine);
                        sashimiHsvOutfile.write("\n");
                    }
                    else if (processedRow.getChromosome().equals("NC_009333.1")) {
                        sashimiKshvOutfile.write(outputLine);
                        sashimiKshvOutfile.write("\n");
                    }
                    else {
                        sashimiHumanOutfile.write(outputLine);
                        sashimiHumanOutfile.write("\n");
                    }
                }
            }

            sashimiHsvOutfile.close();
            sashimiKshvOutfile.close();
            sashimiHumanOutfile.close();
        } catch (Exception e) {
            LoggingUtility.printError("Error in SashimiBuilder#buildSashimiOutputFile(): " + e.getMessage());
        }
    }

    private static String buildSashimiHeaderLine() {
        StringBuilder builder = new StringBuilder();

        builder.append("track name=junctions");

        return builder.toString();
    }

    private static String buildOutputLineSashimi(BsjDataRow row) {
        StringBuilder builder = new StringBuilder();

        builder.append(row.getChromosome());
        builder.append("\t");
        builder.append(row.getJunctionEnd() - block_size);
        builder.append("\t");
        builder.append(row.getJunctionStart() + block_size);
        builder.append("\t");
        builder.append(row.getName());
        builder.append("\t");
        builder.append(row.getBsjCount());
        builder.append("\t");
        builder.append(row.getStrand());
        builder.append("\t");
        builder.append(row.getJunctionEnd() - block_size);
        builder.append("\t");
        builder.append(row.getJunctionStart() + block_size);
        builder.append("\t");
        builder.append("0,0,255");
        builder.append("\t");
        builder.append("2");
        builder.append("\t");
        builder.append("1,1");
        builder.append("\t");
        // final line calculations
        builder.append("0,");
        builder.append(row.getJunctionStart() - row.getJunctionEnd() - block_size);

        return builder.toString();
    }
}
