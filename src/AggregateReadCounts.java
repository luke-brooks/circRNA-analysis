
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AggregateReadCounts {

	public static void execute(String logDirectory) throws IOException {
		File logDir = new File("_logs/" + logDirectory);
		String[] logList = logDir.list();
		BufferedWriter out = new BufferedWriter(new FileWriter(new File("./Outputs/logs_" + logDir.getName() + ".txt")));
		out.write("log file" + "\t" + "Number of input reads"  + "\t" + "Uniquely mapped reads number" + "\t" + "Number of reads mapped to multiple loci" + "\t" + "Number of reads mapped to too many loci" + "\t" + "Number of chimeric reads");
		for(String logFile:logList) {
			if(logFile.endsWith(".out")) {
				out.write("\n" + logFile);
				BufferedReader br = new BufferedReader(new FileReader(new File(logDir.getAbsolutePath() + "/" + logFile)));
				String line ="";
				while((line = br.readLine()) != null) {
					if(line.indexOf("Number of input reads") > 0) {
						out.write("\t" + Integer.parseInt(line.substring(line.indexOf("|	")+2)));
						//System.out.println(Integer.parseInt(line.substring(line.indexOf("|	")+2)));
					}else if(line.indexOf("Uniquely mapped reads number") > 0) {
						out.write("\t" + Integer.parseInt(line.substring(line.indexOf("|	")+2)));
					}else if(line.indexOf("Number of reads mapped to multiple loci") > 0) {
						out.write("\t" + Integer.parseInt(line.substring(line.indexOf("|	")+2)));
					}else if(line.indexOf("Number of reads mapped to too many loci") > 0) {
						out.write("\t" + Integer.parseInt(line.substring(line.indexOf("|	")+2)));
					}else if(line.indexOf("Number of chimeric reads") > 0) {
						out.write("\t" + Integer.parseInt(line.substring(line.indexOf("|	")+2)));
					}
				}
			}
		}
		out.close();
	}

}
