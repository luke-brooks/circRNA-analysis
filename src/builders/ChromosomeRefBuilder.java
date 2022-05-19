package builders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import models.ChromosomeRef;
import utilities.FileUtility;
import utilities.LoggingUtility;

public class ChromosomeRefBuilder {

    public static ArrayList<ChromosomeRef> buildChromosomeRefs(String refFileDirName, String refHostFileDirName, int hostChromosomeLength) {
        ArrayList<ChromosomeRef> allAssemblyRefs = getAllAssemblies(hostChromosomeLength);

        for (ChromosomeRef assemblyRef : allAssemblyRefs) {
            // check chromosome ref for isHost (currently named isHuman)
            String targetRefDir = assemblyRef.getIsHuman() ? refHostFileDirName : refFileDirName ;
            LoggingUtility.printInfo("Pulling ref files from: " + targetRefDir);
            String senseFilePath = targetRefDir + assemblyRef.getSenseFileName();
            String antiSenseFilePath = targetRefDir + assemblyRef.getAntiSenseFileName();

            String senseSequence = FileUtility.readFileToString(senseFilePath, true);
            String antiSenseSequence = FileUtility.readFileToString(antiSenseFilePath, true);

            assemblyRef.setSenseSequence(senseSequence);
            assemblyRef.setAntiSenseSequence(antiSenseSequence);
        }

        return allAssemblyRefs;
    }

    public static ArrayList<ChromosomeRef> getAllAssemblies(int hostChromosomeLength) {
        ArrayList<ChromosomeRef> result = buildHumanAssemblies(hostChromosomeLength);
        result.addAll(buildViralAssemblies());
        return result;
    }

    private static ArrayList<ChromosomeRef> buildHumanAssemblies(int hostChromosomeLength) {
        // build list with values 1 to 22
        // need to make chromosome length configurable
        ArrayList<String> humanChromosomes = IntStream.rangeClosed(1, hostChromosomeLength).mapToObj(String::valueOf).collect(ArrayList::new, List::add, List::addAll);

        humanChromosomes.add("X");
        humanChromosomes.add("Y");

        return buildAssemblyNames(humanChromosomes, true, "chr");
    }

    private static ArrayList<ChromosomeRef> buildViralAssemblies() {
        ArrayList<String> viralChromosomes = new ArrayList<String>();

        viralChromosomes.add("KT899744");
        viralChromosomes.add("NC_001806.2");
        viralChromosomes.add("NC_009333.1");
        viralChromosomes.add("MH636806.1");
        viralChromosomes.add("NC_006273.2");
        viralChromosomes.add("NC_007605.1");// this one is ebv

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
