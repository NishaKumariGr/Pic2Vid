package edu.dartmouth.nishacs.cs65project.Model;

/**
 * Created by haris on 03/03/2016.
 */
public class P2VVideoSettings {

    private Integer frameRate;
    private String audioTrackLocation;
    private Boolean audioTrackAvailable;

    public P2VVideoSettings() {
        frameRate = 10; // default value
        audioTrackLocation = "";
        audioTrackAvailable = false;
    }

    public Boolean getAudioTrackAvailable() {
        return audioTrackAvailable;
    }

    public void setAudioTrackAvailable(Boolean audioTrackAvailable) {
        this.audioTrackAvailable = audioTrackAvailable;
    }

    public Integer getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
    }

    public String getAudioTrackLocation() {
        return audioTrackLocation;
    }

    public void setAudioTrackLocation(String audioTrackLocation) {
        this.audioTrackLocation = audioTrackLocation;
    }
}
