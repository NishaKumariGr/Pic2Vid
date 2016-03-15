package edu.dartmouth.nishacs.cs65project.Model;

/**
 * Created by haris on 29/02/2016.
 */
public class FaceObject {

    private Integer face_coord_row;
    private Integer face_coord_col;
    private Integer face_height;
    private Integer face_width;
    private Double face_angle_x;
    private Double face_angle_y;
    private Double face_angle_z;
    private Double is_smiling_probability;
    private Double left_eye_open_prob;
    private Double right_eye_open_prob;

    public FaceObject() {
        face_coord_row = -1;
        face_coord_col = -1;
        face_height = 0;
        face_width = 0;
        face_angle_x = 0.0;
        face_angle_y = 0.0;
        face_angle_z = 0.0;
        is_smiling_probability = 0.0;
        left_eye_open_prob = 0.0;
        right_eye_open_prob = 0.0;
    }

    public Integer getFace_coord_row() {
        return face_coord_row;
    }

    public void setFace_coord_row(Integer face_coord_row) {
        this.face_coord_row = face_coord_row;
    }

    public Integer getFace_coord_col() {
        return face_coord_col;
    }

    public void setFace_coord_col(Integer face_coord_col) {
        this.face_coord_col = face_coord_col;
    }

    public Integer getFace_height() {
        return face_height;
    }

    public void setFace_height(Integer face_height) {
        this.face_height = face_height;
    }

    public Integer getFace_width() {
        return face_width;
    }

    public void setFace_width(Integer face_width) {
        this.face_width = face_width;
    }

    public Double getFace_angle_x() {
        return face_angle_x;
    }

    public void setFace_angle_x(Double face_angle_x) {
        this.face_angle_x = face_angle_x;
    }

    public Double getFace_angle_y() {
        return face_angle_y;
    }

    public void setFace_angle_y(Double face_angle_y) {
        this.face_angle_y = face_angle_y;
    }

    public Double getFace_angle_z() {
        return face_angle_z;
    }

    public void setFace_angle_z(Double face_angle_z) {
        this.face_angle_z = face_angle_z;
    }

    public Double getIs_smiling_probability() {
        return is_smiling_probability;
    }

    public void setIs_smiling_probability(Double is_smiling_probability) {
        this.is_smiling_probability = is_smiling_probability;
    }

    public Double getLeft_eye_open_prob() {
        return left_eye_open_prob;
    }

    public void setLeft_eye_open_prob(Double left_eye_open_prob) {
        this.left_eye_open_prob = left_eye_open_prob;
    }

    public Double getRight_eye_open_prob() {
        return right_eye_open_prob;
    }

    public void setRight_eye_open_prob(Double right_eye_open_prob) {
        this.right_eye_open_prob = right_eye_open_prob;
    }
}
