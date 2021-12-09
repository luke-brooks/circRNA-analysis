package config;

import static config.Constants.SLASH;
import static config.Constants.DIR_REFERENCE;;

public class BsjConfiguration {
    // top-level directory
    String rnaSeqDirPath;

    // input directories
    String refFileDirName;
    String bedFileDirName;

    // reference files
    String spliceDaOptionsFileName;

    // output directories
    String outputsAnalysisDirName;
    String bsjSummaryDirName;
    String bsjNtMatrixDirName;
    String spliceDaFrequencyDirName;
    String sashimiDirName;
    String spliceDaSequenceDirName;

    // Sashimi config values
    int sashimiThreshold;

    public BsjConfiguration(String rnaSeqDirPath) {
        this.rnaSeqDirPath = DIR_REFERENCE + formatDirPath(rnaSeqDirPath);
    }

    // input directory setters
    public void setRefFileDirName(String refFileDirName) {
        this.refFileDirName = formatDirPath(refFileDirName);
    }
    public void setBedFileDirName(String bedFileDirName) {
        this.bedFileDirName = formatDirPath(bedFileDirName);
    }
    // reference files
    public void setSpliceDaOptionsFileName(String spliceDaOptionsFileName) {
        this.spliceDaOptionsFileName = spliceDaOptionsFileName;
    }
    // output directory setters
    public void setOutputsAnalysisDirName(String outputsAnalysisDirName) {
        this.outputsAnalysisDirName = formatDirPath(outputsAnalysisDirName);
    }
    public void setBsjSummaryDirName(String bsjSummaryDirName) {
        this.bsjSummaryDirName = formatDirPath(bsjSummaryDirName);
    }
    public void setBsjNtMatrixDirName(String bsjNtMatrixDirName) {
        this.bsjNtMatrixDirName = formatDirPath(bsjNtMatrixDirName);
    }
    public void setSpliceDaFrequencyDirName(String spliceDaFrequencyDirName) {
        this.spliceDaFrequencyDirName = formatDirPath(spliceDaFrequencyDirName);
    }
    public void setSashimiDirName(String sashimiDirName) {
        this.sashimiDirName = formatDirPath(sashimiDirName);
    }
    public void setSpliceDaSequenceDirName(String spliceDaSequenceDirName) {
        this.spliceDaSequenceDirName = formatDirPath(spliceDaSequenceDirName);
    }
    // sashimi config values
    public void setSashimiThreshold(int sashimiThreshold) {
        this.sashimiThreshold = sashimiThreshold;
    }

    // top-level directory getter
    public String getRnaSeqDirPath() {
        return formatDirPath(rnaSeqDirPath);
    }
    // input directory getters
    public String getRefFilePath() {
        return formatDirPath(rnaSeqDirPath + refFileDirName);
    }
    public String getBedFilePath() {
        return formatDirPath(rnaSeqDirPath + bedFileDirName);
    }
    // reference files
    public String getSpliceDaOptionsFilePath() {
        return formatDirPath(getRefFilePath()) + spliceDaOptionsFileName;
    }
    // output directory getters
    public String getOutputsAnalysisPath() {
        return formatDirPath(rnaSeqDirPath + outputsAnalysisDirName);
    }
    public String getBsjSummaryPath() {
        return formatDirPath(getOutputsAnalysisPath() + bsjSummaryDirName);
    }
    public String getBsjNtMatrixPath() {
        return formatDirPath(getOutputsAnalysisPath() + bsjNtMatrixDirName);
    }
    public String getSpliceDaFrequencyPath() {
        return formatDirPath(getOutputsAnalysisPath() + spliceDaFrequencyDirName);
    }
    public String getSashimiPath() {
        return formatDirPath(getOutputsAnalysisPath() + sashimiDirName);
    }
    public String getSpliceDaSequencePath() {
        return formatDirPath(getOutputsAnalysisPath() + spliceDaSequenceDirName);
    }
    // sashimi config values
    public int getSashimiThreshold() {
        return sashimiThreshold;
    }

    private String formatDirPath(String dir) {
        if (dir.substring(dir.length() - 1).equals(SLASH)) {
            return dir;
        }
        else {
            return dir + SLASH;
        }
    }
}
