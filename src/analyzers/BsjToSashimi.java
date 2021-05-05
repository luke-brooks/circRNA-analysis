package analyzers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BsjToSashimi {

	public static void execute(String bedFileDir) throws IOException {
		int threshold = 1;//minimal reads requirement
		int blockSize = 1;//nt
		File bedDir = new File("./_BSJ_bed_files/"+bedFileDir);
		String[] bedList = bedDir.list();
		for(String bed:bedList) {
			if(bed.endsWith("back_spliced_junction.bed")) {
				BufferedWriter out = new BufferedWriter(new FileWriter(new File(bedDir.getAbsolutePath()+"/"+bed+".sashimi.bed")));
				out.write("track name=junctions" +  "\n");
				BufferedReader br = new BufferedReader(new FileReader(bedDir.getAbsolutePath()+"/"+bed));
				String line = "";
				while((line = br.readLine()) != null) {
					String[] tokens = line.split("\t");
					int start = Integer.parseInt(tokens[1])-blockSize;
					int end = Integer.parseInt(tokens[2])+blockSize;
					int blockEnd = end-start-blockSize;
					int count = Integer.parseInt(tokens[3].substring(tokens[3].indexOf("/")+1));
					String name = tokens[3].substring(0,tokens[3].indexOf("/"));
					if(threshold<count)
					out.write("KT899744" + "\t" + start + "\t" + end + "\t" + name  + "\t" + count + "\t" + start + "\t" + end + "\t" + "0,0,255" + "\t" + "2" + "\t" + blockSize+","+blockSize + "\t" + "0,"+blockEnd + "\n");
				}
				br.close();
				out.close();
			}
		}
	}
}
