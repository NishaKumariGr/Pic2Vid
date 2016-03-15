package edu.dartmouth.nishacs.cs65project.Model;

import java.util.ArrayList;

/**
 * Created by haris on 29/02/2016.
 */
public class FaceData {



    private Long image_id;
    private ArrayList<FaceObject> all_faces;
    private Integer faces_count;

    public FaceData() {
        this.image_id = new Long(-1);
        this.all_faces =  new ArrayList<FaceObject>();
        this.faces_count = -1;
    }

    public Long getImage_id() {
        return image_id;
    }

    public void setImage_id(Long image_id) {
        this.image_id = image_id;
    }

    public ArrayList<FaceObject> getAll_faces() {
        return all_faces;
    }

    public void setAll_faces(ArrayList<FaceObject> all_faces) {
        this.all_faces = all_faces;
    }

    public Integer getFaces_count() {
        return faces_count;
    }

    public void setFaces_count(Integer faces_count) {
        this.faces_count = faces_count;
    }
}
