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
            BufferedWriter sashimiMhv68Outfile = FileUtility.createOutFile(sashimiOutputPath, processedBedFile.getFileName() + "_mhv68_" + SASHIMI_FILE_SUFFIX + BED_EXTENSION);
            BufferedWriter sashimiCmvOutfile = FileUtility.createOutFile(sashimiOutputPath, processedBedFile.getFileName() + "_cmv_" + SASHIMI_FILE_SUFFIX + BED_EXTENSION);
            BufferedWriter sashimiEbvOutfile = FileUtility.createOutFile(sashimiOutputPath, processedBedFile.getFileName() + "_ebv_" + SASHIMI_FILE_SUFFIX + BED_EXTENSION);
            BufferedWriter sashimiHumanOutfile = FileUtility.createOutFile(sashimiOutputPath, processedBedFile.getFileName() + "_human_" + SASHIMI_FILE_SUFFIX + BED_EXTENSION);

            sashimiHsvOutfile.write(buildSashimiHeaderLine());
            sashimiHsvOutfile.write("\n");

            sashimiKshvOutfile.write(buildSashimiHeaderLine());
            sashimiKshvOutfile.write("\n");

            sashimiMhv68Outfile.write(buildSashimiHeaderLine());
            sashimiMhv68Outfile.write("\n");

            sashimiCmvOutfile.write(buildSashimiHeaderLine());
            sashimiCmvOutfile.write("\n");

            sashimiEbvOutfile.write(buildSashimiHeaderLine());
            sashimiEbvOutfile.write("\n");

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
                    else if (processedRow.getChromosome().equals("MH636806.1")) {
                        LoggingUtility.printInfo("buildSashimiOutputFile(): outputLine in mhv68 clause: " + outputLine);
                        sashimiMhv68Outfile.write(outputLine);
                        sashimiMhv68Outfile.write("\n");
                    }
                    else if (processedRow.getChromosome().equals("NC_006273.2")) {
                        LoggingUtility.printInfo("buildSashimiOutputFile(): outputLine in cmv clause: " + outputLine);
                        sashimiCmvOutfile.write(outputLine);
                        sashimiCmvOutfile.write("\n");
                    }
                    else if (processedRow.getChromosome().equals("NC_007605.1")) {
                        LoggingUtility.printInfo("buildSashimiOutputFile(): outputLine in ebv clause: " + outputLine);
                        sashimiEbvOutfile.write(outputLine);
                        sashimiEbvOutfile.write("\n");
                    }
                    else {
                        sashimiHumanOutfile.write(outputLine);
                        sashimiHumanOutfile.write("\n");
                    }
                }
            }

            sashimiHsvOutfile.close();
            sashimiKshvOutfile.close();
            sashimiMhv68Outfile.close();
            sashimiCmvOutfile.close();
            sashimiEbvOutfile.close();
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
