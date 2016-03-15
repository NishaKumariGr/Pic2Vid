package edu.dartmouth.nishacs.cs65project.Model;

/**
 * Created by haris on 29/02/2016.
 */
public class ImageData {

    private Long img_id;
    private String img_location;
    private Long tag_date_time_captured;
    private Double tag_exposure_time;
    private Long tag_flash;
    private Double tag_focal_length;
    private Double tag_gps_altitude;
    private Double tag_gps_longitude;
    private Double tag_gps_latitude;
    private String tag_gps_provider;
    private Integer tag_img_width;
    private Integer tag_img_height;
    private String tag_make;
    private String tag_model;
    private Integer tag_orientation;
    private Double tag_white_balence;
    private String img_source;
    private Boolean has_face;
    private Integer faces_count;

    public ImageData() {
        img_id = new Long(-1);
        img_location = "";
        tag_date_time_captured = new Long(0);
        tag_exposure_time = 0.0;
        tag_flash = new Long(0);
        tag_focal_length = 0.0;
        tag_gps_altitude = 0.0;
        tag_gps_longitude = 0.0;
        tag_gps_latitude = 0.0;
        tag_gps_provider = "NA";
        tag_img_width = 0;
        tag_img_height = 0;
        tag_make = "NA";
        tag_model = "NA";
        tag_orientation = -1;
        tag_white_balence = 0.0;
        img_source = "Camera";
        has_face = false;
        faces_count = 0;


    }

    public Long getImg_id() {
        return img_id;
    }

    public void setImg_id(Long img_id) {
        this.img_id = img_id;
    }

    public String getImg_location() {
        return img_location;
    }

    public void setImg_location(String img_location) {
        this.img_location = img_location;
    }

    public Long getTag_date_time_captured() {
        return tag_date_time_captured;
    }

    public void setTag_date_time_captured(Long tag_date_time_captured) {
        this.tag_date_time_captured = tag_date_time_captured;
    }

    public Double getTag_exposure_time() {
        return tag_exposure_time;
    }

    public void setTag_exposure_time(Double tag_exposure_time) {
        this.tag_exposure_time = tag_exposure_time;
    }

    public Long getTag_flash() {
        return tag_flash;
    }

    public void setTag_flash(Long tag_flash) {
        this.tag_flash = tag_flash;
    }

    public Double getTag_focal_length() {
        return tag_focal_length;
    }

    public void setTag_focal_length(Double tag_focal_length) {
        this.tag_focal_length = tag_focal_length;
    }

    public Double getTag_gps_altitude() {
        return tag_gps_altitude;
    }

    public void setTag_gps_altitude(Double tag_gps_altitude) {
        this.tag_gps_altitude = tag_gps_altitude;
    }

    public Double getTag_gps_longitude() {
        return tag_gps_longitude;
    }

    public void setTag_gps_longitude(Double tag_gps_longitude) {
        this.tag_gps_longitude = tag_gps_longitude;
    }

    public Double getTag_gps_latitude() {
        return tag_gps_latitude;
    }

    public void setTag_gps_latitude(Double tag_gps_latitude) {
        this.tag_gps_latitude = tag_gps_latitude;
    }

    public String getTag_gps_provider() {
        return tag_gps_provider;
    }

    public void setTag_gps_provider(String tag_gps_provider) {
        this.tag_gps_provider = tag_gps_provider;
    }

    public Integer getTag_img_width() {
        return tag_img_width;
    }

    public void setTag_img_width(Integer tag_img_width) {
        this.tag_img_width = tag_img_width;
    }

    public Integer getTag_img_height() {
        return tag_img_height;
    }

    public void setTag_img_height(Integer tag_img_height) {
        this.tag_img_height = tag_img_height;
    }

    public String getTag_make() {
        return tag_make;
    }

    public void setTag_make(String tag_make) {
        this.tag_make = tag_make;
    }

    public String getTag_model() {
        return tag_model;
    }

    public void setTag_model(String tag_model) {
        this.tag_model = tag_model;
    }

    public Integer getTag_orientation() {
        return tag_orientation;
    }

    public void setTag_orientation(Integer tag_orientation) {
        this.tag_orientation = tag_orientation;
    }

    public Double getTag_white_balence() {
        return tag_white_balence;
    }

    public void setTag_white_balence(Double tag_white_balence) {
        this.tag_white_balence = tag_white_balence;
    }

    public String getImg_source() {
        return img_source;
    }

    public void setImg_source(String img_source) {
        this.img_source = img_source;
    }

    public Boolean getHas_face() {
        return has_face;
    }

    public void setHas_face(Boolean has_face) {
        this.has_face = has_face;
    }

    public Integer getFaces_count() {
        return faces_count;
    }

    public void setFaces_count(Integer faces_count) {
        this.faces_count = faces_count;
    }

}
