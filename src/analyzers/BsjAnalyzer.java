package analyzers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import builders.ChromosomeRefBuilder;
import builders.SpliceDaOptionsBuilder;
import config.BsjConfiguration;
import static config.Constants.*;
import models.BedFile;
import models.BsjDataRow;
import models.ChromosomeRef;
import utilities.FileUtility;

public class BsjAnalyzer {
    private BsjConfiguration _config;
    private ArrayList<ChromosomeRef> _chromosomeReferences;
    private ArrayList<String> _spliceOptions;

    public BsjAnalyzer(BsjConfiguration config) {
        this._config = config;
        this._chromosomeReferences = ChromosomeRefBuilder.buildChromosomeRefs(config.getRefFilePath());
        this._spliceOptions = SpliceDaOptionsBuilder.buildSpliceOptions(config.getSpliceDaOptionsFilePath());
    }

    public void execute() {
        ArrayList<BedFile> parsedBedFiles = parseBedFileData();

        // _chromosomeReferences = new ArrayList<>(); // dump data-heavy list

        buildBsjSummaryOutput(parsedBedFiles);
    }

    private void buildBsjSummaryOutput(ArrayList<BedFile> parsedBedFiles) {
        try {
            for (BedFile parsedFile : parsedBedFiles) {
                BufferedWriter bsjSummaryOutfile = FileUtility.createOutFile(_config.getBsjSummaryPath(), parsedFile.getFileName() + BSJ_SUMMARY_FILE_SUFFIX + CSV_EXTENSION);

                bsjSummaryOutfile.write(buildBsjSummaryHeaderLine());
                bsjSummaryOutfile.write("\n");

                for (BsjDataRow parsedRow : parsedFile.getFileBsjData()) {
                    String outputLine = buildOutputLineBsjSummary(parsedRow);
                    bsjSummaryOutfile.write(outputLine);
                    bsjSummaryOutfile.write("\n");
                }

                bsjSummaryOutfile.close();
            }
        } catch (Exception e) {
            System.out.println("Error in BsjAnalyzer#buildBsjSummaryOutput(): " + e.getMessage());
        }
    }

    private String buildBsjSummaryHeaderLine() {
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

    private String buildOutputLineBsjSummary(BsjDataRow row) {
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

    private ArrayList<BedFile> parseBedFileData() {
        ArrayList<BedFile> result = new ArrayList<BedFile>();

        try {
            File bedInputDir = new File(_config.getBedFilePath());

            for (String bedFileName : bedInputDir.list()) {
                if (bedFileName.endsWith(BED_EXTENSION)) {

                    BedFile bedFileData = new BedFile(bedFileName);

                    BufferedReader inputBedFile = new BufferedReader(new FileReader(bedInputDir.getAbsolutePath() + SLASH + bedFileName));
                    for (String line = inputBedFile.readLine(); line != null; line = inputBedFile.readLine()) {
                        String[] dataColumns = line.split("\t");

                        String chromosome = dataColumns[0];
                        int junctionEnd = Integer.parseInt(dataColumns[1]);
                        int junctionStart = Integer.parseInt(dataColumns[2]);
                        String name = dataColumns[3];
                        String bsjCount = dataColumns[4];
                        String strand = dataColumns[5];

                        BsjDataRow row = new BsjDataRow(chromosome, junctionStart, junctionEnd, name, bsjCount, strand);

                        boolean success = calculateBsjSummaryValues(row);

                        if(success) {
                            bedFileData.addBsjDataRow(row);
                        }
                    }
                    result.add(bedFileData);
                    inputBedFile.close();
                } else {
                    System.out.println("Not .bed file type: " + bedFileName);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in BsjAnalysis#parseBedFileData(): " + e.getMessage());
        }
        return result;
    }

    private boolean calculateBsjSummaryValues(BsjDataRow row) {
        String refFileData = getReferenceFileData(row.getChromosome(), row.getStrand());

        if("".equals(refFileData)) {
            // no chromosome ref available
            return false;
        }

        // there's gotta be a way to simplify these...
        row.setBsjFlankingSequence(getBsjFlankingSequence(row.getJunctionStart(), row.getJunctionEnd(), refFileData));
        row.setSpliceDa(getSpliceDaSequence(row.getJunctionStart(), row.getJunctionEnd(), refFileData));

        return true;
    }

    private String getSpliceDaSequence(int junctionStart, int junctionEnd, String refFileData) {
        String result = "";

        int lowB = junctionStart;
        int highB = lowB + 2;
        String fastaB = getFasta(refFileData, lowB, highB);

        int highA = junctionEnd;
        int lowA = highA - 2;
        String fastaA = getFasta(refFileData, lowA, highA);

        result = fastaB + fastaA;

        return result;
    }

    private String getBsjFlankingSequence(int junctionStart, int junctionEnd, String refFileData) {
        String result = "";

        int highB = junctionStart;
        int lowB = highB - 30;
        String fastaB = getFasta(refFileData, lowB, highB);

        int lowA = junctionEnd;
        int highA = lowA + 30;
        String fastaA = getFasta(refFileData, lowA, highA);

        result = fastaB + fastaA;

        return result;
    }

    private String getReferenceFileData(String chromosomeName, String strandType) {
        String result = "";

        ChromosomeRef targetRef = _chromosomeReferences.stream()
                .filter(ref -> ref.getSenseName().toLowerCase().equals(chromosomeName.toLowerCase())).findAny()
                .orElse(null);

        if (targetRef == null) {
            System.out.println("Error: No Reference File for Chromosome: " + chromosomeName);
        } else {
            if (strandType.equals("+")) {
                result = targetRef.getSenseSequence();
            } else if (strandType.equals("-")) {
                result = targetRef.getAntiSenseSequence();
            } else {
                System.out.println("Error: Unrecognized Strand Type: " + strandType);
            }
        }

        return result;
    }

    private String getFasta(String sourceString, int startIndex, int endIndex) {
        if (startIndex > endIndex) {
            int temp = startIndex;
            startIndex = endIndex;
            endIndex = temp;
        }
        String result = "";
        int totalLength = sourceString.length();
        // prevent endIndex from being greater than sourceString.length()
        endIndex = totalLength < endIndex ? totalLength : endIndex;

        result = sourceString.substring(startIndex, endIndex);

        return result;
    }
}
