package builders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import models.ChromosomeRef;
import utilities.FileUtility;

public class ChromosomeRefBuilder {

    public static ArrayList<ChromosomeRef> buildChromosomeRefs(String refFileDirName) {
        ArrayList<ChromosomeRef> allAssemblyRefs = getAllAssemblies();

        for (ChromosomeRef assemblyRef : allAssemblyRefs) {
            String senseFilePath = refFileDirName + assemblyRef.getSenseFileName();
            String antiSenseFilePath = refFileDirName + assemblyRef.getAntiSenseFileName();

            String senseSequence = FileUtility.readFileToString(senseFilePath, true);
            String antiSenseSequence = FileUtility.readFileToString(antiSenseFilePath, true);

            assemblyRef.setSenseSequence(senseSequence);
            assemblyRef.setAntiSenseSequence(antiSenseSequence);
        }

        return allAssemblyRefs;
    }

    private static ArrayList<ChromosomeRef> getAllAssemblies() {
        ArrayList<ChromosomeRef> result = buildHumanAssemblies();
        result.addAll(buildViralAssemblies());
        return result;
    }

    private static ArrayList<ChromosomeRef> buildHumanAssemblies() {
        // build list with values 1 to 22
        ArrayList<String> humanChromosomes = IntStream.rangeClosed(1, 22).mapToObj(String::valueOf).collect(ArrayList::new, List::add, List::addAll);

        humanChromosomes.add("X");
        humanChromosomes.add("Y");

        return buildAssemblyNames(humanChromosomes, true, "chr");
    }

    private static ArrayList<ChromosomeRef> buildViralAssemblies() {
        ArrayList<String> viralChromosomes = new ArrayList<String>();

        viralChromosomes.add("KT899744");
        viralChromosomes.add("NC_001806.2");
        viralChromosomes.add("NC_009333.1");

        return buildAssemblyNames(viralChromosomes, false, "");
    }

    private static ArrayList<ChromosomeRef> buildAssemblyNames(ArrayList<String> names, boolean isHuman, String prefix) {
        ArrayList<ChromosomeRef> result = new ArrayList<>();

        for (String name : names) {
            String senseName = prefix + name;
            ChromosomeRef chrRef = new ChromosomeRef(senseName, isHuman);
            result.add(chrRef);
        }

        return result;
    }
}
