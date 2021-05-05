package analyzers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import models.BedFile;
import models.BsjDataRow;

public class BsjAnalysis {

	public static void execute(String bedFileInputDir, boolean isHSV1, boolean isKT) throws IOException {
		String chromosomeName = "NC_009333.1";

		// Output locations
		String outDir = "./Outputs/" + "BSJtoNtHeatmaps/";
		// String bedABOutDir = outDir + "Bed_A_B/";
		String bsjSeqOutDir = outDir + "ConcatBSJSeq/";
		String heatmapOutDir = outDir + "NtHeatmapOutput/";
		
		if(isHSV1) {
			chromosomeName = isKT ? "KT899744" : "NC_001806.2";
		}
        System.out.println("LTB: hi mom");
		// all .bed files will use the same refFile
		// String refFilePath = "./_Refs/" + chromosomeName + ".txt";
		// String refFileData = readFileToString(refFilePath);

        ArrayList<BedFile> parsedBedFiles = parseBedFileldata(bedFileInputDir);
        System.out.println("LTB: parsed file count " + parsedBedFiles.size());

		// BsjToHeatmap.execute(bsjSeqOutDir, heatmapOutDir);
	}

    public static ArrayList<BedFile> parseBedFileldata(String bedFileInputDir) {
        ArrayList<BedFile> result = new ArrayList<BedFile>();

        try {
            // need to move this config to somewhere more usable
            String test = "./_RNA-Seq/_BSJ_bed_files/" + bedFileInputDir;
            System.out.println("LTB: pulling files from: " + test);
			File bedInputDir = new File(test);
			String[] bedFileList = bedInputDir.list();
			for(String bedFileName:bedFileList) {
                System.out.println("file name: " + bedFileName);
				if(bedFileName.endsWith(".bed")) {
                    System.out.println("LTB: starting bed file processing");

                    BedFile bedFileData = new BedFile(bedFileName);
					
					BufferedReader inputBedFile = new BufferedReader(new FileReader(bedInputDir.getAbsolutePath()+"/"+bedFileName));
					for (String line = inputBedFile.readLine(); line != null; line = inputBedFile.readLine()) {
						String[] dataColumns = line.split("\t");

                        String chromosome = dataColumns[0];
                        String junctionEnd = dataColumns[1];
                        String junctionStart = dataColumns[2];
                        String name = dataColumns[3];
                        String bsjCount = dataColumns[4];
                        String strand = dataColumns[5];

                        BsjDataRow row = new BsjDataRow(chromosome, junctionStart, junctionEnd, name, bsjCount, strand);

                        bedFileData.addBsjDataRow(row);
					}
                    System.out.println("LTB: data row count for " + bedFileName + " is " + bedFileData.getFileBsjData().size());
                    result.add(bedFileData);
					inputBedFile.close();
				} else {
					System.out.println("wrong file type: " + bedFileName);
				}
			}
        } catch (Exception e) {
			System.out.println("Error in BsjAnalysis#parseBedFileldata(): " + e.getMessage());
		}
        return result;
    }

