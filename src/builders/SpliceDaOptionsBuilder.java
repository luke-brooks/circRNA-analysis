package builders;

import java.util.ArrayList;
import java.util.Collections;

import utilities.FileUtility;

public class SpliceDaOptionsBuilder {
    
    public static ArrayList<String> buildSpliceOptions(String spliceOptionsFilePath) {
		ArrayList<String> spliceOptions = FileUtility.readFileLinesToArrayList(spliceOptionsFilePath, false);

		// sort the options for consistency between runs
		Collections.sort(spliceOptions);

        return spliceOptions;
    }
}
