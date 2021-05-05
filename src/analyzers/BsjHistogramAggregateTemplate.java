package analyzers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BsjHistogramAggregateTemplate {

	public static void execute(String bedFileDir, boolean isHSV1, boolean isKT) throws IOException {
		int binLength = 1;
		int genomeLength = 0;
		String prefix = "";
		String chromosomeName = "";
		
		if(isHSV1) {
			// 135164 = KT899744delRR
			// 152222 = NC_001806.2
			genomeLength = isKT ? 135164 : 152222;
			prefix = "hsv1_";
			chromosomeName = isKT ? "KT899744" : "NC_001806.2";
		} else {
			genomeLength = 135080; // NC_009333.1
			prefix = "kshv_";
			chromosomeName = "NC_009333.1";
		}

		int binNum=0;
		if(genomeLength % binLength > 0) {
			binNum = genomeLength/binLength+1;
		} else {
			binNum = genomeLength/binLength;			
		}

		File bedDir = new File("./_BSJ_bed_files/"+bedFileDir);

		String[] bedList = bedDir.list();

		File resultsFile = new File("./Outputs/"+bedDir.getName()+"_hist_aggregate.txt");

		BufferedWriter output = new BufferedWriter(new FileWriter(resultsFile));
		output.write("bin#");
		for(int i =0;i<binNum;i++) {
			output.write("\t"+Integer.toString(i+1));
		}
		output.write("\n");

		for(String bed:bedList) {
			if(bed.endsWith(".bed")) {
				BufferedReader br = new BufferedReader(new FileReader(bedDir.getAbsolutePath()+"/"+bed));
				String line = "";
				ArrayList<int[]> BSes = new ArrayList<int[]>();
				int nonViralBSJreads = 0;
				int viralBSJreads = 0;
				while((line = br.readLine()) != null){

					String[] tokens = line.split("\t");
					int[] BS= {Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3].substring(tokens[3].indexOf("/")+1))};

					if(line.startsWith(chromosomeName)) {
						BSes.add(BS);
						viralBSJreads = viralBSJreads + BS[2];
					} else if(!line.startsWith("NC_")) {
						nonViralBSJreads = nonViralBSJreads + BS[2];
					}
				}
				br.close();

				output.write(bed);
				for(int i =0;i<binNum;i++) {
					int tmpCirc = 0;
					for(int[] tmpBS:BSes) {
						if(!(tmpBS[0] > (i+1)*binLength) && !(tmpBS[1] < (i*binLength+1))) {
							tmpCirc=tmpCirc+tmpBS[2];
						}
					}
					output.write("\t"+Integer.toString(tmpCirc));
				}
				output.write("\n");

				System.out.println(bed +"\t"+viralBSJreads+"\t"+nonViralBSJreads);
			}
		}
		output.close();			
	}

}
