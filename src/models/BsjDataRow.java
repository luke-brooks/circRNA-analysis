package models;

public class BsjDataRow {
    private String chromosome;
    private String junctionStart;
    private String junctionEnd;
    private String name;
    private String bsjCount;
    private String strand;

    public BsjDataRow(String chromosome, String junctionStart, String junctionEnd, String name, String bsjCount, String strand) {
        this.chromosome = chromosome;
        this.junctionStart = junctionStart;
        this.junctionEnd = junctionEnd;
        this.name = name;
        this.bsjCount = bsjCount;
        this.strand = strand;
    }

    public String getChromosome() {
        return chromosome;
    }

    public String getJunctionStart() {
        return junctionStart;
    }

    public String getJunctionEnd() {
        return junctionEnd;
    }

    public String getName() {
        return name;
    }

    public String getBsjCount() {
        return bsjCount;
    }

    public String getStrand() {
        return strand;
    }
}