	public static void buildBsjSequence(String bedFileInputDir, String bsjSeqOutDir, String chromosomeName, String refFileData) {
		try {
            String test = "./_RNA-Seq/_BSJ_bed_files/" + bedFileInputDir;
            System.out.println("LTB: pulling files from: " + test);
			File bedInputDir = new File(test);
			String[] bedFileList = bedInputDir.list();
			for(String bedFileName:bedFileList) {
                System.out.println("file name: " + bedFileName);
				if(bedFileName.endsWith(".bed")) {
                    System.out.println("LTB: starting bed file processing");

                    BedFile bedFileData = new BedFile(bedFileName);
					// BufferedWriter bsjSeqOutfile = createOutFile(bsjSeqOutDir, bedFileName + "_bsj_seq.txt");
					// System.out.println("outfile create");
					
					BufferedReader inputBedFile = new BufferedReader(new FileReader(bedInputDir.getAbsolutePath()+"/"+bedFileName));
					for (String line = inputBedFile.readLine(); line != null; line = inputBedFile.readLine()) {
					// 	// remove tabs from data
						String[] dataColumns = line.split("\t");

                        String chromosome = dataColumns[0];
                        String junctionEnd = dataColumns[1];
                        String junctionStart = dataColumns[2];
                        String name = dataColumns[3];
                        String bsjCount = dataColumns[4];
                        String strand = dataColumns[5];

                        BsjDataRow row = new BsjDataRow(chromosome, junctionStart, junctionEnd, name, bsjCount, strand);

                        bedFileData.addBsjDataRow(row);

					// 	// only process lines with desired chromosome name
					// 	if(line.startsWith(chromosomeName)) {
					// 		// calculate b values
					// 		int highB = Integer.parseInt(dataColumns[2]);
					// 		int lowB = highB - 30;
					// 		String fastaB = getFasta(refFileData, lowB, highB);
					// 		// System.out.println("fasta b: " + fastaB);

					// 		// calculate a values
					// 		int lowA = Integer.parseInt(dataColumns[1]);
					// 		int highA = lowA + 30;
					// 		String fastaA = getFasta(refFileData, lowA, highA);
					// 		// System.out.println("fasta a: " + fastaA);
							
					// 		// save data
					// 		String outputLine = buildOutputLine(dataColumns, fastaB, fastaA);
					// 		bsjSeqOutfile.write(outputLine);
					// 	} else {
					// 		// System.out.println("wrong chromosome name: " + line);
					// 	}
					}
                    System.out.println("LTB: data row count for " + bedFileName + " is " + bedFileData.getFileBsjData().size());
					inputBedFile.close();

					// bsjSeqOutfile.close();
				} else {
					System.out.println("wrong file type: " + bedFileName);
				}
			}
		} catch (Exception e) {
			// System.out.println("Error in BSJtoNtHeatmaps#buildBsjSequence(): " + e.getMessage());
		}
	}

	public static String getFasta(String sourceString, int startIndex, int endIndex) {
		// STRING LENGTH IS ZERO BASED
		String result = "";
		int totalLength = sourceString.length();
		// prevent endIndex from being greater than sourceString.length()
		endIndex = totalLength < endIndex ? totalLength : endIndex;

		result = sourceString.substring(startIndex, endIndex);

		return result;
	}

	public static String readFileToString(String file) {
		StringBuilder  stringBuilder = new StringBuilder();
		boolean isFirstLine = true;
		try {
			BufferedReader fileToRead = new BufferedReader(new FileReader(file));
			for (String line = fileToRead.readLine(); line != null; line = fileToRead.readLine()) {
				if (!isFirstLine) {
					// // System.out.println("Line length: " + line.length());
					stringBuilder.append(line);
				} else {
					// // System.out.println("Skipping: " + line);
					isFirstLine = false;
				}
			}
			fileToRead.close();
		} catch (Exception e) {
			// System.out.println("Error in BSJtoNtHeatmaps#readFileToString(): file - " + file + "Error msg: " + e.getMessage());
		}
		return stringBuilder.toString();
	}

	public static String buildOutputLine(String[] dataColumns, String fastaB, String fastaA) {
		String result = "";

		result = result + dataColumns[0] + "\t";
		result = result + dataColumns[1] + "\t";
		result = result + dataColumns[2] + "\t";
		result = result + dataColumns[3] + "\t";
		result = result + fastaB + "\t";
		result = result + fastaA + "\t";

		result = result + "\n";

		return result;
	}

	public static BufferedWriter createOutFile(String outDir, String outFileName) {
		BufferedWriter result = null;
		try {
			FileWriter initFile = new FileWriter(new File(outDir + outFileName));
			result = new BufferedWriter(initFile);
			result.write("\n");
		} catch(Exception e) {
			// System.out.println("Error in BSJtoNtHeatmaps#createOutFile()" + e.getMessage());
		}
		return result;
	}
}
