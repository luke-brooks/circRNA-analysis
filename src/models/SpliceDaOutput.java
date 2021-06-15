package models;

import java.io.BufferedWriter;
import java.util.ArrayList;

public class SpliceDaOutput {
    ChromosomeRef chromosomeRef;
    BufferedWriter outfile;
    ArrayList<String> spliceCounts;

    public SpliceDaOutput(ChromosomeRef chromosomeRef, BufferedWriter outfile, ArrayList<String> spliceCounts) {
        this.chromosomeRef = chromosomeRef;
        this.outfile = outfile;
        this.spliceCounts = spliceCounts;
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
}
