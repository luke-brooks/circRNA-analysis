package builders;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static config.Constants.CSV_EXTENSION;
import models.BedFile;
import models.BsjDataRow;
import models.ChromosomeRef;
import models.SpliceDaOutput;
import utilities.FileUtility;
import utilities.LoggingUtility;
import utilities.StringUtility;

public class BsjSpliceDaFrequencyBuilder {

    public static void buildBsjSpliceDaFrequencyOutput(ArrayList<BedFile> processedBedFiles,
            String bsjSpliceDaFrequencyOutputPath, ArrayList<ChromosomeRef> _chromosomeRefs,
            ArrayList<String> _spliceOptions) {
        try {

            ArrayList<SpliceDaOutput> spliceDaOutputFiles = getOutputFiles(bsjSpliceDaFrequencyOutputPath,
                    _chromosomeRefs, _spliceOptions);

            for (BedFile processedFile : processedBedFiles) {
                // refresh splice all counts when processing a new file
                refreshAllSpliceCountLists(spliceDaOutputFiles);

                // calculate each chromosome count for every bed file
                for (ChromosomeRef chromosomeRef : _chromosomeRefs) {

                    List<BsjDataRow> bsjRowsByChromosome = processedFile.getFileBsjData()
                            .stream()
                            .filter(bsjRow -> bsjRow.getChromosome().toLowerCase()
                                    .equals(chromosomeRef.getSenseName().toLowerCase()))
                            .collect(Collectors.toList());

                    String targetRef = chromosomeRef.getIsHuman() ? "Human" : chromosomeRef.getSenseName();

                    SpliceDaOutput targetDaOutput = spliceDaOutputFiles
                            .stream()
                            .filter(output -> output.getChromosomeName().toLowerCase()
                                    .equals(targetRef.toLowerCase()))
                            .findAny()
                            .orElse(null);

                    if (targetDaOutput == null) {
                        throw new Exception("Could not match Chromosome: " + chromosomeRef.getSenseName());
                    }

                    // refresh splice counts for each line in human aggregate file
                    if (chromosomeRef.getIsHuman()) {
                        targetDaOutput.refreshSpliceList();
                    }

                    for (BsjDataRow row : bsjRowsByChromosome) {

                        String sequenceToMatch = row.getSpliceDa();
                        int sequenceNum = row.getBsjCount();

                        // get index of matched 4 character splice DA sequence
                        int targetIndex = _spliceOptions.indexOf(sequenceToMatch.toUpperCase());

                        // get & update count for matched splice DA sequence
                        String targetItem = targetDaOutput.getSpliceCountsList().get(targetIndex);
                        int updatedCount = Integer.parseInt(targetItem) + sequenceNum;

                        targetDaOutput.getSpliceCountsList().set(targetIndex, String.valueOf(updatedCount));
                    }

                    String dataLine = buildSpliceTotalLine(processedFile.getFileName(),
                            targetDaOutput.getSpliceCountsList());
                    targetDaOutput.getOutfile().write(dataLine);
                }
            }

            closeAllOutputFiles(spliceDaOutputFiles);
        } catch (Exception e) {
            LoggingUtility.printError(
                    "Error in BsjSpliceDaFrequencyBuilder#buildBsjSpliceDaFrequencyOutput(): " + e.getMessage());
        }
    }

    private static String getSpliceTotal(ArrayList<String> list) {
        int total = 0;
        for (String value : list) {
            total += Integer.parseInt(value);
        }
        return String.valueOf(total);
    }

    private static String buildSpliceTotalLine(String sampleName, ArrayList<String> spliceCounts) {
        String spliceTotal = getSpliceTotal(spliceCounts);

        ArrayList<String> spliceData = new ArrayList<String>();
        spliceData.add(sampleName);
        spliceData.addAll(spliceCounts);
        spliceData.add(spliceTotal);

        return StringUtility.buildCsvLine(spliceData, ",");
    }

    private static void closeAllOutputFiles(ArrayList<SpliceDaOutput> spliceDaOutputFiles) throws IOException {
        for (SpliceDaOutput output : spliceDaOutputFiles) {
            output.getOutfile().close();
        }
    }

    private static void refreshAllSpliceCountLists(ArrayList<SpliceDaOutput> spliceDaOutputFiles) throws IOException {
        for (SpliceDaOutput spliceDaOutput : spliceDaOutputFiles) {
            spliceDaOutput.refreshSpliceList();
        }
    }

    private static ArrayList<SpliceDaOutput> getOutputFiles(String bsjSpliceDaFrequencyOutputPath,
            ArrayList<ChromosomeRef> _chromosomeRefs, ArrayList<String> _spliceOptions) throws IOException {
        ArrayList<SpliceDaOutput> result = new ArrayList<SpliceDaOutput>();

        for (ChromosomeRef ref : _chromosomeRefs) {
            // only create separate files for non-human chromosomes
            if (!ref.getIsHuman()) {
                SpliceDaOutput daOutput = getSpliceDaOutputFile(bsjSpliceDaFrequencyOutputPath, ref.getSenseName(), ref,
                        _spliceOptions);
                result.add(daOutput);
            }
        }

        // one file for all human chromosomes
        SpliceDaOutput humanOutput = getSpliceDaOutputFile(bsjSpliceDaFrequencyOutputPath, "Human", null,
                _spliceOptions);
        result.add(humanOutput);

        return result;
    }

    private static SpliceDaOutput getSpliceDaOutputFile(String bsjSpliceDaFrequencyOutputPath, String fileName,
            ChromosomeRef ref,
            ArrayList<String> _spliceOptions) throws IOException {

        BufferedWriter daOutfile = FileUtility.createOutFile(bsjSpliceDaFrequencyOutputPath,
                buildOutputFileName(fileName));

        daOutfile.write(buildOutputHeader(_spliceOptions));

        SpliceDaOutput result = new SpliceDaOutput(ref, daOutfile, _spliceOptions.size());

        return result;
    }

    private static String buildOutputFileName(String chromosomeName) {
        String result = "";

        result = chromosomeName + "_" + "SpliceDASummary" + CSV_EXTENSION;

        return result;
    }

    private static String buildOutputHeader(ArrayList<String> _spliceOptions) {
        ArrayList<String> headersList = new ArrayList<String>();
        headersList.add("Splice D-A");
        headersList.addAll(_spliceOptions);
        headersList.add("Summary");

        String header = StringUtility.buildCsvLine(headersList, ",");

        return header;
    }
}
