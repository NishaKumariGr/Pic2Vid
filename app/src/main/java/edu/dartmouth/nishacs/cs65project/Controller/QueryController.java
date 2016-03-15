package edu.dartmouth.nishacs.cs65project.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import edu.dartmouth.nishacs.cs65project.Model.LOGTags;
import edu.dartmouth.nishacs.cs65project.Model.P2VVideoObject;
import edu.dartmouth.nishacs.cs65project.Model.P2VVideoSettings;
import edu.dartmouth.nishacs.cs65project.Model.Query;
import edu.dartmouth.nishacs.cs65project.database.DatabaseHelper;

/**
 * Created by haris on 03/03/2016.
 */
public class QueryController {

    private SQLiteDatabase myImageDB;
    private DatabaseHelper myDBHelper;

    // written as public
    protected void open()
    {
        myImageDB = myDBHelper.getReadableDatabase();
    }

    protected void close()
    {
        myImageDB.close();
    }


    private Context mContext;

    public QueryController( Context context )
    {
        mContext = context;
        myDBHelper = new DatabaseHelper(context);
    }

    // Haris Notes:
    // For next revision to this function we have to offload this from the UI Thread
    public ArrayList<String> HandleQuery( Query query )
    {
        // Create a complex query here for access to db
        ArrayList<String> image_paths = new ArrayList<String>();

        String operation_checkbox = ">=";
        String value_checkbox =  "-1";

        Long value_date_begin = query.getmStartDate();
        Long value_date_end = query.getmEnddate();

        Float radius_in_meters = new Float(query.getmRadius());
        radius_in_meters = radius_in_meters*16*100;

        Log.d("QUERYDEBUG","People:"+query.getmIsPeopleChecked());
        Log.d("QUERYDEBUG","Scenery:"+query.getmIsSceneryChecked());
        Log.d("QUERYDEBUG","Selfie:"+query.getmIsSelfieClicked());

        if (query.getmIsPeopleChecked()==true && query.getmIsSceneryChecked()==false && query.getmIsSelfieClicked()==false )
        {
            Log.d(LOGTags.QUERY_CONTROLLER,"RUNNING FOR PEOPLE");
            operation_checkbox = ">";
            value_checkbox = "0";
        }
        else if ( query.getmIsSceneryChecked()==true && query.getmIsPeopleChecked()==false && query.getmIsSelfieClicked()==false)
        {
            Log.d(LOGTags.QUERY_CONTROLLER,"RUNNING FOR NO PEOPLE");
            operation_checkbox = "=";
            value_checkbox = "0";
        }
        else if ( query.getmIsSelfieClicked()==true && query.getmIsPeopleChecked()==false && query.getmIsSceneryChecked()==false)
        {
            Log.d(LOGTags.QUERY_CONTROLLER,"RUNNING FOR SELFIE");
            operation_checkbox = "=";
            value_checkbox = "1";
        }
        else if ( query.getmIsPeopleChecked() &&  query.getmIsSelfieClicked() && query.getmIsSceneryChecked()==false )
        {
            // retrieves everything
            operation_checkbox = ">=";
            value_checkbox = "1";
            Log.d(LOGTags.QUERY_CONTROLLER,"RUNNING FOR SELFIE AND PEOPLE");

        }
        else if ( query.getmIsSceneryChecked() &&  query.getmIsSelfieClicked() && query.getmIsPeopleChecked()==false)
        {
            // retrieves everything
            operation_checkbox = "<=";
            value_checkbox = "1";
            Log.d(LOGTags.QUERY_CONTROLLER,"RUNNING FOR SCENERY AND SELFIE");

        }
        else if ( query.getmIsSceneryChecked() &&  query.getmIsPeopleChecked() && query.getmIsSelfieClicked()==false)
        {
            // retrieves everything
            operation_checkbox = "!=";
            value_checkbox = "1";
            Log.d(LOGTags.QUERY_CONTROLLER,"RUNNING FOR SCENERY AND PEOPLE");

        }



        LatLng base_location_points = query.getmLocation();
        Location base_location = new Location("");
        if (base_location_points != null)
        {
            base_location.setLatitude(base_location_points.latitude);
            base_location.setLongitude(base_location_points.longitude);
        }

        open();


        Cursor cursor = myImageDB.query(myDBHelper.TABLE_IMAGEDATA,
                new String[]{myDBHelper.IMAGEDATA_COL_LOCATIONONDISK,myDBHelper.IMAGEDATA_COL_GPS_ALTITUDE, myDBHelper.IMAGEDATA_COL_GPS_LATITUDE, myDBHelper.IMAGEDATA_COL_GPS_LONGITUDE},
                        myDBHelper.IMAGEDATA_COL_FACESCOUNT + " " +operation_checkbox+"? AND "
                        +myDBHelper.IMAGEDATA_COL_DATETIME +" >= ? AND "
                        +myDBHelper.IMAGEDATA_COL_DATETIME +" <= ? ",
                new String[]{value_checkbox,value_date_begin.toString(), value_date_end.toString()},
                null, null, null);

        Log.d(LOGTags.QUERY_CONTROLLER,"Retrieved entries:"+cursor.getCount());

        Integer count = cursor.getCount();
        int cur_count = 0;
        if ( count >  0)
        {
            // construct array list of strings
            cursor.moveToFirst();


            while( !cursor.isAfterLast())
            {
                String file_path = cursor.getString(0);

                Boolean gps_criteria = true;

                // if no gps_criteria found than choose everything
                if ( base_location_points != null) {

                    Double alt_pos = cursor.getDouble(1);
                    Double lat_pos = cursor.getDouble(2);
                    Double long_pos = cursor.getDouble(3);

                    Location cur_loc = new Location("");

                    cur_loc.setLatitude(lat_pos);
                    cur_loc.setLongitude(long_pos);

                    Float distance_between_points = cur_loc.distanceTo(base_location);

                    Log.d(LOGTags.QUERY_CONTROLLER,"GPS: Distance for image:" + cur_count+ " is "+distance_between_points );
                    if (distance_between_points > radius_in_meters)
                    {
                        gps_criteria = false;
                    }
                }

                // check location criteria met here
                if (gps_criteria)
                {
                    image_paths.add( file_path );
                    //Log.d(LOGTags.QUERY_CONTROLLER, "Added path for item " + cur_count + " : " + image_paths.get(cur_count));

                }
                else
                {
                    Log.d(LOGTags.QUERY_CONTROLLER, "DId not add path for item as gps criteria did not meet " + cur_count );

                }
                cursor.moveToNext();
                cur_count++;
            }



        }

        close();
        return image_paths;
    }


