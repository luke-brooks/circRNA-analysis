package models;

import java.util.ArrayList;

import utilities.StringUtility;

public class BedFile {
    private String fileName; // also referred to as "sample name"
    private ArrayList<BsjDataRow> fileBsjData;

    public BedFile(String fileName) {
        this.fileName = fileName;
        this.fileBsjData = new ArrayList<BsjDataRow>();
    }

    public String getFileName() {
        return fileName;
    }

    // utility func is busted
    // public String getFileNameWithoutExtension() {
    //     return StringUtility.removeFileNameExtension(fileName);
    // }

    public ArrayList<BsjDataRow> getFileBsjData() {
        return fileBsjData;
    }

    public void addBsjDataRow(BsjDataRow dataRow) {
		fileBsjData.add(dataRow);
    }
}
