package models;

import static config.Constants.SEQUENCE_COMPLEMENT;
import static config.Constants.TXT_EXTENSION;

public class ChromosomeRef {
    private String senseName;
    private String antiSenseName;
    private boolean isHuman;

    private String senseSequence;
    private String antiSenseSequence;

    private String _antiSensePostFix = SEQUENCE_COMPLEMENT;
    private String _fileExtension = TXT_EXTENSION;

    public ChromosomeRef(String senseName, boolean isHuman) {
        this.senseName = senseName;
        this.antiSenseName = senseName + _antiSensePostFix;
        this.isHuman = isHuman;
    }

    // property getters
    public String getSenseName() {
        return senseName;
    }
    public String getAntiSenseName() {
        return antiSenseName;
    }
    public boolean getIsHuman() {
        return isHuman;
    }
    public String getSenseSequence() {
		return senseSequence;
    }
    public String getAntiSenseSequence() {
		return antiSenseSequence;
    }

    // computed property getters
    public String getSenseFileName() {
        return senseName + _fileExtension;
    }
    public String getAntiSenseFileName() {
        return antiSenseName + _fileExtension;
    }

    // property setters
    public void setSenseSequence(String senseSequence) {
		this.senseSequence = senseSequence;
    }
    public void setAntiSenseSequence(String antiSenseSequence) {
		this.antiSenseSequence = antiSenseSequence;
    }
}