    public ArrayList<P2VVideoObject> getAllVideos()
    {
        ArrayList<P2VVideoObject> all_videos = new ArrayList<P2VVideoObject>();

        open();

        Cursor cursor = myImageDB.query(myDBHelper.TABLE_VIDEOSCREATED,
                myDBHelper.TABLE_VIDEOSCREATED_COLUMNS,
                null, null,
                null, null, null);

        cursor.moveToFirst();
        Log.d(LOGTags.QUERY_CONTROLLER, "READ TOTAL SAVED VIDEOS:" + cursor.getCount());

        while (!cursor.isAfterLast())
        {
            P2VVideoObject current_video = cursorToP2VVideo( cursor );
            all_videos.add(current_video);
            Log.d(LOGTags.QUERY_CONTROLLER, "ADDED VIDEO :" + current_video.getVid_id() + " with Images: " + current_video.getTotalImagesUsed());
            cursor.moveToNext();
        }

        close();

        return all_videos;
    }

    public P2VVideoObject getVideoById(Long video_id)
    {
        open();

        Cursor cursor = myImageDB.query(myDBHelper.TABLE_VIDEOSCREATED,
                myDBHelper.TABLE_VIDEOSCREATED_COLUMNS,
                myDBHelper.VIDEOSCREATED_COL_ID + "=?", new String[]{video_id.toString()},
                null, null, null);

        cursor.moveToFirst();
        Log.d(LOGTags.QUERY_CONTROLLER, "READ TOTAL SAVED VIDEOS:" + cursor.getCount());

        P2VVideoObject current_video = cursorToP2VVideo( cursor );

        close();

        return current_video;
    }

