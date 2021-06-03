package analyzers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import builders.BsjNtMatrixBuilder;
import builders.BsjSpliceDaFrequencyBuilder;
import builders.BsjSummaryBuilder;
import builders.ChromosomeRefBuilder;
import builders.SpliceDaOptionsBuilder;
import config.BsjConfiguration;
import static config.Constants.BED_EXTENSION;
import static config.Constants.SLASH;
import models.BedFile;
import models.BsjDataRow;
import models.ChromosomeRef;
import utilities.LoggingUtility;
import utilities.StringUtility;

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
        ArrayList<BedFile> processedBedFiles = processBedFileData();

        _chromosomeReferences = new ArrayList<>(); // dump data-heavy list

        compileOutputs(processedBedFiles);
    }
    
    private void compileOutputs(ArrayList<BedFile> processedBedFiles) {
        LoggingUtility.printInfo("Building BSJ Summary Output");
        LoggingUtility.printInfo("Building NT Matrix Output");

        for (BedFile processedFile : processedBedFiles) {

            BsjSummaryBuilder.buildBsjSummaryOutputFile(processedFile, _config.getBsjSummaryPath());
            BsjNtMatrixBuilder.buildBsjNtMatrixOutputFile(processedFile, _config.getBsjNtMatrixPath());
        }

        LoggingUtility.printInfo("Building Splice DA Summary Output");
        BsjSpliceDaFrequencyBuilder.buildBsjSpliceDaFrequencyOutputFile(processedBedFiles, _config.getSpliceDaFrequencyPath(), _spliceOptions);
    }

    private ArrayList<BedFile> processBedFileData() {
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
                        int bsjCount = Integer.parseInt(dataColumns[4]);
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
                    LoggingUtility.printWarning("Not .bed file type: " + bedFileName);
                }
            }
        } catch (Exception e) {
            LoggingUtility.printError("Error in BsjAnalysis#processBedFileData(): " + e.getMessage());
        }
        return result;
    }

    private boolean calculateBsjSummaryValues(BsjDataRow row) {
        ChromosomeRef refFile = getReferenceFile(row.getChromosome());
            
        if (refFile == null) {
            LoggingUtility.printWarning("No Reference File for Chromosome: " + row.getChromosome());
            // no chromosome ref available
            return false;
        }

        String refFileData = refFile.getSequence(row.getIsSense());

        // there's gotta be a way to simplify these...
        row.setBsjFlankingSequence(getBsjFlankingSequence(row.getJunctionStart(), row.getJunctionEnd(), row.getIsSense(), refFileData));
        row.setSpliceDa(getSpliceDaSequence(row.getJunctionStart(), row.getJunctionEnd(), row.getIsSense(), refFileData));

        // set isHuman after refFile match
        row.setIsHuman(refFile.getIsHuman());

        return true;
    }

    private String getSpliceDaSequence(int junctionStart, int junctionEnd, boolean isSense, String refFileData) {
        String result = "";

        int lowB = junctionStart;
        int highB = lowB + 2;
        String fastaB = getFasta(refFileData, lowB, highB);

        int highA = junctionEnd;
        int lowA = highA - 2;
        String fastaA = getFasta(refFileData, lowA, highA);

        result = fastaB + fastaA;

        if (!isSense) {
            result = StringUtility.reverseString(result);
        }

        return result;
    }

    private String getBsjFlankingSequence(int junctionStart, int junctionEnd, boolean isSense, String refFileData) {
        String result = "";

        int highB = junctionStart;
        int lowB = highB - 30;
        String fastaB = getFasta(refFileData, lowB, highB);

        int lowA = junctionEnd;
        int highA = lowA + 30;
        String fastaA = getFasta(refFileData, lowA, highA);

        result = fastaB + fastaA;

        if (!isSense) {
            result = StringUtility.reverseString(result);
        }

        return result;
    }

    private ChromosomeRef getReferenceFile(String chromosomeName) {
        ChromosomeRef targetRef = _chromosomeReferences.stream()
                .filter(ref -> ref.getSenseName().toLowerCase().equals(chromosomeName.toLowerCase()))
                .findAny()
                .orElse(null);

        return targetRef;
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
