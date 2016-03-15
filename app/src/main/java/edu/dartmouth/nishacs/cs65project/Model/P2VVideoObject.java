package edu.dartmouth.nishacs.cs65project.Model;

import java.util.ArrayList;

/**
 * Created by haris on 03/03/2016.
 */
public class P2VVideoObject {

    private Long vid_id;
    private String author;
    private String videoTitle;
    private String dateCreated;
    private String dateLastEdited;
    private Integer totalImagesUsed;
    private ArrayList<String> file_paths;

    P2VVideoSettings video_settings;

    public P2VVideoObject() {
        vid_id = new Long(0);
        author = "";
        videoTitle = "";
        dateCreated = "";
        dateLastEdited = "";
        totalImagesUsed = 0;
        file_paths = new ArrayList<String>();
    }

    public Long getVid_id() {
        return vid_id;
    }

    public void setVid_id(Long vid_id) {
        this.vid_id = vid_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateLastEdited() {
        return dateLastEdited;
    }

    public void setDateLastEdited(String dateLastEdited) {
        this.dateLastEdited = dateLastEdited;
    }

    public Integer getTotalImagesUsed() {
        return totalImagesUsed;
    }

    public void setTotalImagesUsed(Integer totalImagesUsed) { this.totalImagesUsed = totalImagesUsed;}

    public ArrayList<String> getFile_paths() {
        return file_paths;
    }

    public void setFile_paths(ArrayList<String> file_paths) {
        this.file_paths = file_paths;
        this.totalImagesUsed = file_paths.size();
    }

    public P2VVideoSettings getVideo_settings() {
        return video_settings;
    }

    public void setVideo_settings(P2VVideoSettings video_settings) {
        this.video_settings = video_settings;
    }
}