    private P2VVideoObject cursorToP2VVideo( Cursor cursor )
    {

        P2VVideoObject current_video  = new P2VVideoObject();

        current_video.setVid_id(cursor.getLong(0));
        Log.d(LOGTags.QUERY_CONTROLLER, "VIDEO ID: " + current_video.getVid_id());

        current_video.setAuthor(cursor.getString(1));
        Log.d(LOGTags.QUERY_CONTROLLER, "VIDEO AUTHOR: " + current_video.getAuthor());

        current_video.setVideoTitle(cursor.getString(2));
        Log.d(LOGTags.QUERY_CONTROLLER, "VIDEO TITLE: " + current_video.getVideoTitle());

        current_video.setDateCreated(cursor.getString(3));
        Log.d(LOGTags.QUERY_CONTROLLER, "VIDEO CREATED: " + current_video.getDateCreated());

        current_video.setDateLastEdited(cursor.getString(4));
        Log.d(LOGTags.QUERY_CONTROLLER, "VIDEO EDITED: " + current_video.getDateLastEdited());

        current_video.setTotalImagesUsed(cursor.getInt(5));
        Log.d(LOGTags.QUERY_CONTROLLER, "VIDEO IMAGES: " + current_video.getTotalImagesUsed());

        current_video.setFile_paths(getVideoData(current_video.getVid_id()));

        current_video.setVideo_settings(getVideoSettings(current_video.getVid_id()));
        // total images are set based on the number of images retrieved

        return current_video;
    }

    private ArrayList<String> getVideoData(Long video_id)
    {
        //open();
        // isses a query to get all image paths
        Log.d(LOGTags.QUERY_CONTROLLER, "SEARCHING FOR VIDEO DATA " + video_id);

        Cursor data_cursor = myImageDB.query(myDBHelper.TABLE_VIDEODATA,myDBHelper.TABLE_VIDEODATA_COLUMNS,
                myDBHelper.VIDEODATA_COL_VIDID +" = ?",new String[]{video_id.toString()},null,null,null);

        data_cursor.moveToFirst();
        ArrayList<String> all_file_paths = new ArrayList<String>();
        while(!data_cursor.isAfterLast())
        {
            String file_name = data_cursor.getString(1);
            Integer location_in_video = data_cursor.getInt(2);
            all_file_paths.add(location_in_video,file_name);
            data_cursor.moveToNext();
        }
        //close();
        return all_file_paths;
    }

    private P2VVideoSettings getVideoSettings(Long video_id)
    {
        // isses a query to get all image paths
        P2VVideoSettings settings_obj = new P2VVideoSettings();

        Cursor settings_cursor = myImageDB.query(myDBHelper.TABLE_VIDEOSETTINGS,myDBHelper.TABLE_VIDEOSETTINGS_COLUMNS,
                myDBHelper.VIDEOSETTINGS_COL_VIDID +" = ?",new String[]{video_id.toString()},null,null,null);

        settings_cursor.moveToFirst();

        Integer frame_rate = settings_cursor.getInt(1);
        settings_obj.setFrameRate(frame_rate);

        Integer audio_available = settings_cursor.getInt(2);
        if ( audio_available == 0)
        {
            settings_obj.setAudioTrackAvailable(false);
        }
        else
        {
            settings_obj.setAudioTrackAvailable(true);
        }

        String track_location = settings_cursor.getString(3);
        settings_obj.setAudioTrackLocation(track_location);

        return settings_obj;
    }
}
