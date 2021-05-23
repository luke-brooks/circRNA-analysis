package deprecated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BsjStartEnd_aggregate {

	public static void execute(String bedFileDir, boolean isHSV1, boolean isKT) throws IOException {
		//int binLength = 100;//nt
		// boolean isKT = true;//false=>EBV
		boolean ouputBedGraph = true;
		boolean hasHeader = true;
		boolean circExplorer = true;

		String prefix = "";
		int genomeLength = 0;
		String chromosomeName = "";

		if(isHSV1) {
			// 135164 = KT899744delRR
			// 152222 = NC_001806.2
			genomeLength = isKT ? 135164 : 152222;
			prefix = isKT ? "kt_" : "ebv_";
			chromosomeName = isKT ? "KT899744" : "NC_001806.2";
		}
		else {
			genomeLength = 135080; // NC_009333.1
			prefix = "kshv_";
			chromosomeName = "NC_009333.1";
		}

		int binNum=genomeLength;

		File bedDir = new File("./_BSJ_bed_files/"+bedFileDir);

		String[] bedList = bedDir.list();
		
		//starts of splice sites
		BufferedWriter output = new BufferedWriter(new FileWriter(new File("./hist_aggregate_start_"+prefix+"_"+bedDir.getName()+".txt")));
		output.write("bin#");
		for(int i =0;i<binNum;i++) {
			output.write("\t"+Integer.toString(i+1));
		}
		output.write("\n");

		BufferedWriter outBG = null;
		for(String bed:bedList) {
			if(bed.endsWith(".bed")) {
				BufferedReader br = new BufferedReader(new FileReader(bedDir.getAbsolutePath()+"/"+bed));
				String line = "";
				if(hasHeader) {
					line = br.readLine();
				}
				ArrayList<int[]> BSes = new ArrayList<int[]>();
				int nonViralBSJreads = 0;
				int viralBSJreads = 0;
				while((line = br.readLine()) != null){
					String[] tokens = line.split("\t");
					//System.out.println(tokens[0] + "\t" + tokens[1] + "\t" + tokens[2]);
					int[] BS = new int[3];
					BS[0] = Integer.parseInt(tokens[1]);
					BS[1] = Integer.parseInt(tokens[1]);
					BS[2] = circExplorer ? Integer.parseInt(tokens[3].substring(tokens[3].indexOf("/")+1)) : Integer.parseInt(tokens[1]);
					
					if(line.startsWith(chromosomeName)) {
						BSes.add(BS);
						viralBSJreads = viralBSJreads + BS[2];
					} else if(!line.startsWith("KT")) {
						nonViralBSJreads = nonViralBSJreads + BS[2];
					}
				}
				br.close();

				if(ouputBedGraph) outBG = new BufferedWriter(new FileWriter(new File(bedDir.getAbsolutePath()+"/"+bed+".starts_bedgraph.txt")));

				output.write(bed);
				for(int i =0;i<binNum;i++) {
					int tmpCirc = 0;
					for(int[] tmpBS:BSes) {
						if(!(tmpBS[0] > (i+1)) && !(tmpBS[1] < (i+1))) {
							tmpCirc=tmpCirc+tmpBS[2];
						}
					}
					output.write("\t"+Integer.toString(tmpCirc));
					if(ouputBedGraph) {
						if(isKT)
							outBG.write("\n"+"KT899744" + "\t" +i + "\t" +(i+1) + "\t" +tmpCirc);
						else 				
							outBG.write("\n"+"NC_007605.1" + "\t" +i + "\t" +(i+1) + "\t" +tmpCirc);
					}
				}
				output.write("\n");
				System.out.println(bed +"\t"+viralBSJreads+"\t"+nonViralBSJreads);
				if(ouputBedGraph)	outBG.close();

			}
		}
		output.close();	
		
		//ends of splice sites
		output = new BufferedWriter(new FileWriter(new File("./hist_aggregate_end_"+prefix+"_"+bedDir.getName()+".txt")));
		output.write("bin#");
		for(int i =0;i<binNum;i++) {
			output.write("\t"+Integer.toString(i+1));
		}
		output.write("\n");

		outBG = null;
		for(String bed:bedList) {
			if(bed.endsWith(".bed")) {
				BufferedReader br = new BufferedReader(new FileReader(bedDir.getAbsolutePath()+"/"+bed));
				String line = "";
				if(hasHeader) {
					line = br.readLine();
				}
				ArrayList<int[]> BSes = new ArrayList<int[]>();
				int nonViralBSJreads = 0;
				int viralBSJreads = 0;
				while((line = br.readLine()) != null){
					String[] tokens = line.split("\t");
					//System.out.println(tokens[0] + "\t" + tokens[1] + "\t" + tokens[2]);
					if(circExplorer) {
						int[] BS= {Integer.parseInt(tokens[2]),Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3].substring(tokens[3].indexOf("/")+1))};

						if(isKT) {
							if(line.startsWith("KT899744")) {
								BSes.add(BS);
								viralBSJreads = viralBSJreads + BS[2];
							}else if(!line.startsWith("KT")) {
								nonViralBSJreads = nonViralBSJreads + BS[2];
							}

						}else {
							if(line.startsWith("NC_007605.1")) {
								BSes.add(BS);
								viralBSJreads = viralBSJreads + BS[2];
							}else if(!line.startsWith("NC_")) {
								nonViralBSJreads = nonViralBSJreads + BS[2];
							}
						}
					}else {
						int[] BS= {Integer.parseInt(tokens[2]),Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])};		
						if(isKT) {
							if(line.startsWith("KT899744")) {
								BSes.add(BS);
								viralBSJreads = viralBSJreads + BS[2];
							}else if(!line.startsWith("KT")) {
								nonViralBSJreads = nonViralBSJreads + BS[2];
							}

						}else {
							if(line.startsWith("NC_007605.1")) {
								BSes.add(BS);
								viralBSJreads = viralBSJreads + BS[2];
							}else if(!line.startsWith("NC_")) {
								nonViralBSJreads = nonViralBSJreads + BS[2];
							}
						}
					}
				}
				br.close();

				if(ouputBedGraph) outBG = new BufferedWriter(new FileWriter(new File(bedDir.getAbsolutePath()+"/"+bed+".ends_bedgraph.txt")));

				output.write(bed);
				for(int i =0;i<binNum;i++) {
					int tmpCirc = 0;
					for(int[] tmpBS:BSes) {
						if(!(tmpBS[0] > (i+1)) && !(tmpBS[1] < (i+1))) {
							tmpCirc=tmpCirc+tmpBS[2];
						}
					}
					output.write("\t"+Integer.toString(tmpCirc));
					if(ouputBedGraph) {
						if(isKT)
							outBG.write("KT899744" + "\t" +i + "\t" +(i+1) + "\t" +tmpCirc+"\n");
						else 				
							outBG.write("NC_007605.1" + "\t" +i + "\t" +(i+1) + "\t" +tmpCirc+"\n");
					}
				}
				output.write("\n");
				System.out.println(bed +"\t"+viralBSJreads+"\t"+nonViralBSJreads);
				if(ouputBedGraph)	outBG.close();

			}
		}
		output.close();	
	}

}
