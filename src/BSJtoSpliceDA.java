
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BSJtoSpliceDA {

	public static void execute(String bedFileInputDir, boolean isHSV1, boolean isKT) throws IOException {
		String chromosomeName = "NC_009333.1";

		// Output locations
		String outDir = "./Outputs/" + "BSJtoSpliceDA/";
		// String bedABOutDir = outDir + "Bed_A_B/";
		String bsjSeqOutDir = outDir + "SpliceDASeq/";
		String frequencyOutDir = outDir + "FrequencyOutput/";

		if (isHSV1) {
			chromosomeName = isKT ? "KT899744" : "NC_001806.2";
		}

		// all .bed files will use the same chromosomeFile
		String chromosomeFilePath = "./_Refs/" + chromosomeName + ".txt";
		String chromosomeFileData = readFileToString(chromosomeFilePath);

		buildBsjSequence(bedFileInputDir, bsjSeqOutDir, chromosomeName, chromosomeFileData);

		generateSpliceDASummary(bsjSeqOutDir, frequencyOutDir);
	}

	public static void generateSpliceDASummary(String spliceDAInputDir, String summaryOutputDir) {
		System.out.println("generating DA summary");

		String spliceOptionsFilePath = "./_Refs/SpliceDAOptions.txt";
		ArrayList<String> spliceOptions = readFileLinesToArrayList(spliceOptionsFilePath, false);

		// sort the options for consistency between runs
		Collections.sort(spliceOptions);

		ArrayList<String> headersList = new ArrayList<String>();
		headersList.add("Splice D-A");
		headersList.addAll(spliceOptions);
		headersList.add("Summary");

		try {
			BufferedWriter spliceSummaryOutfile = createOutFile(summaryOutputDir, "SpliceDASummary.csv");
			String header = buildCsvLine(headersList);
			spliceSummaryOutfile.write(header);

			File spliceInputDir = new File(spliceDAInputDir);
			String[] spliceFileList = spliceInputDir.list();
			for (String spliceFile : spliceFileList) {
				String spliceFileName = spliceFile.split("\\.")[0];

				if (spliceFileName.length() <= 0) {
					// skip garbage files
					System.out.println("The file " + spliceFile + " could not be processed.");
					continue;
				}

				ArrayList<String> spliceCounts = buildSpliceCountList(spliceOptions.size());

				boolean isFirstLine = true;
				BufferedReader inputSeqFile = new BufferedReader(new FileReader(spliceInputDir.getAbsolutePath() + "/" + spliceFile));
				for (String line = inputSeqFile.readLine(); line != null; line = inputSeqFile.readLine()) {
					if (!isFirstLine) {
						System.out.println("reading file line " + line);
						String[] dataColumns = line.split("\t");
						
						String sequenceToMatch = dataColumns[4] + dataColumns[5];
						String sequenceNum = dataColumns[3].split("/")[1];

						System.out.println("sequence to match " + sequenceToMatch);
						System.out.println("sequenceNum " + sequenceNum);


						int targetIndex = spliceOptions.indexOf(sequenceToMatch.toUpperCase());
						System.out.println("target index: " + targetIndex);

						String targetItem = spliceCounts.get(targetIndex);
						int updatedCount = Integer.parseInt(targetItem) + Integer.parseInt(sequenceNum);
						System.out.println("target item: " + targetItem);

						spliceCounts.set(targetIndex, String.valueOf(updatedCount));
					}
					else {
						System.out.println("skipping line " + line);
						isFirstLine = false;
					}
				}
				String spliceTotal = getSpliceTotal(spliceCounts);
				ArrayList<String> spliceData = new ArrayList<String>();
				spliceData.add("Ct-" + spliceFileName);
				spliceData.addAll(spliceCounts);
				spliceData.add(spliceTotal);

				String dataLine = buildCsvLine(spliceData);
				spliceSummaryOutfile.write(dataLine);

				inputSeqFile.close();
			}

			spliceSummaryOutfile.close();
		} catch (Exception e) {
			System.out.println("Error in BSJtoSpliceDA#generateSpliceDASummary(): " + e.getMessage());
		}
	}

	public static String getSpliceTotal(ArrayList<String> list) {
		int total = 0;
		for(String value : list) {
			total += Integer.parseInt(value);
		}
		return String.valueOf(total);
	}

	public static ArrayList<String> buildSpliceCountList(int listLength) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < listLength; i++) {
			result.add("0");
		}
		return result;
	}

	public static String buildCsvLine(ArrayList<String> valuesToWrite) {
		System.out.println("creating new csv line");
		StringBuilder stringBuilder = new StringBuilder();
		for (String value : valuesToWrite) {
			// System.out.println("data to write: " + value);
			stringBuilder.append(value);
			stringBuilder.append(",");
		}
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

	public static void buildBsjSequence(String bedFileInputDir, String bsjSeqOutDir, String chromosomeName,
			String refFileData) {
		try {
			File bedInputDir = new File("./_BSJ_bed_files/" + bedFileInputDir);
			String[] bedFileList = bedInputDir.list();
			for (String bedFileName : bedFileList) {
				System.out.println("file name " + bedFileName);
				if (bedFileName.endsWith(".bed")) {
					String[] bedFileSplit = bedFileName.split("\\.");
					BufferedWriter bsjSeqOutfile = createOutFile(bsjSeqOutDir, bedFileSplit[0] + ".bsj_seq.txt");
					System.out.println("outfile create");

					BufferedReader inputBedFile = new BufferedReader(new FileReader(bedInputDir.getAbsolutePath() + "/" + bedFileName));
					for (String line = inputBedFile.readLine(); line != null; line = inputBedFile.readLine()) {
						// System.out.println("reading file line " + line);
						// remove tabs from data
						String[] dataColumns = line.split("\t");

						// only process lines with desired chromosome name
						if (line.startsWith(chromosomeName)) {
							// calculate b values
							int lowB = Integer.parseInt(dataColumns[2]);
							int highB = lowB + 2;
							String fastaB = getFasta(refFileData, lowB, highB);
							// System.out.println("fasta b: " + fastaB);

							// calculate a values
							int highA = Integer.parseInt(dataColumns[1]);
							int lowA = highA - 2;
							String fastaA = getFasta(refFileData, lowA, highA);
							// System.out.println("fasta a: " + fastaA);

							// save data
							String outputLine = buildBsjOutputLine(dataColumns, fastaB, fastaA);
							bsjSeqOutfile.write(outputLine);
						} else {
							// System.out.println("wrong chromosome name: " + line);
						}
					}
					inputBedFile.close();

					bsjSeqOutfile.close();
				} else {
					System.out.println("wrong file type: " + bedFileName);
				}
			}
		} catch (Exception e) {
			System.out.println("Error in BSJtoSpliceDA#buildBsjSequence(): " + e.getMessage());
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
		StringBuilder stringBuilder = new StringBuilder();
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
			// System.out.println("Error in BSJtoSpliceDA#readFileToString(): file - " +
			// file + "Error msg: " + e.getMessage());
		}
		return stringBuilder.toString();
	}

	public static ArrayList<String> readFileLinesToArrayList(String file, boolean skipFirstLine) {
		ArrayList<String> fileOutput = new ArrayList<String>();
		try {
			BufferedReader fileToRead = new BufferedReader(new FileReader(file));
			for (String line = fileToRead.readLine(); line != null; line = fileToRead.readLine()) {
				if (!skipFirstLine) {
					// System.out.println("Line data: " + line);
					fileOutput.add(line.toUpperCase());
				} else {
					// System.out.println("Skipping: " + line);
					skipFirstLine = false;
				}
			}
			fileToRead.close();
		} catch (Exception e) {
			// System.out.println("Error in BSJtoSpliceDA#readFileToString(): file - " +
			// file + "Error msg: " + e.getMessage());
		}
		return fileOutput;
	}

	public static String buildBsjOutputLine(String[] dataColumns, String fastaB, String fastaA) {
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
		System.out.println("creating file " + outFileName);
		BufferedWriter result = null;
		try {
			FileWriter initFile = new FileWriter(new File(outDir + outFileName));
			result = new BufferedWriter(initFile);
			result.write("\n");
		} catch (Exception e) {
			System.out.println("Error in BSJtoSpliceDA#createOutFile()" + e.getMessage());
		}
		return result;
	}
}
