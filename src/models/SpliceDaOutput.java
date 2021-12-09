package models;

import java.io.BufferedWriter;
import java.util.ArrayList;

public class SpliceDaOutput {
    ChromosomeRef chromosomeRef;
    BufferedWriter outfile;
    ArrayList<String> spliceCounts;

    private int spliceListLength;
    private boolean isHumanAgg; // aggregate all human chromosomes into one output file

    public SpliceDaOutput(ChromosomeRef chromosomeRef, BufferedWriter outfile, int spliceListLength) {
        this.chromosomeRef = chromosomeRef;
        this.isHumanAgg = chromosomeRef == null;
        this.outfile = outfile;
        this.spliceListLength = spliceListLength;
    }

    public ChromosomeRef getChromosomeRef() {
        return chromosomeRef;
    }
    public String getChromosomeName() {
        if (isHumanAgg) {
            return "Human";
        } else {
            return chromosomeRef.getSenseName();
        }
    }
    public BufferedWriter getOutfile() {
        return outfile;
    }
    public ArrayList<String> getSpliceCountsList() {
        return spliceCounts;
    }
    public boolean getIsHumanAgg() {
        return isHumanAgg;
    }

    public void refreshSpliceList() {
        spliceCounts = buildFreshSpliceCountList(spliceListLength);
    }

    private ArrayList<String> buildFreshSpliceCountList(int spliceListLength) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < spliceListLength; i++) {
            result.add("0");
        }
        return result;
    }
}
