package deprecated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BSJtoNtHeatmaps {

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

		// all .bed files will use the same refFile
		String refFilePath = "./_Refs/" + chromosomeName + ".txt";
		String refFileData = readFileToString(refFilePath);

		buildBsjSequence(bedFileInputDir, bsjSeqOutDir, chromosomeName, refFileData);

		BsjToHeatmap.execute(bsjSeqOutDir, heatmapOutDir);
	}

	public static void buildBsjSequence(String bedFileInputDir, String bsjSeqOutDir, String chromosomeName, String refFileData) {
		try {	
			File bedInputDir = new File("./_BSJ_bed_files/" + bedFileInputDir);
			String[] bedFileList = bedInputDir.list();
			for(String bedFileName:bedFileList) {
				if(bedFileName.endsWith(".bed")) {
					BufferedWriter bsjSeqOutfile = createOutFile(bsjSeqOutDir, bedFileName + "_bsj_seq.txt");
					// System.out.println("outfile create");
					
					BufferedReader inputBedFile = new BufferedReader(new FileReader(bedInputDir.getAbsolutePath()+"/"+bedFileName));
					for (String line = inputBedFile.readLine(); line != null; line = inputBedFile.readLine()) {
						// remove tabs from data
						String[] dataColumns = line.split("\t");

						// only process lines with desired chromosome name
						if(line.startsWith(chromosomeName)) {
                            
							// calculate b values
							int highB = Integer.parseInt(dataColumns[2]);
							int lowB = highB - 30;
							String fastaB = getFasta(refFileData, lowB, highB);
							// System.out.println("fasta b: " + fastaB);

							// calculate a values
							int lowA = Integer.parseInt(dataColumns[1]);
							int highA = lowA + 30;
							String fastaA = getFasta(refFileData, lowA, highA);
							// System.out.println("fasta a: " + fastaA);
							
							// save data
							String outputLine = buildOutputLine(dataColumns, fastaB, fastaA);
							bsjSeqOutfile.write(outputLine);
						} else {
							// System.out.println("wrong chromosome name: " + line);
						}
					}
					inputBedFile.close();

					bsjSeqOutfile.close();
				} else {
					// System.out.println("wrong file type: " + bedFileName);
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
