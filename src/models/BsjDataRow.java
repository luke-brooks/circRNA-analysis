package models;

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

    public BsjDataRow(String chromosome, int junctionStart, int junctionEnd, String name, int bsjCount, String strand) {
        this.chromosome = chromosome;
        this.junctionStart = junctionStart;
        this.junctionEnd = junctionEnd;
        this.name = name;
        this.bsjCount = bsjCount;
        this.strand = strand;
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
}
