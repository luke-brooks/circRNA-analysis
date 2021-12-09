package models;

import java.io.BufferedWriter;
import java.util.ArrayList;

public class SpliceDaOutput {
    ChromosomeRef chromosomeRef;
    BufferedWriter outfile;
    ArrayList<String> spliceCounts;

    private int spliceListLength;

    public SpliceDaOutput(ChromosomeRef chromosomeRef, BufferedWriter outfile, int spliceListLength) {
        this.chromosomeRef = chromosomeRef;
        this.outfile = outfile;
        this.spliceListLength = spliceListLength;
    }

    public ChromosomeRef getChromosomeRef() {
        return chromosomeRef;
    }
    public String getChromosomeName() {
        return chromosomeRef.getSenseName();
    }
    public BufferedWriter getOutfile() {
        return outfile;
    }
    public ArrayList<String> getSpliceCountsList() {
        return spliceCounts;
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
