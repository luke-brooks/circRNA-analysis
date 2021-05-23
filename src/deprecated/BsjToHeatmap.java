package deprecated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class BsjToHeatmap {

	public static void execute(String inputSource, String outDirectory) throws IOException {
		if (inputSource == null) {
			inputSource = "_BSJSeq/";
		}
		if (outDirectory == null) {
			outDirectory = "./Outputs";
		}
		File bedDir = new File(inputSource);
		File outDir = new File(outDirectory);
		String[] bedList = bedDir.list();
		for(String bed:bedList) {
			if(bed.endsWith(".txt")) {
				BufferedReader br = new BufferedReader(new FileReader(bedDir.getAbsolutePath()+"/"+bed));
				File outFile = new File(outDir.getAbsolutePath()+"/"+bed + ".out");
				BufferedWriter out = new BufferedWriter(new FileWriter(outFile));

				String line = br.readLine();
				while((line = br.readLine()) != null){
					String[] tokens = line.split("\t");
					String targetName = bed.substring(bed.indexOf("-")+1, bed.indexOf(".txt"));
					StringBuffer row = new StringBuffer(targetName + "_" + tokens[1] + "-" + tokens[2]);
					int readNum = Integer.parseInt(tokens[3].split("/")[1]);
					for(int i=0;i<tokens[4].length();i++) {
						row.append("\t" + convertNT(tokens[4].charAt(i)));
					}
					for(int i=0;i<tokens[5].length();i++) {
						row.append("\t" + convertNT(tokens[5].charAt(i)));
					}
					for(int i =0;i<readNum;i++) {
						out.write(row.toString() + "\n");
					}
				}
				br.close();
				out.close();
			}
		}
	}
	public static Integer convertNT(char _in) {
		Integer _out = -1;
		switch(_in) {
			case 'a':
			case 'A':
				_out = 1;
				break;
			case 't':
			case 'T':
				_out = 2;
				break;
			case 'g':
			case 'G':
				_out = 3;
				break;
			case 'c':
			case 'C':
				_out = 4;
				break;
		}
		return _out;
	}
}
