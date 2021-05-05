package models;

import java.util.ArrayList;

public class BedFile {
    String fileName;
    ArrayList<BsjDataRow> fileBsjData;

    public BedFile(String fileName) {
        this.fileName = fileName;
        this.fileBsjData = new ArrayList<BsjDataRow>();
    }

    public String getFileName() {
        return fileName;
    }

    public ArrayList<BsjDataRow> getFileBsjData() {
        return fileBsjData;
    }

    public void addBsjDataRow(BsjDataRow dataRow) {
		fileBsjData.add(dataRow);
    }
}
