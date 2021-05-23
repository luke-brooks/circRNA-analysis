import analyzers.BsjAnalyzer;
import config.BsjConfiguration;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting BsjAnalysis!");
        BsjConfiguration config = new BsjConfiguration("_RNA-Seq");
        
        // inputs
        config.setBedFileDirName("_BSJ_bed_files/testfiles");
        config.setRefFileDirName("_Refs");
        config.setSpliceDaOptionsFileName("SpliceDAOptions.txt");
    
        // outputs
        config.setOutputsAnalysisDirName("Outputs/Final_BSJAnalysis");
        config.setBsjSummaryDirName("1.BSJ_Summary");
        config.setBsjNtMatrixDirName("2.BSJ_Nt_Matrix");
        config.setSpliceDaFrequencyDirName("3.SpliceDAFrequency");
        config.setSashimiDirName("4.Sashimi");

        BsjAnalyzer bsjAnalyzer = new BsjAnalyzer(config);
        bsjAnalyzer.execute();

        System.out.println("Completed BsjAnalysis!");
    }
}
