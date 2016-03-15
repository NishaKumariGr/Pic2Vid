package edu.dartmouth.nishacs.cs65project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by haris on 28/02/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "image_data_pic2video.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_VIDEODATA = "VIDEODATA";
    public static final String TABLE_VIDEOSCREATED = "VIDEOSCREATED";
    public static final String TABLE_VIDEOSETTINGS = "VIDEOSETTINGS";

    public static final String TABLE_IMAGEDATA= "IMAGEDATA";
    public static final String TABLE_FACEDATA = "FACEDATA";

    public static final String IMAGEDATA_COL_ID = "img_id";
    public static final String IMAGEDATA_COL_LOCATIONONDISK = "img_location";
    public static final String IMAGEDATA_COL_DATETIME = "tag_datetime";
    public static final String IMAGEDATA_COL_EXPOSURETIME = "tag_exposuretime";
    public static final String IMAGEDATA_COL_FLASH = "tag_flash";
    public static final String IMAGEDATA_COL_FOCALLENGTH = "tag_focallength";
    public static final String IMAGEDATA_COL_GPS_ALTITUDE= "tag_gps_altitude";
    public static final String IMAGEDATA_COL_GPS_LATITUDE = "tag_gps_latitude";
    public static final String IMAGEDATA_COL_GPS_LONGITUDE = "tag_gps_longitude";
    public static final String IMAGEDATA_COL_GPS_PROCESSINGMETHOD = "tag_gps_processing_method";
    public static final String IMAGEDATA_COL_IMGWIDTH = "tag_image_width";
    public static final String IMAGEDATA_COL_IMGHEIGHT = "tag_image_height";
    public static final String IMAGEDATA_COL_MAKE = "tag_make";
    public static final String IMAGEDATA_COL_MODEL = "tag_model";
    public static final String IMAGEDATA_COL_ORIENTATION = "tag_orientation";
    public static final String IMAGEDATA_COL_WHITEBALANCE = "tag_whitebalance";
    public static final String IMAGEDATA_COL_SOURCE = "tag_source";
    public static final String IMAGEDATA_COL_HASFACE = "tag_hasface";
    public static final String IMAGEDATA_COL_FACESCOUNT = "tag_facescount";


    public static final String[] TABLE_IMAGEDATA_COLUMNS = {IMAGEDATA_COL_ID,IMAGEDATA_COL_LOCATIONONDISK,
            IMAGEDATA_COL_DATETIME,IMAGEDATA_COL_EXPOSURETIME,IMAGEDATA_COL_FLASH,
            IMAGEDATA_COL_FOCALLENGTH,IMAGEDATA_COL_GPS_ALTITUDE,IMAGEDATA_COL_GPS_LATITUDE,IMAGEDATA_COL_GPS_LONGITUDE,
            IMAGEDATA_COL_GPS_PROCESSINGMETHOD,IMAGEDATA_COL_IMGWIDTH,IMAGEDATA_COL_IMGHEIGHT,IMAGEDATA_COL_MAKE,
            IMAGEDATA_COL_MODEL,IMAGEDATA_COL_ORIENTATION,IMAGEDATA_COL_WHITEBALANCE,IMAGEDATA_COL_SOURCE,IMAGEDATA_COL_HASFACE,
            IMAGEDATA_COL_FACESCOUNT};

    public static final String VIDEOSCREATED_COL_ID = "vid_id";
    public static final String VIDEOSCREATED_COL_AUTHOR = "author";
    public static final String VIDEOSCREATED_COL_TITLE = "title";
    public static final String VIDEOSCREATED_COL_DATECREATED = "date_created";
    public static final String VIDEOSCREATED_COL_DATELASTEDITED = "date_last_edited";
    public static final String VIDEOSCREATED_COL_TOTALIMAGESUSED = "total_images_used";

    public final static String[] TABLE_VIDEOSCREATED_COLUMNS = {VIDEOSCREATED_COL_ID,VIDEOSCREATED_COL_AUTHOR,
            VIDEOSCREATED_COL_TITLE,VIDEOSCREATED_COL_DATECREATED,VIDEOSCREATED_COL_DATELASTEDITED,
            VIDEOSCREATED_COL_TOTALIMAGESUSED};

    public static final String VIDEODATA_COL_VIDID = "vid_id";
    public static final String VIDEODATA_COL_IMGLOCATIONONDISK = "img_loc";
    public static final String VIDEODATA_COL_LOCATIONINVIDEO = "location_in_video";

    public final static String[] TABLE_VIDEODATA_COLUMNS = {VIDEODATA_COL_VIDID,VIDEODATA_COL_IMGLOCATIONONDISK,
            VIDEODATA_COL_LOCATIONINVIDEO};


    public static final String FACEDATA_COL_IMGID = "img_id";
    public static final String FACEDATA_COL_FACEROWSTART = "face_row_start";
    public static final String FACEDATA_COL_FACECOLSTART = "face_col_start";
    public static final String FACEDATA_COL_FACEHEIGHT = "face_height";
    public static final String FACEDATA_COL_FACEWIDTH = "face_width";
    public static final String FACEDATA_COL_FACEANGLEX = "face_angle_x";
    public static final String FACEDATA_COL_FACEANGLEY = "face_angle_y";
    public static final String FACEDATA_COL_FACEANGLEZ = "face_angle_z";
    public static final String FACEDATA_COL_SMILING = "face_smiling";
    public static final String FACEDATA_COL_LEFTEYEOPEN = "face_left_eye_open";
    public static final String FACEDATA_COL_RIGHTEYEOPEN = "face_right_eye_open";

    public final static String[] TABLE_FACEDATA_COLUMNS = {FACEDATA_COL_IMGID,FACEDATA_COL_FACEROWSTART,
            FACEDATA_COL_FACECOLSTART,FACEDATA_COL_FACEHEIGHT,FACEDATA_COL_FACEWIDTH,FACEDATA_COL_FACEANGLEX,
            FACEDATA_COL_FACEANGLEY,FACEDATA_COL_FACEANGLEZ,FACEDATA_COL_SMILING,FACEDATA_COL_LEFTEYEOPEN,
            FACEDATA_COL_RIGHTEYEOPEN};

    public static final String VIDEOSETTINGS_COL_VIDID = "vid_id";
    public static final String VIDEOSETTINGS_COL_FRAMERATE = "frame_rate";
    public static final String VIDEOSETTINGS_COL_AUDIOAVAILABLE = "audio_available";
    public static final String VIDEOSETTINGS_COL_AUDIOTRACK = "audio_track";

    public static final String[] TABLE_VIDEOSETTINGS_COLUMNS = {VIDEOSETTINGS_COL_VIDID,VIDEOSETTINGS_COL_FRAMERATE,
            VIDEOSETTINGS_COL_AUDIOAVAILABLE,VIDEOSETTINGS_COL_AUDIOTRACK};

    private final String VIDEOSETTINGS_CREATE_STRING = " CREATE TABLE IF NOT EXISTS " + TABLE_VIDEOSETTINGS + "( " +
            VIDEOSETTINGS_COL_VIDID + " INTEGER NOT NULL," +
            VIDEOSETTINGS_COL_FRAMERATE + " INTEGER NOT NULL," +
            VIDEOSETTINGS_COL_AUDIOAVAILABLE + " INTEGER," +
            VIDEOSETTINGS_COL_AUDIOTRACK + " TEXT)";

    private final String FACEDATA_CREATE_STRING = " CREATE TABLE IF NOT EXISTS " + TABLE_FACEDATA + "( " +
            FACEDATA_COL_IMGID + " INTEGER NOT NULL," +
            FACEDATA_COL_FACEROWSTART + " INTEGER NOT NULL," +
            FACEDATA_COL_FACECOLSTART + " INTEGER NOT NULL," +
            FACEDATA_COL_FACEHEIGHT + " INTEGER NOT NULL," +
            FACEDATA_COL_FACEWIDTH + " INTEGER NOT NULL," +
            FACEDATA_COL_FACEANGLEX + " REAL NOT NULL," +
            FACEDATA_COL_FACEANGLEY + " REAL NOT NULL," +
            FACEDATA_COL_FACEANGLEZ + " REAL NOT NULL," +
            FACEDATA_COL_SMILING + " REAL NOT NULL," +
            FACEDATA_COL_LEFTEYEOPEN + " INTEGER, " +
            FACEDATA_COL_RIGHTEYEOPEN + " INTEGER )";

    private final String VIDEODATA_CREATE_STRING = " CREATE TABLE IF NOT EXISTS " + TABLE_VIDEODATA + "( " +
            VIDEODATA_COL_VIDID + " INTEGER NOT NULL," +
            VIDEODATA_COL_IMGLOCATIONONDISK + " TEXT NOT NULL," +
            VIDEODATA_COL_LOCATIONINVIDEO + " INTEGER NOT NULL)";


    private final String VIDEOSCREATED_CREATE_STRING = " CREATE TABLE IF NOT EXISTS " + TABLE_VIDEOSCREATED + "( " +
            VIDEOSCREATED_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            VIDEOSCREATED_COL_AUTHOR + " TEXT NOT NULL," +
            VIDEOSCREATED_COL_TITLE + " TEXT NOT NULL,"+
            VIDEOSCREATED_COL_DATECREATED + " TEXT NOT NULL,"+
            VIDEOSCREATED_COL_DATELASTEDITED +" TEXT,"+
            VIDEOSCREATED_COL_TOTALIMAGESUSED +" INTEGER)";

    private final String IMAGEDATA_CREATE_STRING = " CREATE TABLE IF NOT EXISTS " + TABLE_IMAGEDATA + "( " +
            IMAGEDATA_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            IMAGEDATA_COL_LOCATIONONDISK + " TEXT NOT NULL," +
            IMAGEDATA_COL_DATETIME + " INTEGER NOT NULL," +
            IMAGEDATA_COL_EXPOSURETIME + " INTEGER,"+
            IMAGEDATA_COL_FLASH + " INTEGER,"+
            IMAGEDATA_COL_FOCALLENGTH +" REAL,"+
            IMAGEDATA_COL_GPS_ALTITUDE +" REAL,"+
            IMAGEDATA_COL_GPS_LATITUDE +" REAL,"+
            IMAGEDATA_COL_GPS_LONGITUDE +" REAL,"+
            IMAGEDATA_COL_GPS_PROCESSINGMETHOD + " TEXT,"+
            IMAGEDATA_COL_IMGWIDTH + " INTEGER,"+
            IMAGEDATA_COL_IMGHEIGHT + " INTEGER,"+
            IMAGEDATA_COL_MAKE + " TEXT,"+
            IMAGEDATA_COL_MODEL + " TEXT," +
            IMAGEDATA_COL_ORIENTATION + " INTEGER,"+
            IMAGEDATA_COL_WHITEBALANCE +" REAL," +
            IMAGEDATA_COL_SOURCE + " TEXT,"+
            IMAGEDATA_COL_HASFACE + " INTEGER, " +
            IMAGEDATA_COL_FACESCOUNT + " INTEGER)";


    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(IMAGEDATA_CREATE_STRING);
        db.execSQL(FACEDATA_CREATE_STRING);

        db.execSQL(VIDEOSCREATED_CREATE_STRING);
        db.execSQL(VIDEODATA_CREATE_STRING);
        db.execSQL(VIDEOSETTINGS_CREATE_STRING);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }




}
