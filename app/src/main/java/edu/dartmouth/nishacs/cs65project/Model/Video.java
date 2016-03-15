package edu.dartmouth.nishacs.cs65project.Model;

import java.util.ArrayList;

/**
 * Created by nisha on 2/27/16.
 * This class takes the arrayList of images and the interval. These are the used to create the animation.
 */
public class Video {


    public ArrayList<String> mImageList =  new ArrayList<String>();
    public Integer mInterval;
    public Integer audio_file_path_ID;
    public boolean[] deselectedVideos;
    public String name;

    public Video(ArrayList<String> mImageList, Integer mInterval, Integer audio_file_path_ID, String name) {
        this.mImageList = mImageList;
        this.mInterval = mInterval;
        this.audio_file_path_ID = audio_file_path_ID;
        this.deselectedVideos = new boolean[this.mImageList.size()]; //java initializes boolean arrays to false
        this.name = name;
    }

    public ArrayList<String> getSelectedImageList() {
        ArrayList<String> selectedImageList = mImageList;
        for (int i = 0; i < selectedImageList.size(); i++) {
            if (deselectedVideos[i]) {
                selectedImageList.remove(i);
            }
        }
        return selectedImageList;
    }
}
