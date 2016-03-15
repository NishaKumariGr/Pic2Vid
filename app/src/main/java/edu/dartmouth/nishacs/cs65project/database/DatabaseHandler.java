package edu.dartmouth.nishacs.cs65project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.dartmouth.nishacs.cs65project.Model.FaceData;
import edu.dartmouth.nishacs.cs65project.Model.FaceObject;
import edu.dartmouth.nishacs.cs65project.Model.ImageData;

import java.util.ArrayList;

/**
 * Created by haris on 02/03/2016.
 */
public class DatabaseHandler {

    // This class supports operations for 3 cases
    // 1- Writing to the DB from the indexing thread!
    // 2- Reading data from the DB for the query engine and the History View
    // 3- Writing data to disk from the VideoController on save

    private SQLiteDatabase myImageDB;
    private DatabaseHelper myDBHelper;

    public DatabaseHandler(Context context)
    {
        myDBHelper = new DatabaseHelper(context);
    }

    // written as public
    protected void open()
    {
        myImageDB = myDBHelper.getWritableDatabase();
    }

    protected void close()
    {
        myImageDB.close();
    }

    protected void openForQuery() { myImageDB = myDBHelper.getReadableDatabase(); }

    public Long AddImageData(ImageData imageData)
    {
        ContentValues new_entry = new ContentValues();

        new_entry.put(DatabaseHelper.IMAGEDATA_COL_LOCATIONONDISK,imageData.getImg_location());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_DATETIME,imageData.getTag_date_time_captured());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_EXPOSURETIME, imageData.getTag_exposure_time());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_FLASH, imageData.getTag_flash());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_FOCALLENGTH, imageData.getTag_focal_length());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_GPS_ALTITUDE, imageData.getTag_gps_altitude());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_GPS_LONGITUDE, imageData.getTag_gps_longitude());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_GPS_LATITUDE, imageData.getTag_gps_latitude());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_GPS_PROCESSINGMETHOD, imageData.getTag_gps_provider());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_IMGWIDTH, imageData.getTag_img_width());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_IMGHEIGHT, imageData.getTag_img_height());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_MAKE, imageData.getTag_make());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_MODEL, imageData.getTag_model());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_ORIENTATION, imageData.getTag_orientation());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_WHITEBALANCE, imageData.getTag_white_balence());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_SOURCE, imageData.getImg_source());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_HASFACE, imageData.getHas_face());
        new_entry.put(DatabaseHelper.IMAGEDATA_COL_FACESCOUNT, imageData.getFaces_count());

        open();
        Long image_id= myImageDB.insert(DatabaseHelper.TABLE_IMAGEDATA,null,new_entry);
        close();

        return image_id;
    }

    public Boolean AddFaceData(FaceData faceData)
    {
        Integer total_faces = faceData.getFaces_count();
        Long cur_img_id = faceData.getImage_id();
        ArrayList<FaceObject> all_faces = faceData.getAll_faces();

        for (int a_face = 0; a_face < total_faces; a_face++)
        {
            ContentValues new_entry = new ContentValues();

            FaceObject cur_face_object = all_faces.get(a_face);

            new_entry.put(DatabaseHelper.FACEDATA_COL_IMGID, cur_img_id);
            new_entry.put(DatabaseHelper.FACEDATA_COL_FACEANGLEX, 0.0);
            new_entry.put(DatabaseHelper.FACEDATA_COL_FACEANGLEY, cur_face_object.getFace_angle_y());
            new_entry.put(DatabaseHelper.FACEDATA_COL_FACEANGLEZ, cur_face_object.getFace_angle_z());
            new_entry.put(DatabaseHelper.FACEDATA_COL_FACECOLSTART, cur_face_object.getFace_coord_col());
            new_entry.put(DatabaseHelper.FACEDATA_COL_FACEROWSTART, cur_face_object.getFace_coord_row());
            new_entry.put(DatabaseHelper.FACEDATA_COL_FACEWIDTH, cur_face_object.getFace_width());
            new_entry.put(DatabaseHelper.FACEDATA_COL_FACEHEIGHT, cur_face_object.getFace_height());
            new_entry.put(DatabaseHelper.FACEDATA_COL_LEFTEYEOPEN, cur_face_object.getLeft_eye_open_prob());
            new_entry.put(DatabaseHelper.FACEDATA_COL_RIGHTEYEOPEN, cur_face_object.getRight_eye_open_prob());
            new_entry.put(DatabaseHelper.FACEDATA_COL_SMILING, cur_face_object.getIs_smiling_probability());

            open();
            myImageDB.insert(DatabaseHelper.TABLE_FACEDATA, null, new_entry);
            close();
        }

        return true;
    }

    public Boolean checkImageExists(String image_path) {
        openForQuery();
        Cursor cursor = myImageDB.query(myDBHelper.TABLE_IMAGEDATA,
                new String[]{myDBHelper.IMAGEDATA_COL_ID},
                myDBHelper.IMAGEDATA_COL_LOCATIONONDISK + " LIKE ?", new String[] {"%" + image_path + "%"},
                null, null, null);

        Integer count = cursor.getCount();
        close();
        if (count > 0) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }
}
