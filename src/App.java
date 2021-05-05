import analyzers.BsjAnalysis;
import models.BsjDataRow;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting bsj app!");

        boolean isHSV1 = false;
        boolean isKT = false;
        // String bedFileDir = "BSJ_bed_files-S15-KOS/";
        String bedFileDir = "testfiles/";
        String logDirectory = "S18";

        BsjDataRow test = new BsjDataRow("", "", "", "", "", "");

        BsjAnalysis.execute(bedFileDir, isHSV1, isKT);

        // AggregateReadCounts.execute(logDirectory);
        // Input: STAR2p log final file (logDirectory)
        // Output: breakdown of read mapping in txt file

        // BsjHistogramAggregateTemplate.execute(bedFileDir, isHSV1, isKT);
        // Input: CircExplorer BSJ junction files (bedFileDir)
        // Output: Aggregated per bp count of BSJ reads

        // BsjStartEnd_aggregate.execute();
        // Input: CircExplorer BSJ junction files (bedFileDir)
        // Output: Separate Bedgraph files for 5' and 3' BSJ counts per bp

        // BSJtoNtHeatmaps.execute(bedFileDir, isHSV1, isKT);
        // Input: CircExplorer BSJ junction files (bedFileDir)
        // Ouput: Txt matrix of nt sequence 30 nt upstream of downstream for all viral
        // circRNA

        // BsjToSashimi.execute(bedFileDir);
        // Input: CircExplorer BSJ junction files (bedFileDir)
        // Output: sashimi files

        // BSJtoSpliceDA.execute(bedFileDir, isHSV1, isKT);

        System.out.println("Completed run of bsj app!");
    }
}
