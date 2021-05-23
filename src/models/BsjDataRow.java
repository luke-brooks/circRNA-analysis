package models;

import utilities.LoggingUtility;

public class BsjDataRow {
    // Raw Bed File Data
    private String chromosome;
    private int junctionStart;
    private int junctionEnd;
    private String name;
    private int bsjCount;
    private String strand; // either "+" or "-"

    // Calculated values
    private String bsjFlankingSequence;
    private String spliceDa; // always 4 characters
    private boolean isSense;

    public BsjDataRow(String chromosome, int junctionStart, int junctionEnd, String name, int bsjCount, String strand) throws Exception {
        this.chromosome = chromosome;
        this.junctionStart = junctionStart;
        this.junctionEnd = junctionEnd;
        this.name = name;
        this.bsjCount = bsjCount;
        this.strand = strand;
        this.isSense = calculateIsSense(strand);
    }

    // Raw Bed File Data
    public String getChromosome() {
        return chromosome;
    }
    public int getJunctionStart() {
        return junctionStart;
    }
    public int getJunctionEnd() {
        return junctionEnd;
    }
    public String getName() {
        return name;
    }
    public int getBsjCount() {
        return bsjCount;
    }
    public String getStrand() {
        return strand;
    }

    // Calculated values
    public boolean getIsSense() {
        return isSense;
    }
    public String getBsjFlankingSequence() {
        return bsjFlankingSequence;
    }
    public String getSpliceDa() {
        return spliceDa;
    }
    public void setBsjFlankingSequence(String bsjFlankingSequence) {
        this.bsjFlankingSequence = bsjFlankingSequence;
    }
    public void setSpliceDa(String spliceDa)  {
        this.spliceDa = spliceDa;
    }

    private boolean calculateIsSense(String strandType) throws Exception {
        if (strandType.equals("+")) {
            return true;
        } else if (strandType.equals("-")) {
            return false;
        } else {
            LoggingUtility.printError("Unrecognized Strand Type: " + strandType);
            throw new Exception("Unrecognized Strand Type: " + strandType);
        }
    }
}
